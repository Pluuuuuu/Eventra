package com.eventra.model;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Attendee (Attendee.java)
 * 
 * This class represents an event attendee in the Eventra system.
 * Think of it like a "participant profile" that contains all the information
 * about someone who attends events, including their personal details, preferences,
 * and event participation history.
 * 
 * WHAT THIS CLASS REPRESENTS:
 * - A person who attends events in the system
 * - Their personal information and contact details
 * - Their organization and professional background
 * - Their account status and login preferences
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Attendee: Someone who participates in events
 * - User Profile: Complete information about the attendee
 * - Organization Affiliation: Where the attendee works or studies
 * - Account Management: Login settings and security preferences
 * 
 * BUSINESS VALUE:
 * - Tracks who is attending events
 * - Manages attendee profiles and preferences
 * - Enables personalized event experiences
 * - Provides data for event planning and analysis
 * 
 * REAL-WORLD ANALOGY:
 * - Like a conference attendee badge with personal information
 * - Like a membership profile at a professional organization
 * - Like a student profile in an educational institution
 * - Like a customer profile in a business system
 */
public class Attendee {
    // 
    // CORE IDENTIFICATION DATA - Essential information to identify the attendee
    // 
    // These fields store the fundamental information needed to identify
    // who the attendee is and link them to their user account.
    // 
    // THINK OF IT LIKE:
    // - A unique ID card that identifies each person
    // - A reference number that links to other systems
    // - A primary key that makes each attendee unique
    // 
    
    /** 
     * Unique identifier for this attendee
     * 
     * This is like a serial number that makes each attendee unique in the system.
     * Think of it like an attendee badge number at a conference.
     */
    private int attendeeId;
    
    /** 
     * Link to the user account system
     * 
     * This connects the attendee profile to their main user account.
     * Think of it like linking a profile to a main account number.
     * 
     * FOREIGN KEY RELATIONSHIP:
     * This field connects to the UserM table to get account information.
     */
    private int userId;  // Foreign key to UserM table
    
    // 
    // PERSONAL IDENTIFICATION DATA - Basic personal information
    // 
    // These fields store the attendee's personal details that help
    // identify them and make them recognizable to others.
    // 
    // THINK OF IT LIKE:
    // - A personal information form you fill out for events
    // - A contact card with your basic details
    // - A profile section on a social media platform
    // 
    
    /** 
     * The attendee's first name
     * 
     * This is the person's given name as it appears on official documents.
     * Think of it like the first name on a driver's license.
     */
    private String firstName;
    
    /** 
     * The attendee's middle name (optional)
     * 
     * This is the person's middle name if they have one.
     * Think of it like the middle initial on official forms.
     * 
     * NOTE: This field is optional and can be null.
     */
    private String middleName;
    
    /** 
     * The attendee's last name
     * 
     * This is the person's family name or surname.
     * Think of it like the last name on a passport.
     */
    private String lastName;
    
    /** 
     * The attendee's email address
     * 
     * This is the primary way to contact the attendee electronically.
     * Think of it like the email address on a business card.
     * 
     * USAGE:
     * - Sending event confirmations and updates
     * - Communication about event changes
     * - Follow-up after events
     */
    private String email;
    
    // 
    // PROFESSIONAL AND ORGANIZATIONAL DATA - Work and affiliation information
    // 
    // These fields store information about where the attendee works,
    // studies, or is affiliated with professionally.
    // 
    // THINK OF IT LIKE:
    // - A business card showing your company and role
    // - A student ID showing your school and program
     // - A membership card showing your organization
     // 
    
    /** 
     * Where the attendee works or studies
     * 
     * This shows the company, school, or organization the attendee
     * is affiliated with. Think of it like the company name on a business card.
     * 
     * EXAMPLES:
     * - "Google Inc."
     * - "University of California"
     * - "Self-Employed"
     * - "Freelance Consultant"
     */
    private String organization;
    
    /** 
     * The attendee's phone number
     * 
     * This provides an alternative way to contact the attendee.
     * Think of it like a phone number on a contact list.
     * 
     * USAGE:
     * - Urgent event communications
     * - Phone-based support
     * - Alternative contact method
     * 
     * NOTE: This field is optional and can be null.
     */
    private String phone;
    
