package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.BookReservation;
import com.DigitalClassRoomManagement.Enum.ReservationStatus;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BookReservationService {

    BookReservation saveReservation(BookReservation reservation);

    BookReservation getReservationById(Long id);

    List<BookReservation> getAllReservations();

    BookReservation updateReservationStatus(Long id, ReservationStatus status);

    boolean deleteReservation(Long id);

}

