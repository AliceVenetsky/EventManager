package com.example.eventmanager.event.db;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {

    @Query("""
                SELECT reg FROM EventRegistrationEntity reg
                WHERE reg.userId = :userId AND reg.event.id = :eventId
            """)
    Optional<EventRegistrationEntity> findRegistration(
            @Param("userId") Long userId,
            @Param("eventId") Long eventId);


    @Query("""
            SELECT reg.event FROM EventRegistrationEntity reg
            WHERE reg.userId = :userId
            """)
    List<EventEntity> findRegisteredEventsByUserId(
            @Param("userId") Long userId);


}
