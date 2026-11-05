package org.example.sessionservice.mapper;

import org.example.sessionservice.domain.Session;
import org.example.sessionservice.domain.SessionParticipant;
import org.example.sessionservice.dto.SessionResponseDto;
import org.example.sessionservice.repository.SessionParticipantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionMapper {

    private final SessionParticipantRepository participantRepository;

    public SessionMapper(SessionParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public SessionResponseDto toResponse(Session session) {
        SessionResponseDto dto = new SessionResponseDto();

        dto.setSessionId(session.getId());
        dto.setName(session.getName());
        dto.setGameId(session.getGame().getId());
        dto.setGameName(session.getGame().getName());
        dto.setMaxPlayers(session.getMaxPlayers());
        dto.setSessionType(session.getType().name());
        dto.setStartTime(session.getStartTime());
        dto.setDescription(session.getDescription());
        dto.setStatus(session.getStatus().name());
        // mora zbog frontenda
        dto.setOrganizerId(session.getOrganizerId());

        // Svi učesnici po ID-u
        List<Long> participantIds = participantRepository
                .findBySessionId(session.getId())
                .stream()
                .map(SessionParticipant::getUserId)
                .collect(Collectors.toList());
        dto.setParticipantIds(participantIds);

        return dto;
    }
}