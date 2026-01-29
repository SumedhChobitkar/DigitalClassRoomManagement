package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.AssignmentDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {

    // Create assignment
    AssignmentDto createAssignment(AssignmentDto dto, MultipartFile file);

    // Update assignment with DTO
    AssignmentDto updateAssignment(Long id, AssignmentDto dto, MultipartFile file);

    // Update assignment with individual fields
    AssignmentDto updateAssignment(Long assignmentId, String title, String description,
                                   String dueDateIso, String updatedAtIso,
                                   Long classId, Long sectionId, Long subjectId,
                                   MultipartFile file);

    // Get all assignments
    List<AssignmentDto> AllAssignments();

    // Get assignment by ID
    AssignmentDto getAssignmentById(Long id);

    // Get assignments by teacher
    List<AssignmentDto> getAllAssignmentsByTeacherId(Long teacherId);

    // Get assignment by ID + teacher ID
    AssignmentDto getAssignmentByIdAndTeacherId(Long assignmentId, Long teacherId);

    // Delete assignment by ID + teacher ID
    void deleteAssignmentByIdAndTeacherId(Long assignmentId, Long teacherId);

    // Delete assignment by ID
    void deleteAssignmentById(Long id);

    // Get assignments by class ID
    List<AssignmentDto> getAssignmentsByClassId(Long classId);

    // Get assignment file by assignment ID
    byte[] getFileByAssignmentId(Long assignmentId);

    // Daily at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    void sendAssignmentDueDateReminders();
}
