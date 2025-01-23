package com.example.eventmanager.event.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE EventEntity e SET e.status = :event_status WHERE e.id = :id")
    void changeEventStatus(
            @Param("id") Long eventId,
            @Param("event_status") String status
    );

    @Modifying
    @Transactional
    @Query(""" 
            UPDATE EventEntity e
            SET e.name = :name,
               e.maxPlaces = :maxPlaces,
               e.date = :date,
               e.cost = :cost,
               e.duration = :duration,
               e.locationId = :locationId
            WHERE e.id = :id
            """)
    void updateEvent(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("maxPlaces") Integer maxPlaces,
            @Param("date") LocalDateTime date,
            @Param("cost") Integer cost,
            @Param("duration") Integer duration,
            @Param("locationId") Long locationId
    );

    @Query("""
            SELECT e FROM EventEntity e
            WHERE(:name IS NULL OR e.name LIKE %:name%)
            AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin)
            AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax)
            AND (CAST(:dateStartAfter as date) IS NULL OR e.date >= :dateStartAfter)
            AND (CAST(:dateStartBefore as date) IS NULL OR e.date <= :dateStartBefore)
            AND (:costMin IS NULL OR e.cost >= :costMin)
            AND (:costMax IS NULL OR e.cost <= :costMax)
            AND (:durationMin IS NULL OR e.duration >= :durationMin)
            AND (:durationMax IS NULL OR e.duration <= :durationMax)
            AND (:locationId IS NULL OR e.locationId = :locationId)
            AND (:eventStatus IS NULL OR e.status = :eventStatus)
            """)
    List<EventEntity> searchEvents(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin") Integer costMin,
            @Param("costMax") Integer costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") String eventStatus
    );

    @Query("SELECT e FROM EventEntity e WHERE e.ownerId = :id")
    List<EventEntity> findEventByUser(
            @Param("id") Long id);

    @Query("""
            SELECT e.id FROM EventEntity e
            WHERE e.date < CURRENT_TIMESTAMP
            AND e.status LIKE :status
            """)
    List<Long> findStartedEventsWithStatus(
            @Param("status") String status
    );

    @Query(value = """
            SELECT e.id FROM events e
            WHERE e.date + INTERVAL '1 minute' * e.duration < NOW()
            AND e.event_status LIKE :status
            """, nativeQuery = true)
    List<Long> findEndedEventsWithStatus(
            @Param("status") String status
    );
}
