package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.AuditLog;
import com.DigitalClassRoomManagement.Service.AuditLogService;
import com.DigitalClassRoomManagement.Dto.AuditLogDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditlogs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "AuditLog APIs", description = "APIs for managing audit logs in the system")
public class AuditLogController {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogController.class);
    private final AuditLogService auditLogService;

    // CREATE AUDIT LOG
    @Operation(summary = "Create new AuditLog")
    @PostMapping("/saveAuditlog")
    public ResponseEntity<?> createAuditLog(@RequestBody AuditLogDto dto) {
        logger.info("Request to create AuditLog: {}", dto);

        try {
            AuditLog auditLog = AuditLog.builder()
                    .userId(dto.getUserId())
                    .username(dto.getUsername())
                    .action(dto.getAction())
                    .module(dto.getModule())
                    .time(dto.getTime())
                    .build();

            AuditLog created = auditLogService.createAuditLog(auditLog);
            logger.info("AuditLog created successfully with ID: {}", created.getAuditLogId());

            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (Exception e) {
            logger.error("Error creating AuditLog: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating AuditLog: " + e.getMessage());
        }
    }
    // GET AUDIT LOG BY ID
    @Operation(summary = "Get AuditLog by ID")
    @GetMapping("/getAuditlogById/{id}")
    public ResponseEntity<?> getAuditLogById(@PathVariable Long id) {
        logger.info("Fetching AuditLog by ID: {}", id);

        try {
            AuditLog auditLog = auditLogService.getAuditLogById(id);
            logger.info("AuditLog found: {}", id);
            return ResponseEntity.ok(auditLog);

        } catch (RuntimeException e) {
            logger.warn("AuditLog not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            logger.error("Error fetching AuditLog {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error fetching AuditLog: " + e.getMessage());
        }
    }

    // GET ALL AUDIT LOGS
    @Operation(summary = "Get all AuditLogs")
    @GetMapping("/getAllAuditlogs")
    public ResponseEntity<?> getAllAuditLogs() {
        logger.info("Request to fetch all AuditLogs");

        try {
            List<AuditLog> auditLogs = auditLogService.getAllAuditLogs();
            logger.info("Total AuditLogs retrieved: {}", auditLogs.size());
            return ResponseEntity.ok(auditLogs);

        } catch (Exception e) {
            logger.error("Error fetching all AuditLogs: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error fetching AuditLogs: " + e.getMessage());
        }
    }
    // UPDATE AUDIT LOG
    @Operation(summary = "Update existing AuditLog")
    @PutMapping("/updateAuditlogById/{id}")
    public ResponseEntity<?> updateAuditLog(@PathVariable Long id, @RequestBody AuditLogDto dto) {
        logger.info("Request to update AuditLog ID: {}", id);

        try {
            AuditLog auditLog = AuditLog.builder()
                    .userId(dto.getUserId())
                    .username(dto.getUsername())
                    .action(dto.getAction())
                    .module(dto.getModule())
                    .time(dto.getTime())
                    .build();

            AuditLog updated = auditLogService.updateAuditLog(id, auditLog);
            logger.info("AuditLog updated successfully: {}", id);

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            logger.warn("AuditLog not found for update: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            logger.error("Error updating AuditLog {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error updating AuditLog: " + e.getMessage());
        }
    }
    // DELETE AUDIT LOG

    @Operation(summary = "Delete AuditLog")
    @DeleteMapping("/deleteAuditlogById/{id}")
    public ResponseEntity<?> deleteAuditLog(@PathVariable Long id) {
        logger.info("Request to delete AuditLog ID: {}", id);

        try {
            auditLogService.deleteAuditLog(id);
            logger.info("AuditLog deleted successfully: {}", id);
            return ResponseEntity.ok("AuditLog deleted successfully with ID: " + id);

        } catch (RuntimeException e) {
            logger.warn("AuditLog not found for deletion: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            logger.error("Error deleting AuditLog {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error deleting AuditLog: " + e.getMessage());
        }
    }
}
