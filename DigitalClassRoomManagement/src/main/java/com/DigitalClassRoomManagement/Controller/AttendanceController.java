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

    /// ///////////////////////////////////////////
    @PostMapping("/join/{sessionId}/{email}")
    public ResponseEntity<?> join(@PathVariable Long sessionId,
                                  @PathVariable String email) {
        try {
            log.info("Join request received | sessionId={} | email={}", sessionId, email);
            return ResponseEntity.ok(service.joinSession(email, sessionId));
        } catch (Exception e) {
            log.error("Error while joining session | sessionId={} | email={}", sessionId, email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to join session");
        }
    }

    @PostMapping("/leave/{sessionId}/{email}")
    public ResponseEntity<?> leave(@PathVariable Long sessionId,
                                   @PathVariable String email) {
        try {
            log.info("Leave request received | sessionId={} | email={}", sessionId, email);
            return ResponseEntity.ok(service.leaveSession(email, sessionId));
        } catch (Exception e) {
            log.error("Error while leaving session | sessionId={} | email={}", sessionId, email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to leave session");
        }
    }

    @PostMapping("/absent/{sessionId}/{email}")
    public ResponseEntity<?> absent(@PathVariable Long sessionId,
                                    @PathVariable String email) {
        try {
            log.info("Absent mark request | sessionId={} | email={}", sessionId, email);
            return ResponseEntity.ok(service.markAbsent(sessionId, email));
        } catch (Exception e) {
            log.error("Error while marking absent | sessionId={} | email={}", sessionId, email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to mark absent");
        }
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getList(@PathVariable Long sessionId) {
        try {
            log.info("Fetching attendance list | sessionId={}", sessionId);
            List<Attendance> list = service.getStudentsBySession(sessionId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Error while fetching attendance list | sessionId={}", sessionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch attendance list");
        }
    }

    // NEW APIs
    @GetMapping("/periods/{email}")
    public ResponseEntity<?> getPeriodsAttended(@PathVariable String email) {
        try {
            log.info("Fetching total periods attended | email={}", email);
            return ResponseEntity.ok(service.getTotalPeriodsAttended(email));
        } catch (Exception e) {
            log.error("Error while fetching periods | email={}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch total periods attended");
        }
    }

    @GetMapping("/classes/{email}")
    public ResponseEntity<?> getClassesAttended(@PathVariable String email) {
        try {
            log.info("Fetching total classes attended | email={}", email);
            return ResponseEntity.ok(service.getTotalClassesAttended(email));
        } catch (Exception e) {
            log.error("Error while fetching classes | email={}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch total classes attended");
        }
    }

    @PostMapping("/leave/apply/{sessionId}/{email}")
    public ResponseEntity<?> applyLeave(@PathVariable Long sessionId,
                                        @PathVariable String email,
                                        @RequestParam String reason) {
        try {
            log.info("Applying leave | sessionId={} | email={}", sessionId, email);
            return ResponseEntity.ok(service.applyLeave(sessionId, email, reason));
        } catch (Exception e) {
            log.error("Error while applying leave | sessionId={} | email={}", sessionId, email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to apply leave");
        }
    }

}