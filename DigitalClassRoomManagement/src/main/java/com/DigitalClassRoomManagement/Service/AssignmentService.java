package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.AssignmentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {

    AssignmentDto createAssignment(AssignmentDto dto, MultipartFile file);

    AssignmentDto updateAssignment(Long id, AssignmentDto dto, MultipartFile file);

    List<AssignmentDto> AllAssignments();

    AssignmentDto getAssignmentById(Long id);

    List<AssignmentDto> getAllAssignmentsByTeacherId(Long teacherId);

    AssignmentDto getAssignmentByIdAndTeacherId(Long assignmentId, Long teacherId);

    void deleteAssignmentByIdAndTeacherId(Long assignmentId, Long teacherId);

    byte[] getFileByAssignmentId(Long assignmentId);
}
