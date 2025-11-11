package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;

import lombok.*;

@Data
@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
