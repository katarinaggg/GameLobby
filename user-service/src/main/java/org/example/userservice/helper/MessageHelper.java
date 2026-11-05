package org.example.userservice.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageHelper {

    // posto ovaj user servis samo salje poruke notification servisu
    // ovde nam treba samo metoda za kreiranje te poruke

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
