package org.example.userservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class NotificationMessageDto {

    public enum NotificationType {
        ACTIVATION_EMAIL,
        PASSWORD_CHANGED,

        SESSION_INVITATION,
        SESSION_JOIN_CONFIRMATION,
        SESSION_CANCELED,
        SESSION_REMINDER,

        SESSION_CREATION_REJECTED,
        TITLE_CHANGED
    }

    // poruka koja se razmenjuje izmedju servisa (kroz Active MQ)

    // kome se salje mejl
    @NotBlank
    private String recipientEmail;

    // tip poruke (koji mejl ce da se salje)
    @NotNull
    private NotificationType type;

    // sta je potrebno da se prosledi od podataka za odredjenu poruku
    // kljuc - vrednost
    // npr saljemo ime sesije,vreme,aktivacioni link,...
    @NotNull
    private Map<String, Object> data;

    public NotificationMessageDto() {
    }

    public NotificationMessageDto(String recipientEmail, NotificationType type, Map<String, Object> data) {
        this.recipientEmail = recipientEmail;
        this.type = type;
        this.data = data;
    }

    public @NotBlank String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(@NotBlank String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public @NotNull NotificationType getType() {
        return type;
    }

    public void setType(@NotNull NotificationType type) {
        this.type = type;
    }

    public @NotNull Map<String, Object> getData() {
        return data;
    }

    public void setData(@NotNull Map<String, Object> data) {
        this.data = data;
    }
}
