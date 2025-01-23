package com.example.eventmanager.event.domain;

import com.example.eventmanager.event.api.EventChangeKafkaMessage;
import com.example.eventmanager.event.api.EventUpdateRequestDto;
import com.example.eventmanager.event.db.EventEntity;
import com.example.eventmanager.event.db.EventRepository;
import com.example.eventmanager.kafka.KafkaProducer;
import com.example.eventmanager.user.domain.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final EventRepository eventRepository;
    private final KafkaProducer kafkaProducer;
    private final AuthenticationService authenticationService;

    public NotificationService(
            EventRepository eventRepository,
            KafkaProducer kafkaProducer,
            AuthenticationService authenticationService
    ) {
        this.eventRepository = eventRepository;
        this.kafkaProducer = kafkaProducer;
        this.authenticationService = authenticationService;
    }

    public void changeEventStatus(Long id, EventStatus eventStatus) {
        log.info("Get change event id = {}  status to {}", id, eventStatus);

        var eventEntity = eventRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Event with id = %s not found".formatted(id))
        );

        var eventMessage = new EventChangeKafkaMessage();
        eventMessage.setEventId(id);
        eventMessage.setOwnerId(eventEntity.getOwnerId());
        eventMessage.setChangedBy(authenticationService.getCurrentUser().id());
        eventMessage.setStatus(new FieldChange<>(EventStatus.valueOf(eventEntity.getStatus()), eventStatus));
        kafkaProducer.sendMessage(eventMessage);
    }

    public void changeEventFields(EventEntity entity, EventUpdateRequestDto updateDto) {
        log.info("Get change event for entity {} to {}", entity, updateDto);

        var eventMessage = new EventChangeKafkaMessage();
        eventMessage.setEventId(entity.getId());
        eventMessage.setOwnerId(entity.getOwnerId());
        eventMessage.setChangedBy(authenticationService.getCurrentUser().id());

        Optional.ofNullable(updateDto.name())
                .filter(e -> !e.equals(entity.getName()))
                .ifPresent(e -> eventMessage.setName(new FieldChange<>(entity.getName(), e)));

        Optional.ofNullable(updateDto.locationId())
                .filter(e -> !e.equals(entity.getLocationId()))
                .ifPresent(e -> eventMessage.setLocationId(
                        new FieldChange<>(entity.getLocationId(), e)));

        Optional.ofNullable(updateDto.maxPlaces())
                .filter(e -> !e.equals(entity.getMax_places()))
                .ifPresent(e -> eventMessage.setMaxPlaces(
                        new FieldChange<>(entity.getMax_places(), e)));

        Optional.ofNullable(updateDto.date())
                .filter(e -> !e.equals(entity.getDate()))
                .ifPresent(e -> eventMessage.setDate(new FieldChange<>(entity.getDate(), e)));

        Optional.ofNullable(updateDto.duration())
                .filter(e -> !e.equals(entity.getDuration()))
                .ifPresent(e -> eventMessage.setDuration(new FieldChange<>(entity.getDuration(), e)));

        Optional.ofNullable(updateDto.cost())
                .filter(e -> !e.equals(entity.getCost()))
                .ifPresent(e -> eventMessage.setCost(new FieldChange<>(entity.getCost(), e)));

        kafkaProducer.sendMessage(eventMessage);
    }
}
