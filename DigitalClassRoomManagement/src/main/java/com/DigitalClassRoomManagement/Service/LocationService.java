package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.Location;

import java.util.List;
import java.util.Map;

public interface LocationService {

    Map <String ,String> saveLocation(Long schoolId, Location location);
    Location getLocationById(Long locationId);
    List<Location> getLocationsByschoolName(String schoolName);
    List<Location> getAllLocations();
    void deleteLocation(Long locationId);
    Location updateLocation(Long locationId, Location location);
}
