package com.eventra.model;

import java.time.LocalDateTime;

/**
 * Presenter (Presenter.java)
 * 
 * This class represents a presenter or speaker at an event in the Eventra system.
 * Think of it like a "speaker profile" that contains all the information about
 * someone who will be presenting, speaking, or leading sessions at events.
 * 
 * WHAT THIS CLASS REPRESENTS:
 * - A person who will present or speak at events
 * - Their professional background and credentials
 * - Contact information and social media profiles
 * - Professional photo and biography
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Presenter: Someone who leads sessions, gives talks, or presents content
 * - Speaker Profile: Complete information about a presenter's background
 * - Professional Credentials: Job title, company, and experience
 * - Contact Information: Ways to reach the presenter
 * 
 * BUSINESS VALUE:
 * - Helps attendees learn about speakers before events
 * - Provides credibility and professionalism to events
 * - Enables networking and follow-up with presenters
 * - Creates engaging event marketing materials
 * 
 * REAL-WORLD ANALOGY:
 * - Like a speaker bio in a conference program
 * - Like a presenter profile on a webinar platform
 * - Like a speaker introduction at a TED Talk
 * - Like a panelist bio in a professional event
 */
public class Presenter {
    // 
    // CORE PRESENTER INFORMATION - Essential details about the person
    // 
    // These fields store the fundamental information needed to identify
    // and understand who the presenter is and their background.
    // 
    // THINK OF IT LIKE:
    // - A business card with essential contact information
    // - A resume header with name, title, and company
    // - A speaker introduction at the beginning of a presentation
    // 
    
    /** 
     * Unique identifier for this presenter
     * 
     * This is like a serial number that makes each presenter unique in the system.
     * Think of it like an employee ID or a speaker reference number.
     */
    private int presenterId;
    
    /** 
     * The presenter's full name
     * 
     * This is the person's actual name as it should appear in event materials.
     * Think of it like the name on a business card or conference badge.
     */
    private String name;
    
    /** 
     * The presenter's job title or professional role
     * 
     * This describes what the person does professionally.
     * Think of it like "Senior Software Engineer" or "Marketing Director".
     */
    private String title;
    
    /** 
     * The presenter's company or organization
     * 
     * This shows where the person works or is affiliated with.
     * Think of it like "Google" or "University of California".
     */
    private String company;
    
    /** 
     * The presenter's professional biography
     * 
     * This is a detailed description of their background, experience,
     * and expertise. Think of it like a professional summary on LinkedIn.
     */
    private String bio;
    
    // 
    // VISUAL AND CONTACT INFORMATION - How to see and reach the presenter
    // 
    // These fields provide ways to visually identify the presenter and
    // contact them for questions, networking, or follow-up.
    // 
    // THINK OF IT LIKE:
    // - A professional headshot for visual identification
     // - Contact details for professional communication
     // - Social media profiles for ongoing engagement
     // 
    
    /** 
     * URL to the presenter's professional photo
     * 
     * This points to an image file that shows what the presenter looks like.
     * Think of it like a profile picture on a social media platform.
     * 
     * USAGE:
     * - Displayed on event pages and speaker lists
     * - Used in event marketing materials
     * - Helps attendees recognize speakers at events
     */
    private String imageUrl;
    
    /** 
     * The presenter's professional email address
     * 
     * This is how people can contact the presenter directly with questions
     * or follow-up after events. Think of it like a business email address.
     * 
     * USAGE:
     * - Attendees can ask follow-up questions
     * - Event organizers can communicate with speakers
     * - Networking and professional connections
     */
    private String email;
    
    /** 
     * The presenter's LinkedIn profile URL
     * 
     * This links to their professional LinkedIn profile for networking
     * and learning more about their background. Think of it like a
     * digital business card.
     * 
     * USAGE:
     * - Professional networking after events
     * - Learning more about speaker experience
     * - Building professional relationships
     */
    private String linkedinUrl;
    
    /** 
     * The presenter's Twitter/X profile URL
     * 
     * This links to their social media presence for ongoing engagement
     * and following their thoughts and updates. Think of it like a
     * way to stay connected with speakers.
     * 
     * USAGE:
     * - Following speaker updates and insights
     * - Engaging with speaker content
     * - Building social media connections
     */
    private String twitterUrl;
    
    // 
    // SYSTEM TRACKING DATA - When records were created and modified
    // 
    // These fields help track when presenter information was added or changed.
    // They're useful for debugging, auditing, and understanding data history.
    // 
    // THINK OF IT LIKE:
    // - Timestamps on a document showing when it was created and last modified
    // - Version history in a software program
    // - Edit history in a shared document
    // 
    
    /** 
     * When this presenter record was first created in the system
     * 
     * This helps track when the presenter was originally added.
     * Think of it like the creation date on a file or document.
     */
    private LocalDateTime createdAt;
    
    /** 
     * When this presenter record was last modified
     * 
     * This helps track when any information was last changed.
     * Think of it like the "last modified" date on a file.
     */
    private LocalDateTime updatedAt;

