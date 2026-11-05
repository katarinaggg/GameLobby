package org.example.notificationservice.mapper;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.dto.NotificationResponseDto;

public class NotificationMapper {
    public static NotificationResponseDto toDto(Notification n) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(n.getId());
        dto.setRecipientEmail(n.getRecipientEmail());
        dto.setType(n.getType().name());
        dto.setSubject(n.getSubject());
        dto.setContent(n.getContent());
        dto.setCreatedAt(n.getCreatedAt().toString()); // ISO string
        return dto;
    }
}
