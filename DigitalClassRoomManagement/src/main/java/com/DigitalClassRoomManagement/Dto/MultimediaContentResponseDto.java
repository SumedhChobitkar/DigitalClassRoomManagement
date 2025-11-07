package com.DigitalClassRoomManagement.Dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultimediaContentResponseDto {
    private Long contentId;
    private String title;
    private String type;
    private String url;         // for YOUTUBE or convenience
    private String contentType;
    private Long fileSize;
    private String fileName;
    private String createdAt;
    private String updatedAt;
}
