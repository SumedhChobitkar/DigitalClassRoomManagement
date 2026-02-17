package com.DigitalClassRoomManagement.Controller;


import com.DigitalClassRoomManagement.Entity.Section;
import com.DigitalClassRoomManagement.Entity.Timetable;
import com.DigitalClassRoomManagement.Service.SectionService;
import com.DigitalClassRoomManagement.Service.StudentService;
import com.DigitalClassRoomManagement.Service.TimetableService;
import jakarta.mail.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/studentTimetable")
public class StudentTimetableController {
    private static final Logger logger = LoggerFactory.getLogger(StudentTimetableController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private SectionService sectionService;


//    @PreAuthorize("hasRole('STUDENT')")
//    //  GET Student Timetable by Section ID
//    @GetMapping("/{sectionId}/timetable")
//    public ResponseEntity<?> getStudentTimetableBySectionId(@PathVariable Long sectionId) {
//        logger.info("Fetching student timetable for sectionId: {}", sectionId);
//
//        try {
//            List<Timetable> timetableList = timetableService.getTimetableBySectionId(sectionId);
//
//            if (timetableList == null || timetableList.isEmpty()) {
//                logger.warn("No timetable found for sectionId: {}", sectionId);
//                return ResponseEntity.status(404).body("No timetable found for this section");
//            }
//
//            logger.info("Timetable fetched successfully for sectionId: {}", sectionId);
//            return ResponseEntity.ok(timetableList);
//
//        } catch (Exception e) {
//            logger.error("Error fetching timetable for sectionId: {} | Message: {}", sectionId, e.getMessage());
//            return ResponseEntity.status(500).body("Failed to fetch timetable. Please try again.");
//        }
//    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{sectionId}/timetable")
    public ResponseEntity<?> getStudentTimetableBySectionId(@PathVariable Long sectionId) {

        logger.info("Fetching student timetable for sectionId: {}", sectionId);

        try {
            List<HashMap<String, Object>> timetableList =
                    timetableService.getTimetableBySectionId(sectionId);

            if (timetableList == null || timetableList.isEmpty()) {
                logger.warn("No timetable found for sectionId: {}", sectionId);
                return ResponseEntity.status(404)
                        .body("No timetable found for this section");
            }

            logger.info("Timetable fetched successfully for sectionId: {}", sectionId);
            return ResponseEntity.ok(timetableList);

        } catch (Exception e) {
            logger.error("Error fetching timetable for sectionId: {} | Message: {}",
                    sectionId, e.getMessage());
            return ResponseEntity.status(500)
                    .body("Failed to fetch timetable. Please try again.");
        }
    }



    @PreAuthorize("hasRole('STUDENT')")
    //  GET Student Sessions by Section ID
    @GetMapping("/{sectionId}/section")
    public ResponseEntity<?> getStudentSectionBySectionId(@PathVariable Long sectionId) {
        logger.info("Fetching student section for sectionId: {}", sectionId);

        try {
            Section section = sectionService.getSectionBySectionId(sectionId);

            if (section == null) {
                logger.warn("No section found for sectionId: {}", sectionId);
                return ResponseEntity.status(404).body("No section found for this sectionId");
            }

            logger.info("Section fetched successfully for sectionId: {}", sectionId);
            return ResponseEntity.ok(section);

        } catch (Exception e) {
            logger.error("Error fetching section for sectionId: {} | Message: {}", sectionId, e.getMessage());
            return ResponseEntity.status(500).body("Failed to fetch section. Please try again.");
        }
    }

}
