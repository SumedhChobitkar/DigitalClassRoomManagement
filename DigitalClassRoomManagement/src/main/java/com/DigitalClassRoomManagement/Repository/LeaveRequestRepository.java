package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Enum.LeaveRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByStatus(LeaveRequestStatus status);

    List<LeaveRequest> findByApprovedByTeacher_Id(Long teacherId);
    List<LeaveRequest> findByUserRole(String role);
    List<LeaveRequest> findByUser_UserId(Long userId);

}


