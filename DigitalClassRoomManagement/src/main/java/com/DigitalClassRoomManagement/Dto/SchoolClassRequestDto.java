package com.DigitalClassRoomManagement.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class SchoolClassRequestDto {

    @NotBlank(message = "Class name is required")
    @Size(min = 2, max = 50, message = "Class name must be between 2 and 50 characters")
    public String className;

    @NotBlank(message = "Description is required")
    @Size(max = 200, message = "Description must not exceed 200 characters")
    public String description;

    // optional: list of teacher ids to assign when creating/updating class
    public List<Long> teacherIds;
}
