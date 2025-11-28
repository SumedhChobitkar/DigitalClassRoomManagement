package com.DigitalClassRoomManagement.Dto;


import com.DigitalClassRoomManagement.Enum.ResultStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ResultDto {

    private Long resultId;
    private Long studentId;
    private Long examId;
    private BigDecimal obtainedMarks;
    private BigDecimal percentage;
    private String grade;
    private ResultStatus status;
    private LocalDateTime publishedAt;
}
