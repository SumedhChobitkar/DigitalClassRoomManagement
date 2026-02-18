package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Entity.Attendance;
import com.DigitalClassRoomManagement.Enum.AttendanceStatus;
import com.DigitalClassRoomManagement.Enum.MarkBy;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
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
    private Long sessionId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Column(unique = true)
    private String email;
    private long totalSessions;
    private long attendedSessions;
    private double attendancePercentage;
    private List<Attendance> attendanceDetails;

}
