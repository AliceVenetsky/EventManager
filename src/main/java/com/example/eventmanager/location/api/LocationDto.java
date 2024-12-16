package com.example.eventmanager.location.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LocationDto(
        Long id,
        @NotBlank(message = "Name can't be empty")
        String name,
        @NotBlank(message = "Address can't be empty")
        String address,
        @Min(value = 5, message = "Capacity must be more than 5")
        Integer capacity,
        String description
) {
}
