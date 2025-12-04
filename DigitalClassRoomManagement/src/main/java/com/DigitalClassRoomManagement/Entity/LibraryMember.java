package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.MemberStatus;
import com.DigitalClassRoomManagement.Enum.MembershipType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "library_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    private Integer totalIssuedBooks;
}
