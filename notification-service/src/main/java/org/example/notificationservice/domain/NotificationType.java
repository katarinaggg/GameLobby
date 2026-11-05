package org.example.notificationservice.domain;

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
