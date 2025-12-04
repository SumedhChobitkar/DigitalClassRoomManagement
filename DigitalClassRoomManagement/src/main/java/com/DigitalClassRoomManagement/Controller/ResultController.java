package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ResultDto;
import com.DigitalClassRoomManagement.Service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Results API", description = "Endpoints to manage student exam results")
public class ResultController {

    private final ResultService resultService;

    //<------------------CREATE RESULT----------------->
    @PostMapping("/Create-result")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @Operation(summary = "Create a Result", description = "Create a new exam result for a student")
    @ApiResponse(responseCode = "201", description = "Result created successfully")
    public ResponseEntity<?> createResult(@Valid @RequestBody ResultDto dto) {
        log.info("Creating result for studentId={} examId={}", dto.getStudentId(), dto.getExamId());
        try {
            ResultDto saved = resultService.createResult(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("Failed to create result", e);
            return ResponseEntity.status(500).body("Failed to create result");
        }
    }

    //<---------------GET RESULT BY ID-------------------->
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @Operation(summary = "Get Result by ID", description = "Fetch a single result by its ID")
    @ApiResponse(responseCode = "200", description = "Result retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Result not found")
    public ResponseEntity<?> getResult(@PathVariable Long id) {
        log.info("Fetching result for ID={}", id);
        try {
            ResultDto dto = resultService.getResultById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.warn("Result not found or error fetching result: ID={}", id, e);
            return ResponseEntity.status(404).body("Result not found");
        }
    }

    //<-------------------------GET ALL RESULTS--------------------->
    @GetMapping("/GetAll")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @Operation(summary = "Get all Results", description = "Fetch all exam results")
    @ApiResponse(responseCode = "200", description = "Results retrieved successfully")
    public ResponseEntity<?> getAllResults() {
        log.info("Fetching all results...");
        try {
            List<ResultDto> results = resultService.getAllResults();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error fetching all results", e);
            return ResponseEntity.status(500).body("Failed to fetch results");
        }
    }

    //<----------------------UPDATE RESULT BY ID----------------->
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @Operation(summary = "Update Result", description = "Update an existing result by ID")
    @ApiResponse(responseCode = "200", description = "Result updated successfully")
    @ApiResponse(responseCode = "404", description = "Result not found")
    public ResponseEntity<?> updateResult(@PathVariable Long id, @Valid @RequestBody ResultDto dto) {
        log.info("Updating result ID={}", id);
        try {
            ResultDto updated = resultService.updateResult(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.warn("Failed to update result ID={}", id, e);
            return ResponseEntity.status(404).body("Result not found");
        }
    }

    //<-----------------------DELETE RESULT------------------------->
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Result", description = "Delete a result by ID")
    @ApiResponse(responseCode = "204", description = "Result deleted successfully")
    @ApiResponse(responseCode = "404", description = "Result not found")
    public ResponseEntity<?> deleteResult(@PathVariable Long id) {
        log.info("Deleting result ID={}", id);
        try {
            resultService.deleteResult(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.warn("Failed to delete result ID={}", id, e);
            return ResponseEntity.status(404).body("Result not found");
        }
    }

    //<-------------------GET TOP MARKS RESULTS------------------->
    @GetMapping("/top")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @Operation(summary = "Get Top Marks Results", description = "Fetch results sorted by highest marks")
    @ApiResponse(responseCode = "200", description = "Top results retrieved successfully")
    public ResponseEntity<?> getTopMarksResults() {
        log.info("Fetching results sorted by top marks");
        try {
            List<ResultDto> results = resultService.getResultsByTopMarks();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Failed to fetch top marks results", e);
            return ResponseEntity.status(500).body("Failed to fetch top marks results");
        }
    }
}
