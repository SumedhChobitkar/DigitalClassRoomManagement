package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.TimetableDTO;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Repository.SchoolClassRepository;
import com.DigitalClassRoomManagement.Service.TimetableService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;




@Service
@Slf4j
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    private static final Logger logger = LoggerFactory.getLogger(TimetableServiceImpl.class);


@Override
public Timetable createTimetable(TimetableDTO dto) {
    try {
        log.info("Creating Timetable for classId: {}", dto.getClassId());

        SchoolClass schoolClass = schoolClassRepository.findById(dto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Timetable timetable = Timetable.builder()
                .schoolClass(schoolClass)
                .section(section)
                .subject(subject)
                .teacher(teacher)
                .dayOfWeek(dto.getDayOfWeek())
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();

        validateTimetable(timetable);
        return timetableRepository.save(timetable);

    } catch (Exception e) {
        log.error("Error creating timetable: {}", e.getMessage());

        throw new RuntimeException(e.getMessage());
    }
}


    @Override
    public Timetable updateTimetable(Long id, TimetableDTO dto) {
        try {
            log.info("Updating Timetable ID: {}", id);

            Timetable timetable = timetableRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Timetable not found"));

            // update SchoolClass
            SchoolClass schoolClass = schoolClassRepository.findById(dto.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));

            timetable.setSchoolClass(schoolClass);
            timetable.setDayOfWeek(dto.getDayOfWeek());
            timetable.setDate(dto.getDate());
            timetable.setStartTime(dto.getStartTime());
            timetable.setEndTime(dto.getEndTime());

            validateTimetable(timetable);

            return timetableRepository.save(timetable);

        } catch (Exception e) {
            log.error("Error updating timetable: {}", e.getMessage());
            throw new RuntimeException("Failed to update timetable");
        }
    }

    @Override
    public Timetable getTimetableById(Long id) {
        try {
            log.info("Fetching timetable ID: {}", id);

            return timetableRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Timetable not found"));

        } catch (Exception e) {
            log.error("Fetch failed: {}", e.getMessage());
            throw new RuntimeException("Failed to get timetable");
        }
    }
    @Override
    public List<Timetable> getAllTimetables() {
        try {
            log.info("Fetching all timetables");
            return timetableRepository.findAll();
        } catch (Exception e) {
            log.error("Error fetching timetable list: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch timetable list");
        }
    }

    @Override
    public List<Timetable> getTimetableByTeacherId(Long teacherId) {
        try {
            log.info("Fetching timetable for teacher ID: {}", teacherId);
            return timetableRepository.findByTeacher_Id(teacherId);
        } catch (Exception e) {
            log.error("Error fetching timetable: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch timetable for teacher");
        }
    }

    @Override
    public List<Timetable> getTimetableBySectionId(Long sectionId) {
        logger.info("Fetching timetable for sectionId: {}", sectionId);

        try {
            List<Timetable> timetableList = timetableRepository.findBySection_SectionId(sectionId);

            if (timetableList == null || timetableList.isEmpty()) {
                logger.warn("No timetable found for sectionId: {}", sectionId);
            } else {
                logger.info("Timetable fetched successfully for sectionId: {}", sectionId);
            }

            return timetableList;

        } catch (Exception e) {
            logger.error("Error fetching timetable for sectionId: {} | Message: {}", sectionId, e.getMessage());
            throw new RuntimeException("Error fetching timetable for sectionId: " + sectionId);
        }
    }


    @Override
    public String deleteTimetable(Long id) {
        try {
            log.info("Deleting timetable ID: {}", id);

            timetableRepository.deleteById(id);
            return "Timetable deleted successfully";

        } catch (Exception e) {
            log.error("Delete failed: {}", e.getMessage());
            throw new RuntimeException("Failed to delete timetable");
        }
    }

    // validation
    private void validateTimetable(Timetable timetable) {
        if (timetable.getSchoolClass() == null ||
                timetable.getSchoolClass().getClassName() == null ||
                !ValidationClass.TIMETABLE_CLASS_PATTERN.matcher(timetable.getSchoolClass().getClassName()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid Class Format. Allowed: letters, numbers, space. Max 20 characters."
            );
        }


        if (timetable.getDayOfWeek() == null ||
                !ValidationClass.DAY_OF_WEEK_PATTERN.matcher(timetable.getDayOfWeek().toString()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid Day. Must be MONDAY to SUNDAY only."
            );
        }

        if (timetable.getDate() == null ||
                !ValidationClass.DATE_PATTERN.matcher(timetable.getDate().toString()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid Date Format. Use: YYYY-MM-DD."
            );
        }
        if (timetable.getDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date cannot be in the past."
            );
        }
        if (timetable.getStartTime() == null ||
                !ValidationClass.TIME_PATTERN.matcher(timetable.getStartTime().toString()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid Start Time Format. Use: YYYY-MM-DDTHH:MM"
            );
        }
        if (timetable.getEndTime() == null ||
                !ValidationClass.TIME_PATTERN.matcher(timetable.getEndTime().toString()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid End Time Format. Use: YYYY-MM-DDTHH:MM"
            );
        }
        if (timetable.getStartTime().isAfter(timetable.getEndTime())) {
            throw new IllegalArgumentException(
                    "Start Time must be before End Time."
            );
        }
        long minutes = java.time.Duration.between(
                timetable.getStartTime(),
                timetable.getEndTime()
        ).toMinutes();

        if (minutes < ValidationClass.MIN_PERIOD_MINUTES ||
                minutes > ValidationClass.MAX_PERIOD_MINUTES) {

            throw new IllegalArgumentException(
                    "Invalid Lecture Duration. Allowed: " +
                            ValidationClass.MIN_PERIOD_MINUTES + " to " +
                            ValidationClass.MAX_PERIOD_MINUTES + " minutes."
            );
        }

        List<Timetable> existing = timetableRepository
                .findByTeacher_IdAndDayOfWeek(
                        timetable.getTeacher().getId(),
                        timetable.getDayOfWeek()
                );

        for (Timetable t : existing) {

            boolean isOverlapping =
                    timetable.getStartTime().isBefore(t.getEndTime()) &&
                            timetable.getEndTime().isAfter(t.getStartTime());

            if (isOverlapping) {
                throw new IllegalArgumentException(
                        "This teacher already has a lecture during this time."
                );
            }
        }

        // Validate Teacher
        if (timetable.getTeacher() == null) {
            throw new IllegalArgumentException("Teacher is required.");
        }

        // Validate Section
        if (timetable.getSection() == null) {
            throw new IllegalArgumentException("Section is required.");
        }

        // Validate Subject
        if (timetable.getSubject() == null) {
            throw new IllegalArgumentException("Subject is required.");
        }
    }

}
