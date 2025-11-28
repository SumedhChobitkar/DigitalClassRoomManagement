package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ResultDto;
import com.DigitalClassRoomManagement.Service.ResultService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ResultController {


    @Autowired
    private  ResultService resultService;


    //<------------------CREATE RESULT----------------->
    @PostMapping("/SaverResult")
    public ResponseEntity<?> createResult(@Valid @RequestBody ResultDto dto) {
        try {
            log.info("Creating result for studentId={} examId={}", dto.getStudentId(), dto.getExamId());
            ResultDto saved = resultService.createResult(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            log.error("Error creating result: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    //<---------------GET RESULT BY iD -------------------->
    @GetMapping("/GetResult/{id}")
    public ResponseEntity<?> getResult(@PathVariable Long id) {
        try {
            log.info("Fetching result for ID={}", id);
            ResultDto dto = resultService.getResultById(id);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            log.error("Error fetching result with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
   //<-------------------------GET ALL RESULT --------------------->
    @GetMapping("/getAllResult")
    public ResponseEntity<?> getAllResults() {
        try {
            log.info("Fetching all results...");
            List<ResultDto> results = resultService.getAllResults();
            return ResponseEntity.ok(results);

        } catch (Exception e) {
            log.error("Error fetching results: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to fetch results");
        }
    }
     //<----------------------UPDATE RESULT BY iD----------------->
    @PutMapping("/UpdateResult/{id}")
    public ResponseEntity<?> updateResult(@PathVariable Long id, @RequestBody ResultDto dto) {
        try {
            log.info("Updating result ID={}", id);
            ResultDto updated = resultService.updateResult(id, dto);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            log.error("Error updating result ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //<-----------------------DELETE RESULT ------------------------->
    @DeleteMapping("/DeleteById/{id}")
    public ResponseEntity<?> deleteResult(@PathVariable Long id) {

        try {
            log.info("Deleting result ID={}", id);
            resultService.deleteResult(id);
            return ResponseEntity.ok("Result deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting result ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    // ------------------- Get Top Marks Results -------------------
    @GetMapping("/top")
    public ResponseEntity<List<ResultDto>> getTopMarksResults() {
        try {
            log.info("Request to fetch results sorted by top marks");
            List<ResultDto> results = resultService.getResultsByTopMarks();
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching top marks results", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
