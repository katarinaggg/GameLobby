package org.example.sessionservice.dto;

import java.util.Map;

public class CloseSessionDto {

    private Long sessionId;

    // mapa: userId -> prisustvo (true = prisustvovao, false = nije prisustvovao ili napustio)
    private Map<Long, Boolean> participantsAttendance;

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Map<Long, Boolean> getParticipantsAttendance() { return participantsAttendance; }
    public void setParticipantsAttendance(Map<Long, Boolean> participantsAttendance) { this.participantsAttendance = participantsAttendance; }
}
