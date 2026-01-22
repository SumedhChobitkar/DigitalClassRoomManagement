package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.AdmissionDTO;
import com.DigitalClassRoomManagement.Exception.AdmissionNotFoundException;
import com.DigitalClassRoomManagement.Service.AdmissionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // 👈 Added Here
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admissions")
@CrossOrigin("*")
public class AdmissionController {

    private static final Logger log =
            LoggerFactory.getLogger(AdmissionController.class);

    @Autowired
    private AdmissionService service;

    // ================= CREATE =================
    @PostMapping("/createAdmissions")
    public ResponseEntity<?> createAdmission(@RequestBody AdmissionDTO dto) {
        log.info("Received request to create admission for student: {}", dto.getStudentName());

        try {
            AdmissionDTO response = service.createAdmission(dto);
            log.info("Admission created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.warn("Validation failed while creating admission: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            log.error("Error while creating admission: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create admission: " + e.getMessage());
        }
    }

    // ================= GET ALL =================
    @GetMapping("/getAllAdmissions")
    public ResponseEntity<?> getAllAdmissions() {
        log.info("Received request to fetch all admissions");

        try {
            List<AdmissionDTO> list = service.getAllAdmissions();
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            log.error("Error while fetching admissions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch admissions");
        }
    }

    // ================= GET BY ID =================
    @GetMapping("/getByIdAdmissions/{id}")
    public ResponseEntity<?> getAdmissionById(@PathVariable Long id) {
        log.info("Received request to fetch admission with id: {}", id);

        try {
            AdmissionDTO dto = service.getAdmissionById(id);
            return ResponseEntity.ok(dto);

        } catch (AdmissionNotFoundException e) {
            log.warn("Admission not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Error while fetching admission: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch admission");
        }
    }

    // ================= UPDATE =================
    @PutMapping("/updateAdmissions/{id}")
    public ResponseEntity<?> updateAdmission(
            @PathVariable Long id,
            @RequestBody AdmissionDTO dto) {

        log.info("Received request to update admission with id: {}", id);

        try {
            AdmissionDTO updated = service.updateAdmission(id, dto);
            return ResponseEntity.ok(updated);

        } catch (AdmissionNotFoundException e) {
            log.warn("Admission not found for update with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (IllegalArgumentException e) {
            log.warn("Validation failed while updating admission: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            log.error("Error while updating admission: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update admission");
        }
    }

    // ================= DELETE =================
    @DeleteMapping("/deleteAdmissions/{id}")
    public ResponseEntity<?> deleteAdmission(@PathVariable Long id) {
        log.info("Received request to delete admission with id: {}", id);

        try {
            service.deleteAdmission(id);
            return ResponseEntity.ok("Admission deleted successfully");

        } catch (AdmissionNotFoundException e) {
            log.warn("Admission not found for deletion with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Error while deleting admission: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete admission");
        }
    }
}
