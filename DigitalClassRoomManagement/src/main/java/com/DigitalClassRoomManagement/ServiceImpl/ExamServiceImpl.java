package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import com.DigitalClassRoomManagement.Entity.Admin;
import com.DigitalClassRoomManagement.Entity.Exam;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Repository.AdminRepository;
import com.DigitalClassRoomManagement.Repository.ExamRepository;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Service.ExamService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private static final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AdminRepository adminRepository;


    // ------------------- CREATE EXAM --------------------------
    @Override
    public ExamDto createExam(ExamDto examDto) {
        log.info("Attempting to create exam for teacherId: {}", examDto.getTeacherId());

        try {
            Teacher teacher = teacherRepository.findById(examDto.getTeacherId())
                    .orElseThrow(() -> {
                        log.error("Teacher not found with ID: {}", examDto.getTeacherId());
                        return new ResourceNotFoundException("Teacher not found with ID: " + examDto.getTeacherId());
                    });

            Exam exam = new Exam();
            exam.setTeacher(teacher);
            exam.setTerm(examDto.getTerm());
            exam.setStartTime(examDto.getStartTime());
            exam.setEndTime(examDto.getEndTime());
            exam.setDuration(examDto.getDuration());
            exam.setTotalMarks(examDto.getTotalMarks());

            Exam savedExam = examRepository.save(exam);

            log.info("Exam created successfully with ID: {}", savedExam.getExamId());

            return mapToDto(savedExam);

        } catch (Exception e) {
            log.error("Error while creating exam", e);
            throw new RuntimeException("Error while creating exam: " + e.getMessage(), e);
        }
    }

    // ------------------- GET EXAM BY ID --------------------------
    @Override
    public ExamDto getExamById(Long examId) {
        log.info("Fetching exam with ID: {}", examId);

        try {
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> {
                        log.warn("Exam not found with ID: {}", examId);
                        return new ResourceNotFoundException("Exam not found with ID: " + examId);
                    });

            log.info("Exam fetched successfully: {}", examId);
            return mapToDto(exam);

        } catch (Exception e) {
            log.error("Error while fetching exam with ID {}", examId, e);
            throw new RuntimeException("Error while fetching exam: " + e.getMessage(), e);
        }
    }

    //<----------------------GET EXAM BY TEACHER ID----------------->


    @Override
    public List<ExamDto> getExamsByTeacherId(Long teacherId) {

        log.info("Fetching exams for teacher ID: {}", teacherId);

        try {
            List<Exam> exams = examRepository.findByTeacherId(teacherId);

            if (exams.isEmpty()) {
                log.warn("No exams found for teacher ID: {}", teacherId);
                throw new ResourceNotFoundException("No exams found for teacher ID: " + teacherId);
            }

            List<ExamDto> examDto = exams.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            log.info("Found {} exams for teacher ID: {}", examDto.size(), teacherId);

            return examDto;

        } catch (ResourceNotFoundException e) {
            log.warn("Error: {}", e.getMessage());
            throw e; // rethrow custom exception

        } catch (Exception e) {
            log.error("Unexpected error while fetching exams for teacher ID: {}", teacherId, e);
            throw new RuntimeException("Error fetching exams for teacher ID: " + teacherId, e);
        }
    }

    @Override
    public List<ExamDto> getExams(Long examId, Long teacherId) {
        try {
            List<Exam> exams = examRepository.findAll(); // Fetch all exams first

            if (examId != null) {
                exams = exams.stream()
                        .filter(e -> e.getExamId().equals(examId))
                        .collect(Collectors.toList());
            }

            if (teacherId != null) {
                exams = exams.stream()
                        .filter(e -> e.getTeacher() != null && e.getTeacher().getId().equals(teacherId))
                        .collect(Collectors.toList());
            }

            return exams.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // Log the error and throw a runtime exception or handle it as needed
            log.error("Error fetching exams with examId: {} and teacherId: {}", examId, teacherId, e);
            throw new RuntimeException("Failed to fetch exams: " + e.getMessage(), e);
        }
    }

    // ------------------- ADMIN CREATE EXAM --------------------------
    @Override
    public ExamDto adminCreateExam(ExamDto examDto) {

        log.info("Admin creating exam for teacherId={}, adminId={}",
                examDto.getTeacherId(), examDto.getAdminId());

        Admin admin = adminRepository.findById(examDto.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin not found with ID: " + examDto.getAdminId()));

        Teacher teacher = teacherRepository.findById(examDto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Teacher not found with ID: " + examDto.getTeacherId()));

        Exam exam = Exam.builder()
                .admin(admin)
                .teacher(teacher)
                .term(examDto.getTerm())
                .startTime(examDto.getStartTime())
                .endTime(examDto.getEndTime())
                .duration(examDto.getDuration())
                .totalMarks(examDto.getTotalMarks())
                .build();

        Exam savedExam = examRepository.save(exam);

        log.info("Exam created successfully with examId={}", savedExam.getExamId());

        return mapToDto(savedExam);
    }

    // ------------------- ADMIN GET ALL EXAMS --------------------------
    @Override
    public List<ExamDto> adminGetAllExams() {

        log.info("Admin fetching all exams");

        return examRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ------------------- GET ALL EXAMS --------------------------
    @Override
    public List<ExamDto> getAllExams() {
        log.info("Fetching all exams");

        try {
            List<ExamDto> list = examRepository.findAll()
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            log.info("Total exams found: {}", list.size());
            return list;

        } catch (Exception e) {
            log.error("Error while fetching all exams", e);
            throw new RuntimeException("Error while fetching all exams: " + e.getMessage(), e);
        }
    }

    // ------------------- UPDATE EXAM --------------------------
    @Override
    public ExamDto updateExam(Long examId, ExamDto examDto) {
        log.info("Updating exam with ID: {}", examId);

        try {
            Exam existingExam = examRepository.findById(examId)
                    .orElseThrow(() -> {
                        log.error("Exam not found with ID: {}", examId);
                        return new ResourceNotFoundException("Exam not found with ID: " + examId);
                    });

            Teacher teacher = teacherRepository.findById(examDto.getTeacherId())
                    .orElseThrow(() -> {
                        log.error("Teacher not found with ID: {}", examDto.getTeacherId());
                        return new ResourceNotFoundException("Teacher not found!");
                    });

            existingExam.setTeacher(teacher);
            existingExam.setTerm(examDto.getTerm());
            existingExam.setStartTime(examDto.getStartTime());
            existingExam.setEndTime(examDto.getEndTime());
            existingExam.setDuration(examDto.getDuration());
            existingExam.setTotalMarks(examDto.getTotalMarks());

            Exam updatedExam = examRepository.save(existingExam);

            log.info("Exam updated successfully: {}", examId);

            return mapToDto(updatedExam);

        } catch (Exception e) {
            log.error("Error while updating exam with ID {}", examId, e);
            throw new RuntimeException("Error while updating exam: " + e.getMessage(), e);
        }
    }

    // ------------------- DELETE EXAM --------------------------
    @Override
    public void deleteExam(Long examId) {
        log.info("Attempting to delete exam with ID: {}", examId);

        try {
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> {
                        log.warn("Exam not found with ID: {}", examId);
                        return new ResourceNotFoundException("Exam not found with ID: " + examId);
                    });

            examRepository.delete(exam);
            log.info("Exam deleted successfully: {}", examId);

        } catch (Exception e) {
            log.error("Error while deleting exam with ID {}", examId, e);
            throw new RuntimeException("Error while deleting exam: " + e.getMessage(), e);
        }
    }

    // ------------------- MAPPER --------------------------
    private ExamDto mapToDto(Exam entity) {

        return ExamDto.builder()
                .examId(entity.getExamId())
                .teacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null)
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getAdminId() : null)
                .term(entity.getTerm())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .duration(entity.getDuration())
                .totalMarks(entity.getTotalMarks())
                .build();
    }
}
