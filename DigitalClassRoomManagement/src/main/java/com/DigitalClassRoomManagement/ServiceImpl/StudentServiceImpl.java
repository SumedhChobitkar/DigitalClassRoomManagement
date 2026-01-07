package com.DigitalClassRoomManagement.ServiceImpl;


import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Dto.StudentDTO;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Exception.StudentNotFoundException;
import com.DigitalClassRoomManagement.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.DigitalClassRoomManagement.commonUtil.ValidationClass.*;



@Service

public class StudentServiceImpl implements StudentService {

    private static final Logger logger = Logger.getLogger(StudentServiceImpl.class.getName());

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;
    //  Save a new student

    // Save a new student
    //@Override
    /*public Student saveStudent(Student student) {
        try {
            *//*validateStudent(student);*//*
            logger.info("Saving new student with Roll No: " + student.getRollNumber());
            return studentRepository.save(student);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving student: " + e.getMessage(), e);
            throw new RuntimeException("Failed to save student. Please check input data: " + e.getMessage());
        }
    }*/



    @Override
    public Student createStudent(StudentDTO dto, MultipartFile profileFile) {

        Student student = new Student();

        // BASIC DETAILS
        student.setRollNumber(dto.getRollNumber());
        student.setAdmissionNumber(dto.getAdmissionNumber());
        student.setFirstName(dto.getFirstName());
        student.setMiddleName(dto.getMiddleName());
        student.setLastName(dto.getLastName());
        student.setAcademicYear(dto.getAcademicYear());
        student.setEmail(dto.getEmail());
        student.setMobileNumber(dto.getMobileNumber());
        student.setGender(dto.getGender());

        if (dto.getDateOfBirth() != null) {
            student.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));
        }

        // ADDRESS
        student.setStreet(dto.getStreet());
        student.setCity(dto.getCity());
        student.setState(dto.getState());
        student.setCountry(dto.getCountry());
        student.setPinCode(dto.getPinCode());

        // USER MAPPING
        if (dto.getUsers() == null || dto.getUsers().getUserId() == null) {
            throw new RuntimeException("User ID is required");
        }

        User user = userRepository.findById(dto.getUsers().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        student.setUsers(user);

        // TEACHER MAPPING
        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            student.setTeacher(teacher);
        }

       // student.setTeacherMailId(dto.getTeacherMailId());

        // CLASS MAPPING
        if (dto.getClassId() != null) {
            SchoolClass schoolClass = schoolClassRepository.findById(dto.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));
            student.setSchoolClass(schoolClass);
        }

        // SECTION MAPPING
        if (dto.getSectionId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new RuntimeException("Section not found"));
            student.setSection(section);
        }

        // PROFILE (BLOB)
        if (profileFile != null && !profileFile.isEmpty()) {
            try {
                student.setProfile(profileFile.getBytes());
            } catch (Exception e) {
                throw new RuntimeException("Failed to read profile image");
            }
        }

        return studentRepository.save(student);
    }


    // Get all students
    @Override
    public List<Student> getAllStudents() {
        try {
            logger.info("Fetching all students...");
            List<Student> students = studentRepository.findAll();
            logger.info("Total students found: " + students.size());
            return students;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching student list: " + e.getMessage(), e);
            throw new RuntimeException("Unable to fetch student list.");
        }
    }

    // Get student by ID
    @Override
    public Optional<Student> getStudentById(Long id) {
        try {
            logger.info("Fetching student with ID: " + id);
            return studentRepository.findById(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching student: " + e.getMessage(), e);
            throw new StudentNotFoundException("Unable to retrieve student with ID: " + id);
        }
    }

    // Update student details
    @Override
    public Student updateStudent(Long id, Student updatedStudent) {
        try {
            validateStudent(updatedStudent);
            logger.info("Updating student with ID: " + id);

            return studentRepository.findById(id).map(student -> {
                student.setRollNumber(updatedStudent.getRollNumber());
                student.setAdmissionNumber(updatedStudent.getAdmissionNumber());
                student.setFirstName(updatedStudent.getFirstName());
                student.setLastName(updatedStudent.getLastName());
                student.setAcademicYear(updatedStudent.getAcademicYear());
                student.setEmail(updatedStudent.getEmail());

                // PROFILE — Update only if new image received
                if (updatedStudent.getProfile() != null && updatedStudent.getProfile().length > 0) {
                    student.setProfile(updatedStudent.getProfile());  // byte[] BLOB update
                }


                // USER — only if changed
                if (updatedStudent.getUsers() != null) {
                    student.setUsers(updatedStudent.getUsers());
                }

                return studentRepository.save(student);

            }).orElseThrow(() ->
                    new StudentNotFoundException("Student not found with ID: " + id)
            );

        } catch (StudentNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating student: " + e.getMessage(), e);
            throw new RuntimeException("Unable to update student with ID: " + id + ". " + e.getMessage());
        }
    }

    // Delete student by ID
    @Override
    public void deleteStudent(Long id) {
        try {
            if (!studentRepository.existsById(id)) {
                throw new StudentNotFoundException("Student not found with ID: " + id);
            }
            logger.info("Deleting student with ID: " + id);
            studentRepository.deleteById(id);
            logger.info("Student deleted successfully with ID: " + id);
        } catch (StudentNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting student: " + e.getMessage(), e);
            throw new RuntimeException("Unable to delete student with ID: " + id);
        }
    }

    //  Get students by class/academic year
    @Override
    public List<Student> getStudentsByClass(String className) {
        try {
            logger.info("Fetching students for class/academic year: " + className);
            return studentRepository.findByAcademicYear(className);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching students by class: " + e.getMessage(), e);
            throw new RuntimeException("Unable to fetch students for class: " + className);
        }
    }


    @Override
    public Student assignTeacher(Long studentId, Long teacherId) {
        logger.info("Assigning teacher " + teacherId + " to student " + studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        student.setTeacher(teacher);
        return studentRepository.save(student);
    }

    @Override
    public Student assignSection(Long studentId, Long sectionId) {
        logger.info("Assigning section " + sectionId + " to student " + studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
//        Section section = sectionRepository.findById(sectionId)
//                .orElseThrow(() -> new RuntimeException("Section not found"));
//        Student student = studentService.getStudentById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        student.setSection(section);

        student.setSection(section);
        return studentRepository.save(student);
    }

    @Override
    public Student assignParent(Long studentId, Long parentId) {
        logger.info("Assigning parent " + parentId + " to student " + studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        student.setParent(parent);
        return studentRepository.save(student);
    }

    @Override
    public Student assignClass(Long studentId, Long classId) {
        logger.info("Assigning class " + classId + " to student " + studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        SchoolClass schoolClass = schoolClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        student.setSchoolClass(schoolClass);
        return studentRepository.save(student);

    }




    @Override
    public StudentDTO.StudentCreateResponse enrollStudent(Long studentId, StudentDTO.EnrollmentRequest request) {
        logger.log(Level.INFO, "Service: Starting enrollment process for student ID {0}", studentId);
        try {
            // Fetch student
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        logger.log(Level.WARNING, "Student not found with ID {0}", studentId);
                        return new RuntimeException("Student not found with ID: " + studentId);
                    });
            // Update fields
            logger.log(Level.INFO, "Service: Updating student details for ID {0}", studentId);
            student.setAdmissionNumber(request.getAdmissionNumber());
            student.setRollNumber(request.getRollNo());
            studentRepository.save(student);
            logger.log(Level.INFO, "Service: Enrollment successful for student ID {0}", studentId);
            return new StudentDTO.StudentCreateResponse(
                    "SUCCESS",
                    "Student enrolled successfully"
            );
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE,
                    String.format("Service Error: Enrollment failed for student ID %s: %s", studentId, e.getMessage()));
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE,
                    String.format("Service Error: Unexpected exception for student ID %s: %s", studentId, e.getMessage()),
                    e);
            throw new RuntimeException("Internal error during enrollment");
        }
    }

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;


    // View Leave Approval Status
    @Override
    public LeaveRequest viewLeaveStatus(Long leaveRequestId) {
        try {
            return leaveRequestRepository.findById(leaveRequestId)
                    .orElseThrow(() -> new RuntimeException("Leave request not found"));
        } catch (Exception e) {
//            logger.err("Error while fetching leave status {}", leaveRequestId, e);
            throw e;
        }
    }




    // Validation logic
    private void validateStudent(Student student) {

       /* if (student.getRollNumber() == null || !Pattern.compile("^[A-Za-z0-9]{2,20}$")
                .matcher(student.getRollNumber()).matches()) {
            throw new IllegalArgumentException("Invalid Roll Number. It must be alphanumeric (2–20 characters).");
        }*/

        if (student.getAdmissionNumber() == null || !Pattern.compile("^[A-Za-z0-9]{2,20}$")
                .matcher(student.getAdmissionNumber()).matches()) {
            throw new IllegalArgumentException("Invalid Admission Number. It must be alphanumeric or contain hyphen (2–20 characters).");
        }

        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First Name is required.");
        }
        if (!NAME_PATTERN.matcher(student.getFirstName()).matches()) {
            throw new IllegalArgumentException("Invalid First Name. Must start with a capital letter & contain only letters.");
        }

        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name is required.");
        }
        if (!NAME_PATTERN.matcher(student.getLastName()).matches()) {
            throw new IllegalArgumentException("Invalid Last Name. Must start with a capital letter & contain only letters.");
        }

        if (student.getAcademicYear() == null || student.getAcademicYear().trim().isEmpty()) {
            throw new IllegalArgumentException("Academic Year is required.");
        }
        if (!Pattern.compile("^(\\d{4})-(\\d{4})$").matcher(student.getAcademicYear()).matches()) {
            throw new IllegalArgumentException("Invalid Academic Year. Format: YYYY-YYYY");
        }

        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid Email format.");
        }
    }
}



