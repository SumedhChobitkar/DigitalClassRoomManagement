package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.DigitalClassRoomManagement.commonUtil.ValidationClass.*;

@CrossOrigin(origins = "*")
@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = Logger.getLogger(StudentServiceImpl.class.getName());

    @Autowired
    private StudentRepository studentRepository;

    // ✅ Save a new student
    @Override
    public Student saveStudent(Student student) {
        try {
            validateStudent(student);
            logger.info("Saving new student with Roll No: " + student.getRollNumber());
            return studentRepository.save(student);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving student: " + e.getMessage(), e);
            throw new RuntimeException("Failed to save student. Please check input data: " + e.getMessage());
        }
    }

    // ✅ Get all students
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

    // ✅ Get student by ID
    @Override
    public Optional<Student> getStudentById(Long id) {
        try {
            logger.info("Fetching student with ID: " + id);
            return studentRepository.findById(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching student: " + e.getMessage(), e);
            throw new RuntimeException("Unable to retrieve student with ID: " + id);
        }
    }

    // ✅ Update student details
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
                return studentRepository.save(student);
            }).orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating student: " + e.getMessage(), e);
            throw new RuntimeException("Unable to update student with ID: " + id + ". " + e.getMessage());
        }
    }

    // ✅ Delete student by ID
    @Override
    public void deleteStudent(Long id) {
        try {
            if (!studentRepository.existsById(id)) {
                throw new RuntimeException("Student not found with ID: " + id);
            }
            logger.info("Deleting student with ID: " + id);
            studentRepository.deleteById(id);
            logger.info("Student deleted successfully with ID: " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting student: " + e.getMessage(), e);
            throw new RuntimeException("Unable to delete student with ID: " + id);
        }
    }

    // ✅ Validation logic
    private void validateStudent(Student student) {
        // Roll Number
        if (student.getRollNumber() == null || !Pattern.compile("^[A-Za-z0-9]{2,20}$")
                .matcher(student.getRollNumber()).matches()) {
            throw new IllegalArgumentException("Invalid Roll Number. It must be alphanumeric (2–20 characters).");
        }

        // Admission Number
        if (student.getAdmissionNumber() == null || !Pattern.compile("^[A-Za-z0-9-]{2,20}$")
                .matcher(student.getAdmissionNumber()).matches()) {
            throw new IllegalArgumentException("Invalid Admission Number. It must be alphanumeric or contain hyphen (2–20 characters).");
        }

        // First Name
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First Name is required.");
        }
        if (!NAME_PATTERN.matcher(student.getFirstName()).matches()) {
            throw new IllegalArgumentException("Invalid First Name. It must start with a capital letter and contain only alphabets.");
        }

        // Last Name
        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name is required.");
        }
        if (!NAME_PATTERN.matcher(student.getLastName()).matches()) {
            throw new IllegalArgumentException("Invalid Last Name. It must start with a capital letter and contain only alphabets.");
        }

        // Academic Year
        if (student.getAcademicYear() == null || student.getAcademicYear().trim().isEmpty()) {
            throw new IllegalArgumentException("Academic Year is required.");
        }
        if (!Pattern.compile("^(\\d{4})-(\\d{4})$").matcher(student.getAcademicYear()).matches()) {
            throw new IllegalArgumentException("Invalid Academic Year. Format should be YYYY-YYYY (e.g., 2024-2025).");
        }

        // Email
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid Email format.");
        }
    }
}
