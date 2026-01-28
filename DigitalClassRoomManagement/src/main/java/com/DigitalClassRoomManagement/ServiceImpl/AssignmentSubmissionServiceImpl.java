package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.AssignmentSubmissionDTO;
import com.DigitalClassRoomManagement.Entity.Assignment;
import com.DigitalClassRoomManagement.Entity.AssignmentSubmission;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Enum.SubmissionStatus;
import com.DigitalClassRoomManagement.Repository.AssignmentRepository;
import com.DigitalClassRoomManagement.Repository.AssignmentSubmissionRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Service.AssignmentSubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {

    private final AssignmentSubmissionRepository repository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;

    // ====================== SUBMIT ASSIGNMENT ======================
    @Override
    public AssignmentSubmissionDTO submitAssignment(
            Long studentId,
            Long assignmentId,
            MultipartFile file
    ) {

        if (studentId == null) {
            throw new RuntimeException("VALIDATION_FAILED: studentId is null");
        }

        if (assignmentId == null) {
            throw new RuntimeException("VALIDATION_FAILED: assignmentId is null");
        }

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("VALIDATION_FAILED: Uploaded file is empty or missing");
        }

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() ->
                        new RuntimeException("DATA_NOT_FOUND: Assignment not found for id=" + assignmentId)
                );

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("DATA_NOT_FOUND: Student not found for id=" + studentId)
                );

        try {
            AssignmentSubmission submission = AssignmentSubmission.builder()
                    .assignment(assignment)
                    .student(student)
                    .fileData(file.getBytes())
                    .fileName(file.getOriginalFilename())
                    .submittedAt(LocalDateTime.now())
                    .status(SubmissionStatus.SUBMITTED)
                    .marks(0.0)
                    .feedback("")
                    .build();

            repository.save(submission);
            return convertToDTO(submission);

        } catch (Exception e) {
            log.error("SUBMIT_ASSIGNMENT_FAILED", e);
            throw new RuntimeException(
                    "SUBMIT_ASSIGNMENT_FAILED: Unable to save submission. Reason: " + e.getMessage(),
                    e
            );
        }
    }

    // ====================== GET SUBMISSION ======================
    @Override
    public AssignmentSubmissionDTO getSubmission(Long id) {

        if (id == null) {
            throw new RuntimeException("VALIDATION_FAILED: submissionId is null");
        }

        AssignmentSubmission submission = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("DATA_NOT_FOUND: Submission not found for id=" + id)
                );

        return convertToDTO(submission);
    }

    // ====================== UPDATE FILE ======================
    @Override
    public AssignmentSubmissionDTO updateSubmissionFile(
            Long id,
            MultipartFile file
    ) {

        if (id == null) {
            throw new RuntimeException("VALIDATION_FAILED: submissionId is null");
        }

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("VALIDATION_FAILED: Uploaded file is empty or missing");
        }

        AssignmentSubmission submission = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("DATA_NOT_FOUND: Submission not found for id=" + id)
                );

        try {
            submission.setFileData(file.getBytes());
            submission.setFileName(file.getOriginalFilename());
            submission.setSubmittedAt(LocalDateTime.now());

            repository.save(submission);
            return convertToDTO(submission);

        } catch (Exception e) {
            log.error("UPDATE_FILE_FAILED", e);
            throw new RuntimeException(
                    "UPDATE_FILE_FAILED: Unable to update file for submissionId=" + id +
                            ". Reason: " + e.getMessage(),
                    e
            );
        }
    }

    // ====================== UPDATE MARKS & FEEDBACK ======================
    @Override
    public AssignmentSubmissionDTO updateFeedbackAndMarks(
            Long id,
            Double marks,
            String feedback
    ) {

        if (id == null) {
            throw new RuntimeException("VALIDATION_FAILED: submissionId is null");
        }

        if (marks == null || marks < 0) {
            throw new RuntimeException("VALIDATION_FAILED: marks must be >= 0");
        }

        AssignmentSubmission submission = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("DATA_NOT_FOUND: Submission not found for id=" + id)
                );

        submission.setMarks(marks);
        submission.setFeedback(feedback);
        submission.setStatus(SubmissionStatus.GRADED);

        repository.save(submission);
        return convertToDTO(submission);
    }

    // ====================== DELETE SUBMISSION ======================
    @Override
    public void deleteSubmission(Long id) {

        if (id == null) {
            throw new RuntimeException("VALIDATION_FAILED: submissionId is null");
        }

        if (!repository.existsById(id)) {
            throw new RuntimeException("DATA_NOT_FOUND: Submission not found for id=" + id);
        }

        repository.deleteById(id);
    }

    // ====================== DTO MAPPER ======================
    private AssignmentSubmissionDTO convertToDTO(AssignmentSubmission submission) {

        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();

        dto.setSubmissionId(submission.getSubmissionId());
        dto.setStudentId(submission.getStudent().getStudentId());
        dto.setAssignmentId(submission.getAssignment().getAssignmentId());
        dto.setFileName(submission.getFileName());

        dto.setFile(
                (submission.getFileData() != null && submission.getFileData().length > 0)
                        ? "UPLOADED"
                        : "NOT_UPLOADED"
        );

        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setStatus(submission.getStatus());
        dto.setMarks(submission.getMarks());
        dto.setFeedback(submission.getFeedback());

        return dto;
    }
}
