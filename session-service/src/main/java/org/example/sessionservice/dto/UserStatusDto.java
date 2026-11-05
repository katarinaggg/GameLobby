package org.example.sessionservice.dto;

public class UserStatusDto {

    private Long userId;
    private int totalSessionsApplied;
    private int totalSessionsAttended;
    private int totalSessionsMissed;
    private double attendancePercentage;
    private int organizedSessionsCount;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public int getTotalSessionsApplied() { return totalSessionsApplied; }
    public void setTotalSessionsApplied(int totalSessionsApplied) { this.totalSessionsApplied = totalSessionsApplied; }

    public int getTotalSessionsAttended() { return totalSessionsAttended; }
    public void setTotalSessionsAttended(int totalSessionsAttended) { this.totalSessionsAttended = totalSessionsAttended; }

    public int getTotalSessionsMissed() { return totalSessionsMissed; }
    public void setTotalSessionsMissed(int totalSessionsMissed) { this.totalSessionsMissed = totalSessionsMissed; }

    public double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public int getOrganizedSessionsCount() { return organizedSessionsCount; }
    public void setOrganizedSessionsCount(int organizedSessionsCount) { this.organizedSessionsCount = organizedSessionsCount; }
}