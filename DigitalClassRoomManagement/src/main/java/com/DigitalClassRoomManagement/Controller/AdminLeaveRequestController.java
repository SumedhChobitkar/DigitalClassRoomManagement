package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/leaves")
@Slf4j
public class AdminLeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    // View All Pending Leave Requests
    @Operation(
            summary = "Get all pending leave requests",
            description = "This API returns list of all teacher pending leave requests.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched pending leave requests"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/pending")
    public ResponseEntity<?> viewAllPendingLeaveRequests() {
        try {
            List<LeaveRequest> requests = leaveRequestService.getAllPendingRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error while fetching pending leave requests", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while fetching pending leave requests");
        }
    }

    //Approve Leave Request
    @Operation(
            summary = "Approve teacher leave request",
            description = "Admin approves a teacher's leave request using leaveRequestId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave approved"),
                    @ApiResponse(responseCode = "404", description = "Leave request not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PutMapping("/approve/{leaveRequestId}")
    public ResponseEntity<?> approveTeacherLeaveRequest(@PathVariable Long leaveRequestId) {
        try {
            String msg = leaveRequestService.approveRequest(leaveRequestId);
            return ResponseEntity.ok(msg);
        } catch (RuntimeException ex) {
            log.error("Leave Request not found: {}", leaveRequestId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            log.error("Error while approving leave request {}", leaveRequestId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while approving leave request");
        }
    }

    //Reject Leave Request
    @Operation(
            summary = "Reject teacher leave request",
            description = "Admin rejects a teacher's leave request using leaveRequestId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave rejected"),
                    @ApiResponse(responseCode = "404", description = "Leave request not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PutMapping("/reject/{leaveRequestId}")
    public ResponseEntity<?> rejectTeacherLeaveRequest(@PathVariable Long leaveRequestId) {
        try {
            String msg = leaveRequestService.rejectRequest(leaveRequestId);
            return ResponseEntity.ok(msg);
        } catch (RuntimeException ex) {
            log.error("Leave Request not found: {}", leaveRequestId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            log.error("Error while rejecting leave request {}", leaveRequestId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while rejecting leave request");
        }
    }
}
