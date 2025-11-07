package com.DigitalClassRoomManagement.Dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MultimediaMetaUpdateDto {

    @NotBlank
    @Size(max = 255)
    private String title;
}
