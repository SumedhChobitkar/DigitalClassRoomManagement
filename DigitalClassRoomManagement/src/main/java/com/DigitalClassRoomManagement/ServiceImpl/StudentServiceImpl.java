package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.StudentDTO;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Exception.StudentNotFoundException;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Save a new student
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

/*    @Override
    public StudentDTO.EnrollmentRequest.StudentCreateResponse enrollStudent(Long studentId, StudentDTO.EnrollmentRequest request) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // update academic information
        student.setAcademicYear(request.getAcademicYear());
        student.setStandard(request.getStandard());
        student.setDivision(request.getDivision());

        studentRepository.save(student);

        return new StudentDTO.EnrollmentRequest.StudentCreateResponse("SUCCESS",
                "Student enrolled successfully for academic year " + request.getAcademicYear());
    }*/


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



// Validation logic
private void validateStudent(Student student) {

    if (student.getRollNumber() == null || !Pattern.compile("^[A-Za-z0-9]{2,20}$")
            .matcher(student.getRollNumber()).matches()) {
        throw new IllegalArgumentException("Invalid Roll Number. It must be alphanumeric (2–20 characters).");
    }

    if (student.getAdmissionNumber() == null || !Pattern.compile("^[A-Za-z0-9-]{2,20}$")
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



