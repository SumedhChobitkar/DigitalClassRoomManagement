package com.DigitalClassRoomManagement.dto;


import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarDto {
    private Long id;
    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private String createdBy;
    private List<HolidayDto> holidays;
    private List<EventDto> events;
}
