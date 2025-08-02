package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Session;
import com.eventra.util.SessionManager;
import com.eventra.controller.CreateEventController.PresenterInfo;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EventSaveDAO {
    
    /**
     * Clean up duplicate presenters in the database
     * Keeps the first occurrence of each presenter (by FirstName + LastName combination)
     */
    public static void cleanupDuplicatePresenters() {
        // First, let's see what duplicates exist
        String countSql = "SELECT FirstName, LastName, COUNT(*) as cnt " +
                         "FROM Presenter " +
                         "GROUP BY FirstName, LastName " +
                         "HAVING COUNT(*) > 1";
        
        try (Connection conn = Db.get();
             Statement stmt = conn.createStatement()) {
            
            // Show duplicate counts
            ResultSet rs = stmt.executeQuery(countSql);
            System.out.println("🔍 Duplicate presenters found:");
            boolean foundDuplicates = false;
            while (rs.next()) {
                foundDuplicates = true;
                System.out.println("  - " + rs.getString("FirstName") + " " + rs.getString("LastName") + 
                                 " appears " + rs.getInt("cnt") + " times");
            }
            rs.close();
            
            if (!foundDuplicates) {
                System.out.println("✅ No duplicate presenters found");
                return;
            }
            
            // More aggressive cleanup - remove duplicates based on FirstName + LastName only
            String deleteSql = "WITH DuplicatePresenters AS (" +
                             "SELECT PresenterID, " +
                             "ROW_NUMBER() OVER (PARTITION BY UPPER(TRIM(FirstName)), UPPER(TRIM(LastName)) ORDER BY PresenterID) as rn " +
                             "FROM Presenter" +
                             ") " +
                             "DELETE FROM Presenter WHERE PresenterID IN (" +
                             "SELECT PresenterID FROM DuplicatePresenters WHERE rn > 1" +
                             ")";
            
            int deletedCount = stmt.executeUpdate(deleteSql);
            if (deletedCount > 0) {
                System.out.println("🧹 Cleaned up " + deletedCount + " duplicate presenters from database");
            }
            
            // Verify cleanup
            ResultSet verifyRs = stmt.executeQuery(countSql);
            System.out.println("🔍 After cleanup - remaining duplicates:");
            boolean stillHasDuplicates = false;
            while (verifyRs.next()) {
                stillHasDuplicates = true;
                System.out.println("  - " + verifyRs.getString("FirstName") + " " + verifyRs.getString("LastName") + 
                                 " appears " + verifyRs.getInt("cnt") + " times");
            }
            verifyRs.close();
            
            if (!stillHasDuplicates) {
                System.out.println("✅ All duplicates successfully removed");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error cleaning up duplicate presenters: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Save a complete event with sessions and presenters to the database
     */
    public static class EventSaveResult {
        public boolean success;
        public String message;
        public int eventId;
        
        public EventSaveResult(boolean success, String message, int eventId) {
            this.success = success;
            this.message = message;
            this.eventId = eventId;
        }
    }
    
    public static EventSaveResult saveEvent(
            String title, 
            String description, 
            LocalDate startDate, 
            LocalDate endDate, 
            String location,
            int capacity,
            List<Session> sessions,
            List<PresenterInfo> presenters,
            boolean isDraft
    ) {
        Connection conn = null;
        try {
            conn = Db.get();
            conn.setAutoCommit(false); // Start transaction
            
            int currentUserId = SessionManager.getCurrentUser().getUserId();
            
            // 1. Get or create venue
            int venueId = getOrCreateVenue(conn, location, capacity);
            
            // 2. Save the main event
            int eventId = saveMainEvent(conn, title, description, startDate, endDate, 
                                      venueId, currentUserId, isDraft);
            
            // 3. Save sessions if any
            if (sessions != null && !sessions.isEmpty()) {
                saveSessions(conn, eventId, sessions, currentUserId);
            }
            
            // 4. Save presenters if any
            if (presenters != null && !presenters.isEmpty()) {
                savePresenters(conn, presenters, currentUserId);
            }
            
            conn.commit(); // Commit transaction
            
            String message = isDraft ? "Event saved as draft successfully!" : 
                                     "Event published successfully!";
            System.out.println("✅ " + message + " (EventID: " + eventId + ")");
            
            return new EventSaveResult(true, message, eventId);
            
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            System.err.println("❌ Error saving event: " + e.getMessage());
            e.printStackTrace();
            return new EventSaveResult(false, "Failed to save event: " + e.getMessage(), -1);
            
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static int getOrCreateVenue(Connection conn, String location, int capacity) throws SQLException {
        // First try to find existing venue
        String selectSql = "SELECT VenueID FROM Venue WHERE Name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, location);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("VenueID");
                }
            }
        }
        
        // Create new venue if not found
        String insertSql = "INSERT INTO Venue (Name, Address, Capacity, Rooms, MapLink) " +
                          "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, location);
            stmt.setString(2, location); // Use same as address for now
            stmt.setInt(3, capacity > 0 ? capacity : 100); // Default capacity
            stmt.setInt(4, 1); // Default to 1 room
            stmt.setString(5, ""); // Empty map link for now
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int venueId = rs.getInt(1);
                    System.out.println("✅ Created new venue: " + location + " (ID: " + venueId + ")");
                    return venueId;
                }
            }
        }
        
        throw new SQLException("Failed to create or find venue");
    }
    
    private static int saveMainEvent(Connection conn, String title, String description,
                                   LocalDate startDate, LocalDate endDate, int venueId, 
                                   int createdByUserId, boolean isDraft) throws SQLException {
        
        String sql = "INSERT INTO EventM (Title, Description, StartDate, EndDate, VenueID, " +
                    "CreatedByUserID, StatusTypeID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            
            // Convert LocalDate to DateTime (assuming 9 AM start, 5 PM end)
            stmt.setTimestamp(3, Timestamp.valueOf(startDate.atTime(9, 0)));
            stmt.setTimestamp(4, Timestamp.valueOf(endDate.atTime(17, 0)));
            
            stmt.setInt(5, venueId);
            stmt.setInt(6, createdByUserId);
            stmt.setInt(7, isDraft ? 1 : 2); // 1 = Draft, 2 = Published (assuming from previous code)
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int eventId = rs.getInt(1);
                    System.out.println("✅ Created event: " + title + " (ID: " + eventId + ")");
                    return eventId;
                }
            }
        }
        
        throw new SQLException("Failed to create event, no ID obtained");
    }
    
    private static void saveSessions(Connection conn, int eventId, List<Session> sessions, 
                                   int createdByUserId) throws SQLException {
        
        String sql = "INSERT INTO SessionM (EventID, CreatedByUserID, Track, Title, Abstract, " +
                    "StartAt, EndAt, StatusTypeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Session session : sessions) {
                stmt.setInt(1, eventId);
                stmt.setInt(2, createdByUserId);
                stmt.setString(3, "General"); // Default track
                stmt.setString(4, session.getName());
                stmt.setString(5, "Session: " + session.getName()); // Default abstract
                
                // Parse session time and create datetime
                LocalDateTime sessionStart = parseSessionDateTime(session.getDate(), session.getTime());
                LocalDateTime sessionEnd = sessionStart.plusHours(1); // Default 1 hour duration
                
                stmt.setTimestamp(6, Timestamp.valueOf(sessionStart));
                stmt.setTimestamp(7, Timestamp.valueOf(sessionEnd));
                stmt.setInt(8, getSessionStatusId(session.getStatus()));
                
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            System.out.println("✅ Created " + results.length + " sessions for event " + eventId);
        }
    }
    
    private static LocalDateTime parseSessionDateTime(String date, String time) {
        try {
            // Try to parse the date (format like "15-Mar" or "2024-03-15")
            LocalDate sessionDate = LocalDate.now(); // Default to today
            
            if (date != null && !date.trim().isEmpty() && !date.equals("TBD")) {
                // Simple parsing - in real app you'd want more robust date parsing
                if (date.contains("-") && date.length() > 5) {
                    // Looks like a full date
                    sessionDate = LocalDate.parse(date);
                }
            }
            
            // Parse time (format like "09:00 AM" or "14:30")
            int hour = 9; // Default 9 AM
            int minute = 0;
            
            if (time != null && !time.trim().isEmpty() && !time.equals("TBD")) {
                // Simple time parsing
                String[] parts = time.replace(" AM", "").replace(" PM", "").split(":");
                if (parts.length >= 2) {
                    hour = Integer.parseInt(parts[0].trim());
                    minute = Integer.parseInt(parts[1].trim());
                    
                    // Handle PM
                    if (time.toUpperCase().contains("PM") && hour != 12) {
                        hour += 12;
                    }
                    // Handle 12 AM
                    if (time.toUpperCase().contains("AM") && hour == 12) {
                        hour = 0;
                    }
                }
            }
            
            return sessionDate.atTime(hour, minute);
            
        } catch (Exception e) {
            System.err.println("⚠️ Error parsing session date/time: " + date + " " + time + 
                             ". Using default.");
            return LocalDate.now().atTime(9, 0);
        }
    }
    
    private static int getSessionStatusId(String status) {
        // Map session status to database IDs
        // This should match your SessionStatusType table
        switch (status.toLowerCase()) {
            case "scheduled": return 1;
            case "in progress": return 2;
            case "completed": return 3;
            case "cancelled": return 4;
            default: return 1; // Default to scheduled
        }
    }
    
    private static void savePresenters(Connection conn, List<PresenterInfo> presenters, 
                                     int createdByUserId) throws SQLException {
        
        String checkSql = "SELECT PresenterID FROM Presenter WHERE UPPER(TRIM(FirstName)) = UPPER(TRIM(?)) AND UPPER(TRIM(LastName)) = UPPER(TRIM(?))";
        String insertSql = "INSERT INTO Presenter (FirstName, LastName, Bio, Email, ContactInfo, CreatedByUserID) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";
        
        int newPresentersCount = 0;
        int existingPresentersCount = 0;
        
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            
            for (PresenterInfo presenter : presenters) {
                if (presenter == null) continue;
                
                // Parse the full name into first and last name
                String[] nameParts = presenter.getName().trim().split("\\s+", 2);
                String firstName = nameParts[0].trim();
                String lastName = nameParts.length > 1 ? nameParts[1].trim() : "";
                String contactInfo = presenter.getCompany() + " - " + presenter.getTitle();
                
                // Check if presenter already exists (case-insensitive, trimmed comparison)
                checkStmt.setString(1, firstName);
                checkStmt.setString(2, lastName);
                
                System.out.println("🔍 Checking for existing presenter: '" + firstName + " " + lastName + "'");
                
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // Presenter already exists, skip insertion
                    existingPresentersCount++;
                    System.out.println("⚠️ Presenter already exists: " + firstName + " " + lastName);
                } else {
                    System.out.println("✅ New presenter, will insert: " + firstName + " " + lastName);
                    // Presenter doesn't exist, insert new record
                    insertStmt.setString(1, firstName);
                    insertStmt.setString(2, lastName);
                    insertStmt.setString(3, presenter.getBio());
                    // Generate unique placeholder email since Email column has UNIQUE constraint
                    String placeholderEmail = firstName.toLowerCase() + "." + lastName.toLowerCase() + 
                                            "." + System.currentTimeMillis() + "@eventra.placeholder";
                    insertStmt.setString(4, placeholderEmail);
                    insertStmt.setString(5, contactInfo); // Store as contact info
                    insertStmt.setInt(6, createdByUserId);
                    
                    insertStmt.addBatch();
                    newPresentersCount++;
                }
                rs.close();
            }
            
            // Execute batch insert for new presenters only
            if (newPresentersCount > 0) {
                int[] results = insertStmt.executeBatch();
                System.out.println("✅ Created " + results.length + " new presenters");
            }
            
            if (existingPresentersCount > 0) {
                System.out.println("ℹ️ Skipped " + existingPresentersCount + " existing presenters");
            }
        }
    }
}