package org.example.notificationservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    // svaki mejl se cuva kao Notification koji cuvamo u bazi

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    public Notification(
            String recipientEmail,
            NotificationType type,
            String subject,
            String content
    ) {
        this.recipientEmail = recipientEmail;
        this.type = type;
        this.subject = subject;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
