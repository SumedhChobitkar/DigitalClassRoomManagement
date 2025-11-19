package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.BookReservation;
import com.DigitalClassRoomManagement.Enum.ReservationStatus;
import com.DigitalClassRoomManagement.Service.BookReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/book_reservation")
@CrossOrigin("*")

public class BookReservationController {
    private final static Logger log= LoggerFactory.getLogger(BookReservationController.class);

    @Autowired
    private BookReservationService bservice;

    @PostMapping("/addBookReservation")
    public ResponseEntity<?> reserve(@RequestBody BookReservation reservation) {
        log.info("Received a new request to perform book reservation");

        try {
            BookReservation saved = bservice.saveReservation(reservation);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            log.error("Error while reserving book: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to reserve book: " + e.getMessage());
        }
    }

    @GetMapping("/getBookReservation/{id}")
    public ResponseEntity<?> fetchReservationById(@PathVariable Long id) {
        log.info("Received a request to fetch book reservation with id: {}", id);

        try {
            BookReservation reservation = bservice.getReservationById(id);
            return ResponseEntity.ok(reservation);

        } catch (NoSuchElementException e) {
            log.warn("Book reservation not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reservation not found with id: " + id);

        } catch (Exception e) {
            log.error("Error fetching reservation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch reservation: " + e.getMessage());
        }
    }

    @GetMapping("/getAllBooksReservations")
    public ResponseEntity<?> getAllBookReservations() {
        log.info("Received a request to fetch all book reservations");

        try {
            return ResponseEntity.ok(bservice.getAllReservations());

        } catch (Exception e) {
            log.error("Error fetching all reservations: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch reservations: " + e.getMessage());
        }
    }

    @PutMapping("/updateBookReservationStatus/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody ReservationStatus status) {
        log.info("Received a request to update reservation status for id: {}", id);

        try {
            BookReservation updated = bservice.updateReservationStatus(id, status);
            return ResponseEntity.ok(updated);

        } catch (NoSuchElementException e) {
            log.warn("Reservation not found for update, id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reservation not found with id: " + id);

        } catch (Exception e) {
            log.error("Error updating reservation status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update reservation: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteBookReservation/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        log.info("Received a request to delete book reservation with id: {}", id);

        try {
            boolean isDeleted = bservice.deleteReservation(id);

            if (isDeleted) {
                log.info("Book reservation deleted successfully");
                return ResponseEntity.ok("Book reservation deleted");
            } else {
                log.warn("Failed to delete reservation (not found), id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Delete failed: Reservation not found with id: " + id);
            }

        } catch (Exception e) {
            log.error("Error deleting reservation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete reservation: " + e.getMessage());
        }
    }
}
