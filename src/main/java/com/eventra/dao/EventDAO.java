package com.eventra.dao;

import com.eventra.Db;
import com.eventra.controller.CreateEventController.PresenterInfo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class EventDAO {
    
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
    public static List<EventItem> getAllEvents() {
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