package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import com.DigitalClassRoomManagement.Exception.ExamNotFoundException;
import com.DigitalClassRoomManagement.Service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin Exam APIs", description = "Operations related to Exams")
@RestController
@RequestMapping("/api/exams")
@Slf4j
@CrossOrigin(origins = "*")
public class AdminExamController {

    @Autowired
    private ExamService examService;

    // <---------------- Create Exam ----------------->
    @Operation(summary = "Create Exam", description = "Creates a new exam")
    @ApiResponse(responseCode = "201", description = "Exam created successfully")
    @ApiResponse(responseCode = "500", description = "Failed to create exam")
    @PostMapping("/Exam-Create")
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL')")
    public ResponseEntity<?> createExam(@RequestBody ExamDto examDto) {
        log.info("Request to create exam: {}", examDto);
        try {
            ExamDto createdExam = examService.createExam(examDto);
            log.info("Exam created successfully: {}", createdExam);
            return ResponseEntity.status(201).body(createdExam);
        } catch (Exception e) {
            log.error("Error creating exam", e);
            return ResponseEntity.status(500).body("Failed to create exam");
        }
    }

    // <---------------- Get All Exams ----------------->
    @Operation(summary = "Get Exams", description = "Fetches all exams or filtered by examId and/or teacherId")
    @ApiResponse(responseCode = "200", description = "Exams fetched successfully")
    @GetMapping("/GetAllExam")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PRINCIPAL')")
    public ResponseEntity<?> getExams(
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false) Long teacherId
    ) {
        log.info("Fetching exams with filters - examId: {}, teacherId: {}", examId, teacherId);
        try {
            List<ExamDto> exams = examService.getExams(examId, teacherId);
            log.info("Exams fetched successfully. Total exams: {}", exams.size());
            return ResponseEntity.ok(exams);
        } catch (Exception e) {
            log.error("Error fetching exams", e);
            return ResponseEntity.status(500).body("Failed to fetch exams");
        }
    }

    // <---------------- Get Exam by ID ----------------->
    @Operation(summary = "Get Exam by ID", description = "Fetches an exam by its ID")
    @ApiResponse(responseCode = "200", description = "Exam found")
    @ApiResponse(responseCode = "404", description = "Exam not found")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<?> getExamById(@PathVariable Long id) {
        log.info("Request to fetch exam with ID: {}", id);
        try {
            ExamDto exam = examService.getExamById(id);
            log.info("Exam retrieved: {}", exam);
            return ResponseEntity.ok(exam);
        } catch (ExamNotFoundException e) {
            log.warn("Exam not found with ID: {}", id);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error fetching exam", e);
            return ResponseEntity.status(500).body("Failed to fetch exam");
        }
    }

    // <---------------- Update Exam ----------------->
    @Operation(summary = "Update Exam", description = "Updates an existing exam by ID")
    @ApiResponse(responseCode = "200", description = "Exam updated successfully")
    @ApiResponse(responseCode = "404", description = "Exam not found")
    @PutMapping("/Update_By/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateExam(@PathVariable Long id, @RequestBody ExamDto examDto) {
        log.info("Request to update exam with ID: {}", id);
        try {
            ExamDto updatedExam = examService.updateExam(id, examDto);
            log.info("Exam updated successfully: {}", updatedExam);
            return ResponseEntity.ok(updatedExam);
        } catch (ExamNotFoundException e) {
            log.warn("Exam not found with ID: {}", id);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating exam", e);
            return ResponseEntity.status(500).body("Failed to update exam");
        }
    }

    // <---------------- Delete Exam ----------------->
    @Operation(summary = "Delete Exam", description = "Deletes an exam by ID")
    @ApiResponse(responseCode = "200", description = "Exam deleted successfully")
    @ApiResponse(responseCode = "404", description = "Exam not found")
    @DeleteMapping("/Delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteExam(@PathVariable Long id) {
        log.info("Request to delete exam with ID: {}", id);
        try {
            examService.deleteExam(id);
            log.info("Exam deleted successfully: {}", id);
            return ResponseEntity.ok("Exam deleted successfully");
        } catch (ExamNotFoundException e) {
            log.warn("Exam not found with ID: {}", id);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting exam", e);
            return ResponseEntity.status(500).body("Failed to delete exam");
        }
    }
}
