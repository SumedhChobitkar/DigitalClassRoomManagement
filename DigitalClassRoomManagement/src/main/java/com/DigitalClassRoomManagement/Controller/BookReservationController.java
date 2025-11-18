package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.BookReservation;
import com.DigitalClassRoomManagement.Enum.ReservationStatus;
import com.DigitalClassRoomManagement.Service.BookReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/book_reservation")
public class BookReservationController {
    private final static Logger log= LoggerFactory.getLogger(BookReservationController.class);

    @Autowired
    private BookReservationService bservice;

    @PostMapping("/book")
    public ResponseEntity<BookReservation> reserve(@RequestBody BookReservation reservation){
        log.info("Receive a new request to perform book resrvation");
        return ResponseEntity.ok(bservice.saveReservation(reservation));
    }

    @GetMapping("/getBook/{id}")
    public ResponseEntity<BookReservation> fetchReservationById(@PathVariable Long id){
        log.info("Receive a request to fetch book reservation with id:{}", id);
        return ResponseEntity.ok(bservice.getReservationById(id));
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<BookReservation>> getAllBookReservations(){
        log.info("Receive a request to fetch all books reservations");
        return ResponseEntity.ok(bservice.getAllReservations());
    }

    @PutMapping("/updateBookStatus/{id}")
    public ResponseEntity<BookReservation> updateStatus(@PathVariable Long id,@RequestBody ReservationStatus status){
        log.info("Received a new request to update reservation status with id:{}", id);
        return ResponseEntity.ok(bservice.updateReservationStatus(id,status));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id){
        log.info("Received a request to delete bokk reservation with id :{}", id);
          boolean isDeleted= bservice.deleteReservation(id);
          if(isDeleted){
              log.info("Boook reservation deleted succsessfully");
              return ResponseEntity.ok("Book Reservation deleted ");
          }
          else{
              log.info("Boook reservation is not deleted");
              return ResponseEntity.ok("Delete book reservation failed");
          }

    }
}
