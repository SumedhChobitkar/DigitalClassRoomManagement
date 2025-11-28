package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Service.AssignmentSubmissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Slf4j
public class AssignmentSubmissionController {

    @Autowired
    private AssignmentSubmissionService service;

//-------submitAssignment------------------------------
    @PostMapping("/submit")
    public ResponseEntity<?> submitAssignment(
            @RequestParam("studentId") Long studentId,
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        log.info("Submit assignment request received - studentId={}, assignmentId={}", studentId, assignmentId);

        try {
            return ResponseEntity.ok(service.submitAssignment(studentId, assignmentId, file));
        } catch (Exception e) {
            log.error("Error submitting assignment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // -------- getSubmission -----------------------------
    @GetMapping("/getSubmissionsById/{id}")
    public ResponseEntity<?> getSubmission(@PathVariable Long id) {
        log.info("Get submission request - id={}", id);
        try {
            return ResponseEntity.ok(service.getSubmission(id));
        } catch (Exception e) {
            log.error("Error getting submission: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ---------- updateSubmissionFile------------------------------
    @PutMapping("/updateSubmissionFileById/{id}")
    public ResponseEntity<?> updateSubmissionFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        log.info("Update file request - submissionId={}", id);

        try {
            return ResponseEntity.ok(service.updateSubmissionFile(id, file));
        } catch (Exception e) {
            log.error("Error updating file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    //--------updateFeedbackAndMarks------------------------------
    @PutMapping("/updateFeedbackAndMarksById/{id}")
    public ResponseEntity<?> updateFeedbackAndMarks(
            @PathVariable Long id,
            @RequestParam Double marks,
            @RequestParam String feedback) {

        log.info("Update feedback request - id={}, marks={}", id, marks);

        try {
            return ResponseEntity.ok(service.updateFeedbackAndMarks(id, marks, feedback));
        } catch (Exception e) {
            log.error("Error updating feedback: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    //---------- deleteSubmission ----------------------------

    @DeleteMapping("/deleteSubmissionById/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable Long id) {
        log.info("Delete submission request - id={}", id);

        try {
            service.deleteSubmission(id);
            return ResponseEntity.ok(Map.of("message", "Submission deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting submission: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

