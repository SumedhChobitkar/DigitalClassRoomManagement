package com.DigitalClassRoomManagement.Dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyMaterialDto {

    private Long materialId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "Notes|Worksheet|LessonPlan",
            message = "Type must be either Notes, Worksheet, or LessonPlan")
    private String type;

    @NotBlank(message = "File URL is required")
    @Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid file URL")
    private String fileUrl;

    @PastOrPresent(message = "Upload date cannot be in the future")
    private LocalDate uploadDate;
}
