package org.example.sessionservice.dto;

import java.time.LocalDateTime;

public class CreateSessionDto {

    private String name;
    private Long gameId;
    private int maxPlayers;
    private String sessionType;
    private LocalDateTime startTime;
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}