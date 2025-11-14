package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.Role;
import com.DigitalClassRoomManagement.Enum.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private Status status;
    private LocalDateTime lastLogin;
    private String otp;
    private LocalDateTime otpExpiry;
    private LocalDateTime otpGenerationTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Role role;
    private String languagePreference;

}

