package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import com.DigitalClassRoomManagement.Service.ExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Create-exam")
@Slf4j
@CrossOrigin(origins = "*")
public class AdminExamController {

    @Autowired
    private ExamService examService;

    // <---------------- Create Exam ----------------->
    @PostMapping("/CreateExam")
    public ResponseEntity<?> createExam(@RequestBody ExamDto examDto) {
        log.info("Request received to create exam: {}", examDto);
        try {
            ExamDto createdExam = examService.createExam(examDto);
            log.info("Exam created successfully: {}", createdExam);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
        } catch (Exception e) {
            log.error("Error creating exam", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create exam: " + e.getMessage());
        }
    }

    //<-------------- Get All Exams with optional filters -------------->
    @GetMapping("/getAll")
    public ResponseEntity<?> getExams(
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false) Long teacherId
    ) {
        log.info("Fetching exams with filters - examId: {}, teacherId: {}", examId, teacherId);

        try {
            List<ExamDto> exams = examService.getExams(examId, teacherId);

            log.info("Exams fetched successfully. Total exams: {}", exams.size());
            return ResponseEntity.ok(exams);

        } catch (Exception e) {
            log.error("Error fetching exams with examId: {} and teacherId: {}", examId, teacherId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch exams: " + e.getMessage());
        }
    }
}
