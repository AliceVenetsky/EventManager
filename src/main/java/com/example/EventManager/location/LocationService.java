package com.example.EventManager.location;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationEntityConverter entityConverter;

    public LocationService(
            LocationRepository locationRepository,
            LocationEntityConverter entityConverter
    ) {
        this.locationRepository = locationRepository;
        this.entityConverter = entityConverter;
    }


    public Location createLocation(Location location) {
        if(location.id() != null)
            throw new IllegalArgumentException(
                    "Can't create new location with id %s, id must be null".formatted(location.id())
            );

        var newLocation = locationRepository.save(entityConverter.toEntity(location));
        return entityConverter.toDomain(newLocation);
    }

    public List<Location> getAllLocations() {
        return locationRepository
                .findAll()
                .stream()
                .map(entityConverter::toDomain)
                .toList();
    }

    public Location getLocationById(Long id) {
        var foundEntity = locationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Entity with id = %s not found".formatted(id))
        );
        return entityConverter.toDomain(foundEntity);
    }

    public Location deleteLocation(Long id) {
        var locationToDelete = getLocationById(id);
        locationRepository.deleteById(id);
        return locationToDelete;
    }


    public Location updateLocation(Long id, Location location) {
        if(location.id() != null)
            throw new IllegalArgumentException("Id must be null");

        var updateLocation = entityConverter.toEntity(getLocationById(id));
        updateLocation.setName(location.name());
        updateLocation.setAddress(location.address());
        updateLocation.setDescription(location.description());
        updateLocation.setCapacity(location.capacity());

        var updateEntity = locationRepository.save(updateLocation);
        return entityConverter.toDomain(updateEntity);
    }
}
