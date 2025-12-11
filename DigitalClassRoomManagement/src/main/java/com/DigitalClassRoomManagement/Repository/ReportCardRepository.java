package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.ReportCard;
import com.DigitalClassRoomManagement.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportCardRepository extends JpaRepository<ReportCard, Long> {

    List<ReportCard> findByStudent(Student student);
}
