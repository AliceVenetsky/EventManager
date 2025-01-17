package com.example.eventmanager.location.db;

import com.example.eventmanager.location.domain.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationEntityConverter {

    public Location toDomain(LocationEntity entity) {
        return new Location(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCapacity(),
                entity.getDescription()
        );
    }

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }
}
