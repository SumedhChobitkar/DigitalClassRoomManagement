package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.AssignmentDto;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
    private final SectionRepository sectionRepository;
    private final TeacherRepository teacherRepository;

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

        SchoolClass schoolClass = schoolClassRepository.findById(dto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Assignment assignment = Assignment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .fileData(fileBytes)
                .fileName(fileName)
                .fileType(fileType)
                .dueDate(dto.getDueDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .schoolClass(schoolClass)
                .section(section)
                .subject(subject)
                .teacher(teacher)
                .build();

        assignment = assignmentRepository.save(assignment);
        return convertToDto(assignment);
    }

    // ---------------- UPDATE (AssignmentController version) ----------------
    @Override
    public AssignmentDto updateAssignment(Long id, AssignmentDto dto, MultipartFile file) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (dto.getTitle() != null) assignment.setTitle(dto.getTitle());
        if (dto.getDescription() != null) assignment.setDescription(dto.getDescription());
        if (dto.getDueDate() != null) assignment.setDueDate(dto.getDueDate());

        if (dto.getSectionId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new RuntimeException("Section not found"));
            assignment.setSection(section);
        }

        if (dto.getClassId() != null) {
            SchoolClass schoolClass = schoolClassRepository.findById(dto.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));
            assignment.setSchoolClass(schoolClass);
        }

        if (dto.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(dto.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found"));
            assignment.setSubject(subject);
        }

        if (file != null && !file.isEmpty()) {
            try {
                assignment.setFileData(file.getBytes());
                assignment.setFileName(file.getOriginalFilename());
                assignment.setFileType(file.getContentType());
            } catch (Exception e) {
                throw new RuntimeException("Error reading file", e);
            }
        }

        assignment.setUpdatedAt(LocalDateTime.now());
        assignment = assignmentRepository.save(assignment);

        return convertToDto(assignment);
    }

    // ---------------- UPDATE (TeacherAssignmentController ) ----------------
    @Override
    public AssignmentDto updateAssignment(Long assignmentId, String title, String description, String dueDateIso,
                                          String updatedAtIso, Long classId, Long sectionId, Long subjectId,
                                          MultipartFile file) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (title != null) assignment.setTitle(title);
        if (description != null) assignment.setDescription(description);

        if (dueDateIso != null) {
            assignment.setDueDate(LocalDateTime.parse(dueDateIso, DateTimeFormatter.ISO_DATE_TIME));
        }

        if (updatedAtIso != null) {
            assignment.setUpdatedAt(LocalDateTime.parse(updatedAtIso, DateTimeFormatter.ISO_DATE_TIME));
        } else {
            assignment.setUpdatedAt(LocalDateTime.now());
        }

        if (classId != null) {
            SchoolClass schoolClass = schoolClassRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("Class not found"));
            assignment.setSchoolClass(schoolClass);
        }

        if (sectionId != null) {
            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new RuntimeException("Section not found"));
            assignment.setSection(section);
        }


        if (subjectId != null) {
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));
            assignment.setSubject(subject);
        }

        if (file != null && !file.isEmpty()) {
            try {
                assignment.setFileData(file.getBytes());
                assignment.setFileName(file.getOriginalFilename());
                assignment.setFileType(file.getContentType());
            } catch (Exception e) {
                throw new RuntimeException("Error reading file", e);
            }
        }

        assignment = assignmentRepository.save(assignment);
        return convertToDto(assignment);
    }

    // ---------------- GET ALL ASSIGNMENTS ----------------
    @Override
    public List<AssignmentDto> AllAssignments() {
        List<Assignment> list = assignmentRepository.findAll();
        return list.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // ---------------- GET BY ID ----------------
    @Override
    public AssignmentDto getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        return convertToDto(assignment);
    }

    // ---------------- GET BY TEACHER ID ----------------
    @Override
    public List<AssignmentDto> getAllAssignmentsByTeacherId(Long teacherId) {
        List<Assignment> list = assignmentRepository.findByTeacherId(teacherId);
        return list.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // ---------------- GET ASSIGNMENTS BY CLASS ID ----------------
    @Override
    public List<AssignmentDto> getAssignmentsByClassId(Long classId) {
        List<Assignment> list = assignmentRepository.findBySchoolClass_ClassId(classId);
        return list.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // ---------------- GET BY ASSIGNMENT ID + TEACHER ID ----------------
    @Override
    public AssignmentDto getAssignmentByIdAndTeacherId(Long assignmentId, Long teacherId) {
        Assignment assignment = assignmentRepository.findByAssignmentIdAndTeacherId(assignmentId, teacherId)
                .orElseThrow(() -> new RuntimeException("Assignment not found for this teacher"));
        return convertToDto(assignment);
    }

    // ---------------- DELETE BY ID ----------------
    @Override
    public void deleteAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignmentRepository.delete(assignment);
    }

    // ---------------- DELETE BY ID + TEACHER ID ----------------
    @Override
    public void deleteAssignmentByIdAndTeacherId(Long assignmentId, Long teacherId) {
        assignmentRepository.deleteByAssignmentIdAndTeacherId(assignmentId, teacherId);
    }

    // ---------------- GET FILE ----------------
    @Override
    public byte[] getFileByAssignmentId(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        if (assignment.getFileData() == null)
            throw new RuntimeException("No file found for this assignment");
        return assignment.getFileData();
    }

    // ---------------- DTO CONVERTER ----------------
    private AssignmentDto convertToDto(Assignment assignment) {
        String fileStatus = (assignment.getFileData() != null) ? "uploaded" : null;

        return AssignmentDto.builder()
                .assignmentId(assignment.getAssignmentId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .fileData(fileStatus)
                .dueDate(assignment.getDueDate())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .classId(assignment.getSchoolClass().getClassId())
                .subjectId(assignment.getSubject().getSubjectId())
                .sectionId(assignment.getSection().getSectionId())
                .teacherId(assignment.getTeacher().getId())
                .build();
    }
}
