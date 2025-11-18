package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.UserDto;
import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Exception.UserNotFoundException;
import com.DigitalClassRoomManagement.Repository.UserRepository;
import com.DigitalClassRoomManagement.Service.UserService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceimpl.class);

    @Override
    public UserDto registeration(User user1) {
        try {
            validation(user1);

            Optional<User> existingUser = userRepository.findByEmail(user1.getEmail());
            if (existingUser.isPresent()) {
                throw new RuntimeException("User already registered with email: " + user1.getEmail());
            }

            user1.setPassword(passwordEncoder.encode(user1.getPassword()));
            user1.setCreatedAt(LocalDateTime.now());
            User savedUser = userRepository.save(user1);

            return toDto(savedUser);
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public User login(String email, String password) {
        try {
            Optional<User> optional = userRepository.findByEmail(email);
            if (optional.isPresent()) {
                User u = optional.get();
                if (passwordEncoder.matches(password, u.getPassword())) {
                    u.setLastLogin(LocalDateTime.now());
                    userRepository.save(u);
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
    public List<UserDto> getAll() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream().map(this::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        try {
            Optional<User> opt = userRepository.findById(id);
            if (opt.isPresent()) {
                return toDto(opt.get());
            }
            throw new RuntimeException("ID NOT FOUND");
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Your OTP for Password Reset");
        message.setText("Your OTP is: " + otp + "\nIt is valid for 5 minutes.");
        mailSender.send(message);

        return "OTP sent to your email!";
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return user.getOtp() != null && user.getOtp().equals(otp)
                && user.getOtpExpiry().isAfter(LocalDateTime.now());
    }

    @Override
    public String resetPassword(String email, String newPassword, String confirmPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New password and confirm password do not match!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        user.setOtpExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return "Password updated successfully!";
    }

    public static void validation(User user1) {

        if (user1.getFirstName() == null || !ValidationClass.NAME_PATTERN.matcher(user1.getFirstName()).matches()) {
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

    private UserDto toDto(User u) {
        UserDto dto = new UserDto();
        dto.setUserId(u.getUserId());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setCreatedAt(u.getCreatedAt());
        dto.setUserName(u.getUserName());
        dto.setLanguagePreference(u.getLanguagePreference());
        return dto;
    }

    private User toEntity(UserDto dto) {
        User u = new User();
        u.setUserId(dto.getUserId());
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setEmail(dto.getEmail());
        u.setRole(dto.getRole());
        u.setCreatedAt(dto.getCreatedAt());
        u.setUserName(dto.getUserName());
        u.setLanguagePreference(dto.getLanguagePreference());
        return u;
    }
}
