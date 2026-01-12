package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.AssignmentDto;
import com.DigitalClassRoomManagement.Service.AssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@RestController
@RequestMapping("api/teacher/assignments")
@Slf4j
@CrossOrigin(origins = "*")
public class TeacherAssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // ------------------- Create Assignment -------------------
    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> createAssignment(
            @ModelAttribute AssignmentDto assignmentDto
    ) {
        try {
            log.info("Creating assignment with title: {}", assignmentDto.getTitle());
            var file = assignmentDto.getFile();

            AssignmentDto created = assignmentService.createAssignment(assignmentDto, file);
            return ResponseEntity.ok(created);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------- Get Assignment By ID -------------------
    @GetMapping("/getAssignmentById/{id}")
    public ResponseEntity<AssignmentDto> getAssignmentById(
            @PathVariable("id") Long assignmentId
    ) {
        try {
            log.info("Fetching assignment with ID {}", assignmentId);
            AssignmentDto dto = assignmentService.getAssignmentById(assignmentId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error fetching assignment with ID {}: {}", assignmentId, e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ------------------- Get All Assignments -------------------
    @GetMapping("/getAllAssignments")
    public ResponseEntity<List<AssignmentDto>> getAllAssignments() {
        try {
            log.info("Fetching all assignments");
            List<AssignmentDto> list = assignmentService.AllAssignments();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Error fetching all assignments: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ------------------- Update Assignment -------------------
    @PutMapping("/updateAssignmentById/{id}")
    public ResponseEntity<?> updateAssignment(
            @PathVariable("id") Long assignmentId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "dueDate", required = false) String dueDateIso,
            @RequestParam(value = "updatedAt", required = false) String updatedAtIso,
            @RequestParam(value = "classId", required = false) Long classId,
            @RequestParam(value = "sectionId", required = false) Long sectionId,
            @RequestParam(value = "subjectId", required = false) Long subjectId,
            @RequestParam(value = "file", required = false) org.springframework.web.multipart.MultipartFile file
    ) {
        try {
            log.info("Updating assignment with ID {}", assignmentId);
            AssignmentDto updated = assignmentService.updateAssignment(
                    assignmentId,
                    title,
                    description,
                    dueDateIso,
                    updatedAtIso,
                    classId,
                    sectionId,
                    subjectId,
                    file
            );
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating assignment with ID {}: {}", assignmentId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------- Delete Assignment -------------------
    @DeleteMapping("/deleteAssignmentById/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        try {
            log.info("Deleting assignment with id {}", id);
            assignmentService.deleteAssignmentById(id);
            return ResponseEntity.ok("Assignment deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting assignment with id {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------- GET FILE BY TEACHER ASSIGNMENT ID -------------------
    @GetMapping(
            value = "/getAssignmentFileByTeacherAssignmentId/{Id}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<?> getFileByTeacherAssignmentId(
            @PathVariable("Id") Long teacherAssignmentId) {

        log.info("API START → Fetching file for Teacher Assignment ID: {}", teacherAssignmentId);

        try {
            byte[] fileData = assignmentService.getFileByAssignmentId(teacherAssignmentId);

            log.info("File fetched successfully for Teacher Assignment ID: {}", teacherAssignmentId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileData);

        } catch (RuntimeException e) {
            log.error("File NOT FOUND for Teacher Assignment ID {}: {}",
                    teacherAssignmentId, e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error while fetching file for Teacher Assignment ID {}",
                    teacherAssignmentId, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch file");
        }
    }





}
