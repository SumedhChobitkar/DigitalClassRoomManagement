package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import com.DigitalClassRoomManagement.Dto.ResultDto;
import com.DigitalClassRoomManagement.Service.ExamService;
import com.DigitalClassRoomManagement.Service.ExamSubmissionService;
import com.DigitalClassRoomManagement.Service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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


    // <-------------------- Submit Exam ------------------------------>
    @Operation(summary = "Submit Exam", description = "Allows a student to submit answers for a specific exam")
    @ApiResponse(responseCode = "200", description = "Exam submitted successfully")
    @ApiResponse(responseCode = "500", description = "Failed to submit exam")
    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitExam(
            @RequestParam Long studentId,
            @RequestParam Long examId,
            @RequestBody Map<Long, String> answers
    ) {

        log.info("API Call: submitExam studentId={}, examId={}, answersCount={}",
                studentId, examId, answers != null ? answers.size() : 0);

        try {
            examSubmissionService.submitExam(studentId, examId, answers);

            log.info("Exam submitted successfully for studentId={}, examId={}", studentId, examId);
            return ResponseEntity.ok("Exam submitted successfully");

        } catch (RuntimeException ex) {
            log.error("Failed to submit exam. studentId={}, examId={}, message={}",
                    studentId, examId, ex.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to submit exam: " + ex.getMessage());

        } catch (Exception ex) {
            log.error("Unexpected error while submitting exam", ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while submitting the exam.");
        }
    }


    // <------------------- Get Scheduled Exams ---------------------->
    @Operation(summary = "Get Scheduled Exams", description = "Fetch all upcoming and ongoing exams")
    @ApiResponse(responseCode = "200", description = "Scheduled exams fetched successfully")
    @ApiResponse(responseCode = "500", description = "Failed to fetch scheduled exams")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER','ADMIN','PRINCIPAL')")
    @GetMapping("/scheduled")
    public ResponseEntity<List<ExamDto>> getScheduledExams() {

        log.info("Request received: Fetch scheduled exams");

        try {
            List<ExamDto> exams = examService.getAllExamList();

            log.info("Scheduled exams fetched successfully. Count: {}", exams.size());

            return ResponseEntity.ok(exams);

        } catch (Exception e) {
            log.error("Error occurred while fetching scheduled exams", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    //  <--------------------- View Results ---------------------------->
    @Operation(summary = "View Exam Results", description = "Fetch results with optional filters by studentId and examId")
    @ApiResponse(responseCode = "200", description = "Results fetched successfully")
    @PreAuthorize("hasAnyRole('STUDENT','PARENT')")
    @GetMapping("/result")
    public ResponseEntity<?> viewResults(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long examId
    ) {

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
