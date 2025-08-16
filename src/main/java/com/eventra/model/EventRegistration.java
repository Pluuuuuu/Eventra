package com.eventra.model;

import java.time.LocalDateTime;

/**
 * Event Registration (EventRegistration.java)
 * 
 * This class represents a user's registration for an event in the Eventra system.
 * Think of it like a "ticket" or "RSVP" that shows someone has signed up to attend
 * an event. It tracks the entire lifecycle from registration to attendance.
 * 
 * WHAT THIS CLASS REPRESENTS:
 * - A user's sign-up for a specific event
 * - The current status of their registration (confirmed, attended, cancelled)
 * - When they registered and when they actually attended
 * - Any special notes or requirements for their attendance
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Registration: The act of signing up for an event
 * - Status Tracking: Following the registration through different stages
 * - Attendance Confirmation: Recording when someone actually shows up
 * - Lifecycle Management: Managing the registration from start to finish
 * 
 * BUSINESS VALUE:
 * - Tracks event capacity and attendance
 * - Manages user commitments to events
 * - Provides data for event planning and analysis
 * - Enables communication with registered attendees
 * 
 * REAL-WORLD ANALOGY:
 * - Like a restaurant reservation that tracks when you booked, confirmed, and dined
 * - Like a concert ticket that shows your seat and attendance status
 * - Like a class enrollment that tracks your registration and participation
 */
public class EventRegistration {
    // 
    // CORE REGISTRATION DATA - Essential information about the registration
    // 
    // These fields store the fundamental information needed to track
    // who is registered for what event and what their status is.
    // 
    // THINK OF IT LIKE:
    // - A library card that links you to specific books
    // - A membership card that connects you to a club
    // - A reservation slip that ties you to a specific table
    // 
    
    /** 
     * Unique identifier for this registration
     * 
     * This is like a serial number that makes each registration unique.
     * Think of it like a confirmation number for a hotel booking.
     */
    private int registrationId;
    
    /** 
     * Which event this registration is for
     * 
     * This links the registration to a specific event in the system.
     * Think of it like the event name on a concert ticket.
     */
    private int eventId;
    
    /** 
     * Which user made this registration
     * 
     * This links the registration to a specific user account.
     * Think of it like the name on a restaurant reservation.
     * 
     * FOREIGN KEY RELATIONSHIP:
     * This field connects to the UserM table to get user details.
     */
    private int userId; // Foreign key to UserM table
    
    /** 
     * Current status of the registration
     * 
     * This tracks where the registration is in its lifecycle.
     * Think of it like the status of a package delivery.
     * 
     * STATUS VALUES:
     * - 1: Pending (waiting for confirmation)
     * - 2: Confirmed (registration is active)
     * - 3: Cancelled (registration was cancelled)
     * - 4: Attended (user actually showed up)
     */
    private int registrationStatusTypeId;
    
    /** 
     * When the user registered for the event
     * 
     * This records the exact moment someone signed up.
     * Think of it like the timestamp on an online order.
     */
    private LocalDateTime registrationDate;
    
    // 
    // ATTENDANCE AND TRACKING DATA - Information about actual participation
    // 
    // These fields track what happened after registration, including
    // whether the user actually attended and any special notes.
    // 
    // THINK OF IT LIKE:
    // - A check-in log that records when you arrived
    // - A participation record that tracks your involvement
    // - A feedback form that captures your experience
    // 
    
    /** 
     * When the user actually attended the event
     * 
     * This records the exact moment someone checked in or attended.
     * Think of it like a timestamp when you scan your ticket at a concert.
     * 
     * NULL MEANING:
     * If this is null, it means the user hasn't attended yet or cancelled.
     */
    private LocalDateTime attendedAt;
    
    /** 
     * Special notes or requirements for this registration
     * 
     * This can store special requests, dietary restrictions, accessibility needs,
     * or any other information that event organizers need to know.
     * 
     * THINK OF IT LIKE:
     * - Special instructions on a food order
     * - Notes on a hotel reservation
     * - Requirements listed on a job application
     */
    private String notes;
    
    // 
    // SYSTEM TRACKING DATA - When records were created and modified
    // 
    // These fields help track when information was added or changed.
    // They're useful for debugging, auditing, and understanding data history.
    // 
    // THINK OF IT LIKE:
    // - Timestamps on a document showing when it was created and last modified
    // - Version history in a software program
    // - Edit history in a shared document
    // 
    
