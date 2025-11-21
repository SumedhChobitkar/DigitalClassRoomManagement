package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.AssignmentDto;
import com.DigitalClassRoomManagement.Entity.Assignment;
import com.DigitalClassRoomManagement.Repository.AssignmentRepository;
import com.DigitalClassRoomManagement.Service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    // ---------------- CREATE ----------------
    @Override
    public AssignmentDto createAssignment(AssignmentDto dto, MultipartFile file) {

        byte[] fileBytes = null;
        String fileName = null;
        String fileType = null;

        if (file != null && !file.isEmpty()) {
            try {
                fileBytes = file.getBytes();
                fileName = file.getOriginalFilename();
                fileType = file.getContentType();
            } catch (Exception e) {
                throw new RuntimeException("Error reading file", e);
            }
        }

        Assignment assignment = Assignment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .fileData(fileBytes)
                .dueDate(dto.getDueDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .classId(dto.getClassId())
                .sectionId(dto.getSectionId())
                .subjectId(dto.getSubjectId())
                .teacherId(dto.getTeacherId())
                .build();

        assignment = assignmentRepository.save(assignment);

        return convertToDto(assignment, fileName, fileType);
    }


    // ---------------- UPDATE ----------------
    @Override
    public AssignmentDto updateAssignment(Long id, AssignmentDto dto, MultipartFile file) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        assignment.setDueDate(dto.getDueDate());
        assignment.setUpdatedAt(LocalDateTime.now());
        assignment.setClassId(dto.getClassId());
        assignment.setSectionId(dto.getSectionId());
        assignment.setSubjectId(dto.getSubjectId());
        assignment.setTeacherId(dto.getTeacherId());

        String fileName = null;
        String fileType = null;

        if (file != null && !file.isEmpty()) {
            try {
                assignment.setFileData(file.getBytes());
                fileName = file.getOriginalFilename();
                fileType = file.getContentType();
            } catch (Exception e) {
                throw new RuntimeException("Error reading file", e);
            }
        }

        assignmentRepository.save(assignment);
        return convertToDto(assignment, fileName, fileType);
    }


    // ---------------- GET ALL ----------------
    @Override
    public List<AssignmentDto> AllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(a -> convertToDto(a, null, null))
                .collect(Collectors.toList());
    }


    // ---------------- GET BY ID ----------------
    @Override
    public AssignmentDto getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        return convertToDto(assignment, null, null);
    }


    // ---------------- GET BY TEACHER ----------------
    @Override
    public List<AssignmentDto> getAllAssignmentsByTeacherId(Long teacherId) {
        return assignmentRepository.findByTeacherId(teacherId).stream()
                .map(a -> convertToDto(a, null, null))
                .collect(Collectors.toList());
    }


    // ---------------- GET BY ID + TEACHER ----------------
    @Override
    public AssignmentDto getAssignmentByIdAndTeacherId(Long assignmentId, Long teacherId) {
        Assignment assignment = assignmentRepository.findByAssignmentIdAndTeacherId(assignmentId, teacherId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        return convertToDto(assignment, null, null);
    }


    // ---------------- DELETE ----------------
    @Override
    public void deleteAssignmentByIdAndTeacherId(Long assignmentId, Long teacherId) {
        Assignment assignment = assignmentRepository.findByAssignmentIdAndTeacherId(assignmentId, teacherId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignmentRepository.delete(assignment);
    }


    // ---------------- RETURN FILE ----------------
    @Override
    public byte[] getFileByAssignmentId(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        return assignment.getFileData(); // you will not use this since you don't need download
    }

    // ---------------- DTO CONVERTER ----------------
    private AssignmentDto convertToDto(Assignment assignment, String name, String type) {

        String fileStatus = (assignment.getFileData() != null) ? "uploaded" : null;

        return AssignmentDto.builder()
                .assignmentId(assignment.getAssignmentId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
               // .fileName(name)
              //  .fileType(type)
                .fileData(fileStatus)
                .dueDate(assignment.getDueDate())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .classId(assignment.getClassId())
                .subjectId(assignment.getSubjectId())
                .sectionId(assignment.getSectionId())
                .teacherId(assignment.getTeacherId())
                .build();
    }
}
