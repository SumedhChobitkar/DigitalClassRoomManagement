package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.AttendanceStatus;
import com.DigitalClassRoomManagement.Enum.MarkBy;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
    @Enumerated(EnumType.STRING)
    private MarkBy markedBy;
}
