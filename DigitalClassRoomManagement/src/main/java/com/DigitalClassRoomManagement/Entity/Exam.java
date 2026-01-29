package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.ExamMode;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.DigitalClassRoomManagement.Enum.ExamStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Table(name = "exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "exam_id")
        private Long examId;

       @ManyToOne(fetch = FetchType.LAZY, optional = false)
       @JoinColumn(name = "admin_id", nullable = false)
       private Admin admin;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "teacher_id", nullable = false)
        private Teacher teacher;

        @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
        private List<ExamQuestion> questions;

        @Column(nullable = false)
        private String term;

        @Column(name = "start_time", nullable = false)
        private LocalDateTime startTime;

        @Column(name = "end_time", nullable = false)
        private LocalDateTime endTime;

        @Column(nullable = false)
        private Integer duration; // in minutes

        @DecimalMin("0.0")
        private BigDecimal totalMarks;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    //@JsonBackReference
    @JsonIgnore
    private Location location;

    @Enumerated(EnumType.STRING)
    private ExamMode examMode;
       @Enumerated(EnumType.STRING)
       @Column(nullable = false)
       private ExamStatus status;

       private LocalDate examDate;
       private String day;



}
