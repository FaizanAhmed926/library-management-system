package com.library.library_backend.service.impl;

import com.library.library_backend.domain.UserRole;
import com.library.library_backend.model.User;
import com.library.library_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        initializeAdminUser();
    }

    private void initializeAdminUser(){
        String adminEmail="faizanahmedcodex@gmail.com";
        String adminPassword="FaizanAhmedCodex";

        if (userRepository.findByEmail(adminEmail)==null){
            User user=User.builder()
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .fullName("Faizan Ahmed")
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            User admin=userRepository.save(user);
        }
    }
}
