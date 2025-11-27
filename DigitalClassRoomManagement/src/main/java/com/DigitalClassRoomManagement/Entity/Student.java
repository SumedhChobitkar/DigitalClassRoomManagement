package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;


import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentRegId;  // Primary key

    //  One-to-One with User
    @OneToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)

    //private User users;
    private User users;

    //  Roll number
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    //  Academic year
    //@Column(nullable = false)
    //@NotBlank(message = "Academic year is required")
    private String academicYear;

    //  Admission number
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Admission number is required")
    private String admissionNumber;

    //  Student name fields
   @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    private String lastName;

    private String teacherMailId;
    //  Email
    //@Column(nullable = false, unique = true)
    //@NotBlank(message = "Email is required")
   // @Email(message = "Invalid email format")
    private String email;

    //  Mobile number
    private String mobileNumber;

    //  Date of birth
    private LocalDate dateOfBirth;

    //  Gender
    private String gender;

    //  Address details

    private String street;


    private String city;


    private String state;


    private String country;

    private String pinCode;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonBackReference(value = "parent-student")
    private Parent parent;


    //  Class and Section mapping
    /*@ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;     */

    //  Fee and teacher relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    @JsonIgnore
    private Teacher teacher;

    /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id", nullable = false)
    private FeeStructure feeStructure;      */

    //  Audit fields
    private LocalDateTime enrolledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //  Invoice and Payment relationships
     /*@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    //  PTM (Parent-Teacher Meeting)
    @ManyToMany(mappedBy = "students")
    private List<PTM> ptms = new ArrayList<>();    */



       //  Convenience methods
    public Long getStudentId() {
        return this.studentRegId;
    }

    public void setStudentId(Long studentId) {
        this.studentRegId = studentId;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    //  Automatically set timestamps
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
