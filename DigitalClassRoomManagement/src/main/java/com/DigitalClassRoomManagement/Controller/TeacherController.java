package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Enum.Status;
import com.DigitalClassRoomManagement.Service.TeacherService;
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
@RequestMapping("/api/teacher")
@CrossOrigin("*")
public class TeacherController {
    @Autowired
    private TeacherService service;
    private static final Logger log= LoggerFactory.getLogger(TeacherController.class);
    @PostMapping("/addTeacher")
    public ResponseEntity<String> addTeacher(@Valid @RequestBody TeacherDto dto) {
        log.info("Received request to register new teacher");

        try {
            String response = service.addTeacher(dto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error while adding teacher : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add teacher: " + e.getMessage());
        }
    }

    @GetMapping("/getAllTeachers")
    public ResponseEntity<?> getAllTeacher() {
        log.info("Fetching all teachers");

        try {
            return ResponseEntity.ok(service.getAllTeacher());

        } catch (Exception e) {
            log.error("Error fetching all teachers: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch teachers: " + e.getMessage());
        }
    }

    @GetMapping("/getTeacherById/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        log.info("Fetching teacher info for Id : {}", id);

        try {
            Teacher t = service.getTeacherById(id);
            return ResponseEntity.ok(t);

        } catch (NoSuchElementException e) {
            log.warn("Teacher not found for Id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Teacher not found for Id: " + id);

        } catch (Exception e) {
            log.error("Error fetching teacher by id: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch teacher: " + e.getMessage());
        }
    }

    @PutMapping("/updateTeacherById/{id}")
    public ResponseEntity<String> updateTeacherInfo(@PathVariable Long id, @Valid @RequestBody TeacherDto dto) {
        log.info("Updating teacher info for Id : {}", id);

        try {
            String response = service.updateTeacherInfo(id, dto);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("Teacher not found for update, Id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Teacher not found for Id: " + id);

        } catch (Exception e) {
            log.error("Error updating teacher info: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update teacher: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteTeacherById/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Long id) {
        log.info("Deleting teacher for Id : {}", id);

        try {
            String response = service.deleteTeacherById(id);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("Teacher not found for deletion, Id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Teacher not found for Id: " + id);

        } catch (Exception e) {
            log.error("Error deleting teacher: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete teacher: " + e.getMessage());
        }
    }


    @GetMapping("/get/unapproved/statusrequest")
    public ResponseEntity<List<?>>getUnapprovedStatusRequest()
    {
        try
        {
            return ResponseEntity.ok(service.getUnapprovedStatusRequest());
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/get/approved/statusrequest")
    public ResponseEntity<List<?>>getapprovedStatusRequest()
    {
        try
        {
            return ResponseEntity.ok(service.getapprovedStatusRequest());
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/update/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,@RequestParam Status status)
    {
        try
        {
            User ad=service.updateStatus(id,status);
            return ResponseEntity.status(HttpStatus.OK).body("Status Changed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not Changed");
        }
    }

}
