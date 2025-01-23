package com.example.eventmanager.event.api;

import com.example.eventmanager.event.domain.EventStatus;
import com.example.eventmanager.event.domain.FieldChange;

import java.time.LocalDateTime;


public class EventChangeKafkaMessage {
    private Long eventId;
    private Long ownerId;
    private Long changedBy;
    private FieldChange<String> name;
    private FieldChange<Integer> maxPlaces;
    private FieldChange<LocalDateTime> date;
    private FieldChange<Integer> cost;
    private FieldChange<Integer> duration;
    private FieldChange<Long> locationId;
    private FieldChange<EventStatus> status;

    public EventChangeKafkaMessage() {
    }

    public EventChangeKafkaMessage(
            Long eventId,
            Long ownerId,
            Long changedBy,
            FieldChange<EventStatus> status,
            FieldChange<Long> locationId,
            FieldChange<Integer> duration,
            FieldChange<Integer> cost,
            FieldChange<LocalDateTime> date,
            FieldChange<Integer> maxPlaces,
            FieldChange<String> name
    ) {
        this.eventId = eventId;
        this.status = status;
        this.locationId = locationId;
        this.duration = duration;
        this.cost = cost;
        this.date = date;
        this.maxPlaces = maxPlaces;
        this.name = name;
        this.ownerId = ownerId;
        this.changedBy = changedBy;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public FieldChange<String> getName() {
        return name;
    }

    public void setName(FieldChange<String> name) {
        this.name = name;
    }

    public FieldChange<Integer> getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(FieldChange<Integer> maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public FieldChange<LocalDateTime> getDate() {
        return date;
    }

    public void setDate(FieldChange<LocalDateTime> date) {
        this.date = date;
    }

    public FieldChange<Integer> getCost() {
        return cost;
    }

    public void setCost(FieldChange<Integer> cost) {
        this.cost = cost;
    }

    public FieldChange<Integer> getDuration() {
        return duration;
    }

    public void setDuration(FieldChange<Integer> duration) {
        this.duration = duration;
    }

    public FieldChange<Long> getLocationId() {
        return locationId;
    }

    public void setLocationId(FieldChange<Long> locationId) {
        this.locationId = locationId;
    }

    public FieldChange<EventStatus> getStatus() {
        return status;
    }

    public void setStatus(FieldChange<EventStatus> status) {
        this.status = status;
    }

    public Long getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Long changedBy) {
        this.changedBy = changedBy;
    }
}
