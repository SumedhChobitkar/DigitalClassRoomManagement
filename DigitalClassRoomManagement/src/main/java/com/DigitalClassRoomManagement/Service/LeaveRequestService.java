package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.LeaveRequestDto;
import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import java.util.List;

public interface LeaveRequestService {
   // LeaveRequest createLeaveRequest(LeaveRequest leaveRequest);
   LeaveRequest createLeaveRequest(LeaveRequestDto dto);
    LeaveRequest getLeaveRequestById(Long id);
    List<LeaveRequest> getAllLeaveRequests();
    LeaveRequest updateLeaveRequest(Long id, LeaveRequestDto dto);;
    void deleteLeaveRequest(Long id);
    List<LeaveRequest> getAllPendingRequests();
    String approveRequest(Long leaveRequestId);
    String rejectRequest(Long leaveRequestId);
}
