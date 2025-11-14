package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.Role;
import com.DigitalClassRoomManagement.Enum.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    private String userName;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Status status;//ACTIVE,INACTIVE

    private LocalDateTime lastLogin;

    private String otp;

    private LocalDateTime otpExpiry;
    private LocalDateTime otpGenerationTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, PRINCIPAL, TEACHER, STUDENT, PARENT
    private String languagePreference;
}
