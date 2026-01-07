package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentRegId;  // Primary key


    /* ---------------------- USER MAPPING ---------------------- */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User users;


    /* ---------------------- STUDENT BASIC DETAILS ---------------------- */
    @Column(nullable = false, unique = true)
    //@NotBlank(message = "Roll number is required")
    private String rollNumber;

    private String academicYear;

    @Column(nullable = false, unique = true)
    //@NotBlank(message = "Admission number is required")
    private String admissionNumber;

    //@NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;
    private String lastName;

    private String email;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String gender;


    /* ---------------------- ADDRESS DETAILS ---------------------- */
    private String street;
    private String city;
    private String state;
    private String country;
    private String pinCode;


    /* ---------------------- PARENT MAPPING ---------------------- */
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonBackReference(value = "parent-student")
    private Parent parent;


    /* ---------------------- TEACHER MAPPING ---------------------- */
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonIgnore
    private Teacher teacher;




    /* ---------------------- CLASS MAPPING ---------------------- */
    @ManyToOne
    @JoinColumn(name = "class_id")
    @JsonBackReference
    private SchoolClass schoolClass;


    /* ---------------------- SECTION MAPPING ---------------------- */
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    /*--------------------------profile------------------------------*/
    /*---------------------- Profile as BLOB ------------------------*/
    @Lob
    @Column(name = "profile", columnDefinition = "LONGBLOB")
    private byte[] profile;


    /* ---------------------- AUDIT FIELDS ---------------------- */
    private LocalDateTime enrolledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    /* ---------------------- UTILITY METHODS ---------------------- */
    public Long getStudentId() {
        return this.studentRegId;
    }

    public void setStudentId(Long studentId) {
        this.studentRegId = studentId;
    }

    public String getName() {
        return firstName + " " + lastName;
    }


    /* ---------------------- AUTO TIMESTAMPS ---------------------- */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        enrolledAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
