package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

//    @OneToOne(mappedBy = "location")
//    @JoinColumn(name = "studentId")
//    @JsonIgnor
//    private Student student;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore   // ✅ VERY IMPORTANT
    private List<Student> students;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<Exam> exams;


}
