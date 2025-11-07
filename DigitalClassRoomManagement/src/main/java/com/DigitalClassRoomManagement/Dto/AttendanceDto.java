package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Entity.AttendanceStatus;
import com.DigitalClassRoomManagement.Entity.MarkBy;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class AttendanceDto {
    private Long attendanceId;
    private LocalDate date;
    private LocalDateTime joinTime;
    private LocalDateTime exitTime;
    private Long durationMinutes;
    private AttendanceStatus status;
    private MarkBy markedBy;
}
