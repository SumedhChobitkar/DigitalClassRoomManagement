package com.DigitalClassRoomManagement.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayDto {
    private Long id;
    private String holidayName;
    private LocalDate holidayDate;
    private String description;
}