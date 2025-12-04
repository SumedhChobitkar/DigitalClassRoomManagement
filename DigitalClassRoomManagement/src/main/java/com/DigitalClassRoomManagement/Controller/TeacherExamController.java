package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.ExamQuestion;
import com.DigitalClassRoomManagement.Entity.ExamSubmission;
import com.DigitalClassRoomManagement.Service.ExamQuestionService;
import com.DigitalClassRoomManagement.Service.ExamSubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Teacher Exam APIs", description = "APIs for teachers to manage exam questions and view submissions")
@RestController
@RequestMapping("/api/teacher/exam")
@Slf4j
@CrossOrigin(origins = "*")
public class TeacherExamController {

    @Autowired
    private ExamQuestionService questionRequestService;

    @Autowired
    private ExamSubmissionService examSubmissionService;

    // ------------------ Add Questions to Exam ------------------
    @Operation(summary = "Add Questions", description = "Allows a teacher to add multiple questions to a specific exam")
    @ApiResponse(responseCode = "200", description = "Questions added successfully")
    @ApiResponse(responseCode = "400", description = "Failed to add questions")
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{examId}/questions")
    public ResponseEntity<?> addQuestions(
            @PathVariable Long examId,
            @RequestParam Long teacherId,
            @RequestBody List<ExamQuestion> questions
    ) {
        try {
            log.info("Adding {} questions for examId {} by teacherId {}", questions.size(), examId, teacherId);

            questionRequestService.addQuestions(examId, questions, teacherId);

            return ResponseEntity.ok("Questions added successfully");

        } catch (Exception e) {
            log.error("Failed to add questions for examId {} | Error: {}", examId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Failed to add questions: " + e.getMessage());
        }
    }

    // ------------------ Get Exam Submissions ------------------
    @Operation(summary = "Get Exam Submissions", description = "Fetches all student submissions for a specific exam")
    @ApiResponse(responseCode = "200", description = "Submissions fetched successfully")
    @ApiResponse(responseCode = "500", description = "Failed to fetch submissions")
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/submissions")
    public ResponseEntity<?> getSubmissions(@RequestParam Long examId) {
        log.info("Fetching submissions for examId: {}", examId);

        try {
            List<ExamSubmission> submissions = examSubmissionService.getSubmissionsByExam(examId);

            if (submissions.isEmpty()) {
                log.info("No submissions found for examId: {}", examId);
                return ResponseEntity.ok("No submissions found for this exam.");
            }

            log.info("Found {} submissions for examId: {}", submissions.size(), examId);
            return ResponseEntity.ok(submissions);

        } catch (Exception e) {
            log.error("Failed to fetch submissions for examId {} | Error: {}", examId, e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to fetch submissions: " + e.getMessage());
        }
    }
}
