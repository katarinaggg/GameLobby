package org.example.sessionservice.repository;

import org.example.sessionservice.domain.Session;
import org.example.sessionservice.domain.SessionStatus;
import org.example.sessionservice.domain.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    // vraca sve sesije sa zadatatim statusom
    List<Session> findByStatus(SessionStatus status);

    // vraca sesije koje pocinju nakon zadatog vremena(za reminder scheduler)
    List<Session> findByStartTimeAfter(LocalDateTime time);

    // za scheduler(reminder)
    List<Session> findByStatusAndStartTimeBetween(SessionStatus status, LocalDateTime from, LocalDateTime to);

    Optional<Session> findByNameIgnoreCase(String name);

    // za filtriranje
    @Query("SELECT s FROM Session s " +
            "WHERE (:gameId IS NULL OR s.game.id = :gameId) AND " +
            "(:type IS NULL OR s.type = :type) AND " +
            "(:maxPlayers IS NULL OR s.maxPlayers <= :maxPlayers) AND " +
            "(:description IS NULL OR LOWER(s.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:userId IS NULL OR EXISTS (SELECT 1 FROM SessionParticipant sp WHERE sp.session.id = s.id AND sp.userId = :userId))")
    List<Session> findFilteredSessions(
            @Param("gameId") Long gameId,
            @Param("type") SessionType type,
            @Param("maxPlayers") Integer maxPlayers,
            @Param("description") String description,
            @Param("userId") Long userId
    );
}