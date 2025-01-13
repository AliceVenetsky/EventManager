package com.example.eventmanager.event.db;

import com.example.eventmanager.event.domain.EventStatus;
import com.example.eventmanager.event.domain.Event;
import com.example.eventmanager.event.domain.EventRegistration;
import org.springframework.stereotype.Component;

@Component
public class EventEntityConverter {
    public Event toDomain(EventEntity entity) {
        return new Event(
                entity.getId(),
                entity.getName(),
                entity.getOwnerId(),
                entity.getMax_places(),
                entity.getRegistrationList().stream()
                        .map(it -> new EventRegistration(
                                it.getId(),
                                it.getUserId(),
                                entity.getId())
                        ).toList(),
                entity.getDate(),
                entity.getCost(),
                entity.getDuration(),
                entity.getLocationId(),
                EventStatus.valueOf(entity.getStatus())
        );
    }
}
