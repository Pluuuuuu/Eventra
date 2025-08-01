package com.eventra.model;

import java.time.LocalDateTime;

public class EventRegistration {
    private int registrationId;
    private int eventId;
    private int userId; // Foreign key to UserM table
    private int registrationStatusTypeId;
    private LocalDateTime registrationDate;
    private LocalDateTime attendedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public EventRegistration() {}

    public EventRegistration(int eventId, int userId) {
        this.eventId = eventId;
        this.userId = userId;
        this.registrationStatusTypeId = 2; // Confirmed by default
        this.registrationDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getRegistrationId() { return registrationId; }
    public void setRegistrationId(int registrationId) { this.registrationId = registrationId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRegistrationStatusTypeId() { return registrationStatusTypeId; }
    public void setRegistrationStatusTypeId(int registrationStatusTypeId) { 
        this.registrationStatusTypeId = registrationStatusTypeId; 
    }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public LocalDateTime getAttendedAt() { return attendedAt; }
    public void setAttendedAt(LocalDateTime attendedAt) { this.attendedAt = attendedAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public boolean isConfirmed() {
        return registrationStatusTypeId == 2; // Confirmed status
    }

    public boolean isAttended() {
        return registrationStatusTypeId == 4; // Attended status
    }

    public boolean isCancelled() {
        return registrationStatusTypeId == 3; // Cancelled status
    }

    @Override
    public String toString() {
        return "EventRegistration{" +
                "registrationId=" + registrationId +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", status=" + registrationStatusTypeId +
                ", registrationDate=" + registrationDate +
                '}';
    }
} 