package com.isap.ISAProject.service.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.isap.ISAProject.domain.factory.CerberusUserFactory;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private RegisteredUserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	  RegisteredUser user = this.userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
    } else {
      return CerberusUserFactory.create(user);
    }
  }

}
