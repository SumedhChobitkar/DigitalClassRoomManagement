package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.ContactUsDto;
import com.DigitalClassRoomManagement.Entity.ContactUs;
import com.DigitalClassRoomManagement.Exception.ContactNotFoundException;
import com.DigitalClassRoomManagement.Repository.ContactUsRepository;
import com.DigitalClassRoomManagement.Service.ContactUsService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepository repository;

    private ContactUsDto convertToDto(ContactUs entity) {
        return ContactUsDto.builder()
                .id(entity.getId())
                .schoolName(entity.getSchoolName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .build();
    }

    private ContactUs convertToEntity(ContactUsDto dto) {
        return ContactUs.builder()
                .id(dto.getId())
                .schoolName(dto.getSchoolName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

    @Override
    public ContactUsDto create(ContactUsDto dto) {
        log.info("Creating new contact: {}", dto.getEmail());
        try {
            ValidationClass.validateContactUs(dto.getSchoolName(), dto.getEmail(), dto.getPhone(), dto.getAddress());
            ContactUs saved = repository.save(convertToEntity(dto));
            log.info("Contact created successfully: ID={}", saved.getId());
            return convertToDto(saved);

        } catch (DataAccessException ex) {
            log.error("Database error while creating contact. Error: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while creating contact. Error: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ContactUsDto getById(Long id) {
        log.info("Fetching contact by ID={}", id);
        try {
            ContactUs entity = repository.findById(id)
                    .orElseThrow(() -> new ContactNotFoundException("Contact not found: " + id));

            return convertToDto(entity);

        } catch (ContactNotFoundException ex) {
            log.warn("Contact not found: {}", id);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error occurred while fetching contact ID={} Error={}", id, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<ContactUsDto> getAll() {
        log.info("Fetching all contacts...");
        try {
            return repository.findAll().stream().map(this::convertToDto).toList();

        } catch (DataAccessException ex) {
            log.error("Database error while fetching all contacts. Error: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while fetching contact list. Error: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ContactUsDto update(ContactUsDto dto) {
        log.info("Updating contact: ID={}", dto.getId());
        try {
            ValidationClass.validateContactUs(dto.getSchoolName(), dto.getEmail(), dto.getPhone(), dto.getAddress());

            ContactUs existing = repository.findById(dto.getId())
                    .orElseThrow(() -> new ContactNotFoundException("Contact not found: " + dto.getId()));

            existing.setSchoolName(dto.getSchoolName());
            existing.setEmail(dto.getEmail());
            existing.setPhone(dto.getPhone());
            existing.setAddress(dto.getAddress());

            ContactUs updated = repository.save(existing);
            log.info("Contact updated successfully: ID={}", dto.getId());

            return convertToDto(updated);

        } catch (ContactNotFoundException ex) {
            log.warn("Contact update failed. Not found: {}", dto.getId());
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Database error while updating contact ID={}. Error={}", dto.getId(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while updating contact ID={} Error={}", dto.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting contact: ID={}", id);
        try {
            ContactUs entity = repository.findById(id)
                    .orElseThrow(() -> new ContactNotFoundException("Contact not found: " + id));

            repository.delete(entity);
            log.info("Contact deleted successfully: ID={}", id);

        } catch (ContactNotFoundException ex) {
            log.warn("Delete failed. Contact not found: {}", id);
            throw ex;
        } catch (DataAccessException ex) {
            log.error("Database error while deleting contact ID={}. Error={}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while deleting contact ID={} Error={}", id, ex.getMessage());
            throw ex;
        }
    }
}
