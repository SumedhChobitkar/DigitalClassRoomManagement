package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.FeedbackDto;
import com.DigitalClassRoomManagement.Entity.Feedback;
import com.DigitalClassRoomManagement.Entity.Parent;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Repository.FeedbackRepository;
import com.DigitalClassRoomManagement.Repository.ParentRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public FeedbackDto createFeedback(FeedbackDto feedbackDto) {
        try {
            log.info("Creating feedback for teacherId: {}", feedbackDto.getTeacherId());

            Teacher teacher = teacherRepository.findById(feedbackDto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            Parent parent = null;
            if (feedbackDto.getParentId() != null) {
                parent = parentRepository.findById(feedbackDto.getParentId())
                        .orElseThrow(() -> new RuntimeException("Parent not found"));
            }

            Student student = null;
            if (feedbackDto.getStudentId() != null) {
                student = studentRepository.findById(feedbackDto.getStudentId())
                        .orElseThrow(() -> new RuntimeException("Student not found"));
            }

            Feedback feedback = Feedback.builder()
                    .teacher(teacher)
                    .parent(parent)
                    .student(student)
                    .feedbackText(feedbackDto.getFeedbackText())
                    .rating(feedbackDto.getRating())
                    .subject(feedbackDto.getSubject())
                    .build();

            feedbackRepository.save(feedback);
            log.info("Feedback created successfully. FeedbackId: {}", feedback.getId());

            return toDto(feedback);

        } catch (Exception e) {
            log.error("Error while creating feedback: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create feedback");
        }
    }

    @Override
    public FeedbackDto getFeedbackById(Long id) {
        try {
            log.info("Fetching feedback by id: {}", id);

            Feedback feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Feedback not found"));

            return toDto(feedback);

        } catch (Exception e) {
            log.error("Error fetching feedback by id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch feedback");
        }
    }

    @Override
    public List<FeedbackDto> findFeedbackByStudentIdWithName(Long studentId, String studentName) {
        try {
            log.info("Fetching feedback for studentId: {} with name: {}", studentId, studentName);

            List<Feedback> feedbackList = feedbackRepository.findByStudent_StudentRegIdAndStudent_FirstName(studentId, studentName);

            return feedbackList.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error finding feedback for studentId {} and name {}: {}",
                    studentId, studentName, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch feedback by student details");
        }
    }

    @Override
    public List<FeedbackDto> findFeedbackByParentIdWithName(Long parentId, String parentName) {
        try {
            log.info("Fetching feedback for parentId: {} with name: {}", parentId, parentName);

            //  ADD THESE 3 LINES
            String[] nameParts = parentName.trim().split("\\s+", 2);
            String firstName = nameParts[0].trim();
            String lastName = nameParts.length > 1 ? nameParts[1].trim() : "";

            List<Feedback> feedbackList =
                   // feedbackRepository.findByParent_ParentIdAndParent_Name(parentId, parentName);
                    feedbackRepository
                            .findByParent_ParentIdAndParent_FirstNameAndParent_LastName(
                                    parentId,
                                    firstName,
                                    lastName
                            );



            return feedbackList.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error while fetching feedback for parentId {} and name {}: {}",
                    parentId, parentName, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch feedback by parent details");
        }
    }


    @Override
    public List<FeedbackDto> getAllFeedbacks() {
        try {
            log.info("Fetching all feedbacks");

            return feedbackRepository.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error fetching all feedbacks: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch feedback list");
        }
    }

    @Override
    public FeedbackDto markReviewed(Long id) {
        try {
            log.info("Marking feedback reviewed. FeedbackId: {}", id);

            Feedback feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Feedback not found"));

            feedback.setReviewedByAdmin(true);
            feedbackRepository.save(feedback);

            log.info("Feedback marked as reviewed. FeedbackId: {}", id);

            return toDto(feedback);

        } catch (Exception e) {
            log.error("Error marking feedback {} as reviewed: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to mark feedback reviewed");
        }
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        try {
            log.info("Deleting feedback with ID: {}", feedbackId);

            Feedback feedback = feedbackRepository.findById(feedbackId)
                    .orElseThrow(() -> new RuntimeException("Feedback not found"));

            feedbackRepository.delete(feedback);

            log.info("Feedback deleted successfully. ID: {}", feedbackId);

        } catch (Exception e) {
            log.error("Error deleting feedback {}: {}", feedbackId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete feedback");
        }
    }


    // ---------------- Helper Method ----------------
    public FeedbackDto toDto(Feedback feedback) {
        return FeedbackDto.builder()
                .id(feedback.getId())
                .teacherId(feedback.getTeacher().getId())
                .parentId(feedback.getParent() != null ? feedback.getParent().getParentId() : null)
                .studentId(feedback.getStudent() != null ? feedback.getStudent().getStudentId() : null)
                .feedbackText(feedback.getFeedbackText())
                .rating(feedback.getRating())
                .subject(feedback.getSubject())
                .createdAt(feedback.getCreatedAt())
                .reviewedByAdmin(feedback.isReviewedByAdmin())
                .build();
    }
}
