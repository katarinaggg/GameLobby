package org.example.sessionservice.domain;

import javax.persistence.*;

@Entity
@Table(name = "session_invites")
public class SessionInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sessionId;
    private Long invitedUserId;

    @Column(nullable = false, unique = true)
    private String token;

    private boolean used;

    public SessionInvite() {}

    public SessionInvite(Long sessionId, Long invitedUserId, String token) {
        this.sessionId = sessionId;
        this.invitedUserId = invitedUserId;
        this.token = token;
        this.used = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(Long invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
