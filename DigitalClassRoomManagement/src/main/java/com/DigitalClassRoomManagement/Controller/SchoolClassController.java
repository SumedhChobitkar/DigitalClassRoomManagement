package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.SchoolClassRequestDto;
import com.DigitalClassRoomManagement.Dto.SchoolClassResponseDto;
import com.DigitalClassRoomManagement.Service.SchoolClassService;
import com.DigitalClassRoomManagement.Service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;
    private final TeacherService teacherService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN','PRINCIPAL')")
    public ResponseEntity<?> create(@Valid @RequestBody SchoolClassRequestDto request) {
        try {
            log.info("Admin creating class: {}", request.className);            SchoolClassResponseDto body = schoolClassService.create(request);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            log.error("Error in createClass: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN','PRINCIPAL')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody SchoolClassRequestDto request) {
        try {
            log.info("Admin updating class ID: {}", id);
            return ResponseEntity.ok(schoolClassService.update(id, request));
        } catch (Exception e) {
            log.error("Error in updateClass: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN','PRINCIPAL')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            log.info("Admin deleting class ID: {}", id);
            schoolClassService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error in deleteClass: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT','PRINCIPAL')")
    public ResponseEntity<?> getAll() {
        try {
            log.info("Fetching all classes");
            List<SchoolClassResponseDto> list = schoolClassService.getAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Error in getAll: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT','PRINCIPAL')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            log.info("Fetching class by id: {}", id);
            return ResponseEntity.ok(schoolClassService.getById(id));
        } catch (Exception e) {
            log.error("Error in getById: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/getTeachersOfClass/{classId}/teachers")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PRINCIPAL')")
    public ResponseEntity<?> getTeachersOfClass(@PathVariable Long classId) {
        try {
            log.info("Fetching teachers of class {}", classId);
            return ResponseEntity.ok(teacherService.getTeachersOfClass(classId));
        } catch (Exception e) {
            log.error("Error in getTeachersOfClass: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
