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

    @Operation(summary = "Create new AuditLog", description = "Create and save a new audit log record")
    @PostMapping("/saveAuditlog")
    public ResponseEntity<AuditLog> createAuditLog(@RequestBody AuditLogDto dto) {
        logger.info("Request to create AuditLog: {}", dto);

        AuditLog auditLog = AuditLog.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .action(dto.getAction())
                .module(dto.getModule())
                .time(dto.getTime())
                .build();

        AuditLog created = auditLogService.createAuditLog(auditLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get AuditLog by ID", description = "Fetch a specific audit log using its ID")
    @GetMapping("/getAuditlogById/{id}")
    public ResponseEntity<AuditLog> getAuditLogById(@PathVariable Long id) {
        AuditLog auditLog = auditLogService.getAuditLogById(id);
        return ResponseEntity.ok(auditLog);
    }

    @Operation(summary = "Get all AuditLogs", description = "Retrieve all audit log records")
    @GetMapping("/getAllAuditlogs")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        List<AuditLog> auditLogs = auditLogService.getAllAuditLogs();
        return ResponseEntity.ok(auditLogs);
    }

    @Operation(summary = "Update existing AuditLog", description = "Update an existing audit log record using its ID")
    @PutMapping("/updateAuditlogById/{id}")
    public ResponseEntity<AuditLog> updateAuditLog(@PathVariable Long id, @RequestBody AuditLogDto dto) {
        AuditLog auditLog = AuditLog.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .action(dto.getAction())
                .module(dto.getModule())
                .time(dto.getTime())
                .build();

        AuditLog updated = auditLogService.updateAuditLog(id, auditLog);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete AuditLog", description = "Delete a specific audit log record by ID")
    @DeleteMapping("/deleteAuditlogById/{id}")
    public ResponseEntity<Void> deleteAuditLog(@PathVariable Long id) {
        auditLogService.deleteAuditLog(id);
        return ResponseEntity.noContent().build();
    }
}
