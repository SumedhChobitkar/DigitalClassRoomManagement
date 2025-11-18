package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReservationDto {

    //    @ManyToOne
    //    @JoinColumn(name = "book_id")
    //    private Book book;
    private String book;
    //    @ManyToOne
    //    @JoinColumn(name = "member_id")
    //    private LibraryMember member;
    private String library_member;
    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;


}


