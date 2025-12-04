package com.DigitalClassRoomManagement.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignTeacherRequestDto {
    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
}
