package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.homework;
import com.DigitalClassRoomManagement.Service.HomeworkService;
import com.DigitalClassRoomManagement.Dto.homeworkDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.DigitalClassRoomManagement.commonUtil.ValidationClass.validateHomework;

@Tag(
        name = "Homework Management APIs",
        description = "Operations to create, update, delete and fetch Homework details"
)
@RestController
@RequestMapping("/api/homeworks")
@CrossOrigin(origins = "*")
public class HomeWorkController {

    private static final Logger logger = LoggerFactory.getLogger(HomeWorkController.class);

    private final HomeworkService homeworkService;

    public HomeWorkController(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    //  Create Homework
    @Operation(summary = "Create Homework", description = "Create a new homework.\n\nEg: POST /save")
    @ApiResponse(responseCode = "201", description = "Homework created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid homework data")
    @PostMapping("/saveHomework")
    public ResponseEntity<?> createHomework(@RequestBody homeworkDto dto) {
        logger.info("Request to create homework: {}", dto);
        try {
            validateHomework(dto); //  static call
            homework hw = homework.builder()
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .assignedDate(dto.getAssignedDate())
                    .dueDate(dto.getDueDate())
                    .build();

            homework saved = homeworkService.createHomework(hw);
            logger.info("Homework created successfully with ID: {}", saved.getHomeworkId());
            return ResponseEntity.created(URI.create("/api/homeworks/getById/" + saved.getHomeworkId()))
                    .body(saved);

        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed while creating homework: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error creating homework: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating homework: " + e.getMessage());
        }
    }

    //  Get Homework by ID
    @Operation(summary = "Get Homework by ID", description = "Fetch homework details by its ID.\n\nEg: GET /getById/{id}")
    @ApiResponse(responseCode = "200", description = "Homework fetched successfully")
    @ApiResponse(responseCode = "404", description = "Homework not found")
    @GetMapping("/getHomeworkById/{id}")
    public ResponseEntity<?> getHomeworkById(@PathVariable Long id) {
        logger.info("Fetching homework by ID: {}", id);
        try {
            homework hw = homeworkService.getHomeworkById(id);
            logger.info("Homework found: {}", hw.getHomeworkId());
            return ResponseEntity.ok(hw);
        } catch (RuntimeException e) {
            logger.warn("Homework not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error fetching homework {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error fetching homework: " + e.getMessage());
        }
    }

    //  Get All Homeworks
    @Operation(summary = "Get All Homeworks", description = "Fetch all homework records.\n\nEg: GET /get")
    @ApiResponse(responseCode = "200", description = "All homeworks fetched successfully")
    @GetMapping("/getAllHomework")
    public ResponseEntity<?> getAllHomeworks() {
        logger.info("Request to fetch all homeworks");
        try {
            List<homework> homeworks = homeworkService.getAllHomeworks();
            logger.info("Total homeworks retrieved: {}", homeworks.size());
            return ResponseEntity.ok(homeworks);
        } catch (Exception e) {
            logger.error("Error fetching all homeworks: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error fetching homeworks: " + e.getMessage());
        }
    }

    //  Update Homework by ID
    @Operation(summary = "Update Homework", description = "Update an existing homework by ID.\n\nEg: PUT /update/{id}")
    @PutMapping("/updateHomeworkById/{id}")
    public ResponseEntity<?> updateHomework(@PathVariable Long id, @RequestBody homeworkDto dto) {
        logger.info("Request to update homework ID: {}", id);
        try {
            validateHomework(dto); //  static call
            homework hw = homework.builder()
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .assignedDate(dto.getAssignedDate())
                    .dueDate(dto.getDueDate())
                    .build();

            homework updated = homeworkService.updateHomework(id, hw);
            logger.info("Homework updated successfully: {}", id);
            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed for homework {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.warn("Homework not found for update: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating homework {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error updating homework: " + e.getMessage());
        }
    }

    //  Delete Homework by ID
    @Operation(summary = "Delete Homework", description = "Delete a homework by its ID.\n\nEg: DELETE /delete/{id}")
    @DeleteMapping("/deleteHomeworkById/{id}")
    public ResponseEntity<?> deleteHomework(@PathVariable Long id) {
        logger.info("Request to delete homework ID: {}", id);
        try {
            homeworkService.deleteHomework(id);
            logger.info("Homework deleted successfully: {}", id);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Homework deleted successfully with ID: " + id);
        } catch (RuntimeException e) {
            logger.warn("Homework not found for deletion: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error deleting homework {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error deleting homework: " + e.getMessage());
        }
    }
}
