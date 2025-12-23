package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.LeaveRequestStatus;
import com.DigitalClassRoomManagement.Enum.LeaveType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import lombok.*;



//import com.DigitalClassRoomManagement.Enum.LeaveRequestStatus;

//import com.DigitalClassRoomManagement.Enum.LeaveType;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;

    @NoArgsConstructor

    @AllArgsConstructor

    @Setter

    @Getter

    @Builder

    @Entity

    public class LeaveRequest {

        @Id

        @GeneratedValue(strategy = GenerationType.AUTO)

        private Long leaveId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JsonIgnore
        @JoinColumn(name = "userId", nullable = false)
        private User user; // (FK → User → Student/Teacher)

        @Enumerated(EnumType.STRING)

        @Column(nullable = false)

        private LeaveType leaveType; // (ENUM: Sick, Casual, Exam, Personal, Emergency)

        @Column(nullable = false)

        private LocalDate fromDate;

        @Column(nullable = false)

        private LocalDate toDate;

        @Column(nullable = false)

        private String reason; // (Text)

        @Enumerated(EnumType.STRING)

        @Column(nullable = false)

        private LeaveRequestStatus status; // (ENUM: Pending, Approved, Rejected)

        @Column(nullable = false)

        private LocalDate appliedOn; // (DateTime)

        @ManyToOne(fetch = FetchType.LAZY)

        @JoinColumn(name = "teacherId")

        private Teacher approvedByTeacher; // (FK → Teacher/Admin, Nullable)

 @ManyToOne(fetch = FetchType.LAZY)

        @JoinColumn(name = "approvedByAdmin")

      private Admin approvedByAdmin;

        private LocalDate approvalDate; // (Nullable DateTime)

        private String remarks; // (Optional String)

    }






