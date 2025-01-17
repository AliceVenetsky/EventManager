package com.example.eventmanager.event.api;

import com.example.eventmanager.event.domain.EventRegistrationService;
import com.example.eventmanager.user.domain.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
public class EventRegistrationController {

    private final static Logger log = LoggerFactory.getLogger(EventController.class);
    private final EventRegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final EventDtoConverter dtoConverter;

    public EventRegistrationController(
            EventRegistrationService registrationService,
            AuthenticationService authenticationService,
            EventDtoConverter dtoConverter
    ) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registerUserOnEvent(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Get request to register user on event = {}", eventId);
        var currentUser = authenticationService.getCurrentUser();
        registrationService.registerUserOnEvent(eventId, currentUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistrationOnEvent(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Get request to cancel registration  on event = {}", eventId);
        var currentUser = authenticationService.getCurrentUser();
        registrationService.cancelRegistrationOnEvent(eventId, currentUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getUserRegistrations() {
        var currentUser = authenticationService.getCurrentUser();
        log.info("Get request for user registrations = {}", currentUser.id());
        var events = registrationService.getUserRegistrations(currentUser.id());
        return ResponseEntity.status(HttpStatus.OK)
                .body(events.stream().map(dtoConverter::toDto).toList());
    }


}
