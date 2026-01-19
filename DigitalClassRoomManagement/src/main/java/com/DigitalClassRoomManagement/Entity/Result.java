package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.ResultStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "studentRegId")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", referencedColumnName = "exam_id", nullable = false)   // FIXED
    private Exam exam;

    private BigDecimal obtainedMarks;
    private BigDecimal percentage;
    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ResultStatus status;

    private LocalDateTime publishedAt;
}
