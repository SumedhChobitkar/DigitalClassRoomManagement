package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import com.DigitalClassRoomManagement.Exception.ExamNotFoundException;
import com.DigitalClassRoomManagement.Service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/AdminExam-Create")
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','TEACHER')")
    public ResponseEntity<ExamDto> createExam(
            @Valid @RequestBody ExamDto examDto) {

        log.info("Admin request to create exam: teacherId={}, adminId={}",
                examDto.getTeacherId(), examDto.getAdminId());

        try {
            ExamDto createdExam = examService.adminCreateExam(examDto);

            log.info("Exam created successfully by admin, examId={}",
                    createdExam.getExamId());

            return new ResponseEntity<>(createdExam, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Error occurred while creating exam by admin", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // <----------------Admin Get All Exams ----------------->
    @Operation(summary = "Get Exams", description = "Fetches all exams or filtered by examId and/or teacherId")
    @ApiResponse(responseCode = "200", description = "Exams fetched successfully")
    @GetMapping("/AdminGetAllExam")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PRINCIPAL')")
    public ResponseEntity<List<ExamDto>> getAllExams() {

        log.info("Request received to fetch all exams");

        try {
            List<ExamDto> exams = examService.adminGetAllExams();

            log.info("Fetched {} exams successfully", exams.size());

            return ResponseEntity.ok(exams);

        } catch (Exception e) {
            log.error("Error occurred while fetching exams", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // <----------------Admin Get Exam by ID ----------------->
    @Operation(summary = "Get Exam by ID", description = "Fetches an exam by its ID")
    @ApiResponse(responseCode = "200", description = "Exam found")
    @ApiResponse(responseCode = "404", description = "Exam not found")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PRINCIPLE')")
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
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPLE')")
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
    @PreAuthorize("hasRole('ADMIN','PRINCIPLE')")
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
