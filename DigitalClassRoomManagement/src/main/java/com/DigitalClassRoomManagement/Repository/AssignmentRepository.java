package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // Find all assignments by teacher ID
    List<Assignment> findByTeacherId(Long teacherId);

    // Find assignment by assignment ID and teacher ID
    Optional<Assignment> findByAssignmentIdAndTeacherId(Long assignmentId, Long teacherId);

    // Delete assignment by assignment ID and teacher ID
    void deleteByAssignmentIdAndTeacherId(Long assignmentId, Long teacherId);

    // Find assignments by class ID
    List<Assignment> findBySchoolClass_ClassId(Long classId);

    @Query("""
SELECT s.studentRegId, a.title, a.dueDate
FROM Assignment a
JOIN Student s
  ON s.schoolClass = a.schoolClass
 AND s.section = a.section
WHERE a.dueDate >= :start
  AND a.dueDate < :end
""")
    List<Object[]> findStudentRegIdsForAssignmentReminder(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


}
