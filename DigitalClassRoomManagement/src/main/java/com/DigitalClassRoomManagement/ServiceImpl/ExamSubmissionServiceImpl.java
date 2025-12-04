package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.Exam;
import com.DigitalClassRoomManagement.Entity.ExamSubmission;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Repository.ExamRepository;
import com.DigitalClassRoomManagement.Repository.ExamSubmissionRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Service.ExamSubmissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ExamSubmissionServiceImpl implements ExamSubmissionService {

    @Autowired
    private ExamSubmissionRepository examSubmissionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // <---------------------- SUBMIT EXAM ------------------------------>
    @Override
    public ExamSubmission submitExam(Long examId, Long studentId, String answers) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + examId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        boolean alreadySubmitted = examSubmissionRepository
                .existsByExamAndStudent(exam, student);

        if (alreadySubmitted) {
            throw new RuntimeException("Student has already submitted this exam");
        }
        ExamSubmission submission = new ExamSubmission();
        submission.setExam(exam);
        submission.setStudent(student);
        submission.setAnswers(answers);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setSubmissionStatus(LocalDateTime.now()); // Use current timestamp
        return examSubmissionRepository.save(submission);
    }

    // <---------------------- GET SUBMISSIONS ------------------------------>
    @Override
    public List<ExamSubmission> getSubmissionsByExam(Long examId) {
        log.info("Fetching submissions for examId: {}", examId);

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> {
                    log.error("Exam not found with id: {}", examId);
                    return new RuntimeException("Exam not found with id: " + examId);
                });

        List<ExamSubmission> submissions = examSubmissionRepository.findByExam(exam);

        log.info("Fetched {} submissions for examId: {}", submissions.size(), examId);

        return submissions;
    }
}
