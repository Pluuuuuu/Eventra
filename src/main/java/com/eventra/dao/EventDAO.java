package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Event;
import com.eventra.controller.CreateEventController.PresenterInfo;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class EventDAO {
    
    // ===== ATTENDEE-FOCUSED METHODS (from tfkk branch) =====
    
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
    
    // ===== ADMIN-FOCUSED METHODS (from tf branch) =====
    
    /**
     * Get all events created by a specific admin user
     */
    public static List<EventItem> getEventsForAdmin(int adminUserId) {
        List<EventItem> events = new ArrayList<>();
        String sql = "SELECT e.EventID, e.Title, e.Description, e.StartDate, e.EndDate, " +
                     "v.Name as VenueName, e.StatusTypeID, e.CreatedAt, " +
                     "COUNT(r.RegistrationID) as AttendeeCount " +
                     "FROM EventM e " +
                     "LEFT JOIN Venue v ON e.VenueID = v.VenueID " + 
                     "LEFT JOIN Registration r ON e.EventID = r.EventID " +
                     "WHERE e.CreatedByUserID = ? " +
                     "GROUP BY e.EventID, e.Title, e.Description, e.StartDate, e.EndDate, " +
                     "v.Name, e.StatusTypeID, e.CreatedAt " +
                     "ORDER BY e.StartDate DESC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, adminUserId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                EventItem event = new EventItem();
                event.setEventId(rs.getInt("EventID"));
                event.setTitle(rs.getString("Title"));
                event.setDescription(rs.getString("Description"));
                
                // Format date and time
                Timestamp startDate = rs.getTimestamp("StartDate");
                if (startDate != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
                    event.setDateTime(startDate.toLocalDateTime().format(formatter));
                } else {
                    event.setDateTime("TBD");
                }
                
                // Set location from venue
                String venueName = rs.getString("VenueName");
                event.setLocation(venueName != null ? venueName : "Online Event");
                
                // Map status type ID to readable status
                int statusTypeId = rs.getInt("StatusTypeID");
                switch (statusTypeId) {
                    case 1: event.setStatus("Draft"); break;
                    case 2: event.setStatus("Published"); break;
                    case 3: event.setStatus("Cancelled"); break;
                    case 4: event.setStatus("Completed"); break;
                    default: event.setStatus("Unknown");
                }
                
                // Set attendee count
                event.setAttendeeCount(rs.getInt("AttendeeCount"));
                
                events.add(event);
            }
            
            System.out.println("✅ Loaded " + events.size() + " events for admin user " + adminUserId);
            
        } catch (SQLException e) {
            System.err.println("❌ Error loading events for admin " + adminUserId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return events;
    }
    
    /**
     * Get all events (for SuperAdmin or system-wide view)
     */
    public static List<EventItem> getAllEventsForAdmin() {
        List<EventItem> events = new ArrayList<>();
        String sql = "SELECT e.EventID, e.Title, e.Description, e.StartDate, e.EndDate, " +
                     "v.Name as VenueName, e.StatusTypeID, e.CreatedAt, " +
                     "u.FirstName + ' ' + u.LastName as CreatedBy, " +
                     "COUNT(r.RegistrationID) as AttendeeCount " +
                     "FROM EventM e " +
                     "LEFT JOIN Venue v ON e.VenueID = v.VenueID " +
                     "LEFT JOIN UserM u ON e.CreatedByUserID = u.UserID " +
                     "LEFT JOIN Registration r ON e.EventID = r.EventID " +
                     "GROUP BY e.EventID, e.Title, e.Description, e.StartDate, e.EndDate, " +
                     "v.Name, e.StatusTypeID, e.CreatedAt, u.FirstName, u.LastName " +
                     "ORDER BY e.StartDate DESC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                EventItem event = new EventItem();
                event.setEventId(rs.getInt("EventID"));
                event.setTitle(rs.getString("Title"));
                event.setDescription(rs.getString("Description"));
                
                // Format date and time
                Timestamp startDate = rs.getTimestamp("StartDate");
                if (startDate != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
                    event.setDateTime(startDate.toLocalDateTime().format(formatter));
                } else {
                    event.setDateTime("TBD");
                }
                
                // Set location from venue
                String venueName = rs.getString("VenueName");
                event.setLocation(venueName != null ? venueName : "Online Event");
                
                // Map status type ID to readable status
                int statusTypeId = rs.getInt("StatusTypeID");
                switch (statusTypeId) {
                    case 1: event.setStatus("Draft"); break;
                    case 2: event.setStatus("Published"); break;
                    case 3: event.setStatus("Cancelled"); break;
                    case 4: event.setStatus("Completed"); break;
                    default: event.setStatus("Unknown");
                }
                
                // Set attendee count
                event.setAttendeeCount(rs.getInt("AttendeeCount"));
                
                events.add(event);
            }
            
            System.out.println("✅ Loaded " + events.size() + " total events");
            
        } catch (SQLException e) {
            System.err.println("❌ Error loading all events: " + e.getMessage());
            e.printStackTrace();
        }
        
        return events;
    }
    
    /**
     * Delete an event from the database by its ID
     */
    public static boolean deleteEvent(int eventId) {
        String sql = "DELETE FROM EventM WHERE EventID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Event with ID " + eventId + " deleted from database successfully");
                return true;
            } else {
                System.out.println("⚠️ No event found with ID " + eventId);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error deleting event with ID " + eventId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get all presenters from the database
     */
    public static List<PresenterInfo> getAllPresenters() {
        List<PresenterInfo> presenters = new ArrayList<>();
        String sql = "SELECT PresenterID, FirstName, LastName, Bio, ContactInfo " +
                     "FROM Presenter ORDER BY FirstName, LastName";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String fullName = firstName + (lastName != null && !lastName.trim().isEmpty() ? " " + lastName : "");
                
                String bio = rs.getString("Bio");
                String contactInfo = rs.getString("ContactInfo");
                
                // Parse title and company from ContactInfo if available
                String title = "";
                String company = "";
                if (contactInfo != null && contactInfo.contains(" - ")) {
                    String[] parts = contactInfo.split(" - ", 2);
                    company = parts[0].trim();
                    title = parts[1].trim();
                }
                
                presenters.add(new PresenterInfo(fullName, title, company, bio != null ? bio : ""));
            }
            
            System.out.println("✅ Loaded " + presenters.size() + " presenters from database");
            
        } catch (SQLException e) {
            System.err.println("❌ Error loading presenters: " + e.getMessage());
            e.printStackTrace();
        }
        
        return presenters;
    }
    
    // ===== HELPER METHODS =====
    
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
    
    /**
     * Inner class to represent an Event item for table display
     */
    public static class EventItem {
        private int eventId;
        private String title;
        private String description;
        private String dateTime;
        private String location;
        private String status;
        private int attendeeCount;
        
        public EventItem() {}
        
        public EventItem(int eventId, String title, String dateTime, String location, String status, int attendeeCount) {
            this.eventId = eventId;
            this.title = title;
            this.dateTime = dateTime;
            this.location = location;
            this.status = status;
            this.attendeeCount = attendeeCount;
        }
        
        // Getters and Setters
        public int getEventId() { return eventId; }
        public void setEventId(int eventId) { this.eventId = eventId; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getDateTime() { return dateTime; }
        public void setDateTime(String dateTime) { this.dateTime = dateTime; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public int getAttendeeCount() { return attendeeCount; }
        public void setAttendeeCount(int attendeeCount) { this.attendeeCount = attendeeCount; }
    }
}
