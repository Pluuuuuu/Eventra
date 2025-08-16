package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Event;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Event Registration DAO (EventRegistrationDAO.java)
 * 
 * This class handles all the database operations related to event registration
 * in the Eventra application. Think of it like a "registration desk" at an
 * event where people sign up to attend.
 * 
 * WHAT THIS CLASS DOES:
 * - Registers users for events they want to attend
 * - Checks if users are already registered for events
 * - Manages the relationship between users and events
 * - Handles attendee creation and management
 * - Provides registration status and history information
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Event Registration: The process of signing up to attend an event
 * - User Management: Handling user accounts and their registration status
 * - Attendee Creation: Creating attendee records for users who register
 * - Registration Status: Tracking whether registrations are active, cancelled, etc.
 * - Data Validation: Ensuring users and events exist before registration
 * 
 * BUSINESS VALUE:
 * - Enables users to sign up for events they want to attend
 * - Prevents duplicate registrations for the same event
 * - Maintains accurate attendance records for event planning
 * - Supports event capacity management and planning
 * - Provides data for event analytics and reporting
 * 
 * REAL-WORLD ANALOGY:
 * - Like a ticket booth where people buy tickets to events
 * - Like a registration desk at a conference or workshop
 * - Like an RSVP system for parties or gatherings
 * - Like a sign-up sheet for classes or activities
 * - Like a membership system for clubs or organizations
 */
public class EventRegistrationDAO {
    
