package org.example.sessionservice.domain;

import javax.persistence.*;

@Entity
@Table(name = "session_participants",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"session_id", "user_id"})
        })
public class SessionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // samo id korisnika iz user-service
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean attended;

    @Column(nullable = false)
    private boolean leftEarly;

    public SessionParticipant() {}

    public SessionParticipant(Session session, Long userId, boolean attended, boolean leftEarly) {
        this.session = session;
        this.userId = userId;
        this.attended = attended;
        this.leftEarly = leftEarly;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public boolean isLeftEarly() {
        return leftEarly;
    }

    public void setLeftEarly(boolean leftEarly) {
        this.leftEarly = leftEarly;
    }
}