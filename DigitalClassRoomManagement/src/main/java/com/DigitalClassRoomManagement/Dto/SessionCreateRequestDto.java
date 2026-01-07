package com.DigitalClassRoomManagement.Dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class SessionCreateRequestDto {

    private Long timetableId;
    private Long classId;
    private Long sectionId;
    private Long teacherId;

    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String topic;
    private String description;
}
