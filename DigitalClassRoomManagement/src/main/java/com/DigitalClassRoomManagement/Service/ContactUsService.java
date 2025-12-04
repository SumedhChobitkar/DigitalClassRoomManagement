package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.ContactUsDto;
import java.util.List;

public interface ContactUsService {
    ContactUsDto create(ContactUsDto dto);
    ContactUsDto getById(Long id);
    List<ContactUsDto> getAll();
    ContactUsDto update(ContactUsDto dto);
    void delete(Long id);
}
