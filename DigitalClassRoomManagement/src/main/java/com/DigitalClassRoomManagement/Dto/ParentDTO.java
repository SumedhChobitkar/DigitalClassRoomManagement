package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.Relationship;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentDTO {

    private Long parentId;

    // User relationship (just storing the user ID)
    private Long userId;

    // Name validation
    @NotBlank(message = "Name is required.")
    @Pattern(regexp = "^[A-Za-z ]{2,50}$", message = "Name must contain only letters and spaces (2–50 characters).")
    private String name;

    //  Email validation
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    // Phone validation (optional but must be 10 digits if present)
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits.")
    private String phone;

    private String address;

    // Relationship — Enum (FATHER, MOTHER, GUARDIAN)
    private Relationship relationship;
}
