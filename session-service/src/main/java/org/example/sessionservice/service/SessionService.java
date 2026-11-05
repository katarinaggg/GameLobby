package org.example.sessionservice.service;

import org.example.sessionservice.dto.CloseSessionDto;
import org.example.sessionservice.dto.CreateSessionDto;
import org.example.sessionservice.dto.SessionResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionService {

    SessionResponseDto createSession(CreateSessionDto createSessionDto, Long organizerId);

    List<SessionResponseDto> getAllSessions();

    SessionResponseDto getSessionById(Long sessionId);

    SessionResponseDto getSessionByName(String name);

    //SessionResponseDto updateSession(Long sessionId, CreateSessionDto updatedSessionDto);

    //void deleteSession(Long sessionId);

    public void cancelSession(Long sessionId, Long currentUserId);

    // ucesnici

    SessionResponseDto joinSession(Long sessionId, Long userId);

    SessionResponseDto leaveSession(Long sessionId, Long userId);

    public SessionResponseDto acceptInvite(String token);

    public void inviteUser(Long sessionId, Long organizerId, Long invitedUserId);

    // zakljucivanje sesije

    void closeSession(CloseSessionDto closeSessionDto, Long currentUserId);

    // statistika (interno / za scheduler / user-service)

    long countParticipants(Long sessionId);

    long countAttendedParticipants(Long sessionId);

    // za filtriranje i sortiranje sesija
    List<SessionResponseDto> getFilteredSessions(Long gameId, String type, Integer maxPlayers, String description, Long userId,
            String sortBy // "startTime" ili "participantsCount"
    );
}