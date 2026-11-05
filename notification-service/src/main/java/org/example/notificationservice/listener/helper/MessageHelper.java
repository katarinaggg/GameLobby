package org.example.notificationservice.listener.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MessageHelper {

    private final Validator validator;
    private final ObjectMapper objectMapper;

    public MessageHelper(Validator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    // na consumer strani -> citanje poruke sa ActiveMQ-a
    public <T> T getMessage(Message message, Class<T> clazz)
            throws JMSException {

        try {
            String json = ((TextMessage) message).getText();
            T data = objectMapper.readValue(json, clazz);

            Set<ConstraintViolation<T>> violations = validator.validate(data);
            if (!violations.isEmpty()) {
                throw new RuntimeException(formatViolations(violations));
            }

            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JMS message.", e);
        }
    }

    // na producer strani -> kreiranje poruke za slanje na ActiveMQ
    public String createTextMessage(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create JMS message.", e);
        }
    }

    private <T> String formatViolations(Set<ConstraintViolation<T>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }
}
