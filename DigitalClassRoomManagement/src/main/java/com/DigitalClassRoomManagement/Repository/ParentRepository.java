package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Parent;
import com.DigitalClassRoomManagement.Enum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    @Query("SELECT p FROM Parent p JOIN p.student s WHERE s.studentRegId = :studentId")
    Parent findByStudentId(Long studentId);
    //new line
    List<Parent> findByStatus(Status status);
}
