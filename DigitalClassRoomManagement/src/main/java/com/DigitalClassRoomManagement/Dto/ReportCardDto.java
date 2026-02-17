package com.DigitalClassRoomManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportCardDto {
    private Long reportCardId;
    private Long studentId;
    private Long subjectId;
    private Long resultId;
    private Long submissionId;
    private String term;
    private BigDecimal totalMarks;
    private BigDecimal obtainedMarks;
    private BigDecimal percentage;
    private String grade;
    private String remarks;
    private LocalDateTime generatedAt;
}
