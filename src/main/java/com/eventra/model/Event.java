/**
 * Event Model Class (Event.java)
 * 
 * This class represents an EVENT in the Eventra system. Think of it as a "template"
 * that defines what information we store about each event that can be created and managed.
 * 
 * WHAT THIS CLASS DOES:
 * - Stores all the information about an event (title, description, dates, location, etc.)
 * - Tracks event capacity and current attendance
 * - Manages event status (draft, published, cancelled, etc.)
 * - Links events to their organizers (users with admin roles)
 * - Provides helper methods for event management and display
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Model Class: A class that represents data (like a blueprint for event information)
 * - Encapsulation: Data is private, but accessed through public methods (getters/setters)
 * - Date/Time Handling: Uses modern Java time classes for accurate event scheduling
 * - Business Logic: Helper methods that implement event-specific rules and calculations
 * - Foreign Key Relationships: Links events to users (organizers) and other entities
 * 
 * EVENT STATUS TYPES:
 * - 1: Draft (event is being planned, not visible to attendees)
 * - 2: Published (event is visible and accepting registrations)
 * - 3: Cancelled (event will not take place)
 * - 4: Completed (event has finished)
 * - 5: Postponed (event delayed to a later date)
 * 
 * EVENT CAPACITY MANAGEMENT:
 * - maxAttendees: Maximum number of people who can attend (0 = unlimited)
 * - currentAttendees: How many people are currently registered
 * - Available spots are calculated dynamically
 * 
 * RELATIONSHIPS:
 * - organizerId links to UserM table (the person who created/manages the event)
 * - Event can have multiple sessions (handled by Session class)
 * - Event can have multiple registrations (handled by EventRegistration class)
 */
package com.eventra.model;

// These imports give us access to the tools we need:
import java.time.LocalDateTime;            // For precise event start/end times (includes date and time)
import java.time.LocalDate;                // For just the date part (useful for display purposes)

/**
 * Event class - Data Model for Event Management
 * 
 * This class represents the core data structure for events in the Eventra system.
 * It follows the same pattern as the User class with private fields and public
 * getters/setters, plus additional helper methods for event-specific business logic.
 * 
 * The class is designed to be:
 * - Immutable-friendly: Can be safely shared between different parts of the application
 * - Database-ready: Fields match the database table structure
 * - Business-logic-rich: Contains methods that implement event management rules
 */
public class Event {
    /**
     * INSTANCE VARIABLES - These store the actual data for each event
     * 
     * Each variable represents a piece of information about an event.
     * Think of them as the "slots" where we store event details.
     */
    private int eventId;                   // Unique identifier for each event (like a serial number)
    private String title;                  // Event name (e.g., "Tech Conference 2024")
    private String description;            // Detailed description of what the event is about
    private LocalDateTime startDateTime;   // When the event begins (date and time)
    private LocalDateTime endDateTime;     // When the event ends (date and time)
    private String location;               // Where the event takes place (venue, address, etc.)
    private String imageUrl;               // Path to event banner/poster image
    private int eventStatusTypeId;         // Current status (1=draft, 2=published, 3=cancelled, etc.)
    private int organizerId;               // ID of the user who created/manages this event (links to UserM table)
    private int maxAttendees;              // Maximum number of people who can attend (0 = unlimited)
    private int currentAttendees;          // How many people are currently registered
    private String eventType;              // Category of event (e.g., "Conference", "Workshop", "Seminar")
    private LocalDateTime createdAt;       // When the event was first created in the system
    private LocalDateTime updatedAt;       // When the event information was last modified

    /**
     * CONSTRUCTORS - Special methods that create new Event objects
     * 
     * Constructors have the same name as the class and are called when you use "new Event()"
     * Think of them as "setup instructions" for creating new events.
     */
    
    /**
     * Default Constructor - creates an empty Event object
     * 
     * This constructor takes no parameters and creates an Event with no data.
     * Useful when you want to create an Event and fill in the data later,
     * or when reading event data from the database.
     */
    public Event() {}
    
