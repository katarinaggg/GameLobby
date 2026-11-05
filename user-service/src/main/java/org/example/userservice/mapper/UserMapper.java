package org.example.userservice.mapper;

import org.example.userservice.domain.Role;
import org.example.userservice.domain.Title;
import org.example.userservice.domain.User;
import org.example.userservice.dto.RegisterRequestDto;
import org.example.userservice.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User fromRegisterDto(RegisterRequestDto dto) {
        User user = new User();

        // podaci koje korisnik unosi
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDateOfBirth(dto.getDateOfBirth());

        // sistemska podrazumevana stanja
        user.setRole(Role.PLAYER);
        user.setActive(false);
        user.setBlocked(false);
        user.setTotalSessionsApplied(0);
        user.setTotalSessionsAttended(0);
        user.setTotalSessionsMissed(0);
        user.setAttendancePercentage(100.0);
        user.setOrganizedSessionsCount(0);
        user.setOrganizerTitle(Title.NONE);

        return user;
    }

    public UserResponseDto toUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setActive(user.isActive());
        dto.setBlocked(user.isBlocked());
        dto.setRole(user.getRole());
        dto.setDateOfBirth(user.getDateOfBirth());

        if(user.getRole() == Role.PLAYER){
            dto.setTotalSessionsApplied(user.getTotalSessionsApplied());
            dto.setTotalSessionsAttended(user.getTotalSessionsAttended());
            dto.setTotalSessionsMissed(user.getTotalSessionsMissed());
            dto.setAttendancePercentage(user.getAttendancePercentage());
            dto.setOrganizedSessionsCount(user.getOrganizedSessionsCount());
            dto.setOrganizerTitle(user.getOrganizerTitle());
        }
        return dto;
    }

}
