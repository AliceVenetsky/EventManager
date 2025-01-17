package com.example.eventmanager.event.api;

import com.example.eventmanager.event.domain.Event;
import org.springframework.stereotype.Component;

@Component
public class EventDtoConverter {
    public EventDto toDto(Event event) {
        return new EventDto(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.eventRegistrationList().size(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }
}
