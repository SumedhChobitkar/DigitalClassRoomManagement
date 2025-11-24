package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.LibraryMember;
import com.DigitalClassRoomManagement.Exception.LibraryMemberNotFoundException;
import com.DigitalClassRoomManagement.Repository.LibraryMemberRepository;
import com.DigitalClassRoomManagement.Service.LibraryMemberService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LibraryMemberServiceImpl implements LibraryMemberService {

    private static final Logger logger = LoggerFactory.getLogger(LibraryMemberServiceImpl.class);

    private final LibraryMemberRepository repository;

    public LibraryMemberServiceImpl(LibraryMemberRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public LibraryMember createMember(LibraryMember member) {
        try {
            logger.info("Creating LibraryMember: {}", member);

            validateMember(member);

            LibraryMember saved = repository.save(member);
            logger.info("LibraryMember created with ID: {}", saved.getMemberId());
            return saved;
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            logger.error("Failed to create LibraryMember: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while creating LibraryMember: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create LibraryMember", e);
        }
    }

    @Override
    public LibraryMember getMemberById(Long memberId) {
        try {
            logger.info("Fetching LibraryMember by ID: {}", memberId);

            return repository.findById(memberId)
                    .orElseThrow(() -> {
                        logger.warn("LibraryMember not found ID: {}", memberId);
                        return new LibraryMemberNotFoundException("LibraryMember not found with ID: " + memberId);
                    });
        } catch (LibraryMemberNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while fetching LibraryMember: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch LibraryMember", e);
        }
    }

    @Override
    public List<LibraryMember> getAllMembers() {
        try {
            logger.info("Fetching all LibraryMembers");
            return repository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while fetching all LibraryMembers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch LibraryMembers", e);
        }
    }

    @Override
    @Transactional
    public LibraryMember updateMember(Long memberId, LibraryMember member) {
        try {
            logger.info("Updating LibraryMember ID: {}", memberId);

            LibraryMember existing = getMemberById(memberId);

            if (member.getUserId() != null) existing.setUserId(member.getUserId());
            if (member.getMembershipType() != null) existing.setMembershipType(member.getMembershipType());
            if (member.getJoinDate() != null) existing.setJoinDate(member.getJoinDate());
            if (member.getStatus() != null) existing.setStatus(member.getStatus());
            if (member.getTotalIssuedBooks() != null) existing.setTotalIssuedBooks(member.getTotalIssuedBooks());

            validateMember(existing);

            LibraryMember updated = repository.save(existing);
            logger.info("LibraryMember updated successfully with ID: {}", updated.getMemberId());
            return updated;
        } catch (LibraryMemberNotFoundException e) {
            throw e;
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            logger.error("Failed to update LibraryMember ID {}: {}", memberId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while updating LibraryMember ID {}: {}", memberId, e.getMessage(), e);
            throw new RuntimeException("Failed to update LibraryMember", e);
        }
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        try {
            logger.info("Deleting LibraryMember ID: {}", memberId);

            LibraryMember existing = getMemberById(memberId);
            repository.delete(existing);

            logger.info("LibraryMember deleted successfully with ID: {}", memberId);
        } catch (LibraryMemberNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while deleting LibraryMember ID {}: {}", memberId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete LibraryMember", e);
        }
    }

    // Validation using ValidationClass
    private void validateMember(LibraryMember member) {
        ValidationClass.validateMember(
                member.getUserId(),
                member.getMembershipType() != null ? member.getMembershipType().name() : null,
                member.getStatus() != null ? member.getStatus().name() : null,
                member.getTotalIssuedBooks()
        );
    }
}
