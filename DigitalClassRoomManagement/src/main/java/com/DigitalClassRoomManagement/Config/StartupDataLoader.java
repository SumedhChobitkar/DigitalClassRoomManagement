package com.DigitalClassRoomManagement.Config;

import com.DigitalClassRoomManagement.Entity.Role;
import com.DigitalClassRoomManagement.Entity.user;
import com.DigitalClassRoomManagement.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartupDataLoader {

    @Bean
    public CommandLineRunner loadDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@classroom.com";

            // Check if Admin already exists
            if (userRepository.findByEmail(email).isPresent()) {
                System.out.println("✅ Admin user already exists.");
                return;
            }

            user admin = new user();
            admin.setName("System Admin");
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN); // Available roles: ADMIN, TEACHER, STUDENT, PARENT
            admin.setLanguagePreference("ENGLISH");

            userRepository.save(admin);

            System.out.println("✅ Default Admin created successfully!");
            System.out.println("Email: " + email);
            System.out.println("Password: Admin@123");
        };
    }
}
