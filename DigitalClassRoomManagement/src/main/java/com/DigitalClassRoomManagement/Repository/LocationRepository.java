package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findBySchoolName(String schoolName);
    List<Location> findByschoolId(Long schoolId);
}
