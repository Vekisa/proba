package com.isap.ISAProject;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    RegisteredUserRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        this.userRepo.save(RegisteredUser.builder()
            .username("user")
            .password(this.passwordEncoder.encode("password"))
            .roles(Arrays.asList("USER"))
            .city("Kljajicevo")
            .email("milica@example.org")
            .firstName("Milica")
            .lastName("Matijevic")
            .phoneNumber("853-806")
            .bonusPoints(34l)
            .build()
        );

        this.userRepo.save(RegisteredUser.builder()
            .username("admin")
            .password(this.passwordEncoder.encode("password"))
            .roles(Arrays.asList("USER", "SYSTEM_ADMIN"))
            .city("Kljajicevo")
            .email("mladjo@example.org")
            .firstName("Mladen")
            .lastName("Matijevic")
            .phoneNumber("853-806")
            .bonusPoints(35l)
            .build()
        );
    }
}
