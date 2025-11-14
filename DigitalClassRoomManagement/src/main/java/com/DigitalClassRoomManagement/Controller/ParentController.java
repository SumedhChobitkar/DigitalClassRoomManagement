package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.Parent;
import com.DigitalClassRoomManagement.Service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // Allow frontend (React, Angular, etc.)
@RestController
@RequestMapping("/api/parents")
public class ParentController {

    @Autowired
    private ParentService parentService;

    //  CREATE Parent
    @PostMapping("/saveParent")
    public ResponseEntity<?> createParent(@RequestBody Parent parent) {
        try {
            Parent createdParent = parentService.createParent(parent);
            return new ResponseEntity<>(createdParent, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create parent: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  GET Parent by ID
    @GetMapping("/getParentById")
    public ResponseEntity<?> getParentById(@PathVariable Long id) {
        try {
            Parent parent = parentService.getParentById(id);
            return new ResponseEntity<>(parent, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Parent not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    //  GET All Parents
    @GetMapping("/getAllParent")
    public ResponseEntity<?> getAllParents() {
        try {
            List<Parent> parents = parentService.getAllParents();
            return new ResponseEntity<>(parents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to fetch parent list: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE Parent
    @PutMapping("/updateParent/{id}")
    public ResponseEntity<?> updateParent(@PathVariable Long id, @RequestBody Parent parentDetails) {
        try {
            Parent updatedParent = parentService.updateParent(id, parentDetails);
            return new ResponseEntity<>(updatedParent, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update parent: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  DELETE Parent
    @DeleteMapping("/deleteParentById/{id}")
    public ResponseEntity<?> deleteParent(@PathVariable Long id) {
        try {
            parentService.deleteParent(id);
            return new ResponseEntity<>("Parent deleted successfully with ID: " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete parent: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
