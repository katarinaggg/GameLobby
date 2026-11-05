package org.example.notificationservice.repository;

import org.example.notificationservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // vraca notifikacije jednog igraca, sortirane od najnovijih ka najstarijim
    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String recipientEmail);
}
