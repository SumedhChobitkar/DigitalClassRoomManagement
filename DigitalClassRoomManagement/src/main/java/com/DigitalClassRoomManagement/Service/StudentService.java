package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.LeaveRequestDto;
import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Dto.StudentDTO;
import com.DigitalClassRoomManagement.Entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.util.Optional;

    public interface StudentService {

        //Student saveStudent(Student student);
        public Student createStudent(StudentDTO dto, MultipartFile profileFile);

        List<Student> getAllStudents();

        Optional<Student> getStudentById(Long id);

        Student updateStudent(Long id, Student updatedStudent);

        void deleteStudent(Long id);
       // LeaveRequest applyForLeave(LeaveRequest leaveRequest);

        Student assignTeacher(Long studentId, Long teacherId);

        Student assignSection(Long studentId, Long sectionId);

        Student assignParent(Long studentId, Long parentId);

        Student assignClass(Long studentId, Long classId);


        LeaveRequest viewLeaveStatus(Long leaveRequestId);

//        public LeaveRequest applyForLeave(LeaveRequestDto dto);
        List<Student> getStudentsByClass(String className);

       // StudentDTO.EnrollmentRequest.StudentCreateResponse enrollStudent(Long studentId, StudentDTO.EnrollmentRequest request);
       StudentDTO.StudentCreateResponse enrollStudent(Long studentId, StudentDTO.EnrollmentRequest request);

        Student updatelocation(Long studentId, Long locationId);




    }
