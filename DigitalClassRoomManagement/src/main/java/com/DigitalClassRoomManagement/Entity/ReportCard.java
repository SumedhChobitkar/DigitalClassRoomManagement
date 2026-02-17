package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report_cards")
@Data
public class ReportCard {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long reportCardId;


        // Link to Student entity
        @ManyToOne()
        @JoinColumn(name = "student_id", nullable = false)
        private Student student;

        // Optional: link to Subject entity (if report card is subject-wise)
        @ManyToOne()
        @JoinColumn(name = "subject_id", nullable = false)
        private Subject subject;

        @ManyToOne()
        @JoinColumn(name = "result_id", nullable = false)
        private Result result; //

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "submission_id", nullable = false)
      @JsonIgnore
      private ExamSubmission submission;



    private String term;
        private BigDecimal totalMarks;
        private BigDecimal obtainedMarks;
        private BigDecimal percentage;
        private String grade;
        private String remarks;
        private LocalDateTime generatedAt;


        @PrePersist
        public void prePersist() { generatedAt = LocalDateTime.now(); }
    }
