package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

}
