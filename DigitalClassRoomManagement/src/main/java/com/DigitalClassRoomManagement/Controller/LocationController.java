package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.Location;
import com.DigitalClassRoomManagement.Service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Slf4j

public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;

    @PostMapping("/save/{schoolId}")
    public ResponseEntity<Map<String, String>> saveLocation(@PathVariable Long schoolId, @RequestBody Location location) {
        Map<String, String> response = new HashMap<>();
        try {
            locationService.saveLocation(schoolId, location);
            response.put("message", "Location saved successfully for schoolId: " + schoolId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error saving location: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long locationId) {

        Location location = locationService.getLocationById(locationId);

        return ResponseEntity.ok(location);
    }

    @GetMapping("/name/{schoolName}")
    public ResponseEntity<?> getLocationsByschoolName(@PathVariable String schoolName) {

        try {
            List<Location> locations = locationService.getLocationsByschoolName(schoolName);
            return ResponseEntity.ok(locations);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{locationId}")
    public ResponseEntity<Location> updateLocation(
            @PathVariable Long locationId,
            @RequestBody Location location) {

        Location updated = locationService.updateLocation(locationId, location);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllLocations() {

        try {
            logger.info("Received request to fetch all locations.");
            List<Location> locations = locationService.getAllLocations();
            // If empty
            if (locations.isEmpty()) {
                logger.warn("No locations found.");
                return ResponseEntity.status(404).body("No locations available.");
            }
            logger.info("Returning {} locations.", locations.size());
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            logger.error("Exception occurred: {}", e.getMessage(), e);

            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Map<String, String>> deleteLocation(@PathVariable Long locationId) {

        Map<String, String> response = new HashMap<>();

        try {
            locationService.deleteLocation(locationId);

            response.put("message", "Location with ID " + locationId + " deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("message", "Error deleting location: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{locationId}/map-link")
    public ResponseEntity<String> getGoogleMapsLink(@PathVariable Long locationId) {

        Location location = locationService.getLocationById(locationId);

        String link = "https://www.google.com/maps/search/?api=1&query="
                + location.getLatitude() + "," + location.getLongitude();

        return ResponseEntity.ok(link);
    }


}
