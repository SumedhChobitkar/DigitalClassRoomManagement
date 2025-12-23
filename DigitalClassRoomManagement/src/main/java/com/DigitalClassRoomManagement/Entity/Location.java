package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    private Double latitude;
    private Double longitude;

    private String address;
    private String city;
    private String schoolName;

    @Column(name = "school_id")
    private Long schoolId;
}
