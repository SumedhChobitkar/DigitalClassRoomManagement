package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.AttendanceDto;
import com.DigitalClassRoomManagement.Entity.Attendance;
import com.DigitalClassRoomManagement.Service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin("*")
public class AttendanceController {

    private static final Logger log = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService service;

    @PostMapping("/add")
    public ResponseEntity<String> create(@RequestBody AttendanceDto dto) {
        log.info("Received request to add new attendance record for date: {}", dto.getDate());
        String response = service.createAttendance(dto);
        log.info("Attendance creation completed successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Attendance>> getAll() {
        log.info("Fetching all attendance records...");
        List<Attendance> list = service.getAllAttendence();
        log.info("Total attendance records found: {}", list.size());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getByID(@PathVariable Long id) {
        log.info("Fetching attendance record by ID: {}", id);
        Attendance attendance = service.getAttendanceByID(id);
        log.info("Attendance record retrieved successfully for ID: {}", id);
        return ResponseEntity.ok(attendance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable Long id, @RequestBody AttendanceDto dto) {
        log.info("Updating attendance record for ID: {}", id);
        String response = service.updateAttendanceById(id, dto);
        log.info("Attendance updated successfully for ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        log.info("Deleting attendance record for ID: {}", id);
        String response = service.deleteById(id);
        log.info("Attendance deleted successfully for ID: {}", id);
        return ResponseEntity.ok(response);
    }
}
