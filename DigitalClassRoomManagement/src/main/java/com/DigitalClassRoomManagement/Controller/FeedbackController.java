package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.FeedbackDto;
import com.DigitalClassRoomManagement.Exception.FeedbackNotFoundException;
import com.DigitalClassRoomManagement.Service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin Feedback APIs", description = "Operations related to Feedback")
@RestController
@RequestMapping("/api/feedback")
@Slf4j
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // <---------------- Create Feedback ----------------->
    @Operation(summary = "Create Feedback", description = "Creates a new feedback entry")
    @ApiResponse(responseCode = "201", description = "Feedback created successfully")
    @ApiResponse(responseCode = "500", description = "Failed to create feedback")
    @PostMapping("/FeedbackCreate")
    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT','ADMIN','PRINCIPAL')")
    public ResponseEntity<?> createFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        log.info("Request to create feedback: {}", feedbackDto);
        try {
            FeedbackDto createdFeedback = feedbackService.createFeedback(feedbackDto);
            log.info("Feedback created successfully: {}", createdFeedback);
            return ResponseEntity.status(201).body(createdFeedback);
        } catch (Exception e) {
            log.error("Error creating feedback", e);
            return ResponseEntity.status(500).body("Failed to create feedback");
        }
    }

    // <---------------- Get Feedback by ID ----------------->
    @Operation(summary = "Get Feedback by ID", description = "Fetches a feedback entry by its ID")
    @ApiResponse(responseCode = "200", description = "Feedback retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Feedback not found")
    @GetMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN','PRINCIPAL')")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long id) {
        log.info("Request to fetch feedback with ID: {}", id);
        try {
            FeedbackDto feedback = feedbackService.getFeedbackById(id);
            log.info("Feedback retrieved: {}", feedback);
            return ResponseEntity.ok(feedback);
        } catch (FeedbackNotFoundException e) {
            log.warn("Feedback not found with ID: {}", id);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error fetching feedback", e);
            return ResponseEntity.status(500).body("Failed to fetch feedback");
        }
    }

    // <---------------- Get Feedback by Student (ID + Name) ----------------->
    @Operation(summary = "Get Feedback by Student", description = "Fetches feedback using student ID and name")
    @ApiResponse(responseCode = "200", description = "Feedback retrieved successfully")
    @GetMapping("/FeedBack_get_student")
     @PreAuthorize("hasRole('ADMIN','PRINCIPAL')")
    public ResponseEntity<?> getFeedbackByStudent(
            @RequestParam Long studentId,
            @RequestParam String studentName) {

        log.info("Fetching feedback by studentId: {} and name: {}", studentId, studentName);
        try {
            List<FeedbackDto> feedbackList =
                    feedbackService.findFeedbackByStudentIdWithName(studentId, studentName);

            log.info("Feedback fetched successfully. Total: {}", feedbackList.size());
            return ResponseEntity.ok(feedbackList);
        } catch (Exception e) {
            log.error("Error fetching feedback for studentId {} & name {}: {}", studentId, studentName, e.getMessage());
            return ResponseEntity.status(500)
                    .body("Failed to fetch feedback for studentId = " + studentId + ", name = " + studentName);
        }
    }

    // <---------------- Get Feedback by Parent (ID + Name) ----------------->
    @Operation(summary = "Get Feedback by Parent", description = "Fetches feedback using parent ID and name")
    @ApiResponse(responseCode = "200", description = "Feedback retrieved successfully")
     @PreAuthorize("hasRole('ADMIN','PRINCIPAL')")
    @GetMapping("/FeedBack_get_parent")
    @Query("SELECT f FROM Feedback f WHERE f.parent.id = :parentId AND f.parent.name = :parentName")
    public ResponseEntity<?> getFeedbackByParent(
            @RequestParam Long parentId,
            @RequestParam String parentName) {

        log.info("Fetching feedback by parentId: {} and name: {}", parentId, parentName);
        try {
            List<FeedbackDto> feedbackList =
                    feedbackService.findFeedbackByParentIdWithName(parentId, parentName);

            log.info("Feedback fetched successfully. Total: {}", feedbackList.size());
            return ResponseEntity.ok(feedbackList);
        } catch (Exception e) {
            log.error("Error fetching feedback for parentId {} & name {}: {}", parentId, parentName, e.getMessage());
            return ResponseEntity.status(500)
                    .body("Failed to fetch feedback for parentId = " + parentId +
                            ", parentName = " + parentName);
        }
    }

    // <---------------- Get All Feedback ----------------->
    @Operation(summary = "Get All Feedback", description = "Fetches all feedback entries")
    @ApiResponse(responseCode = "200", description = "Feedback list retrieved successfully")
    @GetMapping("/getAll")
     @PreAuthorize("hasRole('ADMIN','PRINCIPAL')")
    public ResponseEntity<?> getAllFeedbacks() {
        log.info("Fetching all feedback entries");
        try {
            List<FeedbackDto> feedbackList = feedbackService.getAllFeedbacks();
            log.info("Total feedback entries fetched: {}", feedbackList.size());
            return ResponseEntity.ok(feedbackList);
        } catch (Exception e) {
            log.error("Error fetching feedback list", e);
            return ResponseEntity.status(500).body("Failed to fetch feedback list");
        }
    }

    // <---------------- Mark Feedback Reviewed ----------------->
    @Operation(summary = "Mark Feedback as Reviewed", description = "Marks a feedback as reviewed by admin")
    @ApiResponse(responseCode = "200", description = "Feedback marked as reviewed successfully")
    @ApiResponse(responseCode = "404", description = "Feedback not found")
    @PutMapping("/{id}/review")
     @PreAuthorize("hasRole('ADMIN','PRINCIPAL')")
    public ResponseEntity<?> markFeedbackReviewed(@PathVariable Long id) {
        log.info("Marking feedback {} as reviewed", id);
        try {
            FeedbackDto reviewedFeedback = feedbackService.markReviewed(id);
            log.info("Feedback reviewed successfully: {}", reviewedFeedback);
            return ResponseEntity.ok(reviewedFeedback);
        } catch (FeedbackNotFoundException e) {
            log.warn("Feedback not found with ID: {}", id);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error reviewing feedback", e);
            return ResponseEntity.status(500).body("Failed to review feedback");
        }
    }

    // <---------------- Delete Feedback ----------------->
    @Operation(summary = "Delete Feedback", description = "Deletes a feedback by ID")
    @ApiResponse(responseCode = "200", description = "Feedback deleted successfully")
    @ApiResponse(responseCode = "404", description = "Feedback not found")
    @DeleteMapping("/Delete/{id}")
    @PreAuthorize("hasRole('ADMIN','PRINCIPAL')")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        log.info("Request to delete feedback with ID: {}", id);
        try {
            feedbackService.deleteFeedback(id);
            log.info("Feedback deleted successfully: {}", id);
            return ResponseEntity.ok("Feedback deleted successfully");
        } catch (FeedbackNotFoundException e) {
            log.warn("Feedback not found with ID: {}", id);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting feedback", e);
            return ResponseEntity.status(500).body("Failed to delete feedback");
        }
    }
}
