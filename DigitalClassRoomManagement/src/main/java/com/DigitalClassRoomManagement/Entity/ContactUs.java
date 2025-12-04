package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact_us")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactUs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String schoolName;//school name

    @Column(nullable = false)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(length = 2000, nullable = false)
    private String address;// address
}
