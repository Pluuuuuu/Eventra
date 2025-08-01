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
        String sql = "SELECT e.*, v.Name as VenueName, v.Address as VenueAddress, v.Capacity as VenueCapacity " +
                    "FROM EventM e " +
                    "LEFT JOIN Venue v ON e.VenueID = v.VenueID " +
                    "WHERE e.StatusTypeID = 1 " +
                    "ORDER BY e.StartDate ASC";
        
        System.out.println("🔍 Fetching all events from database...");
        System.out.println("SQL Query: " + sql);
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
                count++;
                System.out.println("✅ Found event: " + event.getTitle() + " (ID: " + event.getEventId() + ")");
            }
            System.out.println("📊 Total events fetched: " + count);
        } catch (SQLException e) {
            System.err.println("❌ Error fetching all events: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        return events;
    }
    
    public List<Event> getComingSoonEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT e.*, v.Name as VenueName, v.Address as VenueAddress, v.Capacity as VenueCapacity " +
                    "FROM EventM e " +
                    "LEFT JOIN Venue v ON e.VenueID = v.VenueID " +
                    "WHERE e.StatusTypeID = 1 AND e.StartDate > GETUTCDATE() " +
                    "ORDER BY e.StartDate ASC";
        
        System.out.println("🔍 Fetching coming soon events from database...");
        System.out.println("SQL Query: " + sql);
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
                count++;
                System.out.println("✅ Found coming soon event: " + event.getTitle() + " (ID: " + event.getEventId() + ")");
            }
            System.out.println("📊 Total coming soon events fetched: " + count);
        } catch (SQLException e) {
            System.err.println("❌ Error fetching coming soon events: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        return events;
    }
    
    public Event getEventById(int eventId) {
        String sql = "SELECT e.*, v.Name as VenueName, v.Address as VenueAddress, v.Capacity as VenueCapacity " +
                    "FROM EventM e " +
                    "LEFT JOIN Venue v ON e.VenueID = v.VenueID " +
                    "WHERE e.EventID = ?";
        
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
        String sql = "SELECT e.*, v.Name as VenueName, v.Address as VenueAddress, v.Capacity as VenueCapacity " +
                    "FROM EventM e " +
                    "LEFT JOIN Venue v ON e.VenueID = v.VenueID " +
                    "WHERE CAST(e.StartDate AS DATE) = CAST(? AS DATE) AND e.StatusTypeID = 1 " +
                    "ORDER BY e.StartDate ASC";
        
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
        String sql = "SELECT e.*, v.Name as VenueName, v.Address as VenueAddress, v.Capacity as VenueCapacity " +
                    "FROM EventM e " +
                    "LEFT JOIN Venue v ON e.VenueID = v.VenueID " +
                    "WHERE e.CreatedByUserID = ? AND e.StatusTypeID = 1 " +
                    "ORDER BY e.StartDate ASC";
        
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
        String sql = "UPDATE EventM SET CurrentAttendees = " +
                    "(SELECT COUNT(*) FROM Registration WHERE EventID = ? AND RegistrationStatusTypeID = 2) " +
                    "WHERE EventID = ?";
        
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