    /** 
     * When this registration record was first created in the system
     * 
     * This helps track when the registration was originally entered.
     * Think of it like the creation date on a file or document.
     */
    private LocalDateTime createdAt;
    
    /** 
     * When this registration record was last modified
     * 
     * This helps track when any information was last changed.
     * Think of it like the "last modified" date on a file.
     */
    private LocalDateTime updatedAt;

    // 
    // CONSTRUCTORS - Different ways to create a new registration
    // 
    // Constructors provide different ways to create EventRegistration objects
    // depending on what information you have available.
    // 
    // THINK OF IT LIKE:
    // - Different forms you can fill out depending on your situation
    // - Different ways to start a project depending on your starting point
    // 
    
    /**
     * Default constructor - Creates an empty registration
     * 
     * This creates a blank registration that you can fill in later.
     * Think of it like getting a blank form that you fill out step by step.
     */
    public EventRegistration() {}

    /**
     * Main constructor - Creates a confirmed registration
     * 
     * This creates a registration with the essential information and
     * automatically sets it to confirmed status with the current time.
     * 
     * THINK OF IT LIKE:
     * - Filling out a registration form with your basic information
     * - Getting an immediate confirmation when you book something online
     * - Receiving instant confirmation for a restaurant reservation
     * 
     * @param eventId Which event the user is registering for
     * @param userId Which user is making the registration
     */
    public EventRegistration(int eventId, int userId) {
        this.eventId = eventId;
        this.userId = userId;
        this.registrationStatusTypeId = 2; // Confirmed by default
        this.registrationDate = LocalDateTime.now();
    }

    // 
    // GETTERS AND SETTERS - Access and modify registration data
    // 
    // These methods provide controlled access to the registration data.
    // They allow other parts of the application to read and update
    // registration information safely.
    // 
    // THINK OF IT LIKE:
    // - A secure filing cabinet where you need the right key to access files
    // - A form where you can only edit certain fields
    // - A database where you need proper permissions to make changes
    // 
    
    /**
     * Get the unique registration identifier
     * 
     * This returns the registration ID that makes this registration unique.
     * Think of it like reading a confirmation number from a booking.
     * 
     * @return The unique registration ID number
     */
    public int getRegistrationId() { return registrationId; }
    
    /**
     * Set the unique registration identifier
     * 
     * This assigns a unique ID to this registration.
     * Think of it like assigning a serial number to a product.
     * 
     * @param registrationId The unique ID to assign
     */
    public void setRegistrationId(int registrationId) { this.registrationId = registrationId; }

    /**
     * Get which event this registration is for
     * 
     * This returns the event ID that this registration belongs to.
     * Think of it like reading which movie a ticket is for.
     * 
     * @return The event ID number
     */
    public int getEventId() { return eventId; }
    
    /**
     * Set which event this registration is for
     * 
     * This assigns the registration to a specific event.
     * Think of it like assigning a ticket to a specific show.
     * 
     * @param eventId The event ID to assign
     */
    public void setEventId(int eventId) { this.eventId = eventId; }

    /**
     * Get which user made this registration
     * 
     * This returns the user ID that this registration belongs to.
     * Think of it like reading who a reservation is for.
     * 
     * @return The user ID number
     */
    public int getUserId() { return userId; }
    
    /**
     * Set which user made this registration
     * 
     * This assigns the registration to a specific user.
     * Think of it like assigning a reservation to a specific person.
     * 
     * @param userId The user ID to assign
     */
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * Get the current registration status
     * 
     * This returns the status ID that shows where the registration is
     * in its lifecycle. Think of it like checking the status of a package delivery.
     * 
     * @return The registration status ID number
     */
    public int getRegistrationStatusTypeId() { return registrationStatusTypeId; }
    
    /**
     * Set the current registration status
     * 
     * This updates the status of the registration (e.g., from confirmed to attended).
     * Think of it like updating the status of an order from "processing" to "shipped".
     * 
     * @param registrationStatusTypeId The new status ID to assign
     */
    public void setRegistrationStatusTypeId(int registrationStatusTypeId) { 
        this.registrationStatusTypeId = registrationStatusTypeId; 
    }

