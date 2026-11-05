package org.example.sessionservice.service;

import org.example.sessionservice.dto.UserResponseDto;

public interface UserClientService {

    UserResponseDto getUserById(Long userId);

    void incrementSessionsApplied(Long userId);

    void updateAttendance(Long userId, boolean attended);

    void incrementOrganizedSessions(Long userId);

    void decrementSessionsApplied(Long userId);
}