package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.FeeStructureDto;
import com.DigitalClassRoomManagement.Entity.FeeStructure;
import com.DigitalClassRoomManagement.Service.FeeStructureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/fees")
@Slf4j
@Tag(name = "Fee Structure APIs", description = "CRUD operations for Fee Structure")
public class FeeStructureController {

    private final FeeStructureService feeService;

    public FeeStructureController(FeeStructureService feeService) {
        this.feeService = feeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Fee Structure")
    public ResponseEntity<?> create(@RequestBody FeeStructureDto dto) {
        try {
            FeeStructure created = feeService.createFee(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            log.error("Controller error creating fee: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating Fee Structure: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Fee Structure")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FeeStructureDto dto) {
        try {
            FeeStructure updated = feeService.updateFee(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Controller error updating fee: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating Fee Structure: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @Operation(summary = "Get Fee by ID")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            FeeStructure fee = feeService.getFeeById(id);
            return ResponseEntity.ok(fee);
        } catch (Exception e) {
            log.error("Controller error fetching fee by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Fee Structure not found with ID: " + id);
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @Operation(summary = "List All Fees")
    public ResponseEntity<?> list() {
        try {
            List<FeeStructure> fees = feeService.getAllFees();
            return ResponseEntity.ok(fees);
        } catch (Exception e) {
            log.error("Controller error fetching fee list: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching Fee Structures: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Fee Structure")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            String msg = feeService.deleteFee(id);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            log.error("Controller error deleting fee with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting Fee Structure: " + e.getMessage());
        }
    }
}
