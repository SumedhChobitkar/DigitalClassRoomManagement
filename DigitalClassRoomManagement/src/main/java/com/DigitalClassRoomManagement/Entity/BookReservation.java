package com.DigitalClassRoomManagement.Entity;



import com.DigitalClassRoomManagement.Enum.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;



import java.time.LocalDateTime;
@Entity
@Table(name = "book_reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReservation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long reservationId;

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

