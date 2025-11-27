package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
    public interface StudentRepository extends JpaRepository<Student, Long> {
        List<Student> findByAcademicYear(String academicYear);

}
