package org.example.notificationservice.listener;

import org.example.notificationservice.listener.helper.MessageHelper;
import org.springframework.jms.annotation.JmsListener;
import org.example.notificationservice.dto.NotificationMessageDto;
import org.example.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class NotificationListener{

    private final MessageHelper messageHelper;
    private final NotificationService notificationService;

    public NotificationListener(MessageHelper messageHelper,
                                NotificationService notificationService) {
        this.messageHelper = messageHelper;
        this.notificationService = notificationService;
    }

    @JmsListener(destination = "${notification.queue.name}")
    public void onMessage(Message message) throws JMSException {

        // pretvaramo jms poruku koja je stigla iz drugog servisa
        // u dto tip koji moze da obradi ovaj notification servis
        NotificationMessageDto dto =
                messageHelper.getMessage(message, NotificationMessageDto.class);

        // saljemo logici servisa
        notificationService.process(dto);
    }

}
