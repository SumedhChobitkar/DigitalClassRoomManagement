package com.DigitalClassRoomManagement.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackDto {

    private Long id;

    @NotNull(message = "Teacher ID cannot be null")
    private Long teacherId;

    @NotNull(message = "Parent ID cannot be null")
    private Long parentId;

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @NotBlank(message = "Feedback text cannot be blank")
    private String feedbackText;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    @NotBlank(message = "Subject cannot be blank")
    private String subject;

    private LocalDateTime createdAt;

    private boolean reviewedByAdmin;
}
