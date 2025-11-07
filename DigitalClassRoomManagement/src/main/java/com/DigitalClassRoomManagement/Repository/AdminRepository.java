package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Admin,Long> {

}
