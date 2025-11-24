package com.DigitalClassRoomManagement.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentDto {

    private Long assignmentId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

     //File Details
//    private String fileName;     //  "assignment1.pdf"
//    private String fileType;     //  "application/pdf"
    private String fileData;     //  "uploaded"

    @NotNull(message = "Due date cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dueDate;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = "Class ID cannot be null")
    private Long classId;

    @NotNull(message = "Section ID cannot be null")
    private Long sectionId;

    @NotNull(message = "Subject ID cannot be null")
    private Long subjectId;

    @NotNull(message = "Teacher ID cannot be null")
    private Long teacherId;
}
