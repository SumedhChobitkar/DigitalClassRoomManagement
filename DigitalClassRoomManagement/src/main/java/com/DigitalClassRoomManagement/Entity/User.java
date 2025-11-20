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
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 to 50 characters")
    @Column(unique = true, nullable = false)
    private String userName;

    private String firstName;
    private String lastName;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, PRINCIPAL, TEACHER, STUDENT, PARENT
    private String languagePreference;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Teacher teacher;
    private String password;

    @Enumerated(EnumType.STRING)
    private Status status;//ACTIVE,INACTIVE

    private LocalDateTime lastLogin;

    private String otp;

    private LocalDateTime otpExpiry;
    private LocalDateTime otpGenerationTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
