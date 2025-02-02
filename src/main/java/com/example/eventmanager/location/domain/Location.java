package com.example.eventmanager.location.domain;

public record Location(
        Long id,
        String name,
        String address,
        Integer capacity,
        String description
) {
}
