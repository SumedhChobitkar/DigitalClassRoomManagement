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

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {

    private final AssignmentSubmissionRepository repository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;

    // ====================== SUBMIT ASSIGNMENT =====================
    @Override
    public AssignmentSubmissionDTO submitAssignment(Long studentId, Long assignmentId, MultipartFile file) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        try {
            Blob fileBlob = new SerialBlob(file.getBytes());

            AssignmentSubmission submission = AssignmentSubmission.builder()
                    .assignment(assignment)
                    .student(student)
                    .fileUrl(fileBlob)
                    .submittedAt(LocalDateTime.now())
                    .status(SubmissionStatus.SUBMITTED)
                    .marks(0.0)
                    .feedback("")
                    .build();

            repository.save(submission);

            return convertToDTO(submission);

        } catch (Exception e) {
            log.error("Submission failed: {}", e.getMessage());
            throw new RuntimeException("Error while submitting assignment");
        }
    }

    // ====================== GET SUBMISSION =====================
    @Override
    public AssignmentSubmissionDTO getSubmission(Long submissionId) {

        AssignmentSubmission submission = repository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        return convertToDTO(submission);
    }

    // ====================== UPDATE FILE =====================
    @Override
    public AssignmentSubmissionDTO updateSubmissionFile(Long submissionId, MultipartFile file) {

        AssignmentSubmission submission = repository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        try {
            submission.setFileUrl(new SerialBlob(file.getBytes()));
            submission.setSubmittedAt(LocalDateTime.now());

            repository.save(submission);

            return convertToDTO(submission);

        } catch (Exception e) {
            log.error("File update failed: {}", e.getMessage());
            throw new RuntimeException("Error updating submission file");
        }
    }

    // ====================== UPDATE MARKS & FEEDBACK =====================
    @Override
    public AssignmentSubmissionDTO updateFeedbackAndMarks(Long submissionId, Double marks, String feedback) {

        AssignmentSubmission submission = repository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setMarks(marks);
        submission.setFeedback(feedback);
        submission.setStatus(SubmissionStatus.GRADED);

        repository.save(submission);

        return convertToDTO(submission);
    }

    // ====================== DELETE SUBMISSION =====================
    @Override
    public void deleteSubmission(Long submissionId) {
        repository.deleteById(submissionId);
    }

    // ====================== DTO MAPPER =====================
    private AssignmentSubmissionDTO convertToDTO(AssignmentSubmission submission) {
        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();

        dto.setSubmissionId(submission.getSubmissionId());
        dto.setStudentId(submission.getStudent().getStudentId());
        dto.setAssignmentId(submission.getAssignment().getAssignmentId());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setStatus(submission.getStatus());
        dto.setMarks(submission.getMarks());
        dto.setFeedback(submission.getFeedback());

        return dto;
    }
}
