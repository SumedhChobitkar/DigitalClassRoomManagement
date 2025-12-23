package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.LeaveRequestDto;
import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/leaves")
@Tag(name = "Leave Request APIs", description = "APIs for managing leave requests of users")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    // Create Leave Request
    @Operation(summary = "Create a new leave request",
            description = "This API is used by students/teachers to submit a leave request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave request created successfully",
                            content = @Content(schema = @Schema(implementation = LeaveRequest.class))),
                    @ApiResponse(responseCode = "500", description = "Error while creating leave request")
            })
//    @PostMapping("/createLeaveRequest")
//   public ResponseEntity<?> createLeave(@RequestBody LeaveRequestDto leaveRequest) {
//       try {
//          LeaveRequest created = leaveRequestService.createLeaveRequest(leaveRequest);
//           return ResponseEntity.ok(created);        } catch (Exception e) {
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                  .body("Error while creating leave request: " + e.getMessage());
//        }
//       }
    @PostMapping("/createLeaveRequest")
    public ResponseEntity<?> createLeave(@RequestBody LeaveRequestDto leaveRequestDto) {
        try {
            LeaveRequest created = leaveRequestService.createLeaveRequest(leaveRequestDto);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating leave request: " + e.getMessage());
        }
    }




    // Get All Leave Requests
    @Operation(summary = "Get all leave requests",
            description = "Fetches all leave requests from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of leave requests fetched successfully"),
                    @ApiResponse(responseCode = "500", description = "Unable to fetch leave list")
            })

    @GetMapping("/getAllLeaves")
    public ResponseEntity<?> getAllLeaves() {
        try {
            List<LeaveRequest> list = leaveRequestService.getAllLeaveRequests();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching leave list: " + e.getMessage());
        }
    }

    // Get Leave by ID
    @Operation(summary = "Get leave request by ID",
            description = "Fetch a single leave request using its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave request fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "Leave request not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getLeaveById(@PathVariable Long id) {
        try {
            LeaveRequest leave = leaveRequestService.getLeaveRequestById(id);
            return ResponseEntity.ok(leave);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Leave request not found with ID: " + id);
        }
    }

    // Update Leave
    @Operation(summary = "Update a leave request",
            description = "Updates the details of an existing leave request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave request updated successfully"),
                    @ApiResponse(responseCode = "500", description = "Error updating leave request")
            })
//    @PutMapping("/updateLeaveById/{Id}")
////    public ResponseEntity<?> updateLeave(@PathVariable Long id,
////                                         @RequestBody LeaveRequest leaveRequest) {
////        try {
////            LeaveRequest updated = leaveRequestService.updateLeaveRequest(id, leaveRequest);
////            return ResponseEntity.ok(updated);
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                    .body("Error updating leave request: " + e.getMessage());
////        }
////    }

    @PutMapping("/updateleaveid/{id}")
    public ResponseEntity<LeaveRequest> updateLeave(@PathVariable Long id, @RequestBody LeaveRequestDto dto) {
        LeaveRequest updated = leaveRequestService.updateLeaveRequest(id, dto);
        return ResponseEntity.ok(updated);
    }




    // Delete Leave
    @Operation(summary = "Delete a leave request",
            description = "Deletes a leave request by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave request deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Leave request not found")
            })
    @DeleteMapping("/DeleteLeaveById/{Id}")
    public ResponseEntity<?> deleteLeave(@PathVariable Long id) {
        try {
            leaveRequestService.deleteLeaveRequest(id);
            return ResponseEntity.ok("Leave request deleted successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error deleting leave request. ID may not exist: " + id);
        }
    }
}
