package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.DigitalBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigitalBookRepository  extends JpaRepository<DigitalBook, Long> {
}
