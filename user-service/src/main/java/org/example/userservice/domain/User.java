package org.example.userservice.domain;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(columnList = "username", unique = true),
                @Index(columnList = "email", unique = true)
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private LocalDate dateOfBirth;

    @Column(unique = true)
    private String activationToken;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // kada se registruje -> aktivira nalog (aktivacioni mejl)
    @Column(nullable = false)
    private boolean active;

    // flag da li je blokiran
    @Column(nullable = false)
    private boolean blocked;


    // PLAYER STATISTICS
    // samo ako role = PLAYER

    private int totalSessionsApplied;
    private int totalSessionsAttended;
    private int totalSessionsMissed;
    private double attendancePercentage;
    private int organizedSessionsCount;

    @Enumerated(EnumType.STRING)
    private Title organizerTitle;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public int getTotalSessionsApplied() {
        return totalSessionsApplied;
    }

    public void setTotalSessionsApplied(int totalSessionsApplied) {
        this.totalSessionsApplied = totalSessionsApplied;
    }

    public int getTotalSessionsAttended() {
        return totalSessionsAttended;
    }

    public void setTotalSessionsAttended(int totalSessionsAttended) {
        this.totalSessionsAttended = totalSessionsAttended;
    }

    public int getTotalSessionsMissed() {
        return totalSessionsMissed;
    }

    public void setTotalSessionsMissed(int totalSessionsMissed) {
        this.totalSessionsMissed = totalSessionsMissed;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public int getOrganizedSessionsCount() {
        return organizedSessionsCount;
    }

    public void setOrganizedSessionsCount(int organizedSessionsCount) {
        this.organizedSessionsCount = organizedSessionsCount;
    }

    public Title getOrganizerTitle() {
        return organizerTitle;
    }

    public void setOrganizerTitle(Title organizerTitle) {
        this.organizerTitle = organizerTitle;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }
}
