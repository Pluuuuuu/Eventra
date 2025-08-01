package com.eventra.model;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class Event {
    private int eventId;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private String imageUrl;
    private int eventStatusTypeId;
    private int organizerId; // Foreign key to UserM table (Admin role)
    private int maxAttendees;
    private int currentAttendees;
    private String eventType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Event() {}

    public Event(String title, String description, LocalDateTime startDateTime, 
                LocalDateTime endDateTime, String location, int organizerId) {
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.organizerId = organizerId;
        this.eventStatusTypeId = 2; // Published by default
        this.currentAttendees = 0;
    }

    // Getters and Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getEventStatusTypeId() { return eventStatusTypeId; }
    public void setEventStatusTypeId(int eventStatusTypeId) { this.eventStatusTypeId = eventStatusTypeId; }

    public int getOrganizerId() { return organizerId; }
    public void setOrganizerId(int organizerId) { this.organizerId = organizerId; }

    public int getMaxAttendees() { return maxAttendees; }
    public void setMaxAttendees(int maxAttendees) { this.maxAttendees = maxAttendees; }

    public int getCurrentAttendees() { return currentAttendees; }
    public void setCurrentAttendees(int currentAttendees) { this.currentAttendees = currentAttendees; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public LocalDate getEventDate() {
        return startDateTime != null ? startDateTime.toLocalDate() : null;
    }

    public boolean isComingSoon() {
        return eventStatusTypeId == 2; // Published status
    }

    public boolean hasAvailableSpots() {
        return maxAttendees == 0 || currentAttendees < maxAttendees;
    }

    public int getAvailableSpots() {
        return maxAttendees == 0 ? Integer.MAX_VALUE : maxAttendees - currentAttendees;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", startDateTime=" + startDateTime +
                ", location='" + location + '\'' +
                ", organizerId=" + organizerId +
                '}';
    }
} 