    /**
     * Get when the user registered for the event
     * 
     * This returns the exact date and time when the registration was made.
     * Think of it like reading when you placed an online order.
     * 
     * @return The registration date and time
     */
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    
    /**
     * Set when the user registered for the event
     * 
     * This sets the date and time when the registration was made.
     * Think of it like recording when you made a reservation.
     * 
     * @param registrationDate The registration date and time to set
     */
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    /**
     * Get when the user actually attended the event
     * 
     * This returns when the user checked in or attended the event.
     * Think of it like reading when you arrived at a restaurant for your reservation.
     * 
     * @return The attendance date and time, or null if not attended yet
     */
    public LocalDateTime getAttendedAt() { return attendedAt; }
    
    /**
     * Set when the user actually attended the event
     * 
     * This records when the user checked in or attended the event.
     * Think of it like recording your arrival time at an appointment.
     * 
     * @param attendedAt The attendance date and time to set
     */
    public void setAttendedAt(LocalDateTime attendedAt) { this.attendedAt = attendedAt; }

    /**
     * Get any special notes for this registration
     * 
     * This returns any special requests, requirements, or notes associated
     * with this registration. Think of it like reading special instructions on an order.
     * 
     * @return The notes text, or null if no notes
     */
    public String getNotes() { return notes; }
    
    /**
     * Set special notes for this registration
     * 
     * This adds or updates special requests, requirements, or notes for
     * this registration. Think of it like adding special instructions to an order.
     * 
     * @param notes The notes text to set
     */
    public void setNotes(String notes) { this.notes = notes; }

    /**
     * Get when this registration record was created
     * 
     * This returns when this registration was first entered into the system.
     * Think of it like reading the creation date on a document.
     * 
     * @return The creation date and time
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * Set when this registration record was created
     * 
     * This sets when this registration was first entered into the system.
     * Think of it like stamping a creation date on a document.
     * 
     * @param createdAt The creation date and time to set
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Get when this registration record was last updated
     * 
     * This returns when any information in this registration was last changed.
     * Think of it like reading the "last modified" date on a document.
     * 
     * @return The last update date and time
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    /**
     * Set when this registration record was last updated
     * 
     * This updates the timestamp showing when this registration was last modified.
     * Think of it like updating the "last modified" date when you edit a document.
     * 
     * @param updatedAt The last update date and time to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 
    // HELPER METHODS - Convenient ways to check registration status
    // 
    // These methods provide easy ways to check the current status of
    // a registration without having to remember the status ID numbers.
    // 
    // THINK OF IT LIKE:
    // - Quick status indicators on a dashboard
     // - Simple yes/no questions about the current state
     // - Easy ways to check if something is ready or complete
     // 
    
    /**
     * isConfirmed() - Check if registration is confirmed
     * 
     * This method checks if the registration has been confirmed and is active.
     * Think of it like checking if a reservation has been confirmed by the restaurant.
     * 
     * WHAT IT MEANS:
     * - Registration is active and the user is expected to attend
     * - Event organizers can count on this person being there
     * - The user has a confirmed spot at the event
     * 
     * @return true if the registration is confirmed, false otherwise
     */
    public boolean isConfirmed() {
        return registrationStatusTypeId == 2; // Confirmed status
    }

    /**
     * isAttended() - Check if user actually attended the event
     * 
     * This method checks if the user actually showed up and attended the event.
     * Think of it like checking if someone actually used their restaurant reservation.
     * 
     * WHAT IT MEANS:
     * - The user checked in or was marked as present
     * - Event organizers can confirm this person was there
     * - The registration has been fulfilled
     * 
     * @return true if the user attended, false otherwise
     */
    public boolean isAttended() {
        return registrationStatusTypeId == 4; // Attended status
    }

    /**
     * isCancelled() - Check if registration was cancelled
     * 
     * This method checks if the registration was cancelled by the user or organizers.
     * Think of it like checking if a restaurant reservation was cancelled.
     * 
     * WHAT IT MEANS:
     * - The user is no longer planning to attend
     * - Their spot is available for someone else
     * - The registration is no longer active
     * 
     * @return true if the registration was cancelled, false otherwise
     */
    public boolean isCancelled() {
        return registrationStatusTypeId == 3; // Cancelled status
    }

    /**
     * toString() - Create a readable representation of the registration
     * 
     * This method creates a string that shows the key information about
     * this registration in a human-readable format.
     * 
     * THINK OF IT LIKE:
     * - A summary line on a registration list
     * - A quick overview of what the registration contains
     * - A readable format for logging and debugging
     * 
     * WHAT IT SHOWS:
     * - Registration ID for identification
     * - Event ID and User ID for context
     * - Current status and registration date
     * 
     * @return A string representation of the registration
     */
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