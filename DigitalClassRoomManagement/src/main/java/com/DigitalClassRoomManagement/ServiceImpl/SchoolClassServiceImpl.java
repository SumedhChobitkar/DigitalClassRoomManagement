package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.SchoolClassRequestDto;
import com.DigitalClassRoomManagement.Dto.SchoolClassResponseDto;
import com.DigitalClassRoomManagement.Entity.SchoolClass;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Exception.BadRequestException;
import com.DigitalClassRoomManagement.Exception.SchoolClassNotFoundException;
import com.DigitalClassRoomManagement.Repository.SchoolClassRepository;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Service.SchoolClassService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolClassServiceImpl implements SchoolClassService {

    private final SchoolClassRepository classRepository;
    private final TeacherRepository teacherRepository;

    // CREATE
    @Override
    @Transactional
    public SchoolClassResponseDto create(SchoolClassRequestDto request) {
        try {
            validateClassFields(request);
            log.info("Creating class {}", request.className);

            if (classRepository.existsByClassNameIgnoreCase(request.className.trim()))
                throw new DataIntegrityViolationException("Class name already exists");

            SchoolClass newClass = SchoolClass.builder()
                    .className(request.className.trim())
                    .description(request.description.trim())
                    .build();

            // save to generate id
            newClass = classRepository.save(newClass);

            // assign teachers (if any) — this updates teacher side join table entries
            if (request.teacherIds != null && !request.teacherIds.isEmpty()) {
                assignTeachers(newClass.getClassId(), request.teacherIds);
            }

            // load class with teachers (fetch-join) and map to DTO
            SchoolClass loaded = loadWithTeachers(newClass.getClassId());
            return mapToDto(loaded);

        } catch (BadRequestException | DataIntegrityViolationException ex) {
            log.error("Validation/Business Error Creating Class → {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected Error Creating Class: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unable to create class. Try again.");
        }
    }

    // UPDATE
    @Override
    @Transactional
    public SchoolClassResponseDto update(Long id, SchoolClassRequestDto request) {
        try {
            validateClassFields(request);
            log.info("Updating class {}", id);

            SchoolClass existing = classRepository.findById(id)
                    .orElseThrow(() -> new SchoolClassNotFoundException(id));

            if (!existing.getClassName().equalsIgnoreCase(request.className)
                    && classRepository.existsByClassNameIgnoreCase(request.className.trim()))
                throw new DataIntegrityViolationException("Class name already exists");

            existing.setClassName(request.className.trim());
            existing.setDescription(request.description.trim());
            classRepository.save(existing);

            if (request.teacherIds != null) {
                syncTeachers(existing.getClassId(), request.teacherIds);
            }

            SchoolClass loaded = loadWithTeachers(id);
            return mapToDto(loaded);

        } catch (BadRequestException | SchoolClassNotFoundException | DataIntegrityViolationException ex) {
            log.error("Validation Error Updating Class {} → {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected Error Updating Class {} → {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Unable to update class.");
        }
    }

    // DELETE
    @Override
    @Transactional
    public void delete(Long id) {
        try {
            log.info("Deleting class {}", id);

            SchoolClass sc = loadWithTeachers(id);

            // Remove class from teacher side to maintain join table integrity
            for (Teacher t : sc.getTeachers()) {
                t.getAssignedClass().removeIf(c -> Objects.equals(c.getClassId(), id));
            }
            teacherRepository.saveAll(sc.getTeachers());

            classRepository.deleteById(id);

        } catch (SchoolClassNotFoundException ex) {
            log.error("Class not found for delete {}", id);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected Error Deleting Class {} → {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Unable to delete class.");
        }
    }

    // GET ALL
    @Override
    @Transactional(readOnly = true)
    public List<SchoolClassResponseDto> getAll() {
        try {
            log.info("Fetching all classes");

            return classRepository.findAllWithTeachers()
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error("Unexpected Error Fetching All Classes: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unable to fetch class list.");
        }
    }

    // GET BY ID
    @Override
    @Transactional(readOnly = true)
    public SchoolClassResponseDto getById(Long id) {
        try {
            SchoolClass sc = loadWithTeachers(id);
            return mapToDto(sc);
        } catch (SchoolClassNotFoundException ex) {
            log.error("Class not found {}", id);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected Error Fetching Class {} → {}", id, ex.getMessage());
            throw new RuntimeException("Unable to fetch class.");
        }
    }

    // INTERNAL HELPERS

    private SchoolClass loadWithTeachers(Long id) {
        return classRepository.findByIdWithTeachers(id)
                .orElseThrow(() -> new SchoolClassNotFoundException(id));
    }

    private void validateClassFields(SchoolClassRequestDto request) {
        if (request == null)
            throw new BadRequestException("Request cannot be null");

        if (request.className == null ||
                !ValidationClass.CLASS_NAME_PATTERN.matcher(request.className.trim()).matches())
            throw new BadRequestException("Invalid class name");

        if (request.description == null ||
                !ValidationClass.CLASS_DESCRIPTION_PATTERN.matcher(request.description.trim()).matches())
            throw new BadRequestException("Invalid description");
    }

    /**
     * Assign teachers to class by classId (safer than using transient entity)
     */
    private void assignTeachers(Long classId, List<Long> teacherIds) {
        List<Teacher> teachers = teacherRepository.findAllById(teacherIds);
        Set<Long> found = teachers.stream().map(Teacher::getId).collect(Collectors.toSet());

        List<Long> missing = teacherIds.stream()
                .filter(id -> !found.contains(id))
                .toList();

        if (!missing.isEmpty())
            throw new BadRequestException("Teachers not found: " + missing);

        // load the managed class
        SchoolClass managedClass = classRepository.findById(classId)
                .orElseThrow(() -> new SchoolClassNotFoundException(classId));

        for (Teacher t : teachers)
            t.getAssignedClass().add(managedClass);

        teacherRepository.saveAll(teachers);
    }

    /**
     * Synchronize teacher assignments by classId
     */
    private void syncTeachers(Long classId, List<Long> ids) {
        SchoolClass clazz = classRepository.findByIdWithTeachers(classId)
                .orElseThrow(() -> new SchoolClassNotFoundException(classId));

        List<Teacher> currentTeachers = clazz.getTeachers() == null ? List.of() : clazz.getTeachers();
        Set<Long> current = currentTeachers.stream().map(Teacher::getId).collect(Collectors.toSet());
        Set<Long> desired = new HashSet<>(ids);

        // Add
        Set<Long> toAdd = new HashSet<>(desired);
        toAdd.removeAll(current);
        if (!toAdd.isEmpty()) {
            List<Teacher> addList = teacherRepository.findAllById(toAdd);
            for (Teacher t : addList)
                t.getAssignedClass().add(clazz);
            teacherRepository.saveAll(addList);
        }

        // Remove
        Set<Long> toRemove = new HashSet<>(current);
        toRemove.removeAll(desired);
        if (!toRemove.isEmpty()) {
            List<Teacher> removeList = teacherRepository.findAllById(toRemove);
            for (Teacher t : removeList)
                t.getAssignedClass().removeIf(c -> Objects.equals(c.getClassId(), clazz.getClassId()));
            teacherRepository.saveAll(removeList);
        }
    }

    private SchoolClassResponseDto mapToDto(SchoolClass entity) {

        List<Long> teacherIds = entity.getTeachers() == null
                ? Collections.emptyList()
                : entity.getTeachers().stream()
                .map(Teacher::getId)
                .collect(Collectors.toList());

        return SchoolClassResponseDto.builder()
                .classId(entity.getClassId())
                .className(entity.getClassName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .teacherIds(teacherIds)
                .build();
    }
}
