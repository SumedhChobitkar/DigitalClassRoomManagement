package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Entity.User;

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
    private User users;




    // ==============================
    //   EnrollmentRequest DTO
    // ==============================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollmentRequest {
        private String academicYear;
        private String standard;
        private String division;
        private String admissionNumber;
        private String rollNo;
    }

    // ==============================
    //   StudentCreateResponse DTO
    // ==============================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentCreateResponse {
        private String status;
        private String message;
    }

}
