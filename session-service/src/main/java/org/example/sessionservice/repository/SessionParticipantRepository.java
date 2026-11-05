package org.example.sessionservice.repository;

import org.example.sessionservice.domain.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {

    // vraca sve ucesnike jedne sesije
    List<SessionParticipant> findBySessionId(Long sessionId);

    // provera da li je korisnik vec prijavljen na sesiju
    Optional<SessionParticipant> findBySessionIdAndUserId(Long sessionId, Long userId);

    // broj prijavljenih ucesnika po sesiji
    long countBySessionId(Long sessionId);

    //broj ucesnika koju su prisustvovali sesiji(koristi se pri zakljucivanju iste)
    long countBySessionIdAndAttendedTrue(Long sessionId);
}