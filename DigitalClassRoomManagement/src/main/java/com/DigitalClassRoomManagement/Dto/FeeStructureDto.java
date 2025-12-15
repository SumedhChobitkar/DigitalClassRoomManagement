package com.DigitalClassRoomManagement.Dto;


import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;


@Getter
@Setter
public class FeeStructureDto {
    private Long feeId;
    private String feeName;
    private BigDecimal amount;
}