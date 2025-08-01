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
        String sql = "SELECT * FROM Event ORDER BY StartDateTime ASC";
        
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
        String sql = "SELECT * FROM Event WHERE EventStatusTypeID = 2 AND StartDateTime > CURRENT_TIMESTAMP ORDER BY StartDateTime ASC LIMIT 2";
        
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
        String sql = "SELECT * FROM Event WHERE EventID = ?";
        
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
        String sql = "SELECT * FROM Event WHERE CAST(StartDateTime AS DATE) = CAST(? AS DATE) AND EventStatusTypeID = 2 ORDER BY StartDateTime ASC";
        
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
        String sql = "SELECT * FROM Event WHERE OrganizerID = ? AND EventStatusTypeID = 2 ORDER BY StartDateTime ASC";
        
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