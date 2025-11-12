package com.DigitalClassRoomManagement.Dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {
    @NotBlank(message = "First name can not be empty")
    @Pattern(regexp = "^[A-Za-z]+$",message = "First name should only contain alphabets and spaces")
    private String firstName;
    @NotBlank(message = "Last name can not be empty")
    @Pattern(regexp = "^[A-Za-z]+$",message = "Last name should only contain alphabets and spaces")
    private String lastName;
    @NotBlank(message="Email can not be blank")
    @Email(message = "Kindly provide valid email id")
    @Column(nullable = false,unique = true)
    private String email;
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be 10 digits and start with 6, 7, 8, or 9")
    private String phone;
    private String qualification;
    private Integer experienceYears;
    private String gender;
    @Past(message = "Birth date should be from past")
    @NotNull(message = "Date of birth must be provided")
    private LocalDate dateOfBirth;
}
