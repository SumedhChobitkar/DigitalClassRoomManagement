package com.DigitalClassRoomManagement.Dto;


import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAcademicCalenderRequest {
    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;
}
