package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.AttendanceDto;
import com.DigitalClassRoomManagement.Entity.Attendance;
import com.DigitalClassRoomManagement.Service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin("*")
public class AttendanceController {

    private static final Logger log = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService service;

    @PostMapping("/saveAttendance")
    public ResponseEntity<?> create(@RequestBody AttendanceDto dto) {
        log.info("Received request to add new attendance record for date: {}", dto.getDate());

        try {
            String response = service.createAttendance(dto);
            log.info("Attendance creation completed successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error creating attendance record: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create attendance: " + e.getMessage());
        }
    }

    @GetMapping("/getAllAttendance")
    public ResponseEntity<?> getAll() {
        log.info("Fetching all attendance records...");

        try {
            List<Attendance> list = service.getAllAttendence();
            log.info("Total attendance records found: {}", list.size());
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            log.error("Error fetching attendance records: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch attendance records: " + e.getMessage());
        }
    }

    @GetMapping("/getAttendanceById/{id}")
    public ResponseEntity<?> getByID(@PathVariable Long id) {
        log.info("Fetching attendance record by ID: {}", id);

        try {
            Attendance attendance = service.getAttendanceByID(id);
            log.info("Attendance record retrieved successfully for ID: {}", id);
            return ResponseEntity.ok(attendance);

        } catch (NoSuchElementException e) {
            log.warn("Attendance not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Attendance record not found for ID: " + id);

        } catch (Exception e) {
            log.error("Error fetching attendance record: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch attendance: " + e.getMessage());
        }
    }

    @PutMapping("/updateAttendanceById/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody AttendanceDto dto) {
        log.info("Updating attendance record for ID: {}", id);

        try {
            String response = service.updateAttendanceById(id, dto);
            log.info("Attendance updated successfully for ID: {}", id);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("Attendance record not found for update, ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Attendance record not found for ID: " + id);

        } catch (Exception e) {
            log.error("Error updating attendance: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update attendance: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAttendenceById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        log.info("Deleting attendance record for ID: {}", id);

        try {
            String response = service.deleteById(id);
            log.info("Attendance deleted successfully for ID: {}", id);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("Attendance not found for deletion, ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Attendance record not found for ID: " + id);

        } catch (Exception e) {
            log.error("Error deleting attendance: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete attendance: " + e.getMessage());
        }
    }
}
