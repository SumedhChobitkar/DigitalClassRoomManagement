package com.DigitalClassRoomManagement.Entity;


import com.DigitalClassRoomManagement.Enum.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity

@Table(name = "assignment_submissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"assignment_id", "student_id"}))
public class AssignmentSubmission {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long submissionId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assignment_id", nullable = false)
        private Assignment assignment;


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "student_id", nullable = false)
        private Student student;

        private Blob fileUrl;

        private LocalDateTime submittedAt;

        @Enumerated(EnumType.STRING)
        private SubmissionStatus status;

        @Column(nullable = false)
        private Double marks;

        @Column(columnDefinition = "TEXT", nullable = false)
        private String feedback;


    }





