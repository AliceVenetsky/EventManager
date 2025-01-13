package com.example.eventmanager.event.domain;

import com.example.eventmanager.event.db.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class EventStatusScheduler {

    private final static Logger log = LoggerFactory.getLogger(EventStatusScheduler.class);
    private final EventRepository eventRepository;

    public EventStatusScheduler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(fixedDelay = 1000)
    public void updateEventsStatus() {
        log.info("Event status scheduler update event");
        var startedEvents = eventRepository.findStartedEventsWithStatus(EventStatus.WAIT_START.name());
        startedEvents.forEach(eventId -> eventRepository.changeEventStatus(eventId, EventStatus.STARTED.name()));

        var finishedEvents = eventRepository.findEndedEventsWithStatus(EventStatus.STARTED.name());
        finishedEvents.forEach(eventId -> eventRepository.changeEventStatus(eventId, EventStatus.FINISHED.name()));
    }
}
