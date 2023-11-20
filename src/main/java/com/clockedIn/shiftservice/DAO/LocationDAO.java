package com.clockedIn.shiftservice.DAO;

import com.clockedIn.shiftservice.model.Location;
import com.clockedIn.shiftservice.model.Shift;
import com.clockedIn.shiftservice.util.objectmappers.LocationMapper;
import util.ConnectionCreationAndUsage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class LocationDAO {

    public Optional<Location> getLocationById(UUID locationId) {
        String getLocationQuery = "SELECT * FROM locations + WHERE location_id = ?";
        Function<ResultSet, Location> mapper = LocationMapper.map();

        Location location = ConnectionCreationAndUsage.findOne(getLocationQuery, mapper, locationId);

        return Optional.ofNullable(location);
    }

    public Iterable<Location> getAllLocations() {
        String getAllLocationsQuery = "SELECT * locations";
        Function<ResultSet, Location> mapper = LocationMapper.map();

        List<Location> locations = ConnectionCreationAndUsage.findMany(getAllLocationsQuery, mapper);

        return locations;
    }

    public void createLocation(Location location) {
        String insertLocationQuery = "INSERT INTO locations VALUES (?,?,?)";
        ConnectionCreationAndUsage.execute(insertLocationQuery, location.getLocationId(),location.getRoomName(),
                location.getVirtualLink());
    }

    public void updateLocation(Location location) {
        String updateLocationQuery = "UPDATE locations SET room_name=?, virtual_link=? WHERE location_id=?";
        ConnectionCreationAndUsage.execute(updateLocationQuery, location.getRoomName(), location.getVirtualLink(),
                location.getLocationId());
    }

    public void deleteLocation(UUID locationId) {
        String deleteLocationQuery = "DELETE FROM locations WHERE location_id=?";
        ConnectionCreationAndUsage.execute(deleteLocationQuery, locationId);
    }
}
