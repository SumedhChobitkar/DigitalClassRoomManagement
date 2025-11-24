package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.SectionDTO;
import com.DigitalClassRoomManagement.Entity.SchoolClass;
import com.DigitalClassRoomManagement.Entity.Section;
import com.DigitalClassRoomManagement.Exception.SectionNotFoundException;
import com.DigitalClassRoomManagement.Repository.SchoolClassRepository;
import com.DigitalClassRoomManagement.Repository.SectionRepository;
import com.DigitalClassRoomManagement.Service.SectionService;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Entity.Teacher;

import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Slf4j
@Service
public class SectionServiceImpl implements SectionService {

    private static final Logger logger = LoggerFactory.getLogger(SectionServiceImpl.class);

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Transactional
    @Override
    public Section createSection(SectionDTO sectionDTO) {
        try {
            logger.info("Creating new section: {}", sectionDTO.getSectionName());

            // 🔹 Step 1: Validate basic fields (name, capacity)
            Section section = new Section();
            section.setSectionName(sectionDTO.getSectionName());
            section.setCapacity(sectionDTO.getCapacity());

            // 🔹 Step 2: Fetch SchoolClass using classId from DTO
            SchoolClass schoolClass = schoolClassRepository.findById(sectionDTO.getClassId())
                    .orElseThrow(() -> new RuntimeException("Invalid Class ID: " + sectionDTO.getClassId()));

            section.setSchoolClass(schoolClass); // Set mapped entity

            // 🔹 Step 3: Validate section fields
            validateSection(section);

            // 🔹 Step 4: Save section first
            Section savedSection = sectionRepository.save(section);

            // 🔹 Step 5: Assign teachers if provided
            if (sectionDTO.getTeacherIds() != null) {

                List<Teacher> teachers = teacherRepository.findAllById(sectionDTO.getTeacherIds());

                for (Teacher t : teachers) {
                    t.getAssignedSections().add(savedSection);
                    teacherRepository.save(t); // update teacher mapping
                }
            }

            logger.info("Section '{}' created successfully with ID: {}",
                    savedSection.getSectionName(), savedSection.getSectionId());

            return savedSection;

        } catch (Exception e) {
            logger.error("Error creating section: {}", e.getMessage());
            throw new RuntimeException("Failed to create section. Please try again later.");
        }
    }

    @Override
    public List<Section> getAllSections() {
        try {
            logger.info("Fetching all sections...");
            return sectionRepository.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving sections: {}", e.getMessage());
            throw new RuntimeException("Error fetching section list.");
        }
    }

    @Override
    public Section getSectionById(Long id) {
        try {
            logger.info("Fetching section with ID: {}", id);
            return sectionRepository.findById(id)
                    .orElseThrow(() -> new SectionNotFoundException("Section not found with ID: " + id));
        } catch (SectionNotFoundException e) {
            logger.warn("Section not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching section by ID: {}", e.getMessage());
            throw new RuntimeException("Error fetching section details.");
        }
    }


    @Override
    public Section updateSection(Long id, SectionDTO sectionDTO) {
        try {
            logger.info("Updating section with ID: {}", id);

            // 🔹 Step 1: Find existing section
            Section existingSection = sectionRepository.findById(id)
                    .orElseThrow(() -> new SectionNotFoundException("Section not found with ID: " + id));

            // 🔹 Step 2: Update basic fields
            existingSection.setSectionName(sectionDTO.getSectionName());
            existingSection.setCapacity(sectionDTO.getCapacity());

            // 🔹 Step 3: Update SchoolClass using classId from DTO
            SchoolClass schoolClass = schoolClassRepository.findById(sectionDTO.getClassId())
                    .orElseThrow(() -> new RuntimeException("Invalid Class ID: " + sectionDTO.getClassId()));

            existingSection.setSchoolClass(schoolClass);

            // 🔹 Step 4: Validate section
            validateSection(existingSection);

            // 🔹 Step 5: Update teachers if provided
            if (sectionDTO.getTeacherIds() != null && !sectionDTO.getTeacherIds().isEmpty()) {

                List<Teacher> teachers = teacherRepository.findAllById(sectionDTO.getTeacherIds());

                existingSection.setTeachers(teachers);
            }

            // 🔹 Step 6: Save updated section
            Section updatedSection = sectionRepository.save(existingSection);

            logger.info("Section updated successfully: {}", updatedSection.getSectionName());
            return updatedSection;

        } catch (SectionNotFoundException e) {
            logger.warn("Update failed - Section not found: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            logger.error("Error updating section: {}", e.getMessage());
            throw new RuntimeException("Unable to update section. Please try again later.");
        }
    }

    @Override
    public List<Section> findSectionsByTeacherId(Long teacherId) {
        try {
            logger.info("Fetching sections for Teacher ID: {}", teacherId);
            List<Section> sections = sectionRepository.findSectionsByTeacherId(teacherId);

            if (sections.isEmpty()) {
                throw new SectionNotFoundException("No sections found for Teacher ID: " + teacherId);
            }

            logger.info("Found {} sections for Teacher ID: {}", sections.size(), teacherId);
            return sections;

        } catch (SectionNotFoundException e) {
            logger.warn("No sections found for Teacher ID: {}", teacherId);
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching sections by teacher ID: {}", e.getMessage());
            throw new RuntimeException("Error fetching sections for teacher ID: " + teacherId);
        }
    }

    @Override
    public Section getSectionBySectionId(Long sectionId) {
        try {
            return sectionRepository.findById(sectionId)
                    .orElse(null);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching section for sectionId: " + sectionId +
                    " | Message: " + e.getMessage());
        }
    }


    @Override
    public List<Section> getSectionsByClassName(String className) {
        try {
            log.info("Fetching sections for class: {}", className);

            //List<Section> sections = sectionRepository.findBySchoolClass(className);
            List<Section> sections = sectionRepository.findBySchoolClass_ClassName(className);


            if (sections == null || sections.isEmpty()) {
                throw new SectionNotFoundException("No sections found for class: " + className);
            }

            return sections;

        } catch (Exception e) {
            log.error("Error fetching sections by class: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch sections by class");
        }
    }



    @Override
    public boolean deleteSection(Long id) {
        try {
            logger.info("Deleting section with ID: {}", id);
            Section section = sectionRepository.findById(id)
                    .orElseThrow(() -> new SectionNotFoundException("Section not found with ID: " + id));

            sectionRepository.delete(section);
            logger.info("Section deleted successfully!");
            return true;

        } catch (SectionNotFoundException e) {
            logger.warn("Delete failed - Section not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting section: {}", e.getMessage());
            throw new RuntimeException("Unable to delete section. Please try again later.");
        }
    }

    // Validation

    private void validateSection(Section section) {

        if (section.getSectionName() == null ||
                !ValidationClass.SECTION_NAME_PATTERN.matcher(section.getSectionName()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid Section Name. Must start with a letter and contain only letters, numbers, and spaces."
            );
        }

        if (section.getSectionName().length() > ValidationClass.SECTION_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Section name cannot exceed " + ValidationClass.SECTION_NAME_MAX_LENGTH + " characters."
            );
        }

        if (section.getCapacity() == null ||
                section.getCapacity() < ValidationClass.MIN_CAPACITY ||
                section.getCapacity() > ValidationClass.MAX_CAPACITY) {

            throw new IllegalArgumentException(
                    "Invalid Capacity. Allowed range: " +
                            ValidationClass.MIN_CAPACITY + " to " + ValidationClass.MAX_CAPACITY + "."
            );
        }

        if (section.getSchoolClass() == null) {
            throw new IllegalArgumentException("School class must not be null");
        }

    }

}
