package com.example.eventmanager.event.domain;

import com.example.eventmanager.event.api.EventCreateRequestDto;
import com.example.eventmanager.event.api.EventSearchRequestDto;
import com.example.eventmanager.event.api.EventUpdateRequestDto;
import com.example.eventmanager.event.db.EventEntity;
import com.example.eventmanager.event.db.EventEntityConverter;
import com.example.eventmanager.event.db.EventRepository;
import com.example.eventmanager.location.domain.Location;
import com.example.eventmanager.location.domain.LocationService;
import com.example.eventmanager.user.domain.AuthenticationService;
import com.example.eventmanager.user.domain.User;
import com.example.eventmanager.user.domain.UserRole;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final AuthenticationService authenticationService;
    private final EventEntityConverter entityConverter;
    private final NotificationService notificationService;

    public EventService(
            EventRepository eventRepository,
            LocationService locationService,
            AuthenticationService authenticationService,
            EventEntityConverter entityConverter,
            NotificationService notificationService
    ) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.authenticationService = authenticationService;
        this.entityConverter = entityConverter;
        this.notificationService = notificationService;
    }

    public Event createEvent(EventCreateRequestDto createRequestDto) {
        Location location = locationService.getLocationById(createRequestDto.locationId());
        if (location.capacity() < createRequestDto.maxPlaces())
            throw new IllegalArgumentException("Location capacity = %s, event capacity = %s"
                    .formatted(location.capacity(), createRequestDto.maxPlaces())
            );

        User currentUser = authenticationService.getCurrentUser();
        EventEntity newEvent = new EventEntity(
                null,
                createRequestDto.name(),
                currentUser.id(),
                createRequestDto.maxPlaces(),
                createRequestDto.cost(),
                createRequestDto.date(),
                createRequestDto.duration(),
                new ArrayList<>(),
                location.id(),
                EventStatus.WAIT_START.name()
        );
        newEvent = eventRepository.save(newEvent);
        return entityConverter.toDomain(newEvent);
    }

    public Event getEventById(Long id) {
        var foundEntity = eventRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Entity with id = %s not found".formatted(id))
        );
        return entityConverter.toDomain(foundEntity);
    }

    public void cancelEvent(Long eventId) {
        checkOwnerOrAdminRights(eventId);
        var cancelEvent = getEventById(eventId);
        checkEventNotStarted(cancelEvent.status());
        if (cancelEvent.status() == EventStatus.CANCELED)
            return;

        eventRepository.changeEventStatus(eventId, EventStatus.CANCELED.name());
        notificationService.changeEventStatus(eventId, EventStatus.CANCELED);

    }

    public Event updateEvent(
            Long eventId,
            EventUpdateRequestDto updateRequestDto
    ) {
        checkOwnerOrAdminRights(eventId);
        var updateEvent = eventRepository.findById(eventId).orElseThrow();
        checkEventNotStarted(EventStatus.valueOf(updateEvent.getStatus()));


        if (updateRequestDto.maxPlaces() != null || updateRequestDto.locationId() != null) {

            var locationId = Optional.ofNullable(updateRequestDto.locationId())
                    .orElse(updateEvent.getLocationId());
            var maxPlaces = Optional.ofNullable(updateRequestDto.maxPlaces())
                    .orElse(updateEvent.getMax_places());
            var location = locationService.getLocationById(locationId);
            if (maxPlaces > location.capacity())
                throw new IllegalArgumentException("Capacity %s of location is less then max places %s"
                        .formatted(location.capacity(), maxPlaces));
        }
        if (updateRequestDto.maxPlaces() != null
                && updateEvent.getRegistrationList().size() > updateRequestDto.maxPlaces()) {
            throw new IllegalArgumentException("Registrations %s is more than max places %s"
                    .formatted(updateEvent.getRegistrationList().size(), updateRequestDto.maxPlaces()));
        }
        notificationService.changeEventFields(updateEvent, updateRequestDto);

        Optional.ofNullable(updateRequestDto.name())
                .ifPresent(updateEvent::setName);
        Optional.ofNullable(updateRequestDto.maxPlaces())
                .ifPresent(updateEvent::setMax_places);
        Optional.ofNullable(updateRequestDto.date())
                .ifPresent(updateEvent::setDate);
        Optional.ofNullable(updateRequestDto.cost())
                .ifPresent(updateEvent::setCost);
        Optional.ofNullable(updateRequestDto.duration())
                .ifPresent(updateEvent::setDuration);
        Optional.ofNullable(updateRequestDto.locationId())
                .ifPresent(updateEvent::setLocationId);

        eventRepository.save(updateEvent);
        return entityConverter.toDomain(updateEvent);
    }

    private void checkOwnerOrAdminRights(Long eventId) {
        var currentEvent = getEventById(eventId);
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.id().equals(currentEvent.ownerId())
                && currentUser.role() != UserRole.ADMIN)
            throw new IllegalArgumentException("User can't modify event with id=%s".formatted(eventId));
    }

    private void checkEventNotStarted(EventStatus status) {
        if (status != EventStatus.WAIT_START)
            throw new IllegalArgumentException("Can't cancel event with status=%s".formatted(status));

    }

    public List<Event> searchEvents(EventSearchRequestDto searchDto) {
        var events = eventRepository.searchEvents(
                searchDto.name(),
                searchDto.placesMin(),
                searchDto.placesMax(),
                searchDto.dateStartAfter(),
                searchDto.dateStartBefore(),
                searchDto.costMin(),
                searchDto.costMax(),
                searchDto.durationMin(),
                searchDto.durationMax(),
                searchDto.locationId(),
                searchDto.eventStatus() != null ? searchDto.eventStatus().name() : null
        );
        return events.stream().map(entityConverter::toDomain).toList();
    }

    public List<Event> getAllEventsByUser() {
        User currentUser = authenticationService.getCurrentUser();
        var events = eventRepository.findEventByUser(currentUser.id());
        return events.stream().map(entityConverter::toDomain).toList();
    }
}