    /** 
     * Where the attendee is located
     * 
     * This shows the city, state, or country where the attendee lives or works.
     * Think of it like the address on a business card.
     * 
     * USAGE:
     * - Understanding attendee demographics
     * - Planning local events
     * - Networking opportunities
     * 
     * EXAMPLES:
     * - "San Francisco, CA"
     * - "New York, NY"
     * - "London, UK"
     */
    private String location;
    
    // 
    // DEMOGRAPHIC AND PERSONAL DATA - Additional personal characteristics
    // 
    // These fields store demographic information that helps with
    // event planning, diversity tracking, and personalized experiences.
     // 
     // THINK OF IT LIKE:
     // - A demographic survey you might fill out
     // - Personal preferences for event planning
     // - Information that helps create inclusive events
     // 
    
    /** 
     * The attendee's gender identity
     * 
     * This helps with creating inclusive events and understanding attendee diversity.
     * Think of it like gender information on a demographic survey.
     * 
     * USAGE:
     * - Creating inclusive event environments
     * - Understanding attendee demographics
     * - Planning gender-specific networking events
     * 
     * NOTE: This field is optional and can be null.
     */
    private String gender;
    
    /** 
     * The attendee's date of birth
     * 
     * This helps with age-appropriate event planning and age verification.
     * Think of it like a birth date on an ID card.
     * 
     * USAGE:
     * - Age verification for certain events
     * - Birthday celebrations and recognition
     * - Age-appropriate content and activities
     * 
     * NOTE: This field is optional and can be null.
     */
    private LocalDate dateOfBirth;
    
    // 
    // VISUAL AND PROFILE DATA - How the attendee appears in the system
    // 
    // These fields control how the attendee looks and is categorized
    // within the event management system.
     // 
     // THINK OF IT LIKE:
     // - A profile picture on a social media platform
     // - A category label that describes your role
     // - Visual elements that make your profile unique
     // 
    
    /** 
     * URL to the attendee's profile picture
     * 
     * This points to an image file that shows what the attendee looks like.
     * Think of it like a profile picture on LinkedIn or Facebook.
     * 
     * USAGE:
     * - Displayed on attendee lists and profiles
     * - Used in event networking features
     * - Helps attendees recognize each other at events
     * 
     * NOTE: This field is optional and can be null.
     */
    private String profilePicUrl;
    
    /** 
     * Category or type of attendee
     * 
     * This describes what kind of attendee they are or their role.
     * Think of it like a label that categorizes your participation.
     * 
     * EXAMPLES:
     * - "Regular" - Standard event attendee
     * - "VIP" - Special access attendee
     * - "Student" - Educational discount attendee
     * - "Speaker" - Presenter at events
     * - "Organizer" - Event planning team member
     */
    private String type;
    
    // 
    // SECURITY AND ACCOUNT DATA - Login and access control information
    // 
    // These fields manage the attendee's account security, login preferences,
    // and access control within the system.
     // 
     // THINK OF IT LIKE:
     // - A password-protected account with security settings
     // - Login preferences and access controls
     // - Security features that protect the attendee's information
     // 
    
    /** 
     * Encrypted version of the attendee's password
     * 
     * This stores the password in a secure, encrypted format that can't
     * be read by anyone, including system administrators.
     * 
     * SECURITY FEATURES:
     * - Password is encrypted using strong algorithms
     * - Original password cannot be recovered or viewed
     * - Protects attendee account security
     * 
     * THINK OF IT LIKE:
     * - A locked safe that only you can open
     * - An encrypted file that requires a key to read
     */
    private String passwordHash;
    
    /** 
     * Current status of the attendee account
     * 
     * This tracks whether the attendee account is active, suspended,
     * or in some other state. Think of it like an account status indicator.
     * 
     * STATUS VALUES:
     * - 1: Active (account is working normally)
     * - 2: Suspended (temporarily disabled)
     * - 3: Inactive (not currently active)
     * - 4: Banned (permanently disabled)
     */
    private int statusTypeId;
    
    /** 
     * How long the attendee can stay logged in
     * 
     * This sets a time limit for how long the attendee can remain
     * logged in before being automatically logged out.
     * 
     * SECURITY BENEFITS:
     * - Prevents unauthorized access if attendee forgets to log out
     * - Protects attendee information on shared computers
     * - Enforces security policies
     * 
     * SPECIAL VALUES:
     * - 0: No time limit (can stay logged in indefinitely)
     * - Positive number: Minutes before automatic logout
     * 
     * EXAMPLES:
     * - 30: Log out after 30 minutes of inactivity
     * - 120: Log out after 2 hours of inactivity
     * - 0: Never log out automatically
     */
    private int periodCanLoginInMinutes;
    
