package com.eventra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Session (Session.java)
 * 
 * This class represents a session or activity within an event in the Eventra system.
 * Think of it like a "meeting" or "presentation" that happens during an event,
 * such as a keynote speech, workshop, panel discussion, or breakout session.
 * 
 * WHAT THIS CLASS REPRESENTS:
 * - A specific activity or presentation within an event
 * - When the session starts and ends
 * - Who is presenting or leading the session
 * - What type of session it is and its capacity
 * - The current status and location of the session
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Session: A specific activity or presentation within an event
 * - Event Structure: How events are broken down into smaller parts
 * - Time Management: Scheduling and duration of activities
 * - Presenter Assignment: Who leads or presents each session
 * - Session Types: Different kinds of activities (keynote, workshop, panel)
 * 
 * BUSINESS VALUE:
 * - Breaks down events into manageable activities
 * - Enables detailed scheduling and time management
 * - Tracks presenter assignments and session capacity
 * - Provides structure for event agendas and programs
 * - Enables attendee planning and session selection
 * 
 * REAL-WORLD ANALOGY:
 * - Like a session in a conference schedule
 * - Like a class period in a school day
 * - Like a meeting slot in a business agenda
 * - Like a performance in a festival lineup
 */
public class Session {
    // 
    // DATABASE FIELDS - Core data stored in the database
    // 
    // These fields store the fundamental information about each session
    // that gets saved to the database for permanent storage.
    // 
    // THINK OF IT LIKE:
    // - The official record of what each session contains
    // - The data that gets saved and retrieved from storage
    // - The authoritative source of session information
    // 
    
    /** 
     * Unique identifier for this session
     * 
     * This is like a serial number that makes each session unique in the system.
     * Think of it like a session ID number in a conference program.
     */
    private int sessionId;
    
    /** 
     * Which event this session belongs to
     * 
     * This links the session to a specific event in the system.
     * Think of it like the event name that appears above a session in a schedule.
     * 
     * FOREIGN KEY RELATIONSHIP:
     * This field connects to the Event table to get event information.
     */
    private int eventId;
    
    /** 
     * The name or title of the session
     * 
     * This is what the session is called, like "Keynote Address" or
     * "Advanced Java Programming Workshop". Think of it like the title
     * of a chapter in a book.
     */
    private String title;
    
    /** 
     * Detailed description of what the session covers
     * 
     * This explains what attendees can expect to learn or experience
     * during the session. Think of it like a detailed description in
     * a conference program.
     * 
     * EXAMPLES:
     * - "Learn advanced Java programming techniques including multithreading and design patterns"
     * - "Interactive panel discussion on the future of artificial intelligence"
     * - "Hands-on workshop for building web applications with modern frameworks"
     */
    private String description;
    
    /** 
     * When the session begins
     * 
     * This is the exact date and time when the session starts.
     * Think of it like the start time on a meeting invitation.
     * 
     * USAGE:
     * - Creating event schedules and agendas
     * - Sending session reminders to attendees
     * - Planning travel and arrival times
     */
    private LocalDateTime startTime;
    
    /** 
     * When the session ends
     * 
     * This is the exact date and time when the session finishes.
     * Think of it like the end time on a meeting invitation.
     * 
     * USAGE:
     * - Calculating session duration
     * - Planning breaks between sessions
     * - Scheduling subsequent activities
     */
    private LocalDateTime endTime;
    
    /** 
     * Current status of the session
     * 
     * This tracks whether the session is confirmed, pending, cancelled,
     * or in another state. Think of it like a status indicator on a
     * meeting calendar.
     * 
     * STATUS VALUES:
     * - 1: Pending (waiting for confirmation)
     * - 2: Confirmed (session is definitely happening)
     * - 3: Cancelled (session will not occur)
     * - 4: Completed (session has finished)
     */
    private int sessionStatusTypeId;
    
    /** 
     * Who is presenting or leading the session
     * 
     * This links the session to a specific presenter in the system.
     * Think of it like the speaker name listed for a conference session.
     * 
     * FOREIGN KEY RELATIONSHIP:
     * This field connects to the Presenter table to get presenter information.
     */
    private int presenterId; // Foreign key to Presenter table
    
