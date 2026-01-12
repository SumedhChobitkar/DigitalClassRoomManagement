
package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.LeaveRequestDto;
import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/teacher/leave")
@Tag(name = "Teacher Leave Request Controller", description = "Teacher leave management APIs")
public class TeacherLeaveRequestController {

    @Autowired
    private TeacherService teacherService;


    //  Apply For Leave
    @Operation(summary = "Apply for leave as a teacher")
    @ApiResponse(responseCode = "200", description = "Leave request applied successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LeaveRequest.class)))
//    @PostMapping("/apply")
//    public ResponseEntity<?> applyForLeave(@RequestBody LeaveRequest leaveRequest) {
//        try {
//         //   LeaveRequest saved = teacherService.applyForLeave(leaveRequest);
//            log.info("Teacher applied for leave: {}", saved.getLeaveId());
//            return ResponseEntity.ok(saved);
//
//        } catch (Exception e) {
//            log.error("Error applying for teacher leave", e);
//            return ResponseEntity.internalServerError().body("Failed to apply leave");
//        }
//    }
    @PostMapping("/apply")
    public ResponseEntity<?> applyForLeave(@RequestBody LeaveRequestDto dto) {
        try {
            log.info("Teacher applying for leave, teacherId: {}", dto.getUserId());

            LeaveRequest saved = teacherService.applyForLeave(dto);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            log.error("Error applying teacher leave", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while applying for teacher leave");
        }
    }


    //  View all pending leave requests of students
    @Operation(summary = "View all pending student leave requests")
    @GetMapping("/student/pending")
    public ResponseEntity<?> viewStudentPendingLeaveRequests() {
        try {
            List<LeaveRequest> list = teacherService.viewStudentPendingLeaveRequests();
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            log.error("Error fetching student pending leave requests", e);
            return ResponseEntity.internalServerError().body("Could not fetch student leave requests");
        }
    }


    //  Approve Student Leave
    @Operation(summary = "Approve a student leave request")
    @PutMapping("/student/approve/{leaveRequestId}")
    public ResponseEntity<?> approveStudentLeaveRequest(@PathVariable Long leaveRequestId) {
        try {
            LeaveRequest updated = teacherService.approveStudentLeaveRequest(leaveRequestId);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            log.error("Error approving leave request {}", leaveRequestId, e);
            return ResponseEntity.internalServerError().body("Could not approve leave");
        }
    }


    //  Reject Student Leave
    @Operation(summary = "Reject a student leave request")
    @PutMapping("/student/reject/{leaveRequestId}")
    public ResponseEntity<?> rejectStudentLeaveRequest(@PathVariable Long leaveRequestId,
                                                       @RequestParam(required = false) String remarks) {
        try {
            LeaveRequest updated = teacherService.rejectStudentLeaveRequest(leaveRequestId, remarks);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            log.error("Error rejecting leave request {}", leaveRequestId, e);
            return ResponseEntity.internalServerError().body("Could not reject leave");
        }
    }

        //getleavebyteacherid
        @Operation(summary = "Get all student leave requests by teacher ID")
        @GetMapping("/student/teacher/{teacherId}")
        public ResponseEntity<?> getLeaveByTeacherId (@PathVariable Long teacherId){
            try {
                log.info("Fetching leave requests for teacherId: {}", teacherId);

                List<LeaveRequest> leaveList = teacherService.getLeaveByTeacherId(teacherId);

                if (leaveList.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("No leave requests found for this teacher");
                }

                return ResponseEntity.ok(leaveList);

            } catch (Exception e) {
                log.error("Error getting leave list by teacherId {}", teacherId, e);
                return ResponseEntity.internalServerError()
                        .body("Failed to fetch leave requests");
            }
        }

    //  Get ALL teacher leave requests
    @Operation(summary = "Get all teacher leave requests")
    @GetMapping("/all")
    public ResponseEntity<?> getAllTeacherLeaves() {
        try {
            log.info("Fetching all teacher leave requests");

            List<LeaveRequest> leaveList = teacherService.getAllTeacherLeaves();

            if (leaveList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No teacher leave requests found");
            }

            return ResponseEntity.ok(leaveList);

        } catch (Exception e) {
            log.error("Error fetching all teacher leave requests", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to fetch teacher leave requests");
        }
    }

}