    /** 
     * When the attendee last failed to log in
     * 
     * This tracks the last time someone tried to log in with incorrect
     * credentials. It's used for security monitoring and account protection.
     * 
     * SECURITY USAGE:
     * - Detects potential hacking attempts
     * - Triggers account lockout after multiple failures
     * - Provides security audit trail
     * 
     * THINK OF IT LIKE:
     * - A security log that records failed access attempts
     * - A warning system that alerts to potential problems
     * - A record that helps protect your account
     */
    private LocalDateTime lastFailedLoginAt;
    
    // 
    // SYSTEM TRACKING DATA - When records were created and modified
    // 
    // These fields help track when attendee information was added or changed.
    // They're useful for debugging, auditing, and understanding data history.
     // 
     // THINK OF IT LIKE:
     // - Timestamps on a document showing when it was created and last modified
     // - Version history in a software program
     // - Edit history in a shared document
     // 
    
    /** 
     * When this attendee record was first created in the system
     * 
     * This helps track when the attendee was originally added.
     * Think of it like the creation date on a file or document.
     */
    private LocalDateTime createdAt;
    
    /** 
     * When this attendee record was last modified
     * 
     * This helps track when any information was last changed.
     * Think of it like the "last modified" date on a file.
     */
    private LocalDateTime updatedAt;

    // 
    // CONSTRUCTORS - Different ways to create a new attendee
    // 
    // Constructors provide different ways to create Attendee objects
    // depending on what information you have available.
    // 
    // THINK OF IT LIKE:
    // - Different forms you can fill out depending on your situation
    // - Different ways to start a project depending on your starting point
    // 
    
    /**
     * Default constructor - Creates an empty attendee
     * 
     * This creates a blank attendee profile that you can fill in later.
     * Think of it like getting a blank form that you fill out step by step.
     */
    public Attendee() {}

    /**
     * Main constructor - Creates an attendee with essential information
     * 
     * This creates an attendee profile with the most important information
     * needed to get started, setting sensible defaults for other fields.
     * 
     * THINK OF IT LIKE:
     * - Filling out the basic information on a registration form
     * - Creating a profile with essential details
     * - Setting up an account with default settings
     * 
     * DEFAULT VALUES SET:
     * - type: "Regular" (standard attendee)
     * - statusTypeId: 1 (active account)
     * - periodCanLoginInMinutes: 0 (no login time limit)
     * 
     * @param firstName The attendee's first name
     * @param lastName The attendee's last name
     * @param email The attendee's email address
     * @param organization Where the attendee works or studies
     * @param passwordHash The encrypted password for security
     * @param type The category or type of attendee
     */
    public Attendee(String firstName, String lastName, String email, String organization, 
                   String passwordHash, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.organization = organization;
        this.passwordHash = passwordHash;
        this.type = type != null ? type : "Regular";
        this.statusTypeId = 1; // Default active status
        this.periodCanLoginInMinutes = 0;
    }

    // 
    // GETTERS AND SETTERS - Access and modify attendee data
    // 
    // These methods provide controlled access to the attendee data.
    // They allow other parts of the application to read and update
    // attendee information safely.
    // 
    // THINK OF IT LIKE:
    // - A secure filing cabinet where you need the right key to access files
    // - A form where you can only edit certain fields
    // - A database where you need proper permissions to make changes
    // 
    
    /**
     * Get the unique attendee identifier
     * 
     * This returns the attendee ID that makes this attendee unique.
     * Think of it like reading an attendee badge number.
     * 
     * @return The unique attendee ID number
     */
    public int getAttendeeId() { return attendeeId; }
    
    /**
     * Set the unique attendee identifier
     * 
     * This assigns a unique ID to this attendee.
     * Think of it like assigning a serial number to a product.
     * 
     * @param attendeeId The unique ID to assign
     */
    public void setAttendeeId(int attendeeId) { this.attendeeId = attendeeId; }

