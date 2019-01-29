package com.isap.ISAProject.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.user.AuthorizationLevel;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.model.user.ConfirmationToken;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.UsersAdmin;
import com.isap.ISAProject.repository.user.CompanyAdminRepository;
import com.isap.ISAProject.repository.user.ConfirmationTokenRepository;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.repository.user.UsersAdminRepository;
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
	
	private void checkIfUsernameExists(String username) {
		if(companyAdminsRepository.findByUsername(username) != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use.");
		if(usersAdminsRepository.findByUsername(username) != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use.");
		if(registeredUsersRepository.findByUsername(username) != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use.");
	}
	
	private void checkIfEmailExists(String email) {
		if(companyAdminsRepository.findByEmail(email) != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail already in use.");
		if(usersAdminsRepository.findByEmail(email) != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail already in use.");
		if(registeredUsersRepository.findByEmail(email) != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail already in use.");
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public CompanyAdmin createCompanyAdmin(CompanyAdmin admin) {
		logger.info("> save admin with username {} and email {}", admin.getUsername(), admin.getEmail());
		checkIfUsernameExists(admin.getUsername());
		checkIfEmailExists(admin.getEmail());
		companyAdminsRepository.save(admin);
		logger.info("< admin saved");
		return admin;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public UsersAdmin createUserAdmin(UsersAdmin admin) {
		logger.info("> save admin with username {} and email {}", admin.getUsername(), admin.getEmail());
		checkIfUsernameExists(admin.getUsername());
		checkIfEmailExists(admin.getEmail());
		admin.setAuthority(AuthorizationLevel.USERS_ADMIN);
		usersAdminsRepository.save(admin);
		logger.info("< admin saved");
		return admin;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RegisteredUser createRegisteredUser(RegisteredUser user) {
		logger.info("> save user with username {} and email {}", user.getUsername(), user.getEmail());
		checkIfUsernameExists(user.getUsername());
		checkIfEmailExists(user.getEmail());
		user.setAuthority(AuthorizationLevel.GUEST);
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();	
		user.setPassword(bc.encode(user.getPassword()));
	
		//email
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

}
