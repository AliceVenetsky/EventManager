package com.example.eventmanager.location.api;

import com.example.eventmanager.location.domain.Location;
import com.example.eventmanager.location.domain.LocationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationDtoConverter dtoConverter;
    private final static Logger log = LoggerFactory.getLogger(LocationController.class);

    public LocationController(
            LocationService locationService,
            LocationDtoConverter dtoConverter
    ) {
        this.locationService = locationService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationDto) {
        log.info("Get request to create location from dto: {}", locationDto);
        var newLocation = locationService.createLocation(dtoConverter.toDomain(locationDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(newLocation));
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        log.info("Request for all location list");
        List<Location> list = locationService.getAllLocations();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list.stream().map(dtoConverter::toDto).toList());
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(
            @PathVariable("locationId") Long id) {
        log.info("Get request to find location by id: {}", id);
        var foundLocation = locationService.getLocationById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(foundLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteLocation(
            @PathVariable("locationId") Long id) {
        log.info("Get request to delete location with id: {}", id);

        var deletedLocation = locationService.deleteLocation(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(dtoConverter.toDto(deletedLocation));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("locationId") Long id,
            @RequestBody @Valid LocationDto locationDto) {
        log.info("Get request to update location with id:{}", id);
        var updateLocation = locationService.updateLocation(id, dtoConverter.toDomain(locationDto));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(updateLocation));

    }
}
