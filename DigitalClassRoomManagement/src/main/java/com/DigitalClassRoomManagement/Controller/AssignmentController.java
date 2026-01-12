package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.AssignmentDto;
import com.DigitalClassRoomManagement.Service.AssignmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/assignments")
@Slf4j
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    //---------------- CREATE ASSIGNMENT ----------->

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAssignment(
            @Valid @ModelAttribute AssignmentDto dto,
            @RequestPart(value = "fileUrl", required = false) MultipartFile fileUrl
    ) {
        log.info("API - Create Assignment called");

        try {
            dto.setCreatedAt(LocalDateTime.now());
            dto.setUpdatedAt(LocalDateTime.now());

            AssignmentDto created = assignmentService.createAssignment(dto, fileUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (Exception e) {
            log.error("Error creating assignment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create assignment: " + e.getMessage());
        }
    }

    // -------------------------  UPDATE ASSIGNMENT ----------------

    @PutMapping(value = "/updateAssignmentById/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long id,
            @ModelAttribute AssignmentDto dto,
            @RequestPart(value = "fileUrl", required = false) MultipartFile fileUrl
    ) {
        log.info("API - Update Assignment ID: {}", id);

        try {
            AssignmentDto updated = assignmentService.updateAssignment(id, dto, fileUrl);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            log.error("Error updating assignment ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update assignment: " + e.getMessage());
        }
    }

    //  ---------------------- GET ALL ASSIGNMENTS ---------->
    @GetMapping("/getAllAssignments")
    public ResponseEntity<?> getAllAssignments() {

        log.info("API - Fetch all assignments");

        try {
            List<AssignmentDto> list = assignmentService.AllAssignments();
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            log.error("Error fetching assignments: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch all assignments");
        }
    }

    // GET ASSIGNMENT BY ID
    @GetMapping("/getAssignmentsById/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable Long id) {

        log.info("API - Get Assignment by ID: {}", id);

        try {
            AssignmentDto dto = assignmentService.getAssignmentById(id);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            log.error("Error fetching assignment ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Assignment not found: " + e.getMessage());
        }
    }


    // GET ASSIGNMENTS BY TEACHER ID
    @GetMapping("/getAllAssignmentsByTeacherId/{teacherId}")
    public ResponseEntity<?> getAssignmentsByTeacherId(@PathVariable Long teacherId) {

        log.info("API - Get Assignments by Teacher ID: {}", teacherId);

        try {
            List<AssignmentDto> list = assignmentService.getAllAssignmentsByTeacherId(teacherId);
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            log.error("Error fetching assignments for teacher ID {}: {}", teacherId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    // GET ASSIGNMENT BY ID + TEACHER ID
    @GetMapping("/getAssignmentsByIdAndTeacherId/{id}")
    public ResponseEntity<?> getAssignmentByIdAndTeacherId(
            @PathVariable Long assignmentId,
            @PathVariable Long teacherId) {

        log.info("API - Get Assignment ID {} for Teacher ID {}", assignmentId, teacherId);

        try {
            AssignmentDto dto = assignmentService.getAssignmentByIdAndTeacherId(assignmentId, teacherId);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            log.error("Error fetching assignment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    // DELETE ASSIGNMENT BY ID + TEACHER ID
    @DeleteMapping("/deleteAssignmentByIdAndTeacherId/{Id}")
    public ResponseEntity<?> deleteAssignmentByIdAndTeacherId(
            @PathVariable Long assignmentId,
            @PathVariable Long teacherId) {

        log.info("API - Delete Assignment ID {} by Teacher ID {}", assignmentId, teacherId);

        try {
            assignmentService.deleteAssignmentByIdAndTeacherId(assignmentId, teacherId);
            log.info("Assignment deleted successfully ID: {}", assignmentId);
            return ResponseEntity.ok("Assignment deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting assignment ID {}: {}", assignmentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    // -------------------- GET FILE BY ASSIGNMENT ID -------------------------
    @GetMapping("/getAssignmentsFileByAssignmentId/{Id}")
    public ResponseEntity<?> getFileByAssignmentId(@PathVariable ("Id") Long assignmentId) {

        log.info("API - Download file for assignment ID: {}", assignmentId);

        try {
            byte[] fileData = assignmentService.getFileByAssignmentId(assignmentId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"assignment_file\"")
                    .body(fileData);

        } catch (RuntimeException e) {
            log.error("Error retrieving file for assignment ID {}: {}", assignmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
