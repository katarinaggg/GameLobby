package org.example.notificationservice.service;

import org.example.notificationservice.dto.NotificationMessageDto;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.example.notificationservice.domain.Notification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;

    public NotificationService(JavaMailSender mailSender, NotificationRepository notificationRepository) {
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
    }


    public void process(NotificationMessageDto dto) {

        String subject;
        String content;

        // prema tipu cemo praviti razlicite mejlove (drugaciji subject,body..)
        switch (dto.getType()) {

            case ACTIVATION_EMAIL:
                subject = "Activate your account";
                content = "Hello,\n\n"
                        + "Please activate your account using the link below:\n"
                        + dto.getData().get("activationLink");
                break;

            case PASSWORD_CHANGED:
                subject = "Password changed";
                content = "Hello,\n\n"
                        + "Your password has been successfully changed.\n"
                        + "If this was not you, please contact support immediately.";
                break;

            case SESSION_INVITATION:
                subject = "Session invitation";
                content = "Hello,\n\n"
                        + "You are invited to the session:\n"
                        + dto.getData().get("sessionName") + "\n\n"
                        + "Click the link below to accept the invitation:\n"
                        + dto.getData().get("inviteLink");
                break;

            case SESSION_JOIN_CONFIRMATION:
                subject = "Session joined successfully";
                content = "You have successfully joined the session:\n"
                        + dto.getData().get("sessionName");
                break;

            case SESSION_CANCELED:
                subject = "Session canceled";
                content = "Hello,\n\n"
                        + "The following session has been canceled:\n"
                        + dto.getData().get("sessionName") + "\n\n"
                        + "Scheduled start time was:\n"
                        + dto.getData().get("startTime");
                break;

            case SESSION_REMINDER:
                subject = "Session reminder";
                content = "Reminder: your session starts in 60 minutes.\n"
                        + "Session: " + dto.getData().get("sessionName");
                break;

            case SESSION_CREATION_REJECTED:
                subject = "Session creation rejected";
                content = "You cannot create a session because your attendance "
                        + "is below the required threshold.";
                break;

            case TITLE_CHANGED:
                subject = "New organizer title assigned";
                content = "Congratulations!\n\n"
                        + "You have been awarded a new organizer title: "
                        + dto.getData().get("newTitle") + "\n\n"
                        + "Keep up the good work!";
                break;

            default:
                throw new IllegalStateException("Unsupported notification type");
        }

        // slanje mejla
        sendEmail(dto.getRecipientEmail(), subject, content);

        Notification notification = new Notification(
                dto.getRecipientEmail(),
                dto.getType(),
                subject,
                content
        );

        // ubacujemo u bazu
        notificationRepository.save(notification);
    }

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
