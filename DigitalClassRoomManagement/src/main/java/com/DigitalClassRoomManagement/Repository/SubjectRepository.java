package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.SchoolClass;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {

    List<Subject> findBySchoolClassAndIsActiveTrue(SchoolClass schoolClass);
}