package com.DigitalClassRoomManagement.Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequestDTO {
//    private Long studentRegId;
//    private Long parentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amount;
    private Long classId;
    private Long feeId;
}

