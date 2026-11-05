package org.example.sessionservice.dto;

import org.example.sessionservice.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public class SessionResponseDto {

    private Long sessionId;
    private String name;
    private Long gameId;
    private String gameName;
    private int maxPlayers;
    private String sessionType;
    private LocalDateTime startTime;
    private String description;
    private String status;
    private List<Long> participantIds;
    private List<UserResponseDto> participants;

    // mora zbog frontenda
    private Long organizerId;

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Long> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<Long> participantIds) { this.participantIds = participantIds; }

    public List<UserResponseDto> getParticipants() { return participants; }
    public void setParticipants(List<UserResponseDto> participants) { this.participants = participants; }
}