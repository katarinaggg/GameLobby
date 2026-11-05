package org.example.sessionservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sessionservice.dto.NotificationMessageDto;
import org.example.sessionservice.dto.NotificationMessageDto.NotificationType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageHelper {

    private final ObjectMapper objectMapper;

    public MessageHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String createTextMessage(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create JMS message", e);
        }
    }
}