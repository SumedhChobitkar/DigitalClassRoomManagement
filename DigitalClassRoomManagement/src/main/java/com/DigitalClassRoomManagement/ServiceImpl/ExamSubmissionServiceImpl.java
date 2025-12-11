package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Enum.SubmissionStatus;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Service.ExamSubmissionService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private ExamQuestionRepository questionRepository;

    @Autowired
    private ExamSubmissionAnswerRepository answerRepository;

    // <---------------------- SUBMIT EXAM ------------------------------>
    @Override
    @Transactional
    public void submitExam(Long studentId, Long examId, Map<Long, String> answersMap) {
        log.info("Submitting exam. studentId={}, examId={}", studentId, examId);

        try {
            // Fetch student
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        log.error("Student not found with id={}", studentId);
                        return new RuntimeException("Student not found");
                    });

            // Fetch exam
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> {
                        log.error("Exam not found with id={}", examId);
                        return new RuntimeException("Exam not found");
                    });

            // Create ExamSubmission
            ExamSubmission submission = ExamSubmission.builder()
                    .student(student)
                    .exam(exam)
                    .submittedAt(LocalDateTime.now())
                    .submissionStatus(LocalDateTime.now())
                    .build();
            examSubmissionRepository.save(submission);
            log.info("ExamSubmission created: submissionId={}", submission.getSubmissionId() );

            // Create ExamSubmissionAnswer list
            List<ExamSubmissionAnswer> submissionAnswers = new ArrayList<>();
            for (Map.Entry<Long, String> entry : answersMap.entrySet()) {
                Long questionId = entry.getKey();
                String answerText = entry.getValue();

                ExamQuestion question = questionRepository.findById(questionId)
                        .orElseThrow(() -> {
                            log.error("Question not found: questionId={}", questionId);
                            return new RuntimeException("Question not found: " + questionId);
                        });

                ExamSubmissionAnswer answer = ExamSubmissionAnswer.builder()
                        .submission(submission)
                        .student(student)
                        .question(question)
                        .answer(answerText)
                        .marksObtained(null)
                        .build();

                submissionAnswers.add(answer);
            }

            // Save answers
            answerRepository.saveAll(submissionAnswers);
            log.info("Saved {} answers for submissionId={}", submissionAnswers.size(), submission.getSubmissionId());

            // Link answers to submission
            submission.setAnswers(submissionAnswers);
            examSubmissionRepository.save(submission);

            log.info("Exam submission completed successfully: submissionId={}", submission.getSubmissionId());

        } catch (Exception e) {
            log.error("Error while submitting exam. studentId={}, examId={}, error={}",
                    studentId, examId, e.getMessage(), e);
            throw e;
        }
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
