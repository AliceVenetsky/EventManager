package com.example.eventmanager.event.domain;

public record EventRegistration(
        Long id,
        Long userId,
        Long eventId
) {
}
