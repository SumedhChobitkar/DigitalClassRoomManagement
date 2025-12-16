package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Long studentId;

    private String rollNumber;
    private String admissionNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String academicYear;

    private String email;
    private String mobileNumber;
    private String dateOfBirth;
    private String gender;

    private String street;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    // -------------------- USER --------------------
    private User users;

    // -------------------- TEACHER --------------------
    private Long teacherId;         // For assigning teacher
    private String teacherMailId;   // Read only
    private Teacher teacher;        // Response only

    // -------------------- CLASS --------------------
    private Long classId;
    private SchoolClass schoolClass;

    // -------------------- SECTION --------------------
    private Long sectionId;
    private Section section;

    //--------------------profile----------------------
    private String profile;


    // ---------------------------------------------
    // Constructor for simple student list
    // ---------------------------------------------
    public StudentDTO(
            Long studentId,
            String rollNumber,
            String admissionNumber,
            String firstName,
            String middleName,
            String lastName,
            String academicYear,
            String email,
            String mobileNumber,
            String dateOfBirth,
            String gender,
            String street,
            String city,
            String state,
            String country,
            String pinCode,
            User users,
            String teacherMailId,
            Teacher teacher,
            Section section,
            SchoolClass schoolClass
    ) {
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.admissionNumber = admissionNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.academicYear = academicYear;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
        this.users = users;
        this.teacherMailId = teacherMailId;
        this.teacher = teacher;
        this.section = section;
        this.schoolClass = schoolClass;

        if (teacher != null) {
            this.teacherId = teacher.getId();
        }
        if (section != null) {
            this.sectionId = section.getSectionId();
        }
        if (schoolClass != null) {
            this.classId = schoolClass.getClassId();
        }
    }

    // -----------------------------------------------------
    // ENROLLMENT REQUEST DTO
    // -----------------------------------------------------
    @Data
    public static class EnrollmentRequest {
        private Long teacherId;
        private Long classId;
        private Long sectionId;
        private String admissionNumber;
        private String rollNo;
    }

    // -----------------------------------------------------
    // ENROLLMENT RESPONSE DTO
    // -----------------------------------------------------
    @Data
    @AllArgsConstructor
    public static class StudentCreateResponse {
        private String status;
        private String message;
    }
}
