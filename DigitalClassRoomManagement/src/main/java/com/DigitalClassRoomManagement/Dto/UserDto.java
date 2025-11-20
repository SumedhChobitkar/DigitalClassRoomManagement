package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.Role;
import com.DigitalClassRoomManagement.Enum.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
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

