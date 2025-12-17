package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.AttendanceStatus;
import com.DigitalClassRoomManagement.Enum.MarkBy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

//    @Column(nullable = false)
    private LocalDate date;

    private LocalDateTime joinTime;

    private LocalDateTime exitTime;

    private Long durationMinutes;

    private Long sessionId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AttendanceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MarkBy markedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

//    @PrePersist
//    protected void onJoin() {
//        if (this.joinTime == null) {
//            this.joinTime = LocalDateTime.now();
//        }
//    }
//
//    @PreUpdate
//    protected void onLeave() {
//        if (this.exitTime == null && this.status != AttendanceStatus.PRESENT) {
//            // leave time will be auto-set only when status is changed (e.g., leaving)
//            this.exitTime = LocalDateTime.now();
//        }
//    }


}
