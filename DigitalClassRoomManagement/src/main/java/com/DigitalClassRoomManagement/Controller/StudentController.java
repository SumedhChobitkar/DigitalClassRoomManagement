package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.StudentDTO;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Repository.UserRepository;
import com.DigitalClassRoomManagement.Service.StudentService;

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
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserRepository userRepository;

    //  CREATE STUDENT

    @PostMapping("/saveStudent")
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDTO) {
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

            // ✅ Fetch existing user and attach
            if (studentDTO.getUsers() != null && studentDTO.getUsers().getUserId() != null) {
                User existingUser = userRepository.findById(studentDTO.getUsers().getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + studentDTO.getUsers().getUserId()));
                student.setUsers(existingUser);
            } else {
                throw new RuntimeException("User ID is required for student registration.");
            }

            Student saved = studentService.saveStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating student: Failed to save student. Please check input data: " + e.getMessage());
        }
    }

    // ✅ GET ALL STUDENTS
    @GetMapping("/getAllStudent")
    public ResponseEntity<?> getAllStudents() {
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
                            student.getUsers()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(students);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching students: " + e.getMessage());
        }
    }

    // ✅ GET STUDENT BY ID
    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
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
                    student.getUsers()
            );

            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving student: " + e.getMessage());
        }
    }

    // ✅ UPDATE STUDENT
    @PutMapping("/updateStudentById/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
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

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating student: " + e.getMessage());
        }
    }

    // ✅ DELETE STUDENT
    @DeleteMapping("/deleteStudentById/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting student: " + e.getMessage());
        }
    }
}
