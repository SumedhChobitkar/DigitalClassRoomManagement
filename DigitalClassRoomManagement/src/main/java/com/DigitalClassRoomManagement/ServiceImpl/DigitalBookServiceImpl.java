package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.DigitalBookDTO;
import com.DigitalClassRoomManagement.Entity.DigitalBook;
import com.DigitalClassRoomManagement.Exception.BookNotFoundException;
import com.DigitalClassRoomManagement.Repository.DigitalBookRepository;
import com.DigitalClassRoomManagement.Service.DigitalBookService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DigitalBookServiceImpl implements DigitalBookService {

    private static final Logger logger = LoggerFactory.getLogger(DigitalBookServiceImpl.class);

    @Autowired
    private DigitalBookRepository bookRepository;

    @Override
    public DigitalBook saveBook(DigitalBook book) {
        try {
            logger.info("Attempting to save new digital book: {}", book.getTitle());
            validateBook(book);

            DigitalBook savedBook = bookRepository.save(book);
            logger.info("Book saved successfully with ID: {}", savedBook.getBookId());
            return savedBook;

        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed for book: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error while saving book: {}", e.getMessage());
            throw new RuntimeException("Unable to save the digital book. Please try again later.");
        }
    }

    @Override
    public List<DigitalBook> getAllBooks() {
        try {
            logger.info("Fetching all digital books...");
            List<DigitalBook> books = bookRepository.findAll();

            if (books.isEmpty()) {
                logger.warn("No books found in database.");
                throw new BookNotFoundException("No books available in the system.");
            }

            logger.info("Fetched {} books successfully.", books.size());
            return books;

        } catch (BookNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error while fetching all books: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch books from the database.");
        }
    }

    @Override
    public DigitalBook getBookById(Long id) {
        try {
            logger.info("Fetching digital book with ID: {}", id);
            Optional<DigitalBook> book = bookRepository.findById(id);

            if (book.isEmpty()) {
                logger.warn("Book not found with ID: {}", id);
                throw new BookNotFoundException("Book not found with ID: " + id);
            }

            logger.info("Book found successfully with ID: {}", id);
            return book.get();

        } catch (BookNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error while fetching book by ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Unable to fetch the book details. Please try again.");
        }
    }

    @Override
    public DigitalBook updateBook(Long id, DigitalBookDTO bookDTO) {
        try {
            logger.info("Updating book with ID: {}", id);
            Optional<DigitalBook> existingBookOpt = bookRepository.findById(id);

            if (existingBookOpt.isEmpty()) {
                logger.warn("Cannot update. Book not found with ID: {}", id);
                throw new BookNotFoundException("Book not found with ID: " + id);
            }

            DigitalBook existingBook = existingBookOpt.get();

            validateBook(bookDTO);
            existingBook.setTitle(bookDTO.getTitle());
            existingBook.setGrade(bookDTO.getGrade());
            existingBook.setSubject(bookDTO.getSubject());

            MultipartFile file = bookDTO.getFile();
            if (file != null && !file.isEmpty()) {
                existingBook.setFileUrl(file.getBytes());
            }
            existingBook.setUploadDate(LocalDate.now());
            DigitalBook updatedBook = bookRepository.save(existingBook);

            logger.info("Book updated successfully with ID: {}", updatedBook.getBookId());
            return updatedBook;

        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed during update: {}", e.getMessage());
            throw e;
        } catch (BookNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error while updating book with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update book. Please try again.");
        }
    }

    @Override
    public boolean deleteBook(Long id) {
        try {
            logger.info("Deleting book with ID: {}", id);

            if (!bookRepository.existsById(id)) {
                logger.warn("Cannot delete. Book not found with ID: {}", id);
                throw new BookNotFoundException("Book not found with ID: " + id);
            }

            bookRepository.deleteById(id);
            logger.info("Book deleted successfully with ID: {}", id);
            return true;

        } catch (BookNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error while deleting book with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete book. Please try again.");
        }
    }

   // Validations
   private void validateBook(DigitalBook book) {

       if (book.getTitle() == null || !ValidationClass.TITLE_PATTERN.matcher(book.getTitle()).matches()) {
           throw new IllegalArgumentException("Invalid book title format. Must start with a capital letter.");
       }

       if (book.getGrade() != null && !ValidationClass.GRADE_PATTERN.matcher(book.getGrade()).matches()) {
           throw new IllegalArgumentException("Invalid grade format.");
       }

       if (book.getSubject() != null && !ValidationClass.SUBJECT_PATTERN.matcher(book.getSubject()).matches()) {
           throw new IllegalArgumentException("Invalid subject format.");
       }

       if (book.getFileUrl() == null || book.getFileUrl().length == 0) {
           throw new IllegalArgumentException("File cannot be empty.");
       }

       if (book.getFileUrl().length > ValidationClass.MAX_FILE_SIZE) {
           throw new IllegalArgumentException("File size exceeds the maximum allowed limit of 50MB.");
       }
   }

    private void validateBook(DigitalBookDTO bookDTO) {
        if (bookDTO.getTitle() == null || bookDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }
        if (bookDTO.getGrade() == null || bookDTO.getGrade().trim().isEmpty()) {
            throw new IllegalArgumentException("Book grade cannot be empty.");
        }
        if (bookDTO.getSubject() == null || bookDTO.getSubject().trim().isEmpty()) {
            throw new IllegalArgumentException("Book subject cannot be empty.");
        }
    }


}
