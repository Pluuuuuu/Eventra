package com.eventra.model;

import java.time.LocalDateTime;

public class Session {
    private int sessionId;
    private int eventId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int sessionStatusTypeId;
    private int presenterId; // Foreign key to Presenter table
    private String sessionType; // e.g., "Panel", "Keynote", "Workshop"
    private int maxCapacity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Session() {}

    public Session(int eventId, String title, LocalDateTime startTime, 
                  LocalDateTime endTime, int presenterId) {
        this.eventId = eventId;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.presenterId = presenterId;
        this.sessionStatusTypeId = 2; // Confirmed by default
    }

    // Getters and Setters
    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public int getSessionStatusTypeId() { return sessionStatusTypeId; }
    public void setSessionStatusTypeId(int sessionStatusTypeId) { this.sessionStatusTypeId = sessionStatusTypeId; }

    public int getPresenterId() { return presenterId; }
    public void setPresenterId(int presenterId) { this.presenterId = presenterId; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId=" + sessionId +
                ", eventId=" + eventId +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", presenterId=" + presenterId +
                '}';
    }
} 