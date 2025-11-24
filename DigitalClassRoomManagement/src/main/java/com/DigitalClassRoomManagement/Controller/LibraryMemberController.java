package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.LibraryMember;
import com.DigitalClassRoomManagement.Exception.LibraryMemberNotFoundException;
import com.DigitalClassRoomManagement.Service.LibraryMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(
        name = "Library Member Management APIs",
        description = "Operations to create, update, delete and fetch Library Members"
)
@RestController
@RequestMapping("/api/Librarymembers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LibraryMemberController {

    private static final Logger logger = LoggerFactory.getLogger(LibraryMemberController.class);

    private final LibraryMemberService memberService;

    // Create Member
    @Operation(summary = "Create Library Member")
    @PostMapping("/saveLibraryMember")
    public ResponseEntity<?> createMember(@RequestBody LibraryMember member) {
        try {
            logger.info("Creating member: {}", member);
            LibraryMember saved = memberService.createMember(member);
            logger.info("Member created with ID: {}", saved.getMemberId());
            return ResponseEntity.created(URI.create("/api/members/getById/" + saved.getMemberId()))
                    .body(saved);
        } catch (Exception e) {
            logger.error("Error creating member: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get Member by ID
    @Operation(summary = "Get Member by ID")
    @GetMapping("/getByIdLibraryMember/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Long id) {
        try {
            logger.info("Fetching member by ID: {}", id);
            LibraryMember member = memberService.getMemberById(id);
            return ResponseEntity.ok(member);
        } catch (LibraryMemberNotFoundException e) {
            logger.warn("Member not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error fetching member: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get All Members
    @Operation(summary = "Get All LibraryMembers")
    @GetMapping("/getAllLibraryMembers")
    public ResponseEntity<?> getAllMembers() {
        try {
            logger.info("Fetching all members");
            List<LibraryMember> members = memberService.getAllMembers();
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            logger.error("Error fetching members: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update Member
    @Operation(summary = "Update LibraryMember by ID")
    @PutMapping("/updateLibraryMemberById/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody LibraryMember member) {
        try {
            logger.info("Updating member ID: {}", id);
            LibraryMember updated = memberService.updateMember(id, member);
            return ResponseEntity.ok(updated);
        } catch (LibraryMemberNotFoundException e) {
            logger.warn("Member not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating member: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete Member
    @Operation(summary = "Delete LibraryMember by ID")
    @DeleteMapping("/deleteLibraryMemberyById/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        try {
            logger.info("Deleting member ID: {}", id);
            memberService.deleteMember(id);
            return ResponseEntity.ok("Member deleted successfully with ID: " + id);
        } catch (LibraryMemberNotFoundException e) {
            logger.warn("Member not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error deleting member: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
