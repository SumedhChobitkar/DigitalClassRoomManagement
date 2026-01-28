package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Enum.ExamMode;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Dto.ExamQuestionDto;
import com.DigitalClassRoomManagement.Entity.Admin;
import com.DigitalClassRoomManagement.Entity.Exam;
import com.DigitalClassRoomManagement.Entity.ExamQuestion;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Enum.ExamStatus;
import com.DigitalClassRoomManagement.Enum.SubmissionStatus;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Repository.AdminRepository;
import com.DigitalClassRoomManagement.Repository.ExamQuestionRepository;
import com.DigitalClassRoomManagement.Repository.ExamRepository;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Service.ExamService;

import org.apache.catalina.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private static final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private StudentRepository studentRepository;


//    // ------------------- CREATE EXAM --------------------------
//    @Override
//    public ExamDto createExam(ExamDto examDto) {
//        log.info("Attempting to create exam for teacherId: {}", examDto.getTeacherId());
//
//        try {
//            Teacher teacher = teacherRepository.findById(examDto.getTeacherId())
//                    .orElseThrow(() -> {
//                        log.error("Teacher not found with ID: {}", examDto.getTeacherId());
//                        return new ResourceNotFoundException("Teacher not found with ID: " + examDto.getTeacherId());
//                    });
//            Location location = locationRepository.findById(examDto.getLocationId())
//                    .orElseThrow(() -> new RuntimeException("Location not found"));
//
//
//            Exam exam = new Exam();
//            exam.setLocation(location);
//            exam.setTeacher(teacher);
//            exam.setTerm(examDto.getTerm());
//            exam.setStartTime(examDto.getStartTime());
//            exam.setEndTime(examDto.getEndTime());
//            exam.setDuration(examDto.getDuration());
//            exam.setTotalMarks(examDto.getTotalMarks());
//
//            Exam savedExam = examRepository.save(exam);
//
//            log.info("Exam created successfully with ID: {}", savedExam.getExamId());
//
//            return mapToDto(savedExam);
//
//        } catch (Exception e) {
//            log.error("Error while creating exam", e);
//            throw new RuntimeException("Error while creating exam: " + e.getMessage(), e);
//        }
//    }


    @Override
    public ExamDto createExam(ExamDto examDto) {

        log.info("Attempting to create exam for teacherId: {}", examDto.getTeacherId());

        try {
            // 1. Teacher
            Teacher teacher = teacherRepository.findById(examDto.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Teacher not found with ID: " + examDto.getTeacherId()));

            // 2. Location
            Location location = locationRepository.findById(examDto.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Location not found with ID: " + examDto.getLocationId()));

            // 3. Admin  ✅ THIS WAS MISSING
            Admin admin = adminRepository.findById(examDto.getAdminId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Admin not found with ID: " + examDto.getAdminId()));

            // 4. Create Exam
            Exam exam = new Exam();
            exam.setTeacher(teacher);
            exam.setLocation(location);
            exam.setExamMode(examDto.getExamMode());
            exam.setAdmin(admin);   // ⭐ MOST IMPORTANT LINE
            exam.setTerm(examDto.getTerm());
            exam.setStartTime(examDto.getStartTime());
            exam.setEndTime(examDto.getEndTime());
            exam.setDuration(examDto.getDuration());
            exam.setTotalMarks(examDto.getTotalMarks());

            // 5. Save
            Exam savedExam = examRepository.save(exam);

        log.info("Attempting to create exam for teacherId={}, adminId={}",
                examDto.getTeacherId(), examDto.getAdminId());

        // Fetch teacher and admin
        Teacher teacher = teacherRepository.findById(examDto.getTeacherId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found with ID: " + examDto.getTeacherId()));

        Admin admin = adminRepository.findById(examDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found with ID: " + examDto.getAdminId()));

        // Validate exam times
        if (examDto.getStartTime() != null && examDto.getEndTime() != null &&
                examDto.getEndTime().isBefore(examDto.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Create exam entity
        Exam exam = new Exam();
        exam.setAdmin(admin);
        exam.setTeacher(teacher);
        exam.setTerm(examDto.getTerm());
        exam.setStartTime(examDto.getStartTime());
        exam.setEndTime(examDto.getEndTime());
        exam.setTotalMarks(examDto.getTotalMarks());
        exam.setStatus(examDto.getStatus());

        //  Set examDate and day from startTime
        if (examDto.getStartTime() != null) {
            LocalDate examDate = examDto.getStartTime().toLocalDate();
            exam.setExamDate(examDate);
            exam.setDay(examDate.getDayOfWeek().name()); // MONDAY, TUESDAY, etc.
        }

        // Calculate duration if not provided
        if (examDto.getDuration() != null) {
            exam.setDuration(examDto.getDuration());
        } else if (examDto.getStartTime() != null && examDto.getEndTime() != null) {
            exam.setDuration((int) Duration.between(examDto.getStartTime(), examDto.getEndTime()).toMinutes());
        }

        // Handle questions if any
        if (examDto.getQuestions() != null && !examDto.getQuestions().isEmpty()) {
            List<ExamQuestion> questions = examDto.getQuestions().stream()
                    .map(qDto -> {
                        ExamQuestion q = new ExamQuestion();
                        q.setQuestionText(qDto.getQuestionText());
                        q.setMarks(qDto.getMarks());
                        q.setQuestionType(qDto.getQuestionType());
                        q.setCorrectAnswer(qDto.getCorrectAnswer());
                        q.setOptions(qDto.getOptions());
                        q.setExam(exam); // important for bidirectional relationship
                        return q;
                    })
                    .collect(Collectors.toList());
            exam.setQuestions(questions);
        }

        // Save exam
        Exam savedExam = examRepository.save(exam);

        log.info("Exam created successfully with ID: {}", savedExam.getExamId());

        return mapToDto(savedExam);
    }


    // ------------------- GET EXAM BY ID --------------------------
    @Override
    @Transactional(readOnly = true)
    public ExamDto getExamById(Long examId) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Exam not found with ID: " + examId));

        //  Initialize questions
        if (exam.getQuestions() != null) {
            exam.getQuestions().forEach(q -> {
                //  Initialize options (ElementCollection)
                if (q.getOptions() != null) {
                    q.getOptions().size();
                }
            });
        }

        return mapToDto(exam);
    }


    //<---------------------getExamByQuestionId----------------------------------
    @Override
    @Transactional(readOnly = true)
    public ExamDto getExamByQuestionId(Long questionId) {

        ExamQuestion question = examQuestionRepository.findById(questionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Question not found with ID: " + questionId));
        Exam exam = question.getExam();

        if (exam == null) {
            throw new ResourceNotFoundException("No exam associated with question ID: " + questionId);
        }

        // Initialize lazy collections safely
        if (exam.getQuestions() != null) {
            exam.getQuestions().forEach(q -> {
                if (q.getOptions() != null) {
                    q.getOptions().size();
                }
            });
        }

        return mapToDto(exam);
    }



    //<----------------------GET EXAM BY TEACHER ID----------------->

    @Override
    @Transactional(readOnly = true)
    public List<ExamDto> getExamsByTeacherId(Long teacherId) {

        log.info("Fetching exams for teacher ID: {}", teacherId);

        List<Exam> exams = examRepository.findByTeacherId(teacherId);

        if (exams.isEmpty()) {
            log.warn("No exams found for teacher ID: {}", teacherId);
            throw new ResourceNotFoundException("No exams found for teacher ID: " + teacherId);
        }

        // Initialize lazy collections safely
        exams.forEach(exam -> {
            if (exam.getQuestions() != null) {
                exam.getQuestions().forEach(q -> {
                    if (q.getOptions() != null) {
                        q.getOptions().size(); // force initialization
                    }
                });
            }
        });

        List<ExamDto> examDtos = exams.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        log.info("Found {} exams for teacher ID: {}", examDtos.size(), teacherId);

        return examDtos;
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
//<---------------------GET SCHEDULE EXAM---------------------
    @Override
    public ExamDto getExamScheduleByExamId(Long examId) {

        log.info("Fetching exam schedule for examId: {}", examId);

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Exam not found with ID: " + examId)
                );

        return ExamDto.builder()
                .examId(exam.getExamId())
                .examDate(exam.getStartTime().toLocalDate())
                .day(exam.getStartTime().getDayOfWeek().name())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .status(exam.getStatus())
                .build();
    }


    // ------------------- GET ALL EXAMS --------------------------
    @Override
    public List<ExamDto> getAllExamList() {

        return examRepository.findAll()
                .stream()
                .map(exam -> ExamDto.builder()
                        .examId(exam.getExamId())
                        .examDate(exam.getStartTime().toLocalDate())
                        .day(exam.getStartTime().getDayOfWeek().name())
                        .teacherId(
                                exam.getTeacher() != null
                                        ? exam.getTeacher().getId()
                                        : null
                        )
                        .totalMarks(exam.getTotalMarks())
                        .build()
                )
                .collect(Collectors.toList());
    }




    // ------------------- UPDATE EXAM --------------------------
    @Override
    public ExamDto updateExam(Long examId, ExamDto examDto) {
        log.info("Updating exam with ID: {}", examId);

        //  Fetch existing exam
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> {
                    log.error("Exam not found with ID: {}", examId);
                    return new ResourceNotFoundException("Exam not found with ID: " + examId);
                });

        // Fetch teacher
        Teacher teacher = teacherRepository.findById(examDto.getTeacherId())
                .orElseThrow(() -> {
                    log.error("Teacher not found with ID: {}", examDto.getTeacherId());
                    return new ResourceNotFoundException("Teacher not found with ID: " + examDto.getTeacherId());
                });

        //  Update exam fields
        existingExam.setTeacher(teacher);
        existingExam.setTerm(examDto.getTerm());
        existingExam.setStartTime(examDto.getStartTime());
        existingExam.setEndTime(examDto.getEndTime());
        existingExam.setDuration(examDto.getDuration());
        existingExam.setTotalMarks(examDto.getTotalMarks());

        // Optional: update examDate and day based on startTime
        if (examDto.getStartTime() != null) {
            existingExam.setExamDate(examDto.getStartTime().toLocalDate());
            existingExam.setDay(examDto.getStartTime().getDayOfWeek().name());
        }
        if (examDto.getStatus() != null) {
            existingExam.setStatus(examDto.getStatus());
        }
        Exam updatedExam = examRepository.save(existingExam);

        log.info("Exam updated successfully: {}", examId);
        return mapToDto(updatedExam);
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
                .day(entity.getDay())
                .examDate(entity.getExamDate())
                .duration(entity.getDuration())
                .questions(entity.getQuestions() != null
                        ? entity.getQuestions().stream()
                        .map(this::mapToQuestionDto)
                        .toList()
                        : List.of())
                .totalMarks(entity.getTotalMarks())
                .status(entity.getStatus())
                .build();
    }


//    public ExamDto getExamForStudent(Long studentId, Long examId) {
//
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        Exam exam = examRepository.findById(examId)
//                .orElseThrow(() -> new RuntimeException("Exam not found"));
//
//        // 🔥 LOCATION CHECK
//        if (!student.getLocation().getLocationId()
//                .equals(exam.getLocation().getLocationId())) {
//
//            throw new RuntimeException(
//                    "Access denied: Exam location and Student location do not match"
//            );
//        }
//
//        return mapToDto(exam);
//    }

    public ExamDto getExamForStudent(Long studentId, Long examId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        // ✅ ONLINE EXAM → Direct access
        if (exam.getExamMode() == ExamMode.ONLINE) {
            return mapToDto(exam);
        }

        // ✅ OFFLINE EXAM → Location validation
        if (exam.getExamMode() == ExamMode.OFFLINE) {

            if (student.getLocation() == null) {
                throw new RuntimeException(
                        "Access denied: Student location not assigned"
                );
            }

            if (exam.getLocation() == null) {
                throw new RuntimeException(
                        "Access denied: Exam location not assigned"
                );
            }

            if (!student.getLocation().getLocationId()
                    .equals(exam.getLocation().getLocationId())) {

                throw new RuntimeException(
                        "Access denied: Exam location and Student location do not match"
                );
            }

            return mapToDto(exam);
        }

        throw new RuntimeException("Invalid exam mode configured");
    }

}
    private ExamQuestionDto mapToQuestionDto(ExamQuestion question) {
        return ExamQuestionDto.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .marks(question.getMarks())
                .questionType(question.getQuestionType())
                .correctAnswer(question.getCorrectAnswer())
                .examId(question.getExam() != null ? question.getExam().getExamId() : null)
                .options(question.getOptions())
                .build();
    }


}
