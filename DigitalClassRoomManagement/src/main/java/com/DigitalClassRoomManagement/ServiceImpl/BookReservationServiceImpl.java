package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.BookReservation;
import com.DigitalClassRoomManagement.Enum.ReservationStatus;
import com.DigitalClassRoomManagement.Exception.BookNotFoundException;
import com.DigitalClassRoomManagement.Repository.BookReservationRepository;
import com.DigitalClassRoomManagement.Service.BookReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class BookReservationServiceImpl implements BookReservationService {
    private final static Logger log = LoggerFactory.getLogger(BookReservationServiceImpl.class);

    @Autowired
    private BookReservationRepository brepo;

    @Override
    public BookReservation saveReservation(BookReservation reservation) {
        log.info("Adding new book reservation");
        reservation.setBook(reservation.getBook());
        reservation.setLibrary_member(reservation.getLibrary_member());
        reservation.setReservationDate(reservation.getReservationDate());
        reservation.setStatus(reservation.getStatus());
        return brepo.save(reservation);
    }

    @Override
    public BookReservation getReservationById(Long id) {
        log.info("Fetching book reservation with id: {}", id);
        return brepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id :{}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });
    }

    @Override
    public List<BookReservation> getAllReservations() {
        log.info("Fetching list of all book reservations");
        return brepo.findAll();
    }

    @Override
    public BookReservation updateReservationStatus(Long id, ReservationStatus status) {
        try {
            BookReservation saved = brepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Book not found with id :{}", id);
                        return new BookNotFoundException("Book not found with id: " + id);
                    });
            saved.setStatus(status);
            log.info("Book reservation status is updated");
            return brepo.save(saved);
        } catch (BookNotFoundException bex) {
            log.error("Book not found");
            throw bex;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean deleteReservation(Long id) {
        try {
            BookReservation saved = brepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Book not found with id :{}", id);
                        return new BookNotFoundException("Book not found with id: " + id);
                    });
            brepo.delete(saved);
            log.info("Book reservation stats if updated");
            return true;
        } catch (BookNotFoundException bex) {
            log.error("Book not found");
            throw bex;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
    }
}


