package com.DigitalClassRoomManagement.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAcademicCalenderRequest {
    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;
}
