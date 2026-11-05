package org.example.sessionservice.repository;

import org.example.sessionservice.domain.SessionInvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionInviteRepository extends JpaRepository<SessionInvite, Long> {
    Optional<SessionInvite> findByToken(String token);
}
