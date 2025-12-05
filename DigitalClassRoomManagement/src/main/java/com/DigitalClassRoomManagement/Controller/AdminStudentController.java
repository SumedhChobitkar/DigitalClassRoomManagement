package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.StudentDTO;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Service.StudentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
@CrossOrigin("*")
public class AdminStudentController {

    private static final Logger logger = LoggerFactory.getLogger(AdminStudentController.class);

    @Autowired
    private StudentService studentService;

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




    // CREATE STUDENT
    @PostMapping("/saveStudent")
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDTO) {

        logger.info("Received request to create student: {}", studentDTO.getFirstName());

        try {
            Student student = new Student();
            student.setRollNumber(studentDTO.getRollNumber());
            student.setAdmissionNumber(studentDTO.getAdmissionNumber());
            student.setFirstName(studentDTO.getFirstName());
            student.setMiddleName(studentDTO.getMiddleName());
            student.setLastName(studentDTO.getLastName());
            student.setAcademicYear(studentDTO.getAcademicYear());
            student.setEmail(studentDTO.getEmail());
            student.setMobileNumber(studentDTO.getMobileNumber());
            student.setDateOfBirth(LocalDate.parse(studentDTO.getDateOfBirth()));
            student.setGender(studentDTO.getGender());
            student.setStreet(studentDTO.getStreet());
            student.setCity(studentDTO.getCity());
            student.setState(studentDTO.getState());
            student.setCountry(studentDTO.getCountry());
            student.setPinCode(studentDTO.getPinCode());
            student.setTeacherMailId(studentDTO.getTeacherMailId());
            student.setTeacher(studentDTO.getTeacher());
            student.setSection(studentDTO.getSection());
            student.setSchoolClass(studentDTO.getSchoolClass());

            if (studentDTO.getUsers() != null && studentDTO.getUsers().getUserId() != null) {
                logger.info("Fetching user with ID: {}", studentDTO.getUsers().getUserId());

                User existingUser = userRepository.findById(studentDTO.getUsers().getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + studentDTO.getUsers().getUserId()));
                student.setUsers(existingUser);
            } else {
                logger.error("User ID missing for student creation");
                throw new RuntimeException("User ID is required for student registration.");
            }

            Student saved = studentService.saveStudent(student);
            logger.info("Student created successfully with ID: {}", saved.getStudentId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            logger.error("Error creating student: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating student: Failed to save student. Please check input data: " + e.getMessage());
        }
    }

    // GET ALL STUDENTS
    @GetMapping("/getAllStudent")
    public ResponseEntity<?> getAllStudents() {
        logger.info("Fetching all students");

        try {
            List<StudentDTO> students = studentService.getAllStudents()
                    .stream()
                    .map(student -> new StudentDTO(
                            student.getStudentId(),
                            student.getRollNumber(),
                            student.getAdmissionNumber(),
                            student.getFirstName(),
                            student.getMiddleName(),
                            student.getLastName(),
                            student.getAcademicYear(),
                            student.getEmail(),
                            student.getMobileNumber(),
                            (student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null),
                            student.getGender(),
                            student.getStreet(),
                            student.getCity(),
                            student.getState(),
                            student.getCountry(),
                            student.getPinCode(),
                            student.getUsers(),
                            student.getTeacherMailId(),
                            student.getTeacher(),
                            student.getSection(),
                            student.getSchoolClass()

                    ))
                    .collect(Collectors.toList());

            logger.info("Total students fetched: {}", students.size());

            return ResponseEntity.ok(students);

        } catch (Exception e) {
            logger.error("Error fetching students: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching students: " + e.getMessage());
        }
    }

    // GET STUDENT BY ID
    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {

        logger.info("Fetching student by ID: {}", id);

        try {
            Student student = studentService.getStudentById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

            StudentDTO responseDTO = new StudentDTO(
                    student.getStudentId(),
                    student.getRollNumber(),
                    student.getAdmissionNumber(),
                    student.getFirstName(),
                    student.getMiddleName(),
                    student.getLastName(),
                    student.getAcademicYear(),
                    student.getEmail(),
                    student.getMobileNumber(),
                    (student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null),
                    student.getGender(),
                    student.getStreet(),
                    student.getCity(),
                    student.getState(),
                    student.getCountry(),
                    student.getPinCode(),
                    student.getUsers(),
                    student.getTeacherMailId(),
                    student.getTeacher(),
                    student.getSection(),
                    student.getSchoolClass()
            );

            logger.info("Student found: {}", student.getFirstName());
            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            logger.warn("Student not found with ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error retrieving student with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving student: " + e.getMessage());
        }
    }

    // UPDATE STUDENT
    @PutMapping("/updateStudentById/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {

        logger.info("Updating student with ID: {}", id);

        try {
            Student existingStudent = studentService.getStudentById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

            existingStudent.setRollNumber(studentDTO.getRollNumber());
            existingStudent.setAdmissionNumber(studentDTO.getAdmissionNumber());
            existingStudent.setFirstName(studentDTO.getFirstName());
            existingStudent.setMiddleName(studentDTO.getMiddleName());
            existingStudent.setLastName(studentDTO.getLastName());
            existingStudent.setAcademicYear(studentDTO.getAcademicYear());
            existingStudent.setEmail(studentDTO.getEmail());
            existingStudent.setMobileNumber(studentDTO.getMobileNumber());
            if (studentDTO.getDateOfBirth() != null) {
                existingStudent.setDateOfBirth(LocalDate.parse(studentDTO.getDateOfBirth()));
            }
            existingStudent.setGender(studentDTO.getGender());
            existingStudent.setStreet(studentDTO.getStreet());
            existingStudent.setCity(studentDTO.getCity());
            existingStudent.setState(studentDTO.getState());
            existingStudent.setCountry(studentDTO.getCountry());
            existingStudent.setPinCode(studentDTO.getPinCode());
            existingStudent.setUsers(studentDTO.getUsers());

            Student updated = studentService.updateStudent(id, existingStudent);

            logger.info("Student updated successfully: {}", updated.getStudentId());
            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            logger.warn("Student not found for update with ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating student: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating student: " + e.getMessage());
        }
    }

    // DELETE STUDENT
    @DeleteMapping("/deleteStudentById/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {

        logger.info("Deleting student with ID: {}", id);

        try {
            studentService.deleteStudent(id);
            logger.info("Student deleted successfully: {}", id);
            return ResponseEntity.ok("Student deleted successfully!");
        } catch (RuntimeException e) {
            logger.warn("Student not found while deleting ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error deleting student with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting student: " + e.getMessage());
        }
    }
      // assign teacher by studentid
    @PutMapping("/assignTeacher/{studentId}/{teacherId}")
    public ResponseEntity<?> assignTeacherToStudent(
            @PathVariable Long studentId,
            @PathVariable Long teacherId) {

        logger.info("Assigning teacher {} to student {}", teacherId, studentId);

        try {
            Student student = studentService.getStudentById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            student.setTeacher(teacher);
            studentService.updateStudent(studentId, student);

            return ResponseEntity.ok("Teacher assigned successfully!");
        } catch (Exception e) {
            logger.error("Failed assigning teacher: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to assign teacher: " + e.getMessage());
        }
    }
     // assign section by studentid
    @PutMapping("/assignSection/{studentId}/{sectionId}")
    public ResponseEntity<?> assignSectionToStudent(
            @PathVariable Long studentId,
            @PathVariable Long sectionId) {

        logger.info("Assigning section {} to student {}", sectionId, studentId);

        try {
            Student student = studentService.getStudentById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new RuntimeException("Section not found"));

            student.setSection(section);
            studentService.updateStudent(studentId, student);

            return ResponseEntity.ok("Section assigned successfully!");
        } catch (Exception e) {
            logger.error("Failed assigning section: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to assign section: " + e.getMessage());
        }
    }
    // assign parent by studentid
    @PutMapping("/assignParent/{studentId}/{parentId}")
    public ResponseEntity<?> assignParentToStudent(
            @PathVariable Long studentId,
            @PathVariable Long parentId) {

        logger.info("Assigning parent {} to student {}", parentId, studentId);

        try {
            Student student = studentService.getStudentById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Parent parent = parentRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent not found"));

            student.setParent(parent);
            studentService.updateStudent(studentId, student);

            return ResponseEntity.ok("Parent assigned successfully!");
        } catch (Exception e) {
            logger.error("Failed assigning parent: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to assign parent: " + e.getMessage());
        }
    }
     // assign class by studentbyid
    @PutMapping("/assignClass/{studentId}/{classId}")
    public ResponseEntity<?> assignClassToStudent(
            @PathVariable Long studentId,
            @PathVariable Long classId) {

        logger.info("Assigning class {} to student {}", classId, studentId);

        try {
            Student student = studentService.getStudentById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            SchoolClass schoolClass = schoolClassRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("Class not found"));

            student.setSchoolClass(schoolClass);
            studentService.updateStudent(studentId, student);

            return ResponseEntity.ok("Class assigned successfully!");
        } catch (Exception e) {
            logger.error("Failed assigning class: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to assign class: " + e.getMessage());
        }
    }





    @PutMapping("/enroll/{studentId}")
    public ResponseEntity<StudentDTO.StudentCreateResponse> enrollStudent(
            @PathVariable Long studentId,
            @RequestBody StudentDTO.EnrollmentRequest request) {

        logger.info("Enrolling student with ID: {}", studentId);

        try {
            StudentDTO.StudentCreateResponse response =
                    studentService.enrollStudent(studentId, request);
            logger.info("Student enrolled successfully. ID: {}", studentId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Enrollment failed for student ID {}: {}", studentId, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new StudentDTO.StudentCreateResponse(
                            "FAILED",
                            e.getMessage()
                    ));
        } catch (Exception e) {
            logger.error("Unexpected error during enrollment for student ID {}: {}", studentId, e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StudentDTO.StudentCreateResponse(
                            "FAILED",
                            "Internal server error"
                    ));
        }
    }






}
