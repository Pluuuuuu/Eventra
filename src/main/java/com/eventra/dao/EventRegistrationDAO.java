package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Event;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventRegistrationDAO {
    
    public boolean registerUserForEvent(int userId, int eventId) {
        // First check if user exists in UserM table
        String checkUserSql = "SELECT UserID, Username, Email FROM UserM WHERE UserID = ?";
        try (Connection conn = Db.get();
             PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
            
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.err.println("User not found in UserM table: " + userId);
                return false;
            }
            
            String username = rs.getString("Username");
            String email = rs.getString("Email");
            System.out.println("Found user: " + username + " (" + email + ") with ID: " + userId);
            
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        // Check if event exists
        String checkEventSql = "SELECT EventID, Title FROM EventM WHERE EventID = ?";
        try (Connection conn = Db.get();
             PreparedStatement checkEventStmt = conn.prepareStatement(checkEventSql)) {
            
            checkEventStmt.setInt(1, eventId);
            ResultSet rs = checkEventStmt.executeQuery();
            
            if (!rs.next()) {
                System.err.println("Event not found in EventM table: " + eventId);
                return false;
            }
            
            String eventTitle = rs.getString("Title");
            System.out.println("Found event: " + eventTitle + " with ID: " + eventId);
            
        } catch (SQLException e) {
            System.err.println("Error checking event existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        // Get or create attendee record for this user
        int attendeeId = getOrCreateAttendeeForUser(userId);
        if (attendeeId == -1) {
            System.err.println("Failed to get or create attendee for user: " + userId);
            return false;
        }
        
        // Check if already registered
        String checkRegistrationSql = "SELECT COUNT(*) FROM Registration WHERE AttendeeID = ? AND EventID = ? AND RegistrationStatusTypeID = 2";
        try (Connection conn = Db.get();
             PreparedStatement checkRegStmt = conn.prepareStatement(checkRegistrationSql)) {
            
            checkRegStmt.setInt(1, attendeeId);
            checkRegStmt.setInt(2, eventId);
            ResultSet rs = checkRegStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                System.err.println("User " + userId + " is already registered for event " + eventId);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking existing registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        // Now register the user for the event using AttendeeID
        String sql = "INSERT INTO Registration (AttendeeID, EventID, RegistrationStatusTypeID) VALUES (?, ?, 2)";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, attendeeId); // Use AttendeeID, not UserID
            stmt.setInt(2, eventId);
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Registration successful for user " + userId + " (attendee " + attendeeId + ") to event " + eventId + " (rows affected: " + rowsAffected + ")");
            return rowsAffected > 0;
        } catch (SQLException e) {
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
        String sql = "UPDATE Registration SET RegistrationStatusTypeID = 3 WHERE AttendeeID = ? AND EventID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            
            int rowsAffected = stmt.executeUpdate();
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