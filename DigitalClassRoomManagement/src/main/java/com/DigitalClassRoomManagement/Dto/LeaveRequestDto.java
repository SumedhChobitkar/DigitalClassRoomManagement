package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.LeaveRequestStatus;
import com.DigitalClassRoomManagement.Enum.LeaveType;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestDto {

    private Long leaveId;

    // User ID instead of full User object
    private Long userId;

    private LeaveType leaveType;

    private LocalDate fromDate;

    private LocalDate toDate;

    private String reason;

    private LeaveRequestStatus status;

    private LocalDate appliedOn;

    // Teacher ID instead of Teacher entity
    private Long approvedByTeacherId;

    // Admin ID instead of Admin entity
    private Long approvedByAdminId;

    private LocalDate approvalDate;

    private String remarks;
}
