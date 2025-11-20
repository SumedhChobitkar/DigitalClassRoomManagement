package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.SubjectDto;
import com.DigitalClassRoomManagement.Entity.Subject;
import com.DigitalClassRoomManagement.Service.SubjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/subject")
@CrossOrigin("*")
public class AdminSubjectController {
    private static  final Logger log= LoggerFactory.getLogger(AdminSubjectController.class);
    @Autowired
    private SubjectService sservice;

    @PostMapping("/addSubject")
    public ResponseEntity<String> addNewSubject(@Valid @RequestBody SubjectDto dto) {
        log.info("Received request to add subject with name: {}", dto.getSubjectName());

        try {
            String response = sservice.addSubject(dto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error while adding subject: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add subject: " + e.getMessage());
        }
    }

    @GetMapping("/fetchAllSubject")
    public ResponseEntity<?> fetchAllSubject() {
        log.info("Received a request to fetch all subjects");

        try {
            return ResponseEntity.ok(sservice.getAllSubject());

        } catch (Exception e) {
            log.error("Error while fetching subjects: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch subjects: " + e.getMessage());
        }
    }

    @GetMapping("/fetchSubjectById/{subjectId}")
    public ResponseEntity<?> fetchSubjectById(@PathVariable Long subjectId) {
        log.info("Received request to fetch subject with id: {}", subjectId);

        try {
            Subject subject = sservice.getSubjectById(subjectId);
            return ResponseEntity.ok(subject);

        } catch (NoSuchElementException e) {
            log.warn("Subject not found for id: {}", subjectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Subject not found with id: " + subjectId);

        } catch (Exception e) {
            log.error("Error fetching subject by id: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch subject: " + e.getMessage());
        }
    }

    @PutMapping("/updateSubjectById/{subjectId}")
    public ResponseEntity<String> updateSubjectInfo(
            @Valid @RequestBody SubjectDto sdto,
            @PathVariable Long subjectId) {

        log.info("Received request to update subject with id: {}", subjectId);

        try {
            String response = sservice.updateSubject(sdto, subjectId);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("Subject not found for update with id: {}", subjectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Subject not found with id: " + subjectId);

        } catch (Exception e) {
            log.error("Error updating subject: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update subject: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteSubjectById/{subjectId}")
    public ResponseEntity<String> deleteSubject(@PathVariable Long subjectId) {
        log.info("Received request to delete subject with id: {}", subjectId);

        try {
            String response = sservice.deleteSubject(subjectId);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("Subject not found for deletion with id: {}", subjectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Subject not found with id: " + subjectId);

        } catch (Exception e) {
            log.error("Error deleting subject: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete subject: " + e.getMessage());
        }
    }
}