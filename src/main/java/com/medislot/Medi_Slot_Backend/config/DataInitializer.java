package com.medislot.Medi_Slot_Backend.config;

import com.medislot.Medi_Slot_Backend.entity.User;
import com.medislot.Medi_Slot_Backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (!userRepo.existsByEmail("admin@medislot.com")) {
                User admin = new User();
                admin.setEmail("admin@medislot.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(User.Role.ADMIN);
                userRepo.save(admin);
            }
        };
    }
}