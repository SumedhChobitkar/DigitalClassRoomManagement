package com.DigitalClassRoomManagement.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;


@Slf4j

public class LocationWebSocketHandler extends TextWebSocketHandler {

    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connected: {}", session.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("WebSocket disconnected: {}", session.getId());
        sessions.remove(session);
    }

    // ----- SEND LOCATION UPDATE -----
    public void sendLocationUpdate(double latitude, double longitude) {
        try {
            String googleMapsLink =
                    "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;

            Map<String, Object> locationData = new HashMap<>();
            locationData.put("latitude", latitude);
            locationData.put("longitude", longitude);
            locationData.put("mapLink", googleMapsLink);


            String jsonMessage = objectMapper.writeValueAsString(locationData);

            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(jsonMessage));
                }
            }

            log.info("Broadcasted location update → {}", jsonMessage);

        } catch (Exception e) {
            log.error("Error sending location update: {}", e.getMessage(), e);
        }
    }

    // ----- BROADCAST ANY OBJECT -----
    public void broadcastMessage(Object message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);

            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(jsonMessage));
                }
            }

            log.info("Broadcasted message → {}", jsonMessage);

        } catch (Exception e) {
            log.error("Error broadcasting message: {}", e.getMessage(), e);
        }
    }

}
