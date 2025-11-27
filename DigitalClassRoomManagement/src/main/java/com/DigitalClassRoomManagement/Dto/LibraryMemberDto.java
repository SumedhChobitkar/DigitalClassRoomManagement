package com.DigitalClassRoomManagement.Dto;
import com.DigitalClassRoomManagement.Enum.MembershipType;
import com.DigitalClassRoomManagement.Enum.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryMemberDto {

    private Long memberId; // Optional for creation
    private Long userId;
    private MembershipType membershipType;
    private LocalDate joinDate;
    private MemberStatus status;
    private Integer totalIssuedBooks;
}
