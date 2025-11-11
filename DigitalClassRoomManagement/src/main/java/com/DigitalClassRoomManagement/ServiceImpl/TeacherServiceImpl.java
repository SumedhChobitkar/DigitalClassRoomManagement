package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Exception.TeacherNotFoundException;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Autowired
    private TeacherRepository repo;

    @Override
    public String addTeacher(TeacherDto dto) {
        log.info("Adding new teacher with email: {}", dto.getEmail());
        Teacher teacher = new Teacher();
        teacher.setEmail(dto.getEmail());
        teacher.setGender(dto.getGender());
        teacher.setPhone(dto.getPhone());
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setQualification(dto.getQualification());
        teacher.setDateOfBirth(String.valueOf(dto.getDateOfBirth()));
        teacher.setExperienceYears(dto.getExperienceYears());
        teacher.setAssign_class(dto.getAssign_class());
        teacher.setAssign_section(dto.getAssign_section());
        Teacher savedTeacher = repo.save(teacher);
        log.info("Teacher added successfully with ID: {}", savedTeacher.getId());
        return "New Teacher Added Successfully with ID: " + savedTeacher.getId();
    }

    @Override
    public List<Teacher> getAllTeacher() {
        log.info("Fetching all teacher records from database...");
        List<Teacher> teachers = repo.findAll();
        log.info("Total teachers found: {}", teachers.size());
        return teachers;
    }

    @Override
    public Teacher getTeacherById(Long id) {
        log.info("Fetching teacher details for ID: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Teacher not found with ID: {}", id);
                    return new TeacherNotFoundException("Teacher not found with ID: " + id);
                });
    }

    @Override
    public String updateTeacherInfo(Long id, TeacherDto dto) {
        log.info("Updating teacher information for ID: {}", id);
        try {
            Teacher existing = repo.findById(id)
                    .orElseThrow(() -> {
                        log.error("Teacher not found with ID: {}", id);
                        return new TeacherNotFoundException("Teacher not found with ID: " + id);
                    });

            existing.setEmail(dto.getEmail());
            existing.setGender(dto.getGender());
            existing.setPhone(dto.getPhone());
            existing.setFirstName(dto.getFirstName());
            existing.setLastName(dto.getLastName());
            existing.setQualification(dto.getQualification());
            existing.setDateOfBirth(String.valueOf(dto.getDateOfBirth()));
            existing.setExperienceYears(dto.getExperienceYears());

            repo.save(existing);
            log.info("Teacher information updated successfully for ID: {}", id);
            return "Updation successful";
        } catch (TeacherNotFoundException tx) {
            log.warn("Attempted to update non-existing teacher with ID: {}", id);
            throw tx;
        } catch (Exception e) {
            log.error("Error occurred while updating teacher with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Updation failed: " + e.getMessage());
        }
    }

    @Override
    public String deleteTeacherById(Long id) {
        log.info("Attempting to delete teacher with ID: {}", id);
        try {
            Teacher existing = repo.findById(id)
                    .orElseThrow(() -> {
                        log.error("Teacher not found with ID: {}", id);
                        return new TeacherNotFoundException("Teacher not found with ID: " + id);
                    });
            repo.delete(existing);
            log.info("Teacher deleted successfully with ID: {}", id);
            return "Teacher information deleted successfully with ID: " + id;
        } catch (TeacherNotFoundException tx) {
            log.warn("Attempted to delete non-existing teacher with ID: {}", id);
            throw tx;
        } catch (Exception e) {
            log.error("Error occurred while deleting teacher with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Deletion failed: " + e.getMessage());
        }
    }
}
