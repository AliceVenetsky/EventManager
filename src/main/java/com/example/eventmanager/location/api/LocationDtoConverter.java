package com.example.eventmanager.location.api;

import com.example.eventmanager.location.domain.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationDtoConverter {

    public Location toDomain(LocationDto dto) {
        return new Location(
                dto.id(),
                dto.name(),
                dto.address(),
                dto.capacity(),
                dto.description()
        );
    }

    public LocationDto toDto(Location location) {
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }
}
