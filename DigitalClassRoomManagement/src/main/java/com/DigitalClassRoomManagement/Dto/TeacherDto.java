package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.TeacherStatus;
import com.DigitalClassRoomManagement.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {

    private Long id;

    @NotBlank(message = "First name can not be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name should only contain alphabets and spaces")
    private String firstName;

    @NotBlank(message = "Last name can not be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name should only contain alphabets and spaces")
    private String lastName;

    @NotBlank(message = "Email can not be blank")
    @Email(message = "Kindly provide valid email id")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be 10 digits and start with 6, 7, 8, or 9")
    private String phone;

    private String adminMailId;
    private String qualification;
    private Integer experienceYears;
    private String gender;


    @NotNull(message = "Date of birth must be provided")
    private String dateOfBirth;

    @JsonIgnore
    private User user;

    private TeacherStatus status;

    private byte[] profilePicture;

    // Store IDs instead of entity lists
    private List<Long> assignedSectionIds;
    private List<Long> assignedClassIds;
}
