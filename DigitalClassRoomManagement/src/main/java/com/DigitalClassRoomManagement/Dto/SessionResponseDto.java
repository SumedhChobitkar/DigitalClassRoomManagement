package com.DigitalClassRoomManagement.Dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class SessionResponseDto {
    private Long sessionId;
    private Long timetableId; // nullable if you want
    private Long classId;
    private Long sectionId;
    private Long teacherId;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String topic;
    private String description;
    private String joinLink;
    private String googleEventId;
}