    // 
    // CONSTRUCTORS - Different ways to create a new presenter
    // 
    // Constructors provide different ways to create Presenter objects
    // depending on what information you have available.
    // 
    // THINK OF IT LIKE:
    // - Different forms you can fill out depending on your situation
    // - Different ways to start a project depending on your starting point
    // 
    
    /**
     * Default constructor - Creates an empty presenter
     * 
     * This creates a blank presenter profile that you can fill in later.
     * Think of it like getting a blank form that you fill out step by step.
     */
    public Presenter() {}

    /**
     * Main constructor - Creates a presenter with essential information
     * 
     * This creates a presenter profile with the most important information
     * needed to identify and understand who they are.
     * 
     * THINK OF IT LIKE:
     * - Filling out the basic information on a speaker bio form
     * - Creating a professional profile with essential details
     * - Setting up a speaker introduction with key facts
     * 
     * @param name The presenter's full name
     * @param title Their professional job title or role
     * @param company Their company or organization
     * @param bio Their professional biography and background
     */
    public Presenter(String name, String title, String company, String bio) {
        this.name = name;
        this.title = title;
        this.company = company;
        this.bio = bio;
    }

    // 
    // GETTERS AND SETTERS - Access and modify presenter data
    // 
    // These methods provide controlled access to the presenter data.
    // They allow other parts of the application to read and update
    // presenter information safely.
    // 
    // THINK OF IT LIKE:
    // - A secure filing cabinet where you need the right key to access files
    // - A form where you can only edit certain fields
    // - A database where you need proper permissions to make changes
    // 
    
    /**
     * Get the unique presenter identifier
     * 
     * This returns the presenter ID that makes this presenter unique.
     * Think of it like reading an employee ID from a badge.
     * 
     * @return The unique presenter ID number
     */
    public int getPresenterId() { return presenterId; }
    
    /**
     * Set the unique presenter identifier
     * 
     * This assigns a unique ID to this presenter.
     * Think of it like assigning a serial number to a product.
     * 
     * @param presenterId The unique ID to assign
     */
    public void setPresenterId(int presenterId) { this.presenterId = presenterId; }

    /**
     * Get the presenter's full name
     * 
     * This returns the presenter's name as it should appear in materials.
     * Think of it like reading a name from a business card.
     * 
     * @return The presenter's full name
     */
    public String getName() { return name; }
    
    /**
     * Set the presenter's full name
     * 
     * This sets or updates the presenter's name.
     * Think of it like updating a name on a business card.
     * 
     * @param name The name to set
     */
    public void setName(String name) { this.name = name; }

    /**
     * Get the presenter's professional title
     * 
     * This returns the presenter's job title or professional role.
     * Think of it like reading a job title from a resume.
     * 
     * @return The presenter's professional title
     */
    public String getTitle() { return title; }
    
    /**
     * Set the presenter's professional title
     * 
     * This sets or updates the presenter's job title.
     * Think of it like updating a job title on a resume.
     * 
     * @param title The title to set
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Get the presenter's company or organization
     * 
     * This returns where the presenter works or is affiliated with.
     * Think of it like reading a company name from a business card.
     * 
     * @return The presenter's company or organization
     */
    public String getCompany() { return company; }
    
    /**
     * Set the presenter's company or organization
     * 
     * This sets or updates the presenter's company affiliation.
     * Think of it like updating a company name on a business card.
     * 
     * @param company The company to set
     */
    public void setCompany(String company) { this.company = company; }

    /**
     * Get the presenter's professional biography
     * 
     * This returns the detailed description of their background and experience.
     * Think of it like reading a professional summary from LinkedIn.
     * 
     * @return The presenter's biography
     */
    public String getBio() { return bio; }
    
    /**
     * Set the presenter's professional biography
     * 
     * This sets or updates the presenter's professional background.
     * Think of it like updating a professional summary on LinkedIn.
     * 
     * @param bio The biography to set
     */
    public void setBio(String bio) { this.bio = bio; }

    /**
     * Get the presenter's photo URL
     * 
     * This returns the web address where the presenter's photo is stored.
     * Think of it like getting the location of a profile picture.
     * 
     * @return The URL to the presenter's photo
     */
    public String getImageUrl() { return imageUrl; }
    
    /**
     * Set the presenter's photo URL
     * 
     * This sets or updates where the presenter's photo is located.
     * Think of it like uploading a new profile picture.
     * 
     * @param imageUrl The photo URL to set
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /**
     * Get the presenter's email address
     * 
     * This returns the presenter's professional email for contact.
     * Think of it like reading an email address from a business card.
     * 
     * @return The presenter's email address
     */
    public String getEmail() { return email; }
    
    /**
     * Set the presenter's email address
     * 
     * This sets or updates the presenter's contact email.
     * Think of it like updating an email address on a business card.
     * 
     * @param email The email address to set
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Get the presenter's LinkedIn profile URL
     * 
     * This returns the web address to their LinkedIn profile.
     * Think of it like getting a link to their professional network profile.
     * 
     * @return The LinkedIn profile URL
     */
    public String getLinkedinUrl() { return linkedinUrl; }
    
