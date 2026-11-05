package org.example.sessionservice.scheduler;

import org.example.sessionservice.domain.Session;
import org.example.sessionservice.domain.SessionParticipant;
import org.example.sessionservice.domain.SessionStatus;
import org.example.sessionservice.dto.NotificationMessageDto;
import org.example.sessionservice.messaging.MessageHelper;
import org.example.sessionservice.repository.SessionParticipantRepository;
import org.example.sessionservice.repository.SessionRepository;
import org.example.sessionservice.service.UserClientService;
import org.example.sessionservice.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class SessionReminderScheduler {

    private static final int REMINDER_MINUTES = 60;

    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository participantRepository;
    private final UserClientService userClientService;
    private final JmsTemplate jmsTemplate;
    private final MessageHelper messageHelper;

    @Value("${session.notification.queue}")
    private String notificationQueueName;

    public SessionReminderScheduler(SessionRepository sessionRepository, SessionParticipantRepository participantRepository, UserClientService userClientService, JmsTemplate jmsTemplate, MessageHelper messageHelper) {
        this.sessionRepository = sessionRepository;
        this.participantRepository = participantRepository;
        this.userClientService = userClientService;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
    }

    // Pokreće se na svakih 5 minuta
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void sendSessionReminders() {

        LocalDateTime from = LocalDateTime.now().plusMinutes(REMINDER_MINUTES);
        LocalDateTime to = from.plusMinutes(5);

        List<Session> sessions =
                sessionRepository.findByStatusAndStartTimeBetween(
                        SessionStatus.SCHEDULED,
                        from,
                        to
                );

        for (Session session : sessions) {

            List<SessionParticipant> participants =
                    participantRepository.findBySessionId(session.getId());

            for (SessionParticipant participant : participants) {

                UserResponseDto user =
                        userClientService.getUserById(participant.getUserId());

                NotificationMessageDto notification =
                        new NotificationMessageDto(
                                user.getEmail(),
                                NotificationMessageDto.NotificationType.SESSION_REMINDER,
                                Map.of(
                                        "sessionName", session.getName(),
                                        "startTime", session.getStartTime().toString()
                                )
                        );

                jmsTemplate.convertAndSend(
                        notificationQueueName,
                        messageHelper.createTextMessage(notification)
                );
            }
        }
    }
}