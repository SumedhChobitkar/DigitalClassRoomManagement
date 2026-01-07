package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
}
