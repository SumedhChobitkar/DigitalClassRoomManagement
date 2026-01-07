package com.DigitalClassRoomManagement.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Session Not Found
    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(SessionNotFoundException ex){
        Map<String,String> body = new HashMap<>();
        body.put("error", "Session not found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // Teacher not found
    @ExceptionHandler(TeacherNotFoundException.class)
    public ResponseEntity<?> handleTeacherNotFound(TeacherNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    // SchoolClass not found
    @ExceptionHandler(SchoolClassNotFoundException.class)
    public ResponseEntity<?> handleClassNotFound(SchoolClassNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    // General resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    // Bad request / custom validation errors
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBad(BadRequestException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    // Spring validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // Catch all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOther(Exception ex) {
        ex.printStackTrace(); // quick visibility
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Something went wrong", "detail", ex.getMessage()));
    }
     //ExamNotFound Exception
    @ExceptionHandler(ExamNotFoundException.class)
    public ResponseEntity<String> handleExamNotFoundException(ExamNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Handle FeedbackNotFoundException
    @ExceptionHandler(FeedbackNotFoundException.class)
    public ResponseEntity<?> handleFeedbackNotFoundException(FeedbackNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Feedback Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}
