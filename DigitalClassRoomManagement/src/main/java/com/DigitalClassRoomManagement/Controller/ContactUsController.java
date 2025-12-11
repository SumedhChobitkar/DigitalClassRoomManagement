package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ContactUsDto;
import com.DigitalClassRoomManagement.Exception.ContactNotFoundException;
import com.DigitalClassRoomManagement.Service.ContactUsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@Tag(name = "Contact Us APIs", description = "Operations to create, read, update, and delete Contact Us messages")
@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContactUsController {

    private static final Logger logger = LoggerFactory.getLogger(ContactUsController.class);
    private final ContactUsService contactService;

    @PostMapping("/saveContact")
    @Operation(summary = "Create Contact Message")
    public ResponseEntity<?> createContact(@Valid @RequestBody ContactUsDto dto) {
        try {
            ContactUsDto saved = contactService.create(dto);
            logger.info("Created contact message with ID: {}", saved.getId());
            return ResponseEntity.created(URI.create("/api/contact/" + saved.getId()))
                    .body(saved);
        } catch (Exception e) {
            logger.error("Error creating contact message", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("getContactById/{id}")
    @Operation(summary = "Get Contact by ID")
    public ResponseEntity<?> getContactById(@PathVariable Long id) {
        try {
            ContactUsDto contact = contactService.getById(id);
            logger.info("Fetched contact message with ID: {}", id);
            return ResponseEntity.ok(contact);
        } catch (ContactNotFoundException e) {
            logger.warn("Contact not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getAllContacts")
    @Operation(summary = "Get All Contacts")
    public ResponseEntity<?> getAllContacts() {
        try {
            List<ContactUsDto> contacts = contactService.getAll();
            logger.info("Fetched all contact messages, total: {}", contacts.size());
            return ResponseEntity.ok(contacts);

        } catch (Exception e) {
            logger.error("Error fetching all contact messages", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while fetching contacts: " + e.getMessage());
        }
    }



    @PutMapping("updateContactById/{id}")
    @Operation(summary = "Update Contact by ID")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @Valid @RequestBody ContactUsDto dto) {
        try {
            dto.setId(id);
            ContactUsDto updated = contactService.update(dto);
            logger.info("Updated contact message with ID: {}", id);
            return ResponseEntity.ok(updated);
        } catch (ContactNotFoundException e) {
            logger.warn("Contact not found for update: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("deleteContactById/{id}")
    @Operation(summary = "Delete Contact by ID")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        try {
            contactService.delete(id);
            logger.info("Deleted contact message with ID: {}", id);
            return ResponseEntity.ok("Contact message deleted successfully with ID: " + id);
        } catch (ContactNotFoundException e) {
            logger.warn("Contact not found for deletion: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
