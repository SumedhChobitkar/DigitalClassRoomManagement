package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Service.ExamService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*")
public class ExamController {

    private static final Logger log = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    private ExamService examService;

    // ------------------ TEACHER CREATE EXAM --------------------------
    @PostMapping("/TeacherSaveExam")
    public ResponseEntity<?> createExam(@RequestBody ExamDto examDto) {

        log.info("Received request to create exam with teacherId: {}", examDto.getTeacherId());

        try {
            ExamDto createdExam = examService.adminCreateExam(examDto);
            log.info("Exam created successfully with examId: {}", createdExam.getExamId());

            return new ResponseEntity<>(createdExam, HttpStatus.CREATED);

        } catch (ResourceNotFoundException e) {
            log.warn("Failed to create exam: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error while creating exam", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating exam: " + e.getMessage());
        }
    }

    // ------------------TEACHER UPDATE EXAM --------------------------
    @PutMapping("/UpdateByExamId/{examId}")
    public ResponseEntity<?> updateExam(@PathVariable("examId") Long examId,
                                        @RequestBody ExamDto examDto) {

        log.info("Received request to update exam with ID: {}", examId);

        try {
            ExamDto updatedExam = examService.updateExam(examId, examDto);
            log.info("Exam updated successfully with ID: {}", examId);

            return ResponseEntity.ok(updatedExam);

        } catch (ResourceNotFoundException e) {
            log.warn("Exam update failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error while updating exam ID {}", examId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating exam: " + e.getMessage());
        }
    }

    // ------------------TEACHER GET EXAM BY ID --------------------------
    @GetMapping("/GetByExamId/{id}")
    public ResponseEntity<?> getExamById(@PathVariable("id") Long examId) {

        log.info("Received request to fetch exam with ID: {}", examId);

        try {
            ExamDto exam = examService.getExamById(examId);
            log.info("Exam fetched successfully with ID: {}", examId);

            return ResponseEntity.ok(exam);

        } catch (ResourceNotFoundException e) {
            log.warn("Exam not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Error fetching exam with ID {}", examId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching exam: " + e.getMessage());
        }
    }


//<------------------GET EXAM BY TEACHER ID----------->
    @GetMapping("/getByTeacher/{teacherId}")
    public ResponseEntity<?> getExamsByTeacherId(@PathVariable Long teacherId) {

        log.info("Fetching exams for teacherId: {}", teacherId);

        try {
            List<ExamDto> exams = examService.getExamsByTeacherId(teacherId);
            return ResponseEntity.ok(exams);

        } catch (ResourceNotFoundException e) {
            log.warn("No exams found for teacherId {}", teacherId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Error fetching exams for teacherId {}", teacherId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }



    // ------------------ GET ALL EXAMS --------------------------
    @GetMapping("/GetAllExam")
    public ResponseEntity<?> getAllExams() {

        log.info("Fetching all exams...");

        try {
            List<ExamDto> exams = examService.getAllExams();
            log.info("Total exams fetched: {}", exams.size());

            return ResponseEntity.ok(exams);

        } catch (Exception e) {
            log.error("Error fetching exams", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching exams: " + e.getMessage());
        }
    }

    // ------------------ DELETE EXAM --------------------------
    @DeleteMapping("/DeleteByExamId/{id}")
    public ResponseEntity<?> deleteExam(@PathVariable("id") Long examId) {

        log.info("Received request to delete exam with ID: {}", examId);

        try {
            examService.deleteExam(examId);
            log.info("Exam deleted successfully with ID: {}", examId);

            return ResponseEntity.ok("Exam deleted successfully with ID: " + examId);

        } catch (ResourceNotFoundException e) {
            log.warn("Delete failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error while deleting exam ID {}", examId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting exam: " + e.getMessage());
        }
    }
}
