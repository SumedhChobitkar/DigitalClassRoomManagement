package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ExamSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)   // FIXED
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private LocalDateTime submittedAt;

    @Column(columnDefinition = "TEXT")
    private String answers; // JSON string

    private BigDecimal obtainedMarks;

    private LocalDateTime submissionStatus;  // FIX naming (was SubmissionStatus)

    private Long evaluatedBy;

    private LocalDateTime evaluatedAt;
}
