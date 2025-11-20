package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.Student;

import java.util.List;

import java.util.Optional;

    public interface StudentService {

        Student saveStudent(Student student);

        List<Student> getAllStudents();

        Optional<Student> getStudentById(Long id);

        Student updateStudent(Long id, Student updatedStudent);

        void deleteStudent(Long id);


}
