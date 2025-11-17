package com.DigitalClassRoomManagement.Controller;


import com.DigitalClassRoomManagement.Dto.DigitalBookDTO;
import com.DigitalClassRoomManagement.Entity.DigitalBook;
import com.DigitalClassRoomManagement.Exception.BookNotFoundException;
import com.DigitalClassRoomManagement.Service.DigitalBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/books")
public class DigitalBookController {

    @Autowired
    private DigitalBookService bookService;

    @PostMapping(value = "/saveBook", consumes = "multipart/form-data")
    public ResponseEntity<?> saveBook(@Valid @ModelAttribute DigitalBookDTO bookDTO) {
        try {
            DigitalBook book = new DigitalBook();
            book.setTitle(bookDTO.getTitle());
            book.setGrade(bookDTO.getGrade());
            book.setSubject(bookDTO.getSubject());
            book.setUploadDate(LocalDate.now());

            MultipartFile file = bookDTO.getFile();
            if (file != null && !file.isEmpty()) {
                book.setFileUrl(file.getBytes());
            }

            DigitalBook saved = bookService.saveBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save book: " + e.getMessage());
        }
    }


    @GetMapping("/getAllBooks")
    public ResponseEntity<?> getAllBooks() {
        try {
            List<DigitalBook> books = bookService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch books.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            DigitalBook book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve book.");
        }
    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody DigitalBook book) {
//        try {
//            DigitalBook updated = bookService.updateBook(id, book);
//            return ResponseEntity.ok(updated);
//        } catch (BookNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to update book.");
//        }
//    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateBook(
            @PathVariable Long id,
            @ModelAttribute DigitalBookDTO bookDTO) {
        try {
            DigitalBook updatedBook = bookService.updateBook(id, bookDTO);
            return ResponseEntity.ok(updatedBook);
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update book: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            boolean deleted = bookService.deleteBook(id);
            if (deleted)
                return ResponseEntity.ok("Book deleted successfully.");
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book not found with ID: " + id);

        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete book.");
        }
    }
}
