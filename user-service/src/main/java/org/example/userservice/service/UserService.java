package org.example.userservice.service;

import org.example.userservice.domain.User;
import org.example.userservice.dto.*;

import java.util.List;

public interface UserService {

    UserResponseDto registerPlayer(RegisterRequestDto registerRequestDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    void activateUser(String token);

    UserResponseDto updateProfile(Long userId, UpdateProfileDto dto);

    void changePassword(Long userId, String oldPassword, String newPassword);

    User findById(Long id);

    UserResponseDto getUserById(Long id);

    void blockUser(Long userId);

    void unblockUser(Long userId);

    UserResponseDto getProfile(Long userId);

    List<UserResponseDto> getAllUsers();

    // TODO - kada bude gotov session service

    void incrementSessionsApplied(Long userId);

    void updateAttendance(Long userId, boolean attended);

    void incrementOrganizedSessions(Long userId);

    void decrementSessionsApplied(Long userId);
}
