package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Event;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM EventM ORDER BY StartDate ASC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching events: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }
    
    public List<Event> getComingSoonEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM EventM WHERE StatusTypeID = 1 AND StartDate > GETUTCDATE() ORDER BY StartDate ASC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching coming soon events: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }
    
    public Event getEventById(int eventId) {
        String sql = "SELECT * FROM EventM WHERE EventID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEvent(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching event by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Event> getEventsByDate(LocalDateTime date) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM EventM WHERE CAST(StartDate AS DATE) = CAST(? AS DATE) AND StatusTypeID = 1 ORDER BY StartDate ASC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching events by date: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }
    
    public List<Event> getEventsByOrganizer(int organizerId) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM EventM WHERE CreatedByUserID = ? AND StatusTypeID = 1 ORDER BY StartDate ASC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, organizerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching events by organizer: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }
    
    public boolean updateEventAttendeeCount(int eventId) {
        String sql = "UPDATE Event SET CurrentAttendees = (SELECT COUNT(*) FROM EventRegistration WHERE EventID = ? AND RegistrationStatusTypeID = 2) WHERE EventID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            stmt.setInt(2, eventId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating event attendee count: " + e.getMessage());
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
        
        // For location, we'll use a placeholder since your schema doesn't have Location
        event.setLocation("Conference Center"); // Default location
        
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