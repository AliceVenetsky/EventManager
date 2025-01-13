package com.example.eventmanager.event.db;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    @OneToMany(mappedBy = "event")
    private List<EventRegistrationEntity> registrationList;
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    @Column(name = "duration", nullable = false)
    private Integer duration;
    @Column(name = "cost", nullable = false)
    private Integer cost;
    @Column(name = "location_id", nullable = false)
    private Long locationId;
    @Column(name = "event_status", nullable = false)
    private String status;

    public EventEntity() {}

    public EventEntity(
            Long id,
            String name,
            Long ownerId,
            Integer cost,
            Integer maxPlaces,
            LocalDateTime date,
            Integer duration,
            List<EventRegistrationEntity> registrationList,
            Long locationId,
            String status
    ) {
        this.locationId = locationId;
        this.cost = cost;
        this.duration = duration;
        this.date = date;
        this.registrationList = registrationList;
        this.ownerId = ownerId;
        this.maxPlaces = maxPlaces;
        this.name = name;
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMax_places() {
        return maxPlaces;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public List<EventRegistrationEntity> getRegistrationList() {
        return registrationList;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getCost() {
        return cost;
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMax_places(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setRegistrationList(List<EventRegistrationEntity> registrationList) {
        this.registrationList = registrationList;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
