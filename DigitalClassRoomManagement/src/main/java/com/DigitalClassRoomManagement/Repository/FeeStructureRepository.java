package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {

}
