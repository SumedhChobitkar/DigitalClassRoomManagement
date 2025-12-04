package com.DigitalClassRoomManagement.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactUsDto {
    private Long id;
    private String schoolName;
    private String email;
    private String phone;
    private String address;
}
