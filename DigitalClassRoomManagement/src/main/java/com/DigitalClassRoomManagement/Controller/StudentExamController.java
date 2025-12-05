package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import com.DigitalClassRoomManagement.Dto.ResultDto;
import com.DigitalClassRoomManagement.Entity.ExamSubmission;
import com.DigitalClassRoomManagement.Service.ExamService;
import com.DigitalClassRoomManagement.Service.ExamSubmissionService;
import com.DigitalClassRoomManagement.Service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Student Exam APIs", description = "Operations for students to view and submit exams")
@RestController
@RequestMapping("/api/student/exam")
@Slf4j
@CrossOrigin(origins = "*")
public class StudentExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamSubmissionService examSubmissionService;

    @Autowired
    private ResultService resultService;

    // ------------------ Submit Exam ------------------
    @Operation(summary = "Submit Exam", description = "Allows a student to submit answers for a specific exam")
    @ApiResponse(responseCode = "200", description = "Exam submitted successfully")
    @ApiResponse(responseCode = "500", description = "Failed to submit exam")
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/submit")
    public ResponseEntity<?> submitExam(
            @RequestParam Long examId,
            @RequestParam Long studentId,
            @RequestBody ExamSubmission examSubmission
    ) {
        log.info("Received exam submission request | examId: {}, studentId: {}", examId, studentId);

        try {
            ExamSubmission savedSubmission =
                    examSubmissionService.submitExam(examId, studentId, examSubmission.getAnswers());

            log.info("Exam submitted successfully for studentId: {}", studentId);
            return ResponseEntity.ok(savedSubmission);

        } catch (Exception e) {
            log.error("Error while submitting exam for studentId={}: {}", studentId, e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to submit exam: " + e.getMessage());
        }
    }

    // ------------------ Get Scheduled Exams ------------------
    @Operation(summary = "Get Scheduled Exams", description = "Fetches all exams that are upcoming or ongoing for students")
    @ApiResponse(responseCode = "200", description = "Exams fetched successfully")
    @ApiResponse(responseCode = "500", description = "Failed to fetch exams")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/scheduled")
    public ResponseEntity<?> getScheduledExams() {
        log.info("Fetching scheduled exams for students");

        try {
            List<ExamDto> allExams = examService.getAllExams();
            LocalDateTime now = LocalDateTime.now();

            List<ExamDto> scheduledExams = allExams.stream()
                    .filter(exam -> exam.getStartTime().isAfter(now) ||
                            (exam.getStartTime().isBefore(now) && exam.getEndTime().isAfter(now)))
                    .collect(Collectors.toList());

            if (scheduledExams.isEmpty()) {
                log.info("No scheduled or ongoing exams found");
                return ResponseEntity.ok("No scheduled exams found");
            }

            log.info("Scheduled exams retrieved successfully. Count: {}", scheduledExams.size());
            return ResponseEntity.ok(scheduledExams);

        } catch (Exception e) {
            log.error("Error fetching scheduled exams: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error fetching scheduled exams: " + e.getMessage());
        }
    }

    // ------------------ View Results ------------------
    @Operation(summary = "View Exam Results", description = "Fetch results for students with optional filters by studentId and/or examId")
    @ApiResponse(responseCode = "200", description = "Results fetched successfully")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/result")
    public ResponseEntity<?> viewResults(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long examId) {

        log.info("Fetching results with studentId={} and examId={}", studentId, examId);

        try {
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

            log.info("Results fetched successfully. Count: {}", results.size());
            return ResponseEntity.ok(results);

        } catch (Exception e) {
            log.error("Error fetching results: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to fetch results: " + e.getMessage());
        }
    }
}