    /** 
     * What kind of session this is
     * 
     * This categorizes the session by its format or style.
     * Think of it like a label that describes the session type.
     * 
     * SESSION TYPES:
     * - "Keynote" - Main presentation or speech
     * - "Panel" - Group discussion with multiple speakers
     * - "Workshop" - Interactive, hands-on learning
     * - "Breakout" - Small group discussion
     * - "Networking" - Social interaction time
     * - "Q&A" - Question and answer session
     */
    private String sessionType; // e.g., "Panel", "Keynote", "Workshop"
    
    /** 
     * Maximum number of people who can attend
     * 
     * This sets a limit on how many attendees can participate in the session.
     * Think of it like the capacity limit for a classroom or meeting room.
     * 
     * SPECIAL VALUES:
     * - 0: Unlimited capacity (anyone can attend)
     * - Positive number: Specific capacity limit
     * 
     * USAGE:
     * - Managing room assignments
     * - Planning session logistics
     * - Controlling session size for quality
     */
    private int maxCapacity;
    
    /** 
     * When this session record was first created in the system
     * 
     * This helps track when the session was originally planned.
     * Think of it like the creation date on a meeting schedule.
     */
    private LocalDateTime createdAt;
    
    /** 
     * When this session record was last modified
     * 
     * This helps track when any information was last changed.
     * Think of it like the "last modified" date on a meeting schedule.
     */
    private LocalDateTime updatedAt;
    
    // 
    // DISPLAY FIELDS - Formatted data for user interface display
    // 
    // These fields store information that's formatted specifically for
    // displaying to users in the interface. They're derived from the
    // database fields but formatted for easy reading.
     // 
     // THINK OF IT LIKE:
     // - The formatted version of data that users actually see
     // - User-friendly representations of technical data
     // - Display-ready information for the interface
     // 
    
    /** 
     * Display name for the session (alias for title)
     * 
     * This is the session title formatted specifically for display purposes.
     * Think of it like the name that appears on a session card or schedule.
     * 
     * RELATIONSHIP:
     * This field is automatically kept in sync with the title field.
     */
    private String name; // Alias for title for display purposes
    
    /** 
     * Formatted date string for display
     * 
     * This shows the session date in a user-friendly format.
     * Think of it like the date that appears on a calendar or schedule.
     * 
     * FORMAT EXAMPLE:
     * - "October 15, 2024"
     * - "15 Oct 2024"
     * - "10/15/24"
     */
    private String date; // Formatted date string
    
    /** 
     * Day of the week for display
     * 
     * This shows which day of the week the session occurs on.
     * Think of it like the day name that appears on a weekly schedule.
     * 
     * FORMAT EXAMPLE:
     * - "Monday"
     * - "Tuesday"
     * - "Wednesday"
     */
    private String day; // Day of week
    
    /** 
     * Formatted time string for display
     * 
     * This shows the session time in a user-friendly format.
     * Think of it like the time that appears on a meeting invitation.
     * 
     * FORMAT EXAMPLE:
     * - "9:00 AM - 10:30 AM"
     * - "14:00 - 15:30"
     * - "2:00 PM - 3:30 PM"
     */
    private String time; // Formatted time string
    
    /** 
     * Where the session takes place
     * 
     * This shows the physical location or room where the session occurs.
     * Think of it like the room number or location on a conference schedule.
     * 
     * EXAMPLES:
     * - "Room 101"
     * - "Main Auditorium"
     * - "Conference Room A"
     * - "Virtual (Zoom)"
     */
    private String location; // Session location
    
    /** 
     * Name of the presenter (not the ID)
     * 
     * This shows the actual presenter's name for display purposes.
     * Think of it like the speaker name that appears on a session schedule.
     * 
     * RELATIONSHIP:
     * This field is derived from the presenterId but shows the readable name.
     */
    private String presenter; // Presenter name (not ID)
    
