package com.DigitalClassRoomManagement.Dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class AdmissionDTO {

    private Long admissionId;
    private String studentName;
    private String email;
    private String course;
    private LocalDate admissionDate;
}
