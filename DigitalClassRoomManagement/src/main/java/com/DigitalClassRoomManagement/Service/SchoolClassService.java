package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.SchoolClassRequestDto;
import com.DigitalClassRoomManagement.Dto.SchoolClassResponseDto;

import java.util.List;

public interface SchoolClassService {
    SchoolClassResponseDto create(SchoolClassRequestDto request);
    SchoolClassResponseDto update(Long id, SchoolClassRequestDto request);
    void delete(Long id);
    List<SchoolClassResponseDto> getAll();
    SchoolClassResponseDto getById(Long id);
}
