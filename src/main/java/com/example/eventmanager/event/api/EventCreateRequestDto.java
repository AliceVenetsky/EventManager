package com.example.eventmanager.event.api;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventCreateRequestDto(
        @NotBlank(message = "Name can't be empty")
        String name,
        @Positive(message = "Maximum places must be greater then null")
        Integer maxPlaces,
        @Future(message = "Date must be in future")
        LocalDateTime date,
        @PositiveOrZero(message = "Cost must be non-negative")
        Integer cost,
        @Min(value = 30, message = "Duration must be more than 30 minutes")
        Integer duration,
        @NotNull(message = "Location ID must not be null")
        Long locationId
) {
}
