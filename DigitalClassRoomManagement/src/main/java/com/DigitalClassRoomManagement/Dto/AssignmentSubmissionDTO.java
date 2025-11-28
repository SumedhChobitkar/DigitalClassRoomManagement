package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
    public class AssignmentSubmissionDTO {

    private Long submissionId;
    private Long studentId;
    private Long assignmentId;
    private MultipartFile file;
    private LocalDateTime submittedAt;
    private SubmissionStatus status;
    private Double marks;
    private String feedback;

    }


