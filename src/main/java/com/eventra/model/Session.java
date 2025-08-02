package com.eventra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Session {
    // ===== DATABASE FIELDS (from tfkk branch) =====
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
    
    // ===== DISPLAY FIELDS (from tf branch) =====
    private String name; // Alias for title for display purposes
    private String date; // Formatted date string
    private String day; // Day of week
    private String time; // Formatted time string
    private String location; // Session location
    private String presenter; // Presenter name (not ID)
    private String status; // Human-readable status

    // Constructors
    public Session() {}

    public Session(int eventId, String title, LocalDateTime startTime, 
                  LocalDateTime endTime, int presenterId) {
        this.eventId = eventId;
        this.title = title;
        this.name = title; // Set display name
        this.startTime = startTime;
        this.endTime = endTime;
        this.presenterId = presenterId;
        this.sessionStatusTypeId = 2; // Confirmed by default
        this.status = "Confirmed"; // Set display status
        updateDisplayFields();
    }
    
    public Session(String name, String date, String day, String time, String location, String presenter, String status) {
        this.name = name;
        this.title = name; // Set database title
        this.date = date;
        this.day = day;
        this.time = time;
        this.location = location;
        this.presenter = presenter;
        this.status = status;
        // Map status to statusTypeId
        switch (status.toLowerCase()) {
            case "confirmed": this.sessionStatusTypeId = 2; break;
            case "pending": this.sessionStatusTypeId = 1; break;
            case "cancelled": this.sessionStatusTypeId = 3; break;
            default: this.sessionStatusTypeId = 1;
        }
    }
    
    // ===== DATABASE GETTERS AND SETTERS =====
    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title; 
        this.name = title; // Keep display name in sync
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { 
        this.startTime = startTime; 
        updateDisplayFields();
    }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { 
        this.endTime = endTime; 
        updateDisplayFields();
    }

    public int getSessionStatusTypeId() { return sessionStatusTypeId; }
    public void setSessionStatusTypeId(int sessionStatusTypeId) { 
        this.sessionStatusTypeId = sessionStatusTypeId; 
        updateStatusDisplay();
    }

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
    
    // ===== DISPLAY GETTERS AND SETTERS =====
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name; 
        this.title = name; // Keep database title in sync
    }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getPresenter() { return presenter; }
    public void setPresenter(String presenter) { this.presenter = presenter; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        updateStatusTypeId();
    }
    
    // ===== HELPER METHODS =====
    
    /**
     * Update display fields based on database fields
     */
    private void updateDisplayFields() {
        if (startTime != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE");
            
            this.date = startTime.format(dateFormatter);
            this.time = startTime.format(timeFormatter);
            this.day = startTime.format(dayFormatter);
        }
    }
    
    /**
     * Update status display based on statusTypeId
     */
    private void updateStatusDisplay() {
        switch (sessionStatusTypeId) {
            case 1: this.status = "Pending"; break;
            case 2: this.status = "Confirmed"; break;
            case 3: this.status = "Cancelled"; break;
            default: this.status = "Unknown";
        }
    }
    
    /**
     * Update statusTypeId based on status display
     */
    private void updateStatusTypeId() {
        switch (status.toLowerCase()) {
            case "confirmed": this.sessionStatusTypeId = 2; break;
            case "pending": this.sessionStatusTypeId = 1; break;
            case "cancelled": this.sessionStatusTypeId = 3; break;
            default: this.sessionStatusTypeId = 1;
        }
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId=" + sessionId +
                ", eventId=" + eventId +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", presenterId=" + presenterId +
                ", status='" + status + '\'' +
                '}';
    }
}
