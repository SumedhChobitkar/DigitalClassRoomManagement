package com.DigitalClassRoomManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data   //  getJoinUrl(), getEventId()
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {

    private String eventId;
    private String joinUrl;
}
