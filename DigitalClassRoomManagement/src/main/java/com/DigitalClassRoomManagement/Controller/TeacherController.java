package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Service.TeacherService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin("*")
public class TeacherController {

    @Autowired
    private TeacherService service;

    private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

    // ADD
    @PostMapping("/add")
    public ResponseEntity<?> addTeacher(@Valid @RequestBody TeacherDto dto) {
        try {
            log.info("Received request to register new teacher");
            return ResponseEntity.ok(service.addTeacher(dto));
        } catch (Exception e) {
            log.error("Error while adding teacher: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }

    // GET ALL (FIXED – using DTO method)
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTeacher() {
        try {
            log.info("Fetching all teachers");
            return ResponseEntity.ok(service.getAllTeacherDtos());
        } catch (Exception e) {
            log.error("Error fetching teachers: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }

    // GET BY ID (safe)
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        try {
            log.info("Fetching teacher with ID {}", id);
            return ResponseEntity.ok(service.getTeacherById(id));
        } catch (Exception e) {
            log.error("Error fetching teacher: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTeacherInfo(@PathVariable Long id, @Valid @RequestBody TeacherDto dto) {
        try {
            log.info("Updating teacher with ID {}", id);
            return ResponseEntity.ok(service.updateTeacherInfo(id, dto));
        } catch (Exception e) {
            log.error("Error updating teacher: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        try {
            log.info("Deleting teacher with ID {}", id);
            return ResponseEntity.ok(service.deleteTeacherById(id));
        } catch (Exception e) {
            log.error("Error deleting teacher: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }

    // ASSIGN CLASS
    @PostMapping("/{teacherId}/assign/{classId}")
    public ResponseEntity<?> assignClass(@PathVariable Long teacherId, @PathVariable Long classId) {
        try {
            log.info("Assigning class {} to teacher {}", classId, teacherId);
            return ResponseEntity.ok(service.assignClassToTeacher(teacherId, classId));
        } catch (Exception e) {
            log.error("Error assigning class: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }

    // UNASSIGN CLASS
    @DeleteMapping("/{teacherId}/unassign/{classId}")
    public ResponseEntity<?> unassignClass(@PathVariable Long teacherId, @PathVariable Long classId) {
        try {
            log.info("Unassigning class {} from teacher {}", classId, teacherId);
            return ResponseEntity.ok(service.unassignClassFromTeacher(teacherId, classId));
        } catch (Exception e) {
            log.error("Error unassigning class: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }

    // GET CLASS LIST OF TEACHER
    @GetMapping("/{teacherId}/classes")
    public ResponseEntity<?> getTeacherClasses(@PathVariable Long teacherId) {
        try {
            log.info("Fetching classes assigned to teacher {}", teacherId);
            return ResponseEntity.ok(service.getClassesOfTeacher(teacherId));
        } catch (Exception e) {
            log.error("Error fetching classes: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }
}
