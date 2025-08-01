package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventRegistrationDAO {
    
    public boolean registerUserForEvent(int userId, int eventId) {
        String sql = "INSERT INTO Registration (AttendeeID, EventID, RegistrationStatusTypeID) VALUES (?, ?, 2)";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user for event: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isUserRegisteredForEvent(int userId, int eventId) {
        String sql = "SELECT COUNT(*) FROM Registration WHERE AttendeeID = ? AND EventID = ? AND RegistrationStatusTypeID = 2";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
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
        String sql = "SELECT e.*, v.Name as VenueName, v.Address as VenueAddress " +
                    "FROM Registration r " +
                    "INNER JOIN EventM e ON r.EventID = e.EventID " +
                    "LEFT JOIN Venue v ON e.VenueID = v.VenueID " +
                    "WHERE r.AttendeeID = ? AND r.RegistrationStatusTypeID = 2 " +
                    "ORDER BY e.StartDate ASC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
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
        event.setMaxAttendees(0); // Default unlimited
        event.setCurrentAttendees(0);
        event.setEventType("Conference");
        
        // Set timestamps
        event.setCreatedAt(rs.getTimestamp("CreatedAt") != null ? 
                          rs.getTimestamp("CreatedAt").toLocalDateTime() : 
                          java.time.LocalDateTime.now());
        event.setUpdatedAt(rs.getTimestamp("UpdatedAt") != null ? 
                          rs.getTimestamp("UpdatedAt").toLocalDateTime() : 
                          java.time.LocalDateTime.now());
        return event;
    }
} 