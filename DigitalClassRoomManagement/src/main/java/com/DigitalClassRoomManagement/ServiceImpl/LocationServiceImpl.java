package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.Location;
import com.DigitalClassRoomManagement.Handler.LocationWebSocketHandler;
import com.DigitalClassRoomManagement.Repository.LocationRepository;
import com.DigitalClassRoomManagement.Service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    private LocationWebSocketHandler locationWebSocketHandler;

    // SAVE LOCATION
    @Override
    public Map<String, String> saveLocation(Long schoolId, Location location) {
        Map<String, String> response = new HashMap<>();
        try {
            log.info("Saving location for schoolId: {}", schoolId);

            location.setSchoolId(schoolId);
            locationRepository.save(location);
            locationWebSocketHandler.broadcastMessage(location);

            log.info("Location saved successfully for schoolId {}", schoolId);

            response.put("message", "Location saved successfully for schoolId " + schoolId);
            return response;

        } catch (Exception e) {
            log.error("Error saving location for schoolId {}: {}",schoolId, e.getMessage());
            response.put("error", "Could not save location: " + e.getMessage());
            throw new RuntimeException("Could not save location: " + e.getMessage());
        }

    }


    // GET LOCATION BY ID
    @Override
    public Location getLocationById(Long locationId) {
        try {
            log.info("Fetching location with ID: {}", locationId);

            return locationRepository.findById(locationId)
                    .orElseThrow(() -> new RuntimeException("Location not found for ID: " + locationId));

        } catch (Exception e) {
            log.error("Error while fetching location {}: {}", locationId, e.getMessage());
            throw new RuntimeException("Error fetching location: " + e.getMessage());
        }
    }

    // GET LOCATION BY NAME
    @Override
    public List<Location> getLocationsByschoolName(String schoolName) {
        try {
            log.info("Fetching locations with schoolName: {}", schoolName);

            return locationRepository.findBySchoolName(schoolName);

        } catch (Exception e) {
            log.error("Error fetching locations by name {}: {}", schoolName, e.getMessage());
            throw new RuntimeException("Error fetching locations by schoolName: " + e.getMessage());
        }
    }

    // GET ALL LOCATIONS
    @Override
    public List<Location> getAllLocations() {
        try {
            log.info("Fetching all locations...");

            return locationRepository.findAll();

        } catch (Exception e) {
            log.error("Error fetching all locations: {}", e.getMessage());
            throw new RuntimeException("Error fetching all locations: " + e.getMessage());
        }
    }

    @Override
    public Location updateLocation(Long locationId, Location updatedLocation) {
        try {
            log.info("Updating location with ID: {}", locationId);

            Location existingLocation = locationRepository.findById(locationId)
                    .orElseThrow(() -> new RuntimeException("Location not found with ID: " + locationId));

            existingLocation.setLatitude(updatedLocation.getLatitude());
            existingLocation.setLongitude(updatedLocation.getLongitude());
            existingLocation.setAddress(updatedLocation.getAddress());
            existingLocation.setCity(updatedLocation.getCity());
            existingLocation.setSchoolName(updatedLocation.getSchoolName());
            existingLocation.setSchoolId(updatedLocation.getSchoolId());

            Location savedLocation = locationRepository.save(existingLocation);
            locationWebSocketHandler.broadcastMessage(savedLocation);

            log.info("Location updated successfully with ID: {}", locationId);

            return savedLocation;

        } catch (Exception e) {
            log.error("Error updating location {}: {}", locationId, e.getMessage());
            throw new RuntimeException("Could not update location: " + e.getMessage());
        }
    }


    // DELETE LOCATION
    @Override
    public void deleteLocation(Long locationId) {
        try {
            log.info("Deleting location with ID: {}", locationId);

            if (!locationRepository.existsById(locationId)) {
                log.warn("Location not found: {}", locationId);
                throw new RuntimeException("Location not found with ID: " + locationId);
            }

            locationRepository.deleteById(locationId);
            log.info("Location {} deleted successfully.", locationId);

        } catch (RuntimeException re) {
            log.error("Runtime exception while deleting location {}: {}", locationId, re.getMessage());
            throw re;

        } catch (Exception e) {
            log.error("Error deleting location {}: {}", locationId, e.getMessage());
            throw new RuntimeException("Error deleting location: " + e.getMessage());
        }
    }
}
