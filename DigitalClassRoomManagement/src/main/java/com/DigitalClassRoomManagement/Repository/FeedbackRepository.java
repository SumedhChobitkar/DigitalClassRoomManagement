package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByStudent_StudentRegIdAndStudent_FirstName(Long studentRegId, String firstName);
    List<Feedback> findByParent_ParentIdAndParent_Name(Long parentId, String name);


}