    /**
     * Set the presenter's LinkedIn profile URL
     * 
     * This sets or updates the link to their LinkedIn profile.
     * Think of it like adding a LinkedIn link to a business card.
     * 
     * @param linkedinUrl The LinkedIn URL to set
     */
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    /**
     * Get the presenter's Twitter/X profile URL
     * 
     * This returns the web address to their Twitter/X profile.
     * Think of it like getting a link to their social media presence.
     * 
     * @return The Twitter/X profile URL
     */
    public String getTwitterUrl() { return twitterUrl; }
    
    /**
     * Set the presenter's Twitter/X profile URL
     * 
     * This sets or updates the link to their Twitter/X profile.
     * Think of it like adding a Twitter link to a business card.
     * 
     * @param twitterUrl The Twitter/X URL to set
     */
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }

    /**
     * Get when this presenter record was created
     * 
     * This returns when this presenter was first added to the system.
     * Think of it like reading the creation date on a document.
     * 
     * @return The creation date and time
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * Set when this presenter record was created
     * 
     * This sets when this presenter was first added to the system.
     * Think of it like stamping a creation date on a document.
     * 
     * @param createdAt The creation date and time to set
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Get when this presenter record was last updated
     * 
     * This returns when any information was last changed.
     * Think of it like reading the "last modified" date on a document.
     * 
     * @return The last update date and time
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    /**
     * Set when this presenter record was last updated
     * 
     * This updates the timestamp showing when information was last modified.
     * Think of it like updating the "last modified" date when you edit a document.
     * 
     * @param updatedAt The last update date and time to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 
    // HELPER METHODS - Convenient ways to work with presenter data
    // 
    // These methods provide easy ways to get useful combinations of
    // presenter information without having to manually combine fields.
    // 
    // THINK OF IT LIKE:
    // - Quick ways to get formatted information for display
    // - Convenient methods for common data combinations
    // - Easy formatting for user interfaces and reports
    // 
    
    /**
     * getFullTitle() - Get formatted title and company information
     * 
     * This method combines the presenter's job title and company into
     * a single, formatted string that's perfect for display.
     * 
     * WHAT IT DOES:
     * - Combines title and company with proper formatting
     * - Handles cases where either field might be missing
     * - Returns a clean, professional-looking string
     * 
     * FORMATTING LOGIC:
     * - If both title and company exist: "Title, Company"
     * - If only title exists: "Title"
     * - If only company exists: "Company"
     * - If neither exists: empty string
     * 
     * USAGE EXAMPLES:
     * - "Senior Software Engineer, Google"
     * - "Marketing Director" (if no company)
     * - "University of California" (if no title)
     * 
     * THINK OF IT LIKE:
     * - A business card that shows "Job Title, Company Name"
     * - A speaker introduction that says "Name, Title at Company"
     * - A professional profile header with combined credentials
     * 
     * @return A formatted string combining title and company
     */
    public String getFullTitle() {
        // 
        // COMBINE TITLE AND COMPANY - Create professional display format
        // 
        // This logic handles all possible combinations of title and company
        // to create a professional-looking string for display purposes.
        // 
        // THINK OF IT LIKE:
        // - A formatter that creates professional-looking text
        // - A template that fills in available information
        // - A display generator that handles missing data gracefully
        // 
        if (title != null && company != null) {
            // 
            // BOTH FIELDS EXIST - Combine with comma separator
            // 
            // When we have both a title and company, we combine them
            // with a comma to create a professional format.
            // 
            // EXAMPLE: "Senior Software Engineer, Google"
            // 
            return title + ", " + company;
        } else if (title != null) {
            // 
            // ONLY TITLE EXISTS - Return just the title
            // 
            // When we only have a title (no company), we return
            // just the title without any extra formatting.
            // 
            // EXAMPLE: "Marketing Director"
            // 
            return title;
        } else if (company != null) {
            // 
            // ONLY COMPANY EXISTS - Return just the company
            // 
            // When we only have a company (no title), we return
            // just the company name without any extra formatting.
            // 
            // EXAMPLE: "University of California"
            // 
            return company;
        }
        
        // 
        // NEITHER FIELD EXISTS - Return empty string
        // 
        // When neither title nor company exists, we return an empty
        // string to indicate no professional information is available.
        // 
        // This prevents showing "null" or other error text to users.
        // 
        return "";
    }

    /**
     * toString() - Create a readable representation of the presenter
     * 
     * This method creates a string that shows the key information about
     * this presenter in a human-readable format.
     * 
     * THINK OF IT LIKE:
     * - A summary line on a presenter list
     * - A quick overview of what the presenter profile contains
     * - A readable format for logging and debugging
     * 
     * WHAT IT SHOWS:
     * - Presenter ID for identification
     * - Name, title, and company for context
     * - Format suitable for system logs and debugging
     * 
     * @return A string representation of the presenter
     */
    @Override
    public String toString() {
        return "Presenter{" +
                "presenterId=" + presenterId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
} 