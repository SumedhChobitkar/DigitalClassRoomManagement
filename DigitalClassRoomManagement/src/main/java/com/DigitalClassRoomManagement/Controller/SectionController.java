package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.SectionDTO;
import com.DigitalClassRoomManagement.Entity.Section;
import com.DigitalClassRoomManagement.Exception.SectionNotFoundException;
import com.DigitalClassRoomManagement.Service.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@CrossOrigin(origins = "*")
public class SectionController {

    private static final Logger logger = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    private SectionService sectionService;

    @PostMapping("/AddSection")
    public ResponseEntity<?> createSection(@RequestBody SectionDTO sectionDTO) {
        try {
            logger.info("Received request to create new Section: {}", sectionDTO.getSectionName());
            logger.info("Teacher IDs assigned to this section: {}", sectionDTO.getTeacherIds());

            Section section = sectionService.createSection(sectionDTO);
            logger.info("Section created successfully: {}", section.getSectionName());

            return new ResponseEntity<>(section, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error creating section: {}", e.getMessage());
            return new ResponseEntity<>("Failed to create section", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getAllSections")
    public ResponseEntity<?> getAllSections() {
        try {
            logger.info("Fetching all sections...");
            List<Section> sections = sectionService.getAllSections();
            return new ResponseEntity<>(sections, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching sections: {}", e.getMessage());
            return new ResponseEntity<>("Error fetching sections", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getSectionById/{id}")
    public ResponseEntity<?> getSectionById(@PathVariable Long id) {
        try {
            logger.info("Fetching section with ID: {}", id);
            Section section = sectionService.getSectionById(id);
            return new ResponseEntity<>(section, HttpStatus.OK);

        } catch (SectionNotFoundException e) {
            logger.warn("Section not found with ID: {}", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error fetching section: {}", e.getMessage());
            return new ResponseEntity<>("Internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSection(@PathVariable Long id, @RequestBody SectionDTO sectionDTO) {
        try {
            logger.info("Updating section ID: {}", id);
            logger.info("Updated Teacher IDs: {}", sectionDTO.getTeacherIds());

            Section updated = sectionService.updateSection(id, sectionDTO);
            logger.info("Section updated successfully with ID: {}", updated.getSectionId());

            return new ResponseEntity<>(updated, HttpStatus.OK);

        } catch (SectionNotFoundException e) {
            logger.warn("Section not found for update with ID: {}", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error updating section: {}", e.getMessage());
            return new ResponseEntity<>("Update failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<?> getSectionsByTeacherId(@PathVariable Long teacherId) {
        try {
            logger.info("Fetching sections for Teacher ID: {}", teacherId);
            List<Section> sections = sectionService.findSectionsByTeacherId(teacherId);
            return new ResponseEntity<>(sections, HttpStatus.OK);

        } catch (SectionNotFoundException e) {
            logger.warn("No sections found for Teacher ID: {}", teacherId);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error fetching sections for Teacher ID {}: {}", teacherId, e.getMessage());
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getSection/{className}")
    public ResponseEntity<?> getSectionsByClassName(@PathVariable String className) {
        logger.info("Fetching sections for class: {}", className);
        try {
            List<Section> sections = sectionService.getSectionsByClassName(className);
            return ResponseEntity.ok(sections);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSection(@PathVariable Long id) {
        try {
            logger.info("Deleting section with ID: {}", id);
            sectionService.deleteSection(id);
            return new ResponseEntity<>("Section deleted successfully", HttpStatus.OK);

        } catch (SectionNotFoundException e) {
            logger.warn("Section not found for deletion with ID: {}", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error deleting section: {}", e.getMessage());
            return new ResponseEntity<>("Delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
