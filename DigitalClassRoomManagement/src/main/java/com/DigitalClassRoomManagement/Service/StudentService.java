package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.LeaveRequestDto;
import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Entity.Student;

import java.util.List;

import java.util.Optional;

    public interface StudentService {

        Student saveStudent(Student student);

        List<Student> getAllStudents();

        Optional<Student> getStudentById(Long id);

        Student updateStudent(Long id, Student updatedStudent);

        void deleteStudent(Long id);
       // LeaveRequest applyForLeave(LeaveRequest leaveRequest);

        LeaveRequest viewLeaveStatus(Long leaveRequestId);

//        public LeaveRequest applyForLeave(LeaveRequestDto dto);


    }
