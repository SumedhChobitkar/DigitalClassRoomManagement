package com.DigitalClassRoomManagement.Dto;


import com.DigitalClassRoomManagement.Enum.Role;
import com.DigitalClassRoomManagement.Enum.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDTO {
    @Id
    private Long adminId;

    private String firstName;

    private String password;
    private String lastName;
    private String email;
    private String phone;
    private String qualification;
    private Integer experienceYears;

    private String gender;
    private String dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;
}