    /**
     * Parameterized Constructor - creates an Event with initial data
     * 
     * This constructor takes the most important event information and sets it up.
     * It also sets sensible defaults for status and attendance.
     * 
     * @param title - The name of the event
     * @param description - What the event is about
     * @param startDateTime - When the event begins
     * @param endDateTime - When the event ends
     * @param location - Where the event takes place
     * @param organizerId - ID of the user organizing the event
     */
    public Event(String title, String description, LocalDateTime startDateTime, 
                LocalDateTime endDateTime, String location, int organizerId) {
        // Set the provided values to this Event object
        // 'this' refers to the current Event object being created
        this.title = title;                    // Store the event title
        this.description = description;         // Store the event description
        this.startDateTime = startDateTime;     // Store the start date/time
        this.endDateTime = endDateTime;         // Store the end date/time
        this.location = location;               // Store the event location
        this.organizerId = organizerId;         // Store the organizer's user ID
        
        // Set sensible default values for new events
        this.eventStatusTypeId = 2;             // Default status: Published (visible to attendees)
        this.currentAttendees = 0;              // Default: No attendees yet
    }

    /**
     * GETTERS AND SETTERS (also called accessors and mutators)
     * 
     * These methods provide controlled access to the private variables.
     * They follow a standard naming pattern:
     * - getVariableName() - returns the value of a variable
     * - setVariableName(value) - sets the value of a variable
     * 
     * WHY USE GETTERS AND SETTERS:
     * - Control: You can add validation (check if dates are valid)
     * - Logging: You can log when data changes
     * - Security: You can control who can change what data
     * - Flexibility: You can change how data is stored without changing other code
     * 
     * NOTE: In a real application, you might add validation here:
     * - Check if end date is after start date
     * - Ensure maxAttendees is not negative
     * - Validate email formats, etc.
     */
    
    // Event ID getter and setter
    public int getEventId() { return eventId; }                    // Return the event ID
    public void setEventId(int eventId) { this.eventId = eventId; }  // Set the event ID

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

    /**
     * HELPER METHODS - Business logic methods that provide useful information about events
     * 
     * These methods implement event-specific business rules and calculations.
     * They make it easier for other classes to work with events without having to
     * implement the same logic multiple times.
     */
    
    /**
     * getEventDate() - Extracts just the date part from the event start time
     * 
     * This method is useful when you only need the date (not the time) for display purposes.
     * For example, showing "March 15, 2024" instead of "March 15, 2024 at 9:00 AM"
     * 
     * @return LocalDate - The date when the event starts, or null if start time is not set
     */
    public LocalDate getEventDate() {
        // Check if startDateTime exists, then convert it to just a date
        // This is a ternary operator: condition ? valueIfTrue : valueIfFalse
        return startDateTime != null ? startDateTime.toLocalDate() : null;
    }

    /**
     * isComingSoon() - Checks if the event is published and ready for attendees
     * 
     * This method implements the business rule: "Published events are coming soon"
     * It's used to determine which events to show in the "Coming Soon" section.
     * 
     * @return boolean - true if event is published (status = 2), false otherwise
     */
    public boolean isComingSoon() {
        return eventStatusTypeId == 2; // Published status means event is coming soon
    }

    /**
     * hasAvailableSpots() - Checks if people can still register for the event
     * 
     * This method implements the business rule: "Events have available spots if they're unlimited
     * or if current attendance is below the maximum"
     * 
     * @return boolean - true if people can still register, false if event is full
     */
    public boolean hasAvailableSpots() {
        // maxAttendees == 0 means unlimited capacity
        // currentAttendees < maxAttendees means there are still spots left
        return maxAttendees == 0 || currentAttendees < maxAttendees;
    }

    /**
     * getAvailableSpots() - Calculates how many people can still register
     * 
     * This method provides the exact number of available spots for display purposes.
     * It's used to show "5 spots remaining" or "Unlimited capacity"
     * 
     * @return int - Number of available spots, or Integer.MAX_VALUE if unlimited
     */
    public int getAvailableSpots() {
        // If maxAttendees is 0 (unlimited), return the maximum possible integer
        // Otherwise, calculate remaining spots: max - current
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