    /** 
     * Human-readable status description
     * 
     * This shows the session status in plain language that users can understand.
     * Think of it like a status message that appears on a meeting calendar.
     * 
     * EXAMPLES:
     * - "Confirmed"
     * - "Pending"
     * - "Cancelled"
     * - "Completed"
     */
    private String status; // Human-readable status

    // 
    // CONSTRUCTORS - Different ways to create a new session
    // 
    // Constructors provide different ways to create Session objects
    // depending on what information you have available.
    // 
    // THINK OF IT LIKE:
    // - Different forms you can fill out depending on your situation
    // - Different ways to start a project depending on your starting point
    // 
    
    /**
     * Default constructor - Creates an empty session
     * 
     * This creates a blank session that you can fill in later.
     * Think of it like getting a blank form that you fill out step by step.
     */
    public Session() {}

    /**
     * Main constructor - Creates a session with essential information
     * 
     * This creates a session with the most important information needed
     * to get started, setting sensible defaults for other fields.
     * 
     * THINK OF IT LIKE:
     * - Filling out the basic information on a session planning form
     * - Creating a session with essential details
     * - Setting up a meeting with default settings
     * 
     * DEFAULT VALUES SET:
     * - sessionStatusTypeId: 2 (confirmed)
     * - status: "Confirmed" (display status)
     * - name: Same as title (display name)
     * 
     * @param eventId Which event this session belongs to
     * @param title The name or title of the session
     * @param startTime When the session begins
     * @param endTime When the session ends
     * @param presenterId Who is presenting the session
     */
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
    
    /**
     * Display constructor - Creates a session from display information
     * 
     * This constructor creates a session when you already have the
     * formatted display information and need to create a session object.
     * 
     * THINK OF IT LIKE:
     * - Creating a session from information already formatted for display
     * - Converting user interface data into a session object
     * - Building a session from pre-formatted information
     * 
     * STATUS MAPPING:
     * The constructor automatically maps human-readable status to status IDs.
     * 
     * @param name The display name for the session
     * @param date The formatted date string
     * @param day The day of the week
     * @param time The formatted time string
     * @param location Where the session takes place
     * @param presenter The presenter's name
     * @param status The human-readable status
     */
    public Session(String name, String date, String day, String time, String location, String presenter, String status) {
        this.name = name;
        this.title = name; // Set database title
        this.date = date;
        this.day = day;
        this.time = time;
        this.location = location;
        this.presenter = presenter;
        this.status = status;
        // 
        // MAP STATUS TO STATUS TYPE ID - Convert human-readable status to database ID
        // 
        // This automatically converts the human-readable status text into
        // the corresponding database status ID number.
        // 
        // THINK OF IT LIKE:
        // - A translator that converts plain English to system codes
        // - A mapping system that links display text to database values
        // - An automatic conversion that keeps data consistent
        // 
        switch (status.toLowerCase()) {
            case "confirmed": this.sessionStatusTypeId = 2; break;
            case "pending": this.sessionStatusTypeId = 1; break;
            case "cancelled": this.sessionStatusTypeId = 3; break;
            default: this.sessionStatusTypeId = 1;
        }
    }
    
    // 
    // DATABASE GETTERS AND SETTERS - Access and modify core session data
    // 
    // These methods provide controlled access to the session data that's
    // stored in the database. They allow other parts of the application
    // to read and update session information safely.
    // 
    // THINK OF IT LIKE:
    // - A secure filing cabinet where you need the right key to access files
    // - A form where you can only edit certain fields
    // - A database where you need proper permissions to make changes
    // 
    
    /**
     * Get the unique session identifier
     * 
     * This returns the session ID that makes this session unique.
     * Think of it like reading a session number from a conference schedule.
     * 
     * @return The unique session ID number
     */
    public int getSessionId() { return sessionId; }
    
    /**
     * Set the unique session identifier
     * 
     * This assigns a unique ID to this session.
     * Think of it like assigning a serial number to a product.
     * 
     * @param sessionId The unique ID to assign
     */
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    /**
     * Get which event this session belongs to
     * 
     * This returns the event ID that this session is part of.
     * Think of it like reading which conference a session belongs to.
     * 
     * @return The event ID number
     */
    public int getEventId() { return eventId; }
    
    /**
     * Set which event this session belongs to
     * 
     * This assigns the session to a specific event.
     * Think of it like moving a session from one conference to another.
     * 
     * @param eventId The event ID to assign
     */
    public void setEventId(int eventId) { this.eventId = eventId; }

    /**
     * Get the session title
     * 
     * This returns the name or title of the session.
     * Think of it like reading the title of a chapter in a book.
     * 
     * @return The session title
     */
    public String getTitle() { return title; }
    
    /**
     * Set the session title
     * 
     * This sets or updates the session title and automatically keeps
     * the display name in sync. Think of it like updating a chapter title
     * and having it automatically update everywhere it appears.
     * 
     * AUTOMATIC UPDATES:
     * - Updates the title field
     * - Keeps the display name (name field) synchronized
     * 
     * @param title The title to set
     */
    public void setTitle(String title) { 
        this.title = title; 
        this.name = title; // Keep display name in sync
    }

    /**
     * Get the session description
     * 
     * This returns the detailed description of what the session covers.
     * Think of it like reading a detailed description in a conference program.
     * 
     * @return The session description
     */
    public String getDescription() { return description; }
    
    /**
     * Set the session description
     * 
     * This sets or updates the detailed description of the session.
     * Think of it like updating a description in a conference program.
     * 
     * @param description The description to set
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Get when the session starts
     * 
     * This returns the exact date and time when the session begins.
     * Think of it like reading the start time on a meeting invitation.
     * 
     * @return The session start time
     */
    public LocalDateTime getStartTime() { return startTime; }
    
    /**
     * Set when the session starts
     * 
     * This sets or updates the session start time and automatically
     * updates the display fields. Think of it like changing a meeting
     * time and having the calendar automatically update.
     * 
     * AUTOMATIC UPDATES:
     * - Updates the start time
     * - Refreshes display fields (date, day, time)
     * 
     * @param startTime The start time to set
     */
    public void setStartTime(LocalDateTime startTime) { 
        this.startTime = startTime; 
        updateDisplayFields();
    }

    /**
     * Get when the session ends
     * 
     * This returns the exact date and time when the session finishes.
     * Think of it like reading the end time on a meeting invitation.
     * 
     * @return The session end time
     */
    public LocalDateTime getEndTime() { return endTime; }
    
    /**
     * Set when the session ends
     * 
     * This sets or updates the session end time and automatically
     * updates the display fields. Think of it like changing a meeting
     * end time and having the calendar automatically update.
     * 
     * AUTOMATIC UPDATES:
     * - Updates the end time
     * - Refreshes display fields (date, day, time)
     * 
     * @param endTime The end time to set
     */
    public void setEndTime(LocalDateTime endTime) { 
        this.endTime = endTime; 
        updateDisplayFields();
    }

    /**
     * Get the session status ID
     * 
     * This returns the numeric status ID that tracks the session state.
     * Think of it like checking a status code in a system.
     * 
     * @return The session status ID number
     */
    public int getSessionStatusTypeId() { return sessionStatusTypeId; }
    
    /**
     * Set the session status ID
     * 
     * This sets or updates the session status and automatically
     * updates the display status. Think of it like changing a meeting
     * status and having the display automatically update.
     * 
     * AUTOMATIC UPDATES:
     * - Updates the status ID
     * - Refreshes the display status text
     * 
     * @param sessionStatusTypeId The status ID to set
     */
    public void setSessionStatusTypeId(int sessionStatusTypeId) { 
        this.sessionStatusTypeId = sessionStatusTypeId; 
        updateStatusDisplay();
    }

    /**
     * Get the presenter ID
     * 
     * This returns the ID of who is presenting or leading the session.
     * Think of it like reading a presenter reference number.
     * 
     * @return The presenter ID number
     */
    public int getPresenterId() { return presenterId; }
    
