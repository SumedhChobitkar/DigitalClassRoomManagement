package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.AssignmentDto;
import com.DigitalClassRoomManagement.Entity.Assignment;
import com.DigitalClassRoomManagement.Service.AssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/teacher/assignments")
@Slf4j
@CrossOrigin(origins = "*")
public class TeacherAssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // ------------------- CREATE ASSIGNMENT -------------------
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAssignment(@ModelAttribute AssignmentDto assignmentDto) {
        try {
            MultipartFile file = assignmentDto.getFile();
            AssignmentDto created = assignmentService.createAssignment(assignmentDto, file);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------- GET ASSIGNMENT BY ID -------------------
    @GetMapping("/getAssignmentById/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(assignmentService.getAssignmentById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ------------------- GET ALL ASSIGNMENTS -------------------
    @GetMapping("/getAllAssignments")
    public ResponseEntity<?> getAllAssignments() {
        List<AssignmentDto> list = assignmentService.AllAssignments();
        return ResponseEntity.ok(list);
    }

    // ------------------- UPDATE ASSIGNMENT -------------------
    @PutMapping(value = "/updateAssignmentById/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String dueDate,
            @RequestParam(required = false) String updatedAt,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) MultipartFile file
    ) {
        try {
            AssignmentDto updated = assignmentService.updateAssignment(
                    id, title, description, dueDate,
                    updatedAt, classId, sectionId, subjectId, file
            );
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------- DELETE ASSIGNMENT -------------------
    @DeleteMapping("/deleteAssignmentById/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignmentById(id);
        return ResponseEntity.ok("Assignment deleted successfully");
    }


    // -------------------- GET ASSIGNMENT FILE (TEACHER) --------------------
    @GetMapping("/getAssignmentFileByTeacherAssignmentId/{id}")
    public ResponseEntity<byte[]> teacherAssignmentGetFile(@PathVariable Long id) {

        log.info("API - Teacher download assignment file, ID: {}", id);

        Assignment assignment = assignmentService.getAssignmentFileData(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(assignment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + assignment.getFileName() + "\"")
                .body(assignment.getFileData());
    }

}