package com.DigitalClassRoomManagement.Dto;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DigitalBookDTO {

    //@NotBlank(message = "Title is required.")
    private String title;

   // @NotBlank(message = "Grade is required.")
    private String grade;

   // @NotBlank(message = "Subject is required.")
    private String subject;

   // @NotNull(message = "File must be uploaded.")
    private MultipartFile file;  // file is required for upload
}
