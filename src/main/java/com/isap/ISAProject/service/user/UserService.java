package com.isap.ISAProject.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.domain.json.request.AuthenticationRequest;
import com.isap.ISAProject.domain.json.response.AuthenticationResponse;
import com.isap.ISAProject.domain.security.CerberusUser;
import com.isap.ISAProject.model.user.AuthorizationLevel;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.model.user.ConfirmationToken;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.UsersAdmin;
import com.isap.ISAProject.repository.user.CompanyAdminRepository;
import com.isap.ISAProject.repository.user.ConfirmationTokenRepository;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.repository.user.UsersAdminRepository;
import com.isap.ISAProject.security.TokenUtils;
import com.isap.ISAProject.service.EmailSenderService;
import com.isap.ISAProject.serviceInterface.user.UserServiceInterface;

@Service
public class UserService implements UserServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CompanyAdminRepository companyAdminsRepository;

	@Autowired
	private UsersAdminRepository usersAdminsRepository;

	@Autowired
	private RegisteredUserRepository registeredUsersRepository;

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private EmailSenderService emailService;

	@Value("X-Auth-Token")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public CompanyAdmin createCompanyAdmin(CompanyAdmin admin) {
		logger.info("> save admin with username {} and email {}", admin.getUsername(), admin.getEmail());
		companyAdminsRepository.save(admin);
		logger.info("< admin saved");
		return admin;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public UsersAdmin createUserAdmin(UsersAdmin admin) {
		logger.info("> save admin with username {} and email {}", admin.getUsername(), admin.getEmail());
		admin.setAuthority(AuthorizationLevel.USERS_ADMIN);
		usersAdminsRepository.save(admin);
		logger.info("< admin saved");
		return admin;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public RegisteredUser createRegisteredUser(RegisteredUser user) {
		logger.info("> save user with username {} and email {}", user.getUsername(), user.getEmail());
		user.setAuthority(AuthorizationLevel.GUEST);
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		user.setPassword(bc.encode(user.getPassword()));

		// email
		ConfirmationToken confTok = new ConfirmationToken();
		user.setConfirmationToken(confTok);
		confTok.setUser(user);
		registeredUsersRepository.save(user);
		confirmationTokenRepository.save(confTok);

		emailService.send(user);
		registeredUsersRepository.save(user);
		logger.info("< user saved");
		return user;
	}

	@Override
	public AuthenticationResponse signin(AuthenticationRequest authenticationRequest/*, Device device*/) {
		logger.info("> signing in");
		// Perform the authentication
		Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Reload password post-authentication so we can generate token
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String token = this.tokenUtils.generateToken(userDetails/*, device*/);

		logger.info("signed in");
		// Return the token
		return new AuthenticationResponse(token);
	}

	@Override
	public void signout() {
		SecurityContextHolder.clearContext();
	}

	@Override
	public CerberusUser currentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		CerberusUser userDetails = (CerberusUser) userDetailsService.loadUserByUsername(username);
		return userDetails;
	}

}
