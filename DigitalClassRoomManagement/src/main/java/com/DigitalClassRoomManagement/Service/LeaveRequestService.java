package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import java.util.List;

public interface LeaveRequestService {
    LeaveRequest createLeaveRequest(LeaveRequest leaveRequest);
    LeaveRequest getLeaveRequestById(Long id);
    List<LeaveRequest> getAllLeaveRequests();
    LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequest);
    void deleteLeaveRequest(Long id);
}
