package com.example.eventmanager.event.domain;

import com.example.eventmanager.event.db.EventEntityConverter;
import com.example.eventmanager.event.db.EventRegistrationEntity;
import com.example.eventmanager.event.db.EventRegistrationRepository;
import com.example.eventmanager.event.db.EventRepository;
import com.example.eventmanager.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final EventEntityConverter entityConverter;


    public EventRegistrationService(
            EventRegistrationRepository registrationRepository,
            EventRepository eventRepository,
            EventService eventService,
            EventEntityConverter entityConverter
    ) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.entityConverter = entityConverter;
    }

    public void registerUserOnEvent(Long eventId, User user) {
        var event = eventService.getEventById(eventId);
        if (event.ownerId().equals(user.id()))
            throw new IllegalArgumentException("Can't register owner on event id = %s".formatted(eventId));

        var registration = registrationRepository.findRegistration(user.id(), eventId);
        if (registration.isPresent())
            throw new IllegalArgumentException("User id = %s is already registered on event id = %s"
                    .formatted(user.id(), eventId));
        if(event.status() != EventStatus.WAIT_START)
            throw new IllegalArgumentException("Can't register user to event with status =  %s"
                    .formatted(event.status()));

        registrationRepository.save(
                new EventRegistrationEntity(
                        null,
                        user.id(),
                        eventRepository.findById(eventId).orElseThrow()
                )
        );
    }

    public void cancelRegistrationOnEvent(Long eventId, User user) {

        var event = eventService.getEventById(eventId);

        var registration = registrationRepository.findRegistration(user.id(), eventId);
        if (registration.isEmpty())
            throw new IllegalArgumentException("User id = %s is not registered on event id = %s"
                    .formatted(user.id(), eventId));
        if(event.status() != EventStatus.WAIT_START)
            throw new IllegalArgumentException("Can't cancel registration on event with status = %s"
                    .formatted(event.status()));

        registrationRepository.delete(registration.orElseThrow());
    }

    public List<Event> getUserRegistrations(Long id) {
        return registrationRepository.findRegisteredEventsByUserId(id).stream()
                .map(entityConverter::toDomain)
                .toList();
    }
}
