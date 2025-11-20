package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.DigitalBookDTO;
import com.DigitalClassRoomManagement.Entity.DigitalBook;

import java.util.List;

public interface DigitalBookService {

    DigitalBook saveBook(DigitalBook book);
    List<DigitalBook> getAllBooks();
    DigitalBook getBookById(Long id);
    DigitalBook updateBook(Long id, DigitalBookDTO bookDTO);
    boolean deleteBook(Long id);
}
