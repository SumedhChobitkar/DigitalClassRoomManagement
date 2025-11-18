// file: com/DigitalClassRoomManagement/Service/TeacherService.java
package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.SchoolClassResponseDto;
import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Dto.TeacherResponseDto;
import com.DigitalClassRoomManagement.Entity.Teacher;

import java.util.List;

public interface TeacherService {

    // CREATE
    String addTeacher(TeacherDto dto);

    // READ - entities (if you still need raw entities elsewhere)
    List<Teacher> getAllTeacher();

    // READ (DTO) – safer for controllers / JSON serialization (prevents LazyInitializationException)
    List<TeacherResponseDto> getAllTeacherDtos();

    // READ single entity
    Teacher getTeacherById(Long id);

    // UPDATE / DELETE
    String updateTeacherInfo(Long id, TeacherDto dto);
    String deleteTeacherById(Long id);

    // MAPPING: Assign / Unassign classes to teacher
    String assignClassToTeacher(Long teacherId, Long classId);
    String unassignClassFromTeacher(Long teacherId, Long classId);

    // LOOKUPS: For SchoolClassController and TeacherController
    List<SchoolClassResponseDto> getClassesOfTeacher(Long teacherId);

    /**
     * IMPORTANT: This method now returns DTOs (not Entity objects).
     * Controller (SchoolClassController#getTeachersOfClass) expects DTOs to serialize safely.
     */
    List<TeacherResponseDto> getTeachersOfClass(Long classId);
}
