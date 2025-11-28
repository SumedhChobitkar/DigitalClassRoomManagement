package com.DigitalClassRoomManagement.Service;


import com.DigitalClassRoomManagement.Dto.AssignmentSubmissionDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentSubmissionService {

    AssignmentSubmissionDTO submitAssignment (Long studentId, Long assignmentId, MultipartFile file);

    AssignmentSubmissionDTO getSubmission(Long id);

    AssignmentSubmissionDTO updateSubmissionFile(Long id, MultipartFile file);

    AssignmentSubmissionDTO updateFeedbackAndMarks(Long id, Double marks, String feedback);

    void deleteSubmission(Long id);
}
