package com.isap.ISAProject.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.isap.ISAProject.repository.user.RegisteredUserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private RegisteredUserRepository userRepo;

    public CustomUserDetailsService(RegisteredUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Korisničko ime: " + username + " nije pronađeno"));
    }
}
