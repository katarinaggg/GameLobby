package org.example.notificationservice.controller;


import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.dto.NotificationResponseDto;
import org.example.notificationservice.mapper.NotificationMapper;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // ADMIN – vidi sve notifikacije
    @GetMapping
    public List<NotificationResponseDto> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }

    // USER – vidi samo svoje notifikacije
    @GetMapping("/user")
    public List<NotificationResponseDto> getUserNotifications(
            @RequestParam String email
    ) {
        return notificationRepository
                .findByRecipientEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }

}
