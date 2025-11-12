package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, PRINCIPAL, TEACHER, STUDENT, PARENT
    private String languagePreference;
}
