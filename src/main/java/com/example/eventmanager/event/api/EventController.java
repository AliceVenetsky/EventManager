package com.example.eventmanager.event.api;

import com.example.eventmanager.event.domain.Event;
import com.example.eventmanager.event.domain.EventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final static Logger log = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;
    private final EventDtoConverter dtoConverter;

    public EventController(EventService eventService, EventDtoConverter dtoConverter) {
        this.eventService = eventService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody @Valid EventCreateRequestDto eventCreateRequestDto
    ) {
        log.info("Get request to create new event {}", eventCreateRequestDto);
        Event newEvent = eventService.createEvent(eventCreateRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(newEvent));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Get event by id = {}", eventId);
        Event foundEvent = eventService.getEventById(eventId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(foundEvent));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable("eventId") Long eventId,
            @RequestBody @Valid EventUpdateRequestDto updateDto
    ) {
        log.info("Get request to update event by id = {}", eventId);
        Event updatedEvent = eventService.updateEvent(eventId, updateDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(updatedEvent));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> cancelEvent(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Get request to cancel event by id = {}", eventId);
        eventService.cancelEvent(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> searchEvents(
            @RequestBody @Valid EventSearchRequestDto searchDto
    ) {
        log.info("Get request to search event by filter = {}", searchDto);
        var events = eventService.searchEvents(searchDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(events.stream().map(dtoConverter::toDto).toList());
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getAllEventsByUser() {
        log.info("Get request to get all events by user");
        var events = eventService.getAllEventsByUser();
        return ResponseEntity.status(HttpStatus.OK)
                .body(events.stream().map(dtoConverter::toDto).toList());
    }

}
