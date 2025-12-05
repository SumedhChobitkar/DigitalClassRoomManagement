package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.LeaveRequestDto;
import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Enum.LeaveRequestStatus;
import com.DigitalClassRoomManagement.Repository.LeaveRequestRepository;
import com.DigitalClassRoomManagement.Repository.UserRepository;
import com.DigitalClassRoomManagement.Service.LeaveRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private UserRepository userRepository;

//    @Override
//    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
//        try {
//
//            leaveRequest.setAppliedOn(LocalDate.now());
//            leaveRequest.setStatus(LeaveRequestStatus.PENDING);
//           User user= userRepository.findById(leaveRequest.getUser().getUserId())
//                           .orElseThrow(()-> new RuntimeException("User id not found in leave request."));
//            leaveRequest.setUser(user);
//
//            LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
//
//            log.info("Leave request created successfully with ID: {}", saved.getLeaveId());
//            return saved;
//        } catch (Exception e) {
//            log.error("Error creating leave request: {}", e.getMessage());
//            throw new RuntimeException("Failed to create leave request");
//        }
//    }
@Override
public LeaveRequest createLeaveRequest(LeaveRequestDto dto) {
    try {
        log.info("starting leave request "+dto.getUserId());
        // Fetch User
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
        log.info("after chaking leave request "+dto.getUserId());
        // Build LeaveRequest entity
        LeaveRequest leaveRequest = LeaveRequest.builder()

                .user(user)
                .leaveType(dto.getLeaveType())
                .fromDate(dto.getFromDate())
                .toDate(dto.getToDate())
                .reason(dto.getReason())
                .status(LeaveRequestStatus.PENDING)
                .appliedOn(LocalDate.now())
                .remarks(dto.getRemarks())
                .build();
        log.info("end leave request "+dto.getUserId());
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

//    @Override
//    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequest) {
//        try {
//            LeaveRequest existing = getLeaveRequestById(id);
//
//            existing.setLeaveType(leaveRequest.getLeaveType());
//            existing.setFromDate(leaveRequest.getFromDate());
//            existing.setToDate(leaveRequest.getToDate());
//            existing.setReason(leaveRequest.getReason());
//            existing.setStatus(leaveRequest.getStatus());
//            existing.setApprovalDate(leaveRequest.getApprovalDate());
//            existing.setRemarks(leaveRequest.getRemarks());
//            existing.setApprovedByAdmin(leaveRequest.getApprovedByAdmin());
//            existing.setApprovedByTeacher(leaveRequest.getApprovedByTeacher());
//
//            LeaveRequest updated = leaveRequestRepository.save(existing);
//
//            log.info("Leave request updated successfully with ID: {}", id);
//            return updated;
//        } catch (Exception e) {
//            log.error("Error updating leave request with ID {}: {}", id, e.getMessage());
//            throw new RuntimeException("Failed to update leave request");
//        }
//    }
@Override
public LeaveRequest updateLeaveRequest(Long id, LeaveRequestDto dto) {
    LeaveRequest existing = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Leave Request Not Found"));
    // Only update allowed fields
    existing.setLeaveType(dto.getLeaveType());
    existing.setFromDate(dto.getFromDate());
    existing.setToDate(dto.getToDate());
    existing.setReason(dto.getReason());
    existing.setRemarks(dto.getRemarks());

    return leaveRequestRepository.save(existing);
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

    //  Fetch All Pending Requests
    @Override
    public List<LeaveRequest> getAllPendingRequests() {
        try {
            log.info("Fetching all pending leave requests");
            return leaveRequestRepository.findByStatus(LeaveRequestStatus.PENDING);
        } catch (Exception e) {
            log.error("Error while fetching pending requests", e);
            throw new RuntimeException("Unable to fetch pending leave requests");
        }
    }

    // Approve Leave
    @Override
    public String approveRequest(Long leaveRequestId) {
        try {
            LeaveRequest request = leaveRequestRepository.findById(leaveRequestId)
                    .orElseThrow(() -> new RuntimeException("Leave Request Not Found"));

            request.setStatus(LeaveRequestStatus.APPROVED);
            leaveRequestRepository.save(request);

            log.info("Leave Request {} approved", leaveRequestId);
            return "Leave Request Approved Successfully";
        } catch (Exception e) {
            log.error("Error while approving leave request {}", leaveRequestId, e);
            throw e;
        }
    }

    // Reject Leave
    @Override
    public String rejectRequest(Long leaveRequestId) {
        try {
            LeaveRequest request = leaveRequestRepository.findById(leaveRequestId)
                    .orElseThrow(() -> new RuntimeException("Leave Request Not Found"));

            request.setStatus(LeaveRequestStatus.REJECTED);
            leaveRequestRepository.save(request);

            log.info("Leave Request {} rejected", leaveRequestId);
            return "Leave Request Rejected Successfully";
        } catch (Exception e) {
            log.error("Error while rejecting leave request {}", leaveRequestId, e);
            throw e;
        }
    }
}

