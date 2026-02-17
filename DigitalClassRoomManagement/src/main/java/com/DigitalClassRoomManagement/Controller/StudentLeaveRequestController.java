package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.LeaveRequestDto;
import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Service.LeaveRequestService;
import com.DigitalClassRoomManagement.Service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/student/leaves")
@CrossOrigin(origins = "*")
public class StudentLeaveRequestController {

    @Autowired
    private StudentService studentService;
@Autowired
private LeaveRequestService leaveRequestService;
    // 1Apply For Leave
    @Operation(
            summary = "Apply for leave",
            description = "Student applies for leave by sending LeaveRequest entity.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave applied successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
//    @PostMapping("/apply")
//    public ResponseEntity<?> applyForLeave(@RequestBody LeaveRequest leaveRequest) {
//        try {
//            LeaveRequest savedRequest = studentService.applyForLeave(leaveRequest);
//            return ResponseEntity.ok(savedRequest);
//        } catch (Exception e) {
//            log.error("Error while applying for leave", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Something went wrong while applying for leave");
//        }
//    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyForLeave(@RequestBody LeaveRequestDto leaveRequestDto) {
        try {
            log.info("Applying for leave for studentId: {}", leaveRequestDto.getUserId());
            LeaveRequest saved= leaveRequestService.createLeaveRequest(leaveRequestDto);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            log.error("Error while applying for leave", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while applying for leave");
        }
    }



    // View Leave Approval Status By LeaveRequestId
    @Operation(
            summary = "View Leave Approval Status",
            description = "Student checks leave approval status using leaveRequestId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave status retrieved"),
                    @ApiResponse(responseCode = "404", description = "Leave request not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }

    )
    @GetMapping("/status/{leaveRequestId}")
    public ResponseEntity<?> viewLeaveApprovalStatus(@PathVariable Long leaveRequestId) {
        try {
            LeaveRequest request = studentService.viewLeaveStatus(leaveRequestId);
            return ResponseEntity.ok(request);
        } catch (RuntimeException ex) {
            log.error("Leave Request not found: {}", leaveRequestId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            log.error("Error while fetching leave status {}", leaveRequestId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while fetching leave status");
        }
    }
    //  Get All Leaves for a Student
    @Operation(
            summary = "Get all leave requests by studentId",
            description = "Fetch list of all leave requests for a student",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave list retrieved")
            }
    )
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getAllLeavesByStudent(@PathVariable Long studentId) {
        try {
            List<LeaveRequest> leaves = leaveRequestService.getLeavesByStudentId(studentId);
            if (leaves.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No leave requests found for studentId: " + studentId);
            }
            return ResponseEntity.ok(leaves);
        } catch (Exception e) {
            log.error("Error fetching leaves for studentId {}", studentId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while fetching leaves");
        }
    }
}