    /**
     * registerUserForEvent(int userId, int eventId) - Register User for Event
     * 
     * This method handles the complete process of registering a user for an event.
     * It's like a complete registration workflow that checks everything and
     * creates the registration if all conditions are met.
     * 
     * WHAT IT DOES:
     * - Verifies the user exists in the system
     * - Confirms the event exists and is available
     * - Creates an attendee record if needed
     * - Checks for existing registrations to prevent duplicates
     * - Creates the actual registration record
     * - Reports success or failure with detailed logging
     * 
     * USER EXPERIENCE:
     * - Users can easily register for events with one click
     * - System prevents accidental duplicate registrations
     * - Clear feedback on whether registration succeeded or failed
     * - Automatic attendee account creation if needed
     * 
     * TECHNICAL PROCESS:
     * 1. Validate user exists in UserM table
     * 2. Validate event exists in EventM table
     * 3. Get or create attendee record for the user
     * 4. Check for existing registrations to prevent duplicates
     * 5. Create new registration record
     * 6. Return success/failure status
     * 
     * @param userId The unique identifier for the user wanting to register
     * @param eventId The unique identifier for the event to register for
     * @return true if registration successful, false if failed
     */
    public boolean registerUserForEvent(int userId, int eventId) {
        // 
        // STEP 1: VALIDATE USER EXISTS - Check if the user is in our system
        // 
        // Before we can register someone for an event, we need to make sure
        // they actually exist in our user database. This is like checking if
        // someone has a valid ID before letting them into a building.
        // 
        // THINK OF IT LIKE:
        // - Checking if a person is on the guest list
        // - Verifying someone has a valid membership card
        // - Confirming a user account exists before allowing access
        // 
        String checkUserSql = "SELECT UserID, Username, Email FROM UserM WHERE UserID = ?";
        try (Connection conn = Db.get();
             PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
            
            // 
            // SET USER ID PARAMETER - Use the provided user ID
            // 
            // This sets the user ID we're looking for in the database query.
            // Think of it like filling in a form with the person's ID number.
            // 
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            
            // 
            // CHECK IF USER WAS FOUND - Verify user exists
            // 
            // If no user is found with this ID, we can't proceed with registration.
            // This is like trying to register someone who doesn't have an account.
            // 
            // THINK OF IT LIKE:
            // - Looking up a name in a phone book and not finding it
            // - Trying to use a library card that doesn't exist
            // - Attempting to log into an account that was never created
            // 
            if (!rs.next()) {
                System.err.println("User not found in UserM table: " + userId);
                return false; // Can't register someone who doesn't exist
            }
            
            // 
            // USER FOUND - Get user details for confirmation
            // 
            // If we found the user, let's get their basic information to confirm
            // who we're working with. This is like double-checking the person's
            // identity before proceeding.
            // 
            // THINK OF IT LIKE:
            // - Confirming someone's name and contact information
            // - Verifying the person matches their ID
            // - Getting details for the registration record
            // 
            String username = rs.getString("Username");
            String email = rs.getString("Email");
            System.out.println("Found user: " + username + " (" + email + ") with ID: " + userId);
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return false. This prevents the registration from proceeding
            // with invalid data.
            // 
            // THINK OF IT LIKE:
            // - A computer system being down when you try to check in
            // - A database server being unavailable
            // - A network connection problem preventing access
            // 
            System.err.println("Error checking user existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        // 
        // STEP 2: VALIDATE EVENT EXISTS - Check if the event is available
        // 
        // Now we need to make sure the event the user wants to register for
        // actually exists in our system. This is like checking if an event
        // is actually happening before selling tickets.
        // 
        // THINK OF IT LIKE:
        // - Verifying a concert is actually scheduled
        // - Confirming a workshop is actually being offered
        // - Checking if a class is actually available
        // 
        String checkEventSql = "SELECT EventID, Title FROM EventM WHERE EventID = ?";
        try (Connection conn = Db.get();
             PreparedStatement checkEventStmt = conn.prepareStatement(checkEventSql)) {
            
            // 
            // SET EVENT ID PARAMETER - Use the provided event ID
            // 
            // This sets the event ID we're looking for in the database query.
            // Think of it like specifying which event you want to attend.
            // 
            checkEventStmt.setInt(1, eventId);
            ResultSet rs = checkEventStmt.executeQuery();
            
            // 
            // CHECK IF EVENT WAS FOUND - Verify event exists
            // 
            // If no event is found with this ID, we can't proceed with registration.
            // This is like trying to buy tickets for an event that doesn't exist.
            // 
            // THINK OF IT LIKE:
            // - Looking for a movie that was never made
            // - Trying to attend a concert that was cancelled
            // - Attempting to join a class that was never scheduled
            // 
            if (!rs.next()) {
                System.err.println("Event not found in EventM table: " + eventId);
                return false; // Can't register for an event that doesn't exist
            }
            
            // 
            // EVENT FOUND - Get event details for confirmation
            // 
            // If we found the event, let's get its basic information to confirm
            // what we're registering for. This is like double-checking the event
            // details before proceeding.
            // 
            // THINK OF IT LIKE:
            // - Confirming the event name and details
            // - Verifying the event is actually happening
            // - Getting information for the registration record
            // 
            String eventTitle = rs.getString("Title");
            System.out.println("Found event: " + eventTitle + " with ID: " + eventId);
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return false. This prevents the registration from proceeding
            // with invalid data.
            // 
            // THINK OF IT LIKE:
            // - A computer system being down when you try to check event details
            // - A database server being unavailable
            // - A network connection problem preventing access
            // 
            System.err.println("Error checking event existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        // 
        // STEP 3: GET OR CREATE ATTENDEE RECORD - Ensure attendee exists
        // 
        // Every user who registers for an event needs an attendee record.
        // This is like making sure someone has a guest badge before they
        // can attend an event.
        // 
        // THINK OF IT LIKE:
        // - Creating a guest list entry for someone
        // - Making sure someone has proper credentials to attend
        // - Setting up the necessary records for participation
        // 
        int attendeeId = getOrCreateAttendeeForUser(userId);
        if (attendeeId == -1) {
            // 
            // ATTENDEE CREATION FAILED - Can't proceed without attendee record
            // 
            // If we can't create or find an attendee record for this user,
            // we can't register them for the event. This is like trying to
            // give someone a ticket without proper identification.
            // 
            // THINK OF IT LIKE:
            // - Trying to check someone in without proper credentials
            // - Attempting to give someone access without verification
            // - Failing to create necessary records for participation
            // 
            System.err.println("Failed to get or create attendee for user: " + userId);
            return false;
        }
        
        // 
        // STEP 4: CHECK FOR EXISTING REGISTRATION - Prevent duplicates
        // 
        // Before creating a new registration, we need to check if the user
        // is already registered for this event. This prevents duplicate
        // registrations and confusion.
        // 
        // THINK OF IT LIKE:
        // - Checking if someone already has a ticket
        // - Verifying someone isn't already on the guest list
        // - Preventing duplicate entries in a sign-up sheet
        // 
        String checkRegistrationSql = "SELECT COUNT(*) FROM Registration WHERE AttendeeID = ? AND EventID = ? AND RegistrationStatusTypeID = 2";
        try (Connection conn = Db.get();
             PreparedStatement checkRegStmt = conn.prepareStatement(checkRegistrationSql)) {
            
            // 
            // SET QUERY PARAMETERS - Use attendee ID and event ID
            // 
            // This sets the attendee ID and event ID we're checking for
            // in the database query. Think of it like looking up a specific
            // person's registration for a specific event.
            // 
            checkRegStmt.setInt(1, attendeeId);
            checkRegStmt.setInt(2, eventId);
            ResultSet rs = checkRegStmt.executeQuery();
            
            // 
            // CHECK FOR EXISTING REGISTRATION - Prevent duplicates
            // 
            // If we find an existing registration, we don't create a new one.
            // This is like not giving someone a second ticket to the same event.
            // 
            // THINK OF IT LIKE:
            // - Someone already being on the guest list
            // - A person already having a ticket
            // - An entry already existing in a sign-up sheet
            // 
            if (rs.next() && rs.getInt(1) > 0) {
                System.err.println("User " + userId + " is already registered for event " + eventId);
                return false; // Already registered, no need to register again
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return false. This prevents the registration from proceeding
            // with invalid data.
            // 
            // THINK OF IT LIKE:
            // - A computer system being down when you try to check registration
            // - A database server being unavailable
            // - A network connection problem preventing access
            // 
            System.err.println("Error checking existing registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        // 
        // STEP 5: CREATE REGISTRATION RECORD - Final step of registration
        // 
        // Now that we've validated everything, we can create the actual
        // registration record. This is like finally giving someone their
        // ticket to the event.
        // 
        // THINK OF IT LIKE:
        // - Adding someone to the guest list
        // - Creating a ticket for an event
        // - Making an entry in a sign-up sheet
        // 
        String sql = "INSERT INTO Registration (AttendeeID, EventID, RegistrationStatusTypeID) VALUES (?, ?, 2)";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET REGISTRATION PARAMETERS - Use attendee ID and event ID
            // 
            // This sets the attendee ID and event ID for the new registration
            // record. Think of it like filling out a registration form with
            // the person's information and the event details.
            // 
            stmt.setInt(1, attendeeId); // Use AttendeeID, not UserID
            stmt.setInt(2, eventId);
            
            // 
            // EXECUTE REGISTRATION - Create the actual record
            // 
            // This actually creates the registration record in the database.
            // Think of it like submitting a completed registration form.
            // 
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Registration successful for user " + userId + " (attendee " + attendeeId + ") to event " + eventId + " (rows affected: " + rowsAffected + ")");
            return rowsAffected > 0; // Return true if registration was created
        } catch (SQLException e) {
            // 
            // REGISTRATION FAILED - Handle database errors during creation
            // 
            // If something goes wrong while creating the registration record,
            // we log the error and return false. This prevents the system
            // from thinking registration succeeded when it actually failed.
            // 
            // THINK OF IT LIKE:
            // - A computer crash while processing a registration
            // - A database error while saving information
            // - A system failure during the final step
            // 
            System.err.println("Error registering user for event: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }
    
    private int getOrCreateAttendeeForUser(int userId) {
        // First try to find existing attendee for this user
        String findAttendeeSql = "SELECT AttendeeID FROM Attendee WHERE Email = (SELECT Email FROM UserM WHERE UserID = ?)";
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(findAttendeeSql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int attendeeId = rs.getInt("AttendeeID");
                System.out.println("Found existing attendee ID: " + attendeeId + " for user: " + userId);
                return attendeeId;
            }
        } catch (SQLException e) {
            System.err.println("Error finding existing attendee: " + e.getMessage());
        }
        
        // If no attendee exists, create one
        String createAttendeeSql = "INSERT INTO Attendee (FirstName, LastName, Email, Type, PasswordHash, StatusTypeID) " +
                                  "SELECT FirstName, LastName, Email, 'General', PasswordHash, StatusTypeID FROM UserM WHERE UserID = ?";
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(createAttendeeSql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int attendeeId = rs.getInt(1);
                    System.out.println("Created new attendee ID: " + attendeeId + " for user: " + userId);
                    return attendeeId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating attendee: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1; // Failed to get or create attendee
    }
    
    public boolean isUserRegisteredForEvent(int userId, int eventId) {
        // Get attendee ID for this user
        int attendeeId = getOrCreateAttendeeForUser(userId);
        if (attendeeId == -1) {
            return false;
        }
        
        String sql = "SELECT COUNT(*) FROM Registration WHERE AttendeeID = ? AND EventID = ? AND RegistrationStatusTypeID = 2";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, attendeeId);
            stmt.setInt(2, eventId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking user registration: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Event> getUserRegisteredEvents(int userId) {
        List<Event> events = new ArrayList<>();
        
        // Get attendee ID for this user
        int attendeeId = getOrCreateAttendeeForUser(userId);
        if (attendeeId == -1) {
            System.err.println("Could not get attendee ID for user: " + userId);
            return events;
        }
        
        String sql = "SELECT e.*, v.Name as VenueName, v.Address as VenueAddress, v.Capacity as VenueCapacity " +
                    "FROM Registration r " +
                    "INNER JOIN EventM e ON r.EventID = e.EventID " +
                    "LEFT JOIN Venue v ON e.VenueID = v.VenueID " +
                    "WHERE r.AttendeeID = ? AND r.RegistrationStatusTypeID = 2 " +
                    "ORDER BY e.StartDate ASC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, attendeeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user registered events: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }
    
    public boolean unregisterUserFromEvent(int userId, int eventId) {
        // Get attendee ID for this user
        int attendeeId = getOrCreateAttendeeForUser(userId);
        if (attendeeId == -1) {
            System.err.println("Could not get attendee ID for user: " + userId);
            return false;
        }
        
        String sql = "UPDATE Registration SET RegistrationStatusTypeID = 3 WHERE AttendeeID = ? AND EventID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, attendeeId);
            stmt.setInt(2, eventId);
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Unregistered user " + userId + " (attendee " + attendeeId + ") from event " + eventId + " (rows affected: " + rowsAffected + ")");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error unregistering user from event: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("EventID"));
        event.setTitle(rs.getString("Title"));
        event.setDescription(rs.getString("Description"));
        
        // Map StartDate/EndDate to StartDateTime/EndDateTime
        event.setStartDateTime(rs.getTimestamp("StartDate").toLocalDateTime());
        event.setEndDateTime(rs.getTimestamp("EndDate").toLocalDateTime());
        
        // Use venue information for location
        String venueName = rs.getString("VenueName");
        String venueAddress = rs.getString("VenueAddress");
        if (venueName != null && venueAddress != null) {
            event.setLocation(venueName + ", " + venueAddress);
        } else if (venueName != null) {
            event.setLocation(venueName);
        } else {
            event.setLocation("Conference Center"); // Default location
        }
        
        // Set default values for missing columns
        event.setImageUrl(null);
        event.setEventStatusTypeId(rs.getInt("StatusTypeID"));
        event.setOrganizerId(rs.getInt("CreatedByUserID"));
        
        // Get venue capacity for maxAttendees
        int venueCapacity = rs.getInt("VenueCapacity");
        event.setMaxAttendees(venueCapacity > 0 ? venueCapacity : 1000); // Default to 1000 if no capacity set
        
        // Calculate current attendees from registration count
        int currentAttendees = getCurrentAttendeeCount(event.getEventId());
        event.setCurrentAttendees(currentAttendees);
        
        event.setEventType("Conference");
        
        // Set timestamps
        event.setCreatedAt(rs.getTimestamp("CreatedAt") != null ? 
                          rs.getTimestamp("CreatedAt").toLocalDateTime() : LocalDateTime.now());
        event.setUpdatedAt(rs.getTimestamp("UpdatedAt") != null ? 
                          rs.getTimestamp("UpdatedAt").toLocalDateTime() : LocalDateTime.now());
        
        return event;
    }
    
    private int getCurrentAttendeeCount(int eventId) {
        String sql = "SELECT COUNT(*) FROM Registration WHERE EventID = ? AND RegistrationStatusTypeID = 2";
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendee count: " + e.getMessage());
        }
        return 0;
    }
} 