    /**
     * Set the presenter ID
     * 
     * This assigns a specific presenter to the session.
     * Think of it like assigning a speaker to a conference session.
     * 
     * @param presenterId The presenter ID to assign
     */
    public void setPresenterId(int presenterId) { this.presenterId = presenterId; }

    /**
     * Get the session type
     * 
     * This returns the type of the session, such as "Panel", "Keynote", or "Workshop".
     * Think of it like reading the category of a conference session.
     * 
     * @return The session type
     */
    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    /**
     * Get the maximum capacity of the session
     * 
     * This returns the limit on how many people can attend the session.
     * Think of it like reading the capacity of a classroom or meeting room.
     * 
     * @return The maximum capacity
     */
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    /**
     * Get the creation date of the session
     * 
     * This returns the date when the session record was first created.
     * Think of it like reading the "created on" date on a meeting schedule.
     * 
     * @return The creation date
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Get the last update date of the session
     * 
     * This returns the date when the session record was last modified.
     * Think of it like reading the "last modified" date on a meeting schedule.
     * 
     * @return The last update date
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // 
    // DISPLAY GETTERS AND SETTERS - Access and modify display-specific fields
    // 
    // These methods provide controlled access to the display-specific
    // fields that are derived from the database fields.
    // 
    // THINK OF IT LIKE:
    // - A user-friendly interface that lets you read and set display-only data
    // - A form where you can only edit certain fields that affect appearance
    // - A display-ready data structure that you can update
    // 
    
    /**
     * Get the display name for the session
     * 
     * This returns the session title formatted specifically for display.
     * Think of it like reading the name that appears on a session card.
     * 
     * @return The display name
     */
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name; 
        this.title = name; // Keep database title in sync
    }
    
    /**
     * Get the formatted date string for display
     * 
     * This returns the session date in a user-friendly format.
     * Think of it like reading the date that appears on a calendar.
     * 
     * @return The formatted date string
     */
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    /**
     * Get the day of the week for display
     * 
     * This returns which day of the week the session occurs on.
     * Think of it like reading the day name on a weekly schedule.
     * 
     * @return The day of the week
     */
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    
    /**
     * Get the formatted time string for display
     * 
     * This returns the session time in a user-friendly format.
     * Think of it like reading the time that appears on a meeting invitation.
     * 
     * @return The formatted time string
     */
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    
    /**
     * Get the location of the session
     * 
     * This returns the physical room or virtual platform where the session takes place.
     * Think of it like reading the room number or virtual link on a conference schedule.
     * 
     * @return The session location
     */
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    /**
     * Get the presenter's name for display
     * 
     * This returns the actual presenter's name for display purposes.
     * Think of it like reading the speaker name that appears on a session schedule.
     * 
     * @return The presenter's name
     */
    public String getPresenter() { return presenter; }
    public void setPresenter(String presenter) { this.presenter = presenter; }
    
    /**
     * Get the human-readable status description
     * 
     * This returns the session status in plain language.
     * Think of it like reading the status message on a meeting calendar.
     * 
     * @return The human-readable status
     */
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        updateStatusTypeId();
    }
    
    // 
    // HELPER METHODS - Utility methods for internal operations
    // 
    // These methods are used to update or format data that's
    // derived from the database fields.
    // 
    // THINK OF IT LIKE:
    // - Internal helper methods that perform specific tasks
    // - Methods that keep data consistency
    // - Methods that convert between formats
    // 
    
    /**
     * Update display fields based on database fields
     * 
     * This method calculates and sets the display fields (date, day, time)
     * based on the session's start time.
     * 
     * USAGE:
     * - When the start time changes, this method ensures the display fields are updated.
     * - This is called by setStartTime and setEndTime.
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
     * 
     * This method converts the numeric status ID into a human-readable string.
     * 
     * USAGE:
     * - When the status ID changes, this method ensures the display status is updated.
     * - This is called by setSessionStatusTypeId.
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
     * 
     * This method converts a human-readable status string into a numeric status ID.
     * 
     * USAGE:
     * - When the status text changes, this method ensures the status ID is updated.
     * - This is called by setStatus.
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
