package com.example.eventmanager.event.api;

import com.example.eventmanager.event.domain.EventStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventDto(
        @NotNull(message = "ID can't be null")
        Long id,
        @NotBlank(message = "Name can't be empty")
        String name,
        @NotNull(message = "Owner ID can't be null")
        Long ownerId,
        @Positive(message = "Maximum places must be greater then null")
        Integer maxPlaces,
        @PositiveOrZero(message = "Occupied places must be non-negative")
        Integer occupiedPlaces,
        @Future(message = "Date must be in future")
        LocalDateTime date,
        @PositiveOrZero(message = "Cost must be non-negative")
        Integer cost,
        @Min(value = 30, message = "Duration must be more than 30 minutes")
        Integer duration,
        @NotNull(message = "Location ID must not be null")
        Long locationId,
        @NotNull(message = "Status can't be null")
        EventStatus status
) {
}
