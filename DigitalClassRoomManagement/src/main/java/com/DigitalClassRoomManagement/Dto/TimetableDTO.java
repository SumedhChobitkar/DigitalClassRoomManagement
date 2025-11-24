package com.DigitalClassRoomManagement.Dto;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class TimetableDTO {
    private Long timetableId;
    //private String schoolClass;
    private Long classId;

    private Long sectionId;
    private Long subjectId;
    private Long teacherId;
    private DayOfWeek dayOfWeek;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
