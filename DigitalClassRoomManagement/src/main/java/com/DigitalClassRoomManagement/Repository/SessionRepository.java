package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    // For fetching all Sessions related to teacher:-
    List<Session> findByTeacher_Id(Long teacherId);


}
