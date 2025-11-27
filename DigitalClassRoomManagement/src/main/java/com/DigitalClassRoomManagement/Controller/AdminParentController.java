package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.Parent;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Enum.Relationship;
import com.DigitalClassRoomManagement.Service.ParentService;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Repository.ParentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/parents")
public class AdminParentController {

    private static final Logger logger = Logger.getLogger(AdminParentController.class.getName());

    @Autowired
    private ParentService parentService;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private StudentRepository studentRepository;

    // CREATE Parent
    @PostMapping("/saveParent")
    public ResponseEntity<?> createParent(@RequestBody Parent parent) {
        logger.info("Request received to create parent: " + parent.getName());

        try {
            Parent createdParent = parentService.createParent(parent);
            logger.info("Parent created successfully with ID: " + createdParent.getParentId());
            return new ResponseEntity<>(createdParent, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create parent: " + e.getMessage(), e);
            return new ResponseEntity<>("Failed to create parent: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET Parent by ID
    @GetMapping("/getParentById/{id}")
    public ResponseEntity<?> getParentById(@PathVariable Long id) {
        logger.info("Fetching parent with ID: " + id);

        try {
            Parent parent = parentService.getParentById(id);
            logger.info("Parent found: " + parent.getName());
            return new ResponseEntity<>(parent, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Parent not found with ID: " + id, e);
            return new ResponseEntity<>("Parent not found with ID: " + id,
                    HttpStatus.NOT_FOUND);
        }
    }

    // GET all Parents
    @GetMapping("/getAllParent")
    public ResponseEntity<?> getAllParents() {
        logger.info("Fetching all parents...");

        try {
            List<Parent> parents = parentService.getAllParents();
            logger.info("Total parents found: " + parents.size());
            return new ResponseEntity<>(parents, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to fetch parent list: " + e.getMessage(), e);
            return new ResponseEntity<>("Unable to fetch parent list: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE Parent
    @PutMapping("/updateParent/{id}")
    public ResponseEntity<?> updateParent(@PathVariable Long id, @RequestBody Parent parentDetails) {
        logger.info("Updating parent with ID: " + id);

        try {
            Parent updatedParent = parentService.updateParent(id, parentDetails);
            logger.info("Parent updated successfully with ID: " + id);
            return new ResponseEntity<>(updatedParent, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update parent with ID: " + id + " - " + e.getMessage(), e);
            return new ResponseEntity<>("Failed to update parent: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE Parent
    @DeleteMapping("/deleteParentById/{id}")
    public ResponseEntity<?> deleteParent(@PathVariable Long id) {
        logger.info("Deleting parent with ID: " + id);

        try {
            parentService.deleteParent(id);
            logger.info("Parent deleted successfully with ID: " + id);
            return new ResponseEntity<>("Parent deleted successfully with ID: " + id,
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete parent with ID: " + id + " - " + e.getMessage(), e);
            return new ResponseEntity<>("Failed to delete parent: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Link Parent to Student
    @PostMapping("/linkParentToStudent")
    public ResponseEntity<String> linkParentToStudent(
            @RequestParam Long parentId,
            @RequestParam Long studentId,
            @RequestParam String relationship) {

        logger.info("Linking parent to student...");

        try {
            String response = parentService.linkParentToStudent(parentId, studentId, relationship);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            logger.warning("Error linking parent: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            logger.severe("Server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to link parent: " + e.getMessage());
        }
    }

}
