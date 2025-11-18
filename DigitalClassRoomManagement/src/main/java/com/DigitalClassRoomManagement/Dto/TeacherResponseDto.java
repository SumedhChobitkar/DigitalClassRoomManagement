package com.DigitalClassRoomManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;
    private String qualification;
    private Integer experienceYears;
    private String dateOfBirth; // Keep as String if your entity stores it as String
    private List<Long> assignedClassIds; // List of class IDs
}
