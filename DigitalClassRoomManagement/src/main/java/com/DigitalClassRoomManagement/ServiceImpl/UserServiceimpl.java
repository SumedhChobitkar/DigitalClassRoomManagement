package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.userDto;
import com.DigitalClassRoomManagement.Entity.user;
import com.DigitalClassRoomManagement.Exception.UserNotFoundException;
import com.DigitalClassRoomManagement.Repository.UserRepository;
import com.DigitalClassRoomManagement.Service.UserService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceimpl.class);

    @Override
    public userDto registeration(user user1) {
        try {
            validation(user1);

            Optional<user> existingUser = userRepository.findByEmail(user1.getEmail());
            if (existingUser.isPresent()) {
                throw new RuntimeException("User already registered with email: " + user1.getEmail());
            }

            user1.setPassword(passwordEncoder.encode(user1.getPassword()));

            user savedUser = userRepository.save(user1);

            return toDto(savedUser);
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public user login(String email, String password) {
        try {
            Optional<user> optional = userRepository.findByEmail(email);
            if (optional.isPresent()) {
                user u = optional.get();
                if (passwordEncoder.matches(password, u.getPassword())) {
                    return u;
                } else {
                    throw new UserNotFoundException("Password not match");
                }
            } else {
                throw new IllegalArgumentException("Email does not exist");
            }
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<userDto> getAll() {
        try {
            List<user> users = userRepository.findAll();
            return users.stream().map(this::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public userDto getUserById(Long id) {
        try {
            Optional<user> opt = userRepository.findById(id);
            if (opt.isPresent()) {
                return toDto(opt.get());
            }
            throw new RuntimeException("ID NOT FOUND");
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            throw e;
        }
    }

    public static void validation(user user1) {

        if (user1.getName() == null || !ValidationClass.NAME_PATTERN.matcher(user1.getName()).matches()) {
            throw new IllegalArgumentException("Invalid Name: Must start with uppercase and contain only letters, spaces, or dots.");
        }

        if (user1.getEmail() == null || !ValidationClass.EMAIL_PATTERN.matcher(user1.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid Email: Must be a valid email address (e.g., example@domain.com).");
        }

        if (user1.getPassword() == null || !ValidationClass.PASSWORD_PATTERN.matcher(user1.getPassword()).matches()) {
            throw new IllegalArgumentException("Invalid Password: Must contain 6–20 chars, at least one letter, one digit, and one special character.");
        }

        if (user1.getRole() == null || !ValidationClass.ROLE_PATTERN.matcher(user1.getRole().name()).matches()) {
            throw new IllegalArgumentException("Invalid Role: Must be one of ADMIN, PRINCIPAL, TEACHER, STUDENT, or PARENT.");
        }

        if (user1.getLanguagePreference() == null || !ValidationClass.LANGUAGE_PATTERN.matcher(user1.getLanguagePreference()).matches()) {
            throw new IllegalArgumentException("Invalid Language Preference: Only letters, spaces, or dashes allowed (e.g., English, en-US).");
        }
    }

    private userDto toDto(user u) {
        userDto dto = new userDto();
        dto.setUserId(u.getUserId());
        dto.setName(u.getName());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setLanguagePreference(u.getLanguagePreference());
        return dto;
    }

    private user toEntity(userDto dto) {
        user u = new user();
        u.setUserId(dto.getUserId());
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setRole(dto.getRole());
        u.setLanguagePreference(dto.getLanguagePreference());
        return u;
    }
}
