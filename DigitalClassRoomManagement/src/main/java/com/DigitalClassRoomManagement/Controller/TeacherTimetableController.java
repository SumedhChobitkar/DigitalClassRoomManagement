package com.DigitalClassRoomManagement.Controller;


import com.DigitalClassRoomManagement.Entity.Section;
import com.DigitalClassRoomManagement.Entity.Timetable;
import com.DigitalClassRoomManagement.Service.SectionService;
import com.DigitalClassRoomManagement.Service.TeacherService;
import com.DigitalClassRoomManagement.Service.TimetableService;
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
@RequestMapping("/api/teacherTimetable")
public class TeacherTimetableController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherTimetableController.class);

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private SectionService sectionService;

//    @PreAuthorize("hasRole('TEACHER')")
//    //  GET Timetable by Teacher ID
//    @GetMapping("/{teacherId}/timetable")
//    public ResponseEntity<?> getTimetableByTeacherId(@PathVariable Long teacherId) {
//        logger.info("Fetching timetable for teacherId: {}", teacherId);
//
//        try {
//            List<Timetable> timetableList = timetableService.getTimetableByTeacherId(teacherId);
//
//            if (timetableList == null || timetableList.isEmpty()) {
//                logger.warn("No timetable found for teacherId: {}", teacherId);
//                return ResponseEntity.status(404).body("No timetable found for this teacher");
//            }
//
//            logger.info("Timetable fetched successfully for teacherId: {}", teacherId);
//            return ResponseEntity.ok(timetableList);
//
//        } catch (Exception e) {
//            logger.error("Error fetching timetable for teacherId: {} | Message: {}", teacherId, e.getMessage());
//            return ResponseEntity.status(500).body("Failed to fetch timetable. Please try again.");
//        }
//    }

//    @PreAuthorize("hasRole('TEACHER')")
@PreAuthorize("permitAll()")
    @GetMapping("/{teacherId}/timetable")
    public ResponseEntity<?> getTimetableByTeacherId(@PathVariable Long teacherId) {

        logger.info("Fetching timetable for teacherId: {}", teacherId);

        try {
            List<HashMap<String, Object>> timetableList =
                    timetableService.getTimetableByTeacherId(teacherId);

            if (timetableList == null || timetableList.isEmpty()) {
                logger.warn("No timetable found for teacherId: {}", teacherId);
                return ResponseEntity
                        .status(404)
                        .body("No timetable found for this teacher");
            }

            logger.info("Timetable fetched successfully for teacherId: {}", teacherId);
            return ResponseEntity.ok(timetableList);

        } catch (Exception e) {
            logger.error(
                    "Error fetching timetable for teacherId: {} | Message: {}",
                    teacherId, e.getMessage()
            );
            return ResponseEntity
                    .status(500)
                    .body("Failed to fetch timetable. Please try again.");
        }
    }



    @PreAuthorize("hasRole('TEACHER')")
    //  GET Sections Assigned to Teacher
    @GetMapping("/{teacherId}/sections")
    public ResponseEntity<?> getSectionsByTeacherId(@PathVariable Long teacherId) {
        logger.info("Fetching sections for teacherId: {}", teacherId);

        try {
            List<Section> sections = sectionService.findSectionsByTeacherId(teacherId);

            if (sections == null || sections.isEmpty()) {
                logger.warn("No sections found for teacherId: {}", teacherId);
                return ResponseEntity.status(404).body("No sections assigned to this teacher");
            }

            logger.info("Sections fetched successfully for teacherId: {}", teacherId);
            return ResponseEntity.ok(sections);

        } catch (Exception e) {
            logger.error("Error fetching sections for teacherId: {} | Message: {}", teacherId, e.getMessage());
            return ResponseEntity.status(500).body("Failed to fetch sections. Please try again.");
        }
    }


}
