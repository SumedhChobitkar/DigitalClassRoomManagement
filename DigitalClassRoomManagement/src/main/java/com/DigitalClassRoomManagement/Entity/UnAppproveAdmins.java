package com.DigitalClassRoomManagement.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class UnAppproveAdmins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

}