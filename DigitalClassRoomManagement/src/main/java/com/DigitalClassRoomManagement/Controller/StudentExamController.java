package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ExamDto;
//import com.DigitalClassRoomManagement.Dto.ResultDto;
//import com.DigitalClassRoomManagement.Entity.ExamSubmission;
import com.DigitalClassRoomManagement.Dto.ResultDto;
import com.DigitalClassRoomManagement.Entity.ExamSubmission;
import com.DigitalClassRoomManagement.Service.ExamService;
//import com.DigitalClassRoomManagement.Service.ExamSubmissionService;
//import com.DigitalClassRoomManagement.Service.ResultService;
import com.DigitalClassRoomManagement.Service.ExamSubmissionService;
import com.DigitalClassRoomManagement.Service.ResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*")
@Slf4j
public class StudentExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamSubmissionService examSubmissionService;

    @Autowired
    private ResultService resultService;

    // <-----------------------SUBMIT EXAM---------------------------->
    @PostMapping("/submit")
    public ResponseEntity<?> submitExam(
            @RequestParam Long examId,
            @RequestParam Long studentId,
            @RequestBody ExamSubmission examSubmission
    ) {
        log.info("Received exam submission request | examId: {}, studentId: {}", examId, studentId);

        try {
            // Use the answers field from the request body
            ExamSubmission savedSubmission =
                    examSubmissionService.submitExam(examId, studentId, examSubmission.getAnswers());

            log.info("Exam submitted successfully for studentId: {}", studentId);
            return ResponseEntity.ok(savedSubmission);

        } catch (Exception e) {
            log.error("Error while submitting exam for studentId: {} | Error: {}", studentId, e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("Failed to submit exam: " + e.getMessage());
        }
    }


    // <----------------GET ALL EXAMS-------------------------------->
    @GetMapping("/getAllExam")
    public ResponseEntity<?> getScheduledExams() {
        log.info("Fetching scheduled exams for students");

        try {
            List<ExamDto> allExams = examService.getAllExams();
            LocalDateTime now = LocalDateTime.now();

            List<ExamDto> scheduledExams = allExams.stream()
                    .filter(exam ->
                            exam.getStartTime().isAfter(now) || // upcoming
                                    (exam.getStartTime().isBefore(now) && exam.getEndTime().isAfter(now)) // ongoing
                    )
                    .collect(Collectors.toList());

            if (scheduledExams.isEmpty()) {
                log.info("No scheduled or ongoing exams found");
                return ResponseEntity.ok("No scheduled exams found");
            }

            log.info("Scheduled exams retrieved successfully. Count: {}", scheduledExams.size());
            return ResponseEntity.ok(scheduledExams);

        } catch (Exception e) {
            log.error("Error fetching scheduled exams: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("Error fetching scheduled exams: " + e.getMessage());
        }
    }


    /**
     * GET /student/exam/result
     * View all results or filter by studentId and/or examId
     * Example:
     *   /student/exam/result
     *   /student/exam/result?studentId=1
     *   /student/exam/result?examId=2
     *   /student/exam/result?studentId=1&examId=2
     */
    @GetMapping("/result")
    public ResponseEntity<List<ResultDto>> viewResults(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long examId) {

        log.info("Fetching results with studentId={} and examId={}", studentId, examId);

        List<ResultDto> results;

        if (studentId != null && examId != null) {
            results = resultService.getResultsByStudentAndExam(studentId, examId);
        } else if (studentId != null) {
            results = resultService.getResultsByStudent(studentId);
        } else if (examId != null) {
            results = resultService.getResultsByExam(examId);
        } else {
            results = resultService.getAllResults();
        }

        return ResponseEntity.ok(results);
    }
}
