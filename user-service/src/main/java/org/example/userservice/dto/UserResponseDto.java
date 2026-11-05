package org.example.userservice.dto;

import org.example.userservice.domain.Role;
import org.example.userservice.domain.Title;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

public class UserResponseDto {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
    private boolean active;
    private boolean blocked;
    private LocalDate dateOfBirth;

    private Integer totalSessionsApplied;
    private Integer totalSessionsAttended;
    private Integer totalSessionsMissed;
    private Double attendancePercentage;
    private Integer organizedSessionsCount;
    private Title organizerTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Integer getTotalSessionsApplied() {
        return totalSessionsApplied;
    }

    public void setTotalSessionsApplied(Integer totalSessionsApplied) {
        this.totalSessionsApplied = totalSessionsApplied;
    }

    public Integer getTotalSessionsAttended() {
        return totalSessionsAttended;
    }

    public void setTotalSessionsAttended(Integer totalSessionsAttended) {
        this.totalSessionsAttended = totalSessionsAttended;
    }

    public Integer getTotalSessionsMissed() {
        return totalSessionsMissed;
    }

    public void setTotalSessionsMissed(Integer totalSessionsMissed) {
        this.totalSessionsMissed = totalSessionsMissed;
    }

    public Double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(Double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public Integer getOrganizedSessionsCount() {
        return organizedSessionsCount;
    }

    public void setOrganizedSessionsCount(Integer organizedSessionsCount) {
        this.organizedSessionsCount = organizedSessionsCount;
    }

    public Title getOrganizerTitle() {
        return organizerTitle;
    }

    public void setOrganizerTitle(Title organizerTitle) {
        this.organizerTitle = organizerTitle;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
