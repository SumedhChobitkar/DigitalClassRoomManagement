package com.DigitalClassRoomManagement.Repository;


import com.DigitalClassRoomManagement.Entity.AcademicCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendar, Long> {
}