    /**
     * Get the linked user account ID
     * 
     * This returns the user ID that connects this attendee to their main account.
     * Think of it like reading a reference number that links to another system.
     * 
     * @return The linked user account ID
     */
    public int getUserId() { return userId; }
    
    /**
     * Set the linked user account ID
     * 
     * This connects the attendee profile to a specific user account.
     * Think of it like linking a profile to a main account number.
     * 
     * @param userId The user account ID to link
     */
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * Get the attendee's first name
     * 
     * This returns the attendee's given name.
     * Think of it like reading a first name from a name tag.
     * 
     * @return The attendee's first name
     */
    public String getFirstName() { return firstName; }
    
    /**
     * Set the attendee's first name
     * 
     * This sets or updates the attendee's given name.
     * Think of it like updating a name on a name tag.
     * 
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Get the attendee's middle name
     * 
     * This returns the attendee's middle name if they have one.
     * Think of it like reading a middle initial from a form.
     * 
     * @return The attendee's middle name, or null if none
     */
    public String getMiddleName() { return middleName; }
    
    /**
     * Set the attendee's middle name
     * 
     * This sets or updates the attendee's middle name.
     * Think of it like updating a middle initial on a form.
     * 
     * @param middleName The middle name to set
     */
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    /**
     * Get the attendee's last name
     * 
     * This returns the attendee's family name or surname.
     * Think of it like reading a last name from a name tag.
     * 
     * @return The attendee's last name
     */
    public String getLastName() { return lastName; }
    
    /**
     * Set the attendee's last name
     * 
     * This sets or updates the attendee's family name.
     * Think of it like updating a last name on a name tag.
     * 
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Get the attendee's email address
     * 
     * This returns the attendee's primary contact email.
     * Think of it like reading an email address from a contact list.
     * 
     * @return The attendee's email address
     */
    public String getEmail() { return email; }
    
    /**
     * Set the attendee's email address
     * 
     * This sets or updates the attendee's contact email.
     * Think of it like updating an email address in a contact list.
     * 
     * @param email The email address to set
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Get the attendee's organization
     * 
     * This returns where the attendee works or studies.
     * Think of it like reading a company name from a business card.
     * 
     * @return The attendee's organization
     */
    public String getOrganization() { return organization; }
    
    /**
     * Set the attendee's organization
     * 
     * This sets or updates where the attendee works or studies.
     * Think of it like updating a company name on a business card.
     * 
     * @param organization The organization to set
     */
    public void setOrganization(String organization) { this.organization = organization; }

    /**
     * Get the attendee's phone number
     * 
     * This returns the attendee's contact phone number.
     * Think of it like reading a phone number from a contact list.
     * 
     * @return The attendee's phone number, or null if none
     */
    public String getPhone() { return phone; }
    
    /**
     * Set the attendee's phone number
     * 
     * This sets or updates the attendee's contact phone number.
     * Think of it like updating a phone number in a contact list.
     * 
     * @param phone The phone number to set
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Get the attendee's location
     * 
     * This returns where the attendee is located geographically.
     * Think of it like reading a city name from a contact list.
     * 
     * @return The attendee's location, or null if none
     */
    public String getLocation() { return location; }
    
    /**
     * Set the attendee's location
     * 
     * This sets or updates where the attendee is located.
     * Think of it like updating a city name in a contact list.
     * 
     * @param location The location to set
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * Get the attendee's gender
     * 
     * This returns the attendee's gender identity.
     * Think of it like reading gender information from a demographic form.
     * 
     * @return The attendee's gender, or null if none
     */
    public String getGender() { return gender; }
    
    /**
     * Set the attendee's gender
     * 
     * This sets or updates the attendee's gender identity.
     * Think of it like updating gender information on a demographic form.
     * 
     * @param gender The gender to set
     */
    public void setGender(String gender) { this.gender = gender; }

    /**
     * Get the attendee's date of birth
     * 
     * This returns when the attendee was born.
     * Think of it like reading a birth date from an ID card.
     * 
     * @return The attendee's date of birth, or null if none
     */
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    
    /**
     * Set the attendee's date of birth
     * 
     * This sets or updates the attendee's birth date.
     * Think of it like updating a birth date on an ID card.
     * 
     * @param dateOfBirth The date of birth to set
     */
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    /**
     * Get the attendee's profile picture URL
     * 
     * This returns the web address where the attendee's photo is stored.
     * Think of it like getting the location of a profile picture.
     * 
     * @return The URL to the attendee's photo, or null if none
     */
    public String getProfilePicUrl() { return profilePicUrl; }
    
