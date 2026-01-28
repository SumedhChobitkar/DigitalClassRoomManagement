package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Service.ExamService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*")
public class ExamController {

    private static final Logger log = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    private ExamService examService;

    // ------------------ TEACHER CREATE EXAM --------------------------
    @PostMapping("/TeacherSaveExam")
    public ResponseEntity<?> createExam(@RequestBody @Valid ExamDto examDto) {

        log.info("Received request to create exam with teacherId: {}", examDto.getTeacherId());

        try {
            ExamDto createdExam = examService.createExam(examDto);
            log.info("Exam created successfully with examId: {}", createdExam.getExamId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);

        } catch (ResourceNotFoundException e) {
            log.warn("Failed to create exam: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", HttpStatus.NOT_FOUND.value(),
                            "error", "Not Found",
                            "message", e.getMessage()
                    ));

        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", HttpStatus.BAD_REQUEST.value(),
                            "error", "Bad Request",
                            "message", e.getMessage()
                    ));

        } catch (Exception e) {
            log.error("Unexpected error while creating exam", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Internal Server Error",
                            "message", e.getMessage()
                    ));
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

    // ------------------ TEACHER GET EXAM BY ID --------------------------
    @GetMapping("/GetByExamId/{id}")
    public ResponseEntity<?> getExamById(@PathVariable("id") Long examId) {

        log.info("Received request to fetch exam with ID: {}", examId);

        try {
            // Use a service method that fetches questions eagerly
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
    @GetMapping("/getAllExam")
    public ResponseEntity<?> getAllExam() {

        log.info("Request received to fetch all exams for logged-in student");

        try {
            List<ExamDto> exams = examService.getAllExamList();

            if (exams.isEmpty()) {
                log.info("No exams found for student");
                return ResponseEntity.noContent().build(); // 204
            }

            log.info("Fetched {} exams for student", exams.size());
            return ResponseEntity.ok(exams); // 200

        } catch (Exception e) {
            log.error("Error fetching exams for student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "error", "Internal Server Error",
                            "message", "Failed to fetch exams"
                    ));
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

    @GetMapping("/student/{studentId}/exam/{examId}")
    public ResponseEntity<?> getExamForStudent(
            @PathVariable Long studentId,
            @PathVariable Long examId) {

        try {
            ExamDto exam = examService.getExamForStudent(studentId, examId);
            return ResponseEntity.ok(exam);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    // ------------------- GET EXAM SCHEDULE BY EXAM ID -------------------
    @GetMapping("/schedule/{examId}")
    public ResponseEntity<?> getExamScheduleByExamId(@PathVariable Long examId) {

        log.info("Request received to fetch exam schedule for examId: {}", examId);

        try {
            ExamDto schedule = examService.getExamScheduleByExamId(examId);

            return ResponseEntity.ok(schedule); // 200 OK

        } catch (ResourceNotFoundException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", e.getMessage()
                    ));

        } catch (Exception e) {
            log.error("Error fetching exam schedule for examId: {}", examId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "error", "Internal Server Error",
                            "message", "Failed to fetch exam schedule"
                    ));
        }
    }


    // ========================= GET EXAM BY QUESTION ID =========================
   // @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/by-question/{questionId}")
    public ResponseEntity<?> getExamByQuestionId(@PathVariable Long questionId) {

        log.info("Received request to fetch exam by questionId={}", questionId);

        try {
            ExamDto examDto = examService.getExamByQuestionId(questionId);

            log.info("Successfully fetched exam for questionId={}", questionId);
            return ResponseEntity.ok(examDto);

        } catch (ResourceNotFoundException ex) {
            log.warn("Exam not found for questionId={}: {}", questionId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());

        } catch (Exception ex) {
            log.error("Unexpected error while fetching exam for questionId={}", questionId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error while fetching exam");
        }
    }


}

