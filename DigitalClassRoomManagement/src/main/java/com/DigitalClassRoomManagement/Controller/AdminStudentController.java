package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.StudentDTO;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Service.StudentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


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




    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/saveStudent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createStudent(
            @RequestPart("student") StudentDTO studentDTO,
            @RequestPart(value = "profile", required = false) MultipartFile profileFile) {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        try {
            Student savedStudent = studentService.createStudent(studentDTO, profileFile);
            HashMap<String,Object> response=new HashMap<>();
            response.put("teacherId",savedStudent.getTeacher().getId());
            response.put("classId", savedStudent.getSchoolClass().getClassId());
            response.put("Student",savedStudent);
            logger.info("Student created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error while creating student", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)

                    .body("Error creating student: " + e.getMessage());
        }
    }

    // GET ALL STUDENTS
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @GetMapping("/getAllStudent")
    public ResponseEntity<List<Student>> getAllStudents() {
        logger.info("Get all students API called");

        List<Student> students = studentService.getAllStudents();
        logger.info("Fetched all students successfully");
        return ResponseEntity.ok(students);
    }

    // GET STUDENT BY ID
   /* @PreAuthorize("hasAnyRole('STUDENT','TEACHER','ADMIN','PARENT')")
    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        logger.info("Get student by ID API called. ID: {}", id);

        Optional<Student> student = studentService.getStudentById(id);
        logger.info("Get student by ID API completed. ID: {}", id);
        return ResponseEntity.ok(student);
    }*/

    @PreAuthorize("hasAnyRole('STUDENT','TEACHER','ADMIN','PARENT')")
    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {

        HashMap<String, Object> response = new HashMap<>();


        Optional<Student> studentOpt = studentService.getStudentById(id);

        if (studentOpt.isPresent()) {
            response.put("success", true);
            response.put("id",studentOpt.get().getStudentRegId());
            response.put("data", studentOpt.get());
            response.put("message", "Student found");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Student not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // UPDATE STUDENT
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @PutMapping("/updateStudentById/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {

        logger.info("Updating student with ID: {}", id);

        Student updatedStudent = studentService.updateStudent(id, student);

        return ResponseEntity.ok(updatedStudent);
    }


    // DELETE STUDENT
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assignTeacher/{studentId}/{teacherId}")
    public ResponseEntity<?> assignTeacherToStudent(
            @PathVariable Long studentId,
            @PathVariable Long teacherId) {

        logger.info("Assigning teacher {} to student {}", teacherId, studentId);

        try {
            /*Student student = studentService.getStudentById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            student.setTeacher(teacher);
            studentService.updateStudent(studentId, student);*/

            return ResponseEntity.ok(studentService.assignTeacher(studentId, teacherId)
            );
        } catch (Exception e) {
            logger.error("Failed assigning teacher: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to assign teacher: " + e.getMessage());
        }
    }
    // assign section by studentid
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assignSection/{studentId}/{sectionId}")
    public ResponseEntity<?> assignSectionToStudent(
            @PathVariable Long studentId,
            @PathVariable Long sectionId) {

        logger.info("Assigning section {} to student {}", sectionId, studentId);

        try {

            // studentService.assignSection(studentId,sectionId);


            return ResponseEntity.ok( studentService.assignSection(studentId,sectionId));
        } catch (Exception e) {
            logger.error("Failed assigning section: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to assign section: " + e.getMessage());
        }
    }
    // assign parent by studentid
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assignParent/{studentId}/{parentId}")
    public ResponseEntity<?> assignParentToStudent(
            @PathVariable Long studentId,
            @PathVariable Long parentId) {

        logger.info("Assigning parent {} to student {}", parentId, studentId);

        try {
            /*Student student = studentService.getStudentById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Parent parent = parentRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent not found"));

            student.setParent(parent);
            studentService.updateStudent(studentId, student);*/

            return ResponseEntity.ok(studentService.assignParent(studentId, parentId));
        } catch (Exception e) {
            logger.error("Failed assigning parent: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to assign parent: " + e.getMessage());
        }
    }
    // assign class by studentbyid
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assignClass/{studentId}/{classId}")
    public ResponseEntity<?> assignClassToStudent(
            @PathVariable Long studentId,
            @PathVariable Long classId) {

        logger.info("Assigning class {} to student {}", classId, studentId);

        try {
            /*Student student = studentService.getStudentById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            SchoolClass schoolClass = schoolClassRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("Class not found"));

            student.setSchoolClass(schoolClass);
            studentService.updateStudent(studentId, student);*/

            return ResponseEntity.ok(studentService.assignClass(studentId, classId));
        } catch (Exception e) {
            logger.error("Failed assigning class: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to assign class: " + e.getMessage());
        }
    }




    // enroll student

    @PreAuthorize("hasRole('ADMIN')")
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
