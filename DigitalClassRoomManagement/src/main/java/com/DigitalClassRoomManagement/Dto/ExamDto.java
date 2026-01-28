package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.ExamMode;
import jakarta.validation.constraints.NotNull;
import com.DigitalClassRoomManagement.Enum.ExamStatus;
import com.DigitalClassRoomManagement.Enum.SubmissionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDto {


    private Long examId;
    private Long adminId;
    private Long teacherId;
    private String term;
    private LocalDate examDate;
    private String day;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration;
    private BigDecimal totalMarks;
    private Long locationId;
    private ExamMode examMode;
    private List<ExamQuestionDto> questions;
    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus submissionStatus;



}
