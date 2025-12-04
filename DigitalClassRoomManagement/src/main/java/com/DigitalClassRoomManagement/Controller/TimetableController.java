package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.TimetableDTO;
import com.DigitalClassRoomManagement.Entity.Timetable;
import com.DigitalClassRoomManagement.Service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/timetable")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createTimetable")
    public ResponseEntity<?> create(@RequestBody TimetableDTO dto) {
        try {
            return new ResponseEntity<>(timetableService.createTimetable(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(" Failed to create timetable: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TimetableDTO dto) {
        try {
            return new ResponseEntity<>(timetableService.updateTimetable(id, dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(" Failed to update timetable: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(timetableService.getTimetableById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Timetable not found for id: " + id,
                    HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        try {
            List<Timetable> list = timetableService.getAllTimetables();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(" Failed to fetch timetables: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(timetableService.deleteTimetable(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(" Failed to delete timetable with id: " + id,
                    HttpStatus.NOT_FOUND);
        }
    }

}
