package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ExamQuestionDto;
import com.DigitalClassRoomManagement.Entity.ExamQuestion;
import com.DigitalClassRoomManagement.Entity.ExamSubmission;
import com.DigitalClassRoomManagement.Service.ExamQuestionService;
import com.DigitalClassRoomManagement.Service.ExamSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/TeacherExam")
@CrossOrigin(origins = "*")
@Slf4j
public class TeacherExamController {

    @Autowired
    private ExamQuestionService questionRequestService;

    @Autowired
    private ExamSubmissionService examSubmissionService;

    @PostMapping("/{examId}/questions")
    public ResponseEntity<?> addQuestions(
            @PathVariable Long examId,
            @RequestParam Long teacherId,
            @RequestBody List<ExamQuestion> questions
    ) {
        try {
            log.info("API Request Add {} questions for examId {} by teacherId {}",
                    questions.size(), examId, teacherId);

            questionRequestService.addQuestions(examId, questions, teacherId);

            return ResponseEntity.ok("Questions added successfully");

        } catch (Exception e) {
            log.error("API Error Failed to add multiple questions for examId {}: {}", examId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

     //<-------------------GET EXAM SUBMISSION BYE ID------------------------->

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
            log.error("Error fetching submissions for examId: {} | Error: {}", examId, e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("Failed to fetch submissions: " + e.getMessage());
        }
    }

}
