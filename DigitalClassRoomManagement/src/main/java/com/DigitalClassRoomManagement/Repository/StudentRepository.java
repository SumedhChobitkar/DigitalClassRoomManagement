package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Entity.Section;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Enum.LeaveRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface StudentRepository extends JpaRepository<Student, Long> {
        List<Student> findByAcademicYear(String academicYear);
        List<Student> findBySchoolClass_ClassId(Long classId);
        List<Student> findBySection(Section section);
}