    /**
     * Set the attendee's profile picture URL
     * 
     * This sets or updates where the attendee's photo is located.
     * Think of it like uploading a new profile picture.
     * 
     * @param profilePicUrl The photo URL to set
     */
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    /**
     * Get the attendee's type or category
     * 
     * This returns what kind of attendee they are.
     * Think of it like reading a category label from a profile.
     * 
     * @return The attendee's type or category
     */
    public String getType() { return type; }
    
    /**
     * Set the attendee's type or category
     * 
     * This sets or updates what kind of attendee they are.
     * Think of it like updating a category label on a profile.
     * 
     * @param type The type to set
     */
    public void setType(String type) { this.type = type; }

    /**
     * Get the encrypted password
     * 
     * This returns the secure, encrypted version of the attendee's password.
     * Think of it like accessing a locked safe - you can see it exists but can't read the contents.
     * 
     * @return The encrypted password hash
     */
    public String getPasswordHash() { return passwordHash; }
    
    /**
     * Set the encrypted password
     * 
     * This sets or updates the secure, encrypted version of the password.
     * Think of it like changing the combination on a safe.
     * 
     * @param passwordHash The encrypted password hash to set
     */
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    /**
     * Get the account status
     * 
     * This returns whether the attendee account is active, suspended, or in another state.
     * Think of it like checking if an account is working or disabled.
     * 
     * @return The account status ID number
     */
    public int getStatusTypeId() { return statusTypeId; }
    
    /**
     * Set the account status
     * 
     * This sets or updates whether the attendee account is active, suspended, or in another state.
     * Think of it like activating or deactivating an account.
     * 
     * @param statusTypeId The status ID to set
     */
    public void setStatusTypeId(int statusTypeId) { this.statusTypeId = statusTypeId; }

    /**
     * Get the login time limit
     * 
     * This returns how long the attendee can stay logged in before being automatically logged out.
     * Think of it like checking how long a session can last.
     * 
     * @return The login time limit in minutes, or 0 for no limit
     */
    public int getPeriodCanLoginInMinutes() { return periodCanLoginInMinutes; }
    
    /**
     * Set the login time limit
     * 
     * This sets or updates how long the attendee can stay logged in.
     * Think of it like setting a timer for how long a session can last.
     * 
     * @param periodCanLoginInMinutes The time limit in minutes, or 0 for no limit
     */
    public void setPeriodCanLoginInMinutes(int periodCanLoginInMinutes) { 
        this.periodCanLoginInMinutes = periodCanLoginInMinutes; 
    }

    /**
     * Get the last failed login time
     * 
     * This returns when someone last tried to log in with incorrect credentials.
     * Think of it like checking a security log for failed access attempts.
     * 
     * @return The last failed login time, or null if no failures
     */
    public LocalDateTime getLastFailedLoginAt() { return lastFailedLoginAt; }
    
    /**
     * Set the last failed login time
     * 
     * This sets or updates when someone last tried to log in with incorrect credentials.
     * Think of it like recording a failed access attempt in a security log.
     * 
     * @param lastFailedLoginAt The failed login time to set
     */
    public void setLastFailedLoginAt(LocalDateTime lastFailedLoginAt) { 
        this.lastFailedLoginAt = lastFailedLoginAt; 
    }

    /**
     * Get when this attendee record was created
     * 
     * This returns when this attendee was first added to the system.
     * Think of it like reading the creation date on a document.
     * 
     * @return The creation date and time
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * Set when this attendee record was created
     * 
     * This sets when this attendee was first added to the system.
     * Think of it like stamping a creation date on a document.
     * 
     * @param createdAt The creation date and time to set
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Get when this attendee record was last updated
     * 
     * This returns when any information was last changed.
     * Think of it like reading the "last modified" date on a document.
     * 
     * @return The last update date and time
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    /**
     * Set when this attendee record was last updated
     * 
     * This updates the timestamp showing when information was last modified.
     * Think of it like updating the "last modified" date when you edit a document.
     * 
     * @param updatedAt The last update date and time to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Attendee{" +
                "attendeeId=" + attendeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", organization='" + organization + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
} 