package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.EventRegistration;
import com.eventra.model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventRegistrationDAO {
    
    public boolean registerUserForEvent(int eventId, int userId) {
        // Check if user is already registered
        if (isUserRegisteredForEvent(eventId, userId)) {
            return false;
        }
        
        String sql = "INSERT INTO EventRegistration (EventID, UserID, RegistrationStatusTypeID, RegistrationDate, CreatedAt, UpdatedAt) " +
                    "VALUES (?, ?, 2, GETUTCDATE(), GETUTCDATE(), GETUTCDATE())";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Update event attendee count
                EventDAO eventDAO = new EventDAO();
                eventDAO.updateEventAttendeeCount(eventId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error registering user for event: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isUserRegisteredForEvent(int eventId, int userId) {
        String sql = "SELECT COUNT(*) FROM EventRegistration WHERE EventID = ? AND UserID = ? AND RegistrationStatusTypeID IN (2, 4)";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
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
        String sql = "SELECT e.* FROM Event e " +
                    "INNER JOIN EventRegistration er ON e.EventID = er.EventID " +
                    "WHERE er.UserID = ? AND er.RegistrationStatusTypeID IN (2, 4) " +
                    "ORDER BY e.StartDateTime ASC";
        
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
    
    public boolean cancelRegistration(int eventId, int userId) {
        String sql = "UPDATE EventRegistration SET RegistrationStatusTypeID = 3, UpdatedAt = GETUTCDATE() " +
                    "WHERE EventID = ? AND UserID = ? AND RegistrationStatusTypeID = 2";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Update event attendee count
                EventDAO eventDAO = new EventDAO();
                eventDAO.updateEventAttendeeCount(eventId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error cancelling registration: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("EventID"));
        event.setTitle(rs.getString("Title"));
        event.setDescription(rs.getString("Description"));
        event.setStartDateTime(rs.getTimestamp("StartDateTime").toLocalDateTime());
        event.setEndDateTime(rs.getTimestamp("EndDateTime").toLocalDateTime());
        event.setLocation(rs.getString("Location"));
        event.setImageUrl(rs.getString("ImageURL"));
        event.setEventStatusTypeId(rs.getInt("EventStatusTypeID"));
        event.setOrganizerId(rs.getInt("OrganizerID"));
        event.setMaxAttendees(rs.getInt("MaxAttendees"));
        event.setCurrentAttendees(rs.getInt("CurrentAttendees"));
        event.setEventType(rs.getString("EventType"));
        event.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        event.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        return event;
    }
} 