package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Enum.LeaveRequestStatus;
import com.DigitalClassRoomManagement.Repository.LeaveRequestRepository;
import com.DigitalClassRoomManagement.Service.LeaveRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Override
    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        try {
            leaveRequest.setAppliedOn(java.time.LocalDate.now());
            leaveRequest.setStatus(LeaveRequestStatus.PENDING);

            LeaveRequest saved = leaveRequestRepository.save(leaveRequest);

            log.info("Leave request created successfully with ID: {}", saved.getLeaveId());
            return saved;
        } catch (Exception e) {
            log.error("Error creating leave request: {}", e.getMessage());
            throw new RuntimeException("Failed to create leave request");
        }
    }

    @Override
    public LeaveRequest getLeaveRequestById(Long id) {
        try {
            LeaveRequest leave = leaveRequestRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Leave request not found with ID: " + id));

            log.info("Fetched leave request with ID: {}", id);
            return leave;
        } catch (Exception e) {
            log.error("Error fetching leave request with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<LeaveRequest> getAllLeaveRequests() {
        try {
            List<LeaveRequest> list = leaveRequestRepository.findAll();
            log.info("Fetched all leave requests, count = {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("Error fetching all leave requests: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch leave requests");
        }
    }

    @Override
    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequest) {
        try {
            LeaveRequest existing = getLeaveRequestById(id);

            existing.setLeaveType(leaveRequest.getLeaveType());
            existing.setFromDate(leaveRequest.getFromDate());
            existing.setToDate(leaveRequest.getToDate());
            existing.setReason(leaveRequest.getReason());
            existing.setStatus(leaveRequest.getStatus());
            existing.setApprovalDate(leaveRequest.getApprovalDate());
            existing.setRemarks(leaveRequest.getRemarks());
            existing.setApprovedByAdmin(leaveRequest.getApprovedByAdmin());
          existing.setApprovedByTeacher(leaveRequest.getApprovedByTeacher());

            LeaveRequest updated = leaveRequestRepository.save(existing);

            log.info("Leave request updated successfully with ID: {}", id);
            return updated;
        } catch (Exception e) {
            log.error("Error updating leave request with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update leave request");
        }
    }

    @Override
    public void deleteLeaveRequest(Long id) {
        try {
            leaveRequestRepository.deleteById(id);
            log.info("Leave request deleted with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting leave request with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete leave request");
        }
    }
}
