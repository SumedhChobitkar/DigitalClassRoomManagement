package com.DigitalClassRoomManagement.Repository;


import com.DigitalClassRoomManagement.Entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {


    List<Holiday> findByCalendarId(Long calendarId);
    List<Holiday> findByHolidayDate(LocalDate holidayDate);

}
