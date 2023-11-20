package com.clockedIn.shiftservice.services.collection;

import com.clockedIn.shiftservice.model.Location;
import com.clockedIn.shiftservice.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class LocationCollectionService implements LocationRepository {

    private final Map<UUID, Location> locationHashMap;

    public LocationCollectionService(Map<UUID, Location> locationHashMap) {
        this.locationHashMap = locationHashMap;
    }

    @Override
    public Location save(Location location) {
        locationHashMap.put(location.getLocationId(), location);
        return location;
    }

    @Override
    public Optional<Location> findById(UUID locationId) {
        return Optional.of(locationHashMap.get(locationId));
    }

    @Override
    public Iterable<Location> findAll() {
        return locationHashMap.values();
    }

    @Override
    public void delete(UUID locationId) {
        locationHashMap.remove(locationId);
    }
}
