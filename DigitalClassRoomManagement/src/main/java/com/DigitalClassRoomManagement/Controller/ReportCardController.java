package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.ReportCard;
import com.DigitalClassRoomManagement.Service.ReportCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportCards")
public class ReportCardController {

    private static final Logger logger = LoggerFactory.getLogger(ReportCardController.class);

    @Autowired
    private ReportCardService service;

    // CREATE REPORT-CARD
    @PostMapping("/create")
    public ResponseEntity<?> createReportCard(@RequestBody ReportCard reportCard) {
        try {
            logger.info("Creating new ReportCard for student ID: {}",
                    reportCard.getStudent().getStudentId());

            ReportCard created = service.createReportCard(reportCard);
            return ResponseEntity.ok(created);

        } catch (Exception e) {
            logger.error("Error creating report card", e);
            return ResponseEntity.badRequest().body("Failed to create report card: " + e.getMessage());
        }
    }

    // GET ALL
    @GetMapping("/getAllReportCards")
    public ResponseEntity<?> getAllReportCards() {
        try {
            logger.info("Fetching all report cards");
            return ResponseEntity.ok(service.getAllReportCards());

        } catch (Exception e) {
            logger.error("Error fetching report cards", e);
            return ResponseEntity.internalServerError().body("Unable to fetch report cards");
        }
    }

    // GET BY ID
    @GetMapping("getReportCardById/{id}")
    public ResponseEntity<?> getReportCardById(@PathVariable Long id) {
        try {
            logger.info("Fetching report card by ID: {}", id);
            return ResponseEntity.ok(service.getReportCardById(id));

        } catch (Exception e) {
            logger.error("Report card not found for ID {}", id, e);
            return ResponseEntity.status(404).body("Report card not found");
        }
    }

    // GET BY STUDENT ID
    @GetMapping("/getReportCardByStudentId/{studentId}")
    public ResponseEntity<?> getReportCardsByStudent(@PathVariable Long studentId) {
        try {
            logger.info("Fetching report cards for student ID: {}", studentId);
            List<ReportCard> cards = service.getReportCardsByStudent(studentId);
            return ResponseEntity.ok(cards);

        } catch (Exception e) {
            logger.error("Error fetching report cards for student ID {}", studentId, e);
            return ResponseEntity.badRequest().body("Student not found");
        }
    }

    // UPDATE REPORT-CARD
    @PutMapping("/updateReportCardById/{id}")
    public ResponseEntity<?> updateReportCard(
            @PathVariable Long id,
            @RequestBody ReportCard updatedReportCard) {

        try {
            logger.info("Updating report card ID: {}", id);
            return ResponseEntity.ok(service.updateReportCard(id, updatedReportCard));

        } catch (Exception e) {
            logger.error("Error updating report card ID {}", id, e);
            return ResponseEntity.badRequest().body("Failed to update report card");
        }
    }

    // DELETE
    @DeleteMapping("/deleteReportCardById/{id}")
    public ResponseEntity<?> deleteReportCard(@PathVariable Long id) {
        try {
            logger.info("Deleting report card ID: {}", id);
            service.deleteReportCard(id);
            return ResponseEntity.ok("Report card deleted successfully");

        } catch (Exception e) {
            logger.error("Error deleting report card ID {}", id, e);
            return ResponseEntity.badRequest().body("Failed to delete report card");
        }
    }
}
