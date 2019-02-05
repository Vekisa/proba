package com.isap.ISAProject.service.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.domain.factory.CerberusUserFactory;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.SystemUser;
import com.isap.ISAProject.model.user.UserState;
import com.isap.ISAProject.repository.user.CompanyAdminRepository;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.repository.user.UsersAdminRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private RegisteredUserRepository registeredUserRepository;

	@Autowired
	private UsersAdminRepository userAdminsRepository;
	
	@Autowired
	private CompanyAdminRepository companyAdminRepository;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(username+"+++++++++++++++++++");
		SystemUser user = null;
		if(registeredUserRepository.findByUsername(username) != null)
			user = registeredUserRepository.findByUsername(username);
		if(userAdminsRepository.findByUsername(username) != null)
			user = userAdminsRepository.findByUsername(username);
		if(companyAdminRepository.findByUsername(username) != null)
			user = companyAdminRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			if(user instanceof RegisteredUser && user.getState().equals(UserState.INACTIVE))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not activated. Activate it with link.");
			return CerberusUserFactory.create(user);
		}
	}

}
