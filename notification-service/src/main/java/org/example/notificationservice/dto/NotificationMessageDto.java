package org.example.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.notificationservice.domain.NotificationType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDto {

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
}
