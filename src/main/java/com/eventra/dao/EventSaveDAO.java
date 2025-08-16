package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Session;
import com.eventra.model.User;
import com.eventra.util.SessionManager;
import com.eventra.controller.CreateEventController.PresenterInfo;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Event Save DAO (EventSaveDAO.java)
 * 
 * This class handles all the database operations related to saving and managing
 * events in the Eventra application. Think of it like a "event management office"
 * where events are created, updated, and organized in the system.
 * 
 * WHAT THIS CLASS DOES:
 * - Saves new events with all their details to the database
 * - Manages event sessions and presenter information
 * - Cleans up duplicate presenter records to maintain data quality
 * - Handles complex event creation workflows
 * - Provides feedback on save operations success or failure
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Event Persistence: Saving event information to the database permanently
 * - Data Integrity: Ensuring event data is accurate and consistent
 * - Presenter Management: Handling speaker information and preventing duplicates
 * - Session Organization: Managing individual event sessions and schedules
 * - Transaction Management: Ensuring all related data is saved together
 * 
 * BUSINESS VALUE:
 * - Enables event organizers to create and manage events efficiently
 * - Maintains data quality by preventing duplicate presenter entries
 * - Supports complex event structures with multiple sessions
 * - Provides reliable event creation and management capabilities
 * - Ensures event information is properly stored and accessible
 * 
 * REAL-WORLD ANALOGY:
 * - Like an event planning office that files event information
 * - Like a conference management system that organizes speakers and sessions
 * - Like a calendar system that saves event details permanently
 * - Like a filing cabinet that stores event documents and information
 * - Like an event database that maintains all event records
 */
public class EventSaveDAO {
    
    /**
     * cleanupDuplicatePresenters() - Remove Duplicate Presenter Records
     * 
     * This method identifies and removes duplicate presenter records from the
     * database. It's like cleaning up a contact list to remove duplicate
     * entries for the same person.
     * 
     * WHAT IT DOES:
     * - Scans the database for presenters with the same name
     * - Identifies duplicate entries based on first and last name combinations
     * - Removes duplicate records while keeping the first occurrence
     * - Verifies that cleanup was successful
     * - Reports detailed results of the cleanup process
     * 
     * USER EXPERIENCE:
     * - Event organizers get cleaner, more organized presenter lists
     * - System prevents confusion from duplicate presenter entries
     * - Data quality is maintained automatically
     * - Clear feedback on what was cleaned up
     * 
     * TECHNICAL PROCESS:
     * 1. Query database for presenters with duplicate names
     * 2. Display duplicate counts for review
     * 3. Execute cleanup query to remove duplicates
     * 4. Verify cleanup was successful
     * 5. Report results to the console
     * 
     * BUSINESS VALUE:
     * - Prevents confusion when selecting presenters for events
     * - Maintains professional appearance of presenter lists
     * - Reduces data storage requirements
     * - Improves system performance and reliability
     */
    public static void cleanupDuplicatePresenters() {
        // 
        // STEP 1: IDENTIFY DUPLICATE PRESENTERS - Find duplicate entries
        // 
        // First, we need to find all the presenters who appear multiple times
        // in the database. This is like looking through a contact list to find
        // people who are listed more than once.
        // 
        // THINK OF IT LIKE:
        // - Scanning a phone book for duplicate names
        // - Checking a guest list for repeated entries
        // - Reviewing a directory for duplicate listings
        // 
        String countSql = "SELECT FirstName, LastName, COUNT(*) as cnt " +
                         "FROM Presenter " +
                         "GROUP BY FirstName, LastName " +
                         "HAVING COUNT(*) > 1";
        
        try (Connection conn = Db.get();
             Statement stmt = conn.createStatement()) {
            
            // 
            // EXECUTE DUPLICATE SEARCH - Find all duplicates
            // 
            // This query searches the database for presenters who appear
            // multiple times. Think of it like searching for repeated names
            // in a contact list.
            // 
            // THINK OF IT LIKE:
            // - Running a search for duplicate entries
            // - Counting how many times each name appears
            // - Identifying which names have multiple records
            // 
            ResultSet rs = stmt.executeQuery(countSql);
            System.out.println("🔍 Duplicate presenters found:");
            boolean foundDuplicates = false;
            
            // 
            // DISPLAY DUPLICATE RESULTS - Show what was found
            // 
            // For each duplicate found, we display the name and how many
            // times it appears. This is like listing out all the duplicates
            // we found during our search.
            // 
            // THINK OF IT LIKE:
            // - Listing all duplicate names found
            // - Showing how many times each duplicate appears
            // - Creating a report of what needs to be cleaned up
            // 
            while (rs.next()) {
                foundDuplicates = true;
                System.out.println("  - " + rs.getString("FirstName") + " " + rs.getString("LastName") + 
                                 " appears " + rs.getInt("cnt") + " times");
            }
            rs.close();
            
            // 
            // CHECK IF DUPLICATES EXIST - Determine if cleanup is needed
            // 
            // If no duplicates were found, we can exit early since there's
            // nothing to clean up. This is like checking if there are any
            // duplicates before starting the cleanup process.
            // 
            // THINK OF IT LIKE:
            // - Checking if there's anything to clean up
            // - Exiting early if no work is needed
            // - Avoiding unnecessary processing
            // 
            if (!foundDuplicates) {
                System.out.println("✅ No duplicate presenters found");
                return;
            }
            
            // 
            // STEP 2: EXECUTE CLEANUP - Remove duplicate records
            // 
            // Now we execute a more aggressive cleanup that removes all
            // duplicate presenter records. This is like going through a
            // contact list and removing all but one entry for each person.
            // 
            // THINK OF IT LIKE:
            // - Removing duplicate entries from a list
            // - Keeping only the first occurrence of each name
            // - Cleaning up a messy contact database
            // 
            String deleteSql = "WITH DuplicatePresenters AS (" +
                             "SELECT PresenterID, " +
                             "ROW_NUMBER() OVER (PARTITION BY UPPER(TRIM(FirstName)), UPPER(TRIM(LastName)) ORDER BY PresenterID) as rn " +
                             "FROM Presenter" +
                             ") " +
                             "DELETE FROM Presenter WHERE PresenterID IN (" +
                             "SELECT PresenterID FROM DuplicatePresenters WHERE rn > 1" +
                             ")";
            
            // 
            // EXECUTE DELETE OPERATION - Remove duplicates from database
            // 
            // This actually removes the duplicate records from the database.
            // Think of it like executing a cleanup operation that deletes
            // all the duplicate entries we found.
            // 
            // THINK OF IT LIKE:
            // - Running a cleanup program
            // - Removing duplicate files from a computer
            // - Deleting redundant entries from a database
            // 
            int deletedCount = stmt.executeUpdate(deleteSql);
            if (deletedCount > 0) {
                System.out.println("🧹 Cleaned up " + deletedCount + " duplicate presenters from database");
            }
            
            // 
            // STEP 3: VERIFY CLEANUP - Confirm duplicates were removed
            // 
            // After cleanup, we need to verify that all duplicates were
            // successfully removed. This is like double-checking that our
            // cleanup operation worked correctly.
            // 
            // THINK OF IT LIKE:
            // - Double-checking our work
            // - Making sure the cleanup was complete
            // - Verifying the final result
            // 
            ResultSet verifyRs = stmt.executeQuery(countSql);
            System.out.println("🔍 After cleanup - remaining duplicates:");
            boolean stillHasDuplicates = false;
            
            // 
            // CHECK REMAINING DUPLICATES - Verify cleanup success
            // 
            // We run the same duplicate search again to see if any
            // duplicates remain. This confirms our cleanup was successful.
            // 
            // THINK OF IT LIKE:
            // - Double-checking our work
            // - Making sure the cleanup was complete
            // - Verifying the final result
            // 
            while (verifyRs.next()) {
                stillHasDuplicates = true;
                System.out.println("  - " + verifyRs.getString("FirstName") + " " + verifyRs.getString("LastName") + 
                                 " appears " + verifyRs.getInt("cnt") + " times");
            }
            verifyRs.close();
            
            // 
            // REPORT CLEANUP RESULTS - Confirm final status
            // 
            // Finally, we report whether the cleanup was completely
            // successful or if some duplicates still remain.
            // 
            // THINK OF IT LIKE:
            // - Giving a final status report
            // - Confirming the operation was successful
            // - Reporting any remaining issues
            // 
            if (!stillHasDuplicates) {
                System.out.println("✅ All duplicates successfully removed");
            }
            
        } catch (SQLException e) {
            // 
            // CLEANUP FAILED - Handle database errors
            // 
            // If something goes wrong during the cleanup process, we log
            // the error so developers can identify and fix the problem.
            // 
            // THINK OF IT LIKE:
            // - A cleanup operation that failed
            // - A system error during maintenance
            // - A problem that needs to be reported and fixed
            // 
            System.err.println("❌ Error cleaning up duplicate presenters: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * EventSaveResult - Result of Event Save Operation
     * 
     * This inner class provides detailed feedback about the result of
     * saving an event to the database. It's like a receipt that shows
     * whether an operation succeeded or failed.
     * 
     * WHAT IT CONTAINS:
     * - success: Whether the save operation was successful
     * - message: Detailed information about what happened
     * - eventId: The unique identifier of the saved event
     * 
     * USER EXPERIENCE:
     * - Event organizers get clear feedback on save operations
     * - System provides detailed information about any problems
     * - Success confirmation includes the new event ID
     * - Error messages help identify and fix problems
     * 
     * BUSINESS VALUE:
     * - Reduces confusion about whether events were saved
     * - Provides accountability for save operations
     * - Enables better error handling and user support
     * - Supports debugging and troubleshooting
     */
    public static class EventSaveResult {
        // 
        // SUCCESS STATUS - Whether the operation succeeded
        // 
        // This boolean indicates whether the event save operation was
        // successful. Think of it like a "pass/fail" indicator.
        // 
        // THINK OF IT LIKE:
        // - A success/failure flag
        // - A yes/no answer to "did it work?"
        // - A simple status indicator
        // 
        public boolean success;
        
        // 
        // DETAILED MESSAGE - Information about the operation
        // 
        // This string provides detailed information about what happened
        // during the save operation. Think of it like a detailed report.
        // 
        // THINK OF IT LIKE:
        // - A detailed explanation of what happened
        // - A report on the operation's success or failure
        // - Information about any problems encountered
        // 
        public String message;
        
        // 
        // EVENT IDENTIFIER - Unique ID of the saved event
        // 
        // This integer is the unique identifier assigned to the event
        // when it was saved to the database. Think of it like a
        // reference number for the saved event.
        // 
        // THINK OF IT LIKE:
        // - A unique reference number
        // - An ID card for the event
        // - A tracking number for the saved record
        // 
        public int eventId;
        
        // 
        // CONSTRUCTOR - Create result object
        // 
        // This constructor creates a new EventSaveResult object with
        // the specified values. Think of it like filling out a form
        // with the operation results.
        // 
        // @param success Whether the operation succeeded
        // @param message Detailed information about the operation
        // @param eventId Unique identifier of the saved event
        // 
        public EventSaveResult(boolean success, String message, int eventId) {
            this.success = success;
            this.message = message;
            this.eventId = eventId;
        }
    }
    
    /**
     * saveEvent(...) - Save Complete Event to Database
     * 
     * This method saves a complete event with all its details, sessions,
     * and presenter information to the database. It's like creating a
     * complete event file with all the necessary information.
     * 
     * WHAT IT DOES:
     * - Creates the main event record in the database
     * - Saves all event sessions and scheduling information
     * - Manages presenter information and relationships
     * - Handles the complete event creation workflow
     * - Returns detailed results of the save operation
     * 
     * USER EXPERIENCE:
     * - Event organizers can create complete events in one operation
     * - System handles all the complex database operations automatically
     * - Clear feedback on whether the event was saved successfully
     * - Detailed error messages if something goes wrong
     * 
     * TECHNICAL PROCESS:
     * 1. Validate event data and parameters
     * 2. Create main event record in EventM table
     * 3. Save event sessions and scheduling information
     * 4. Manage presenter information and relationships
     * 5. Return detailed results of the operation
     * 
     * @param title The title/name of the event
     * @param description Detailed description of the event
     * @param startDate When the event begins
     * @param endDate When the event ends
     * @return EventSaveResult with operation details
     */
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
            
            // Get current user and validate
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser == null) {
                return new EventSaveResult(false, "No user logged in. Please log in again.", -1);
            }
            
            int currentUserId = currentUser.getUserId();
            System.out.println("🔍 Current user ID: " + currentUserId);
            
            // Validate that the user exists in the database
            System.out.println("🔍 Validating user ID: " + currentUserId);
            if (!isValidUserId(conn, currentUserId)) {
                System.err.println("❌ Invalid user ID: " + currentUserId + ". Using fallback user.");
                // Try to find a valid admin user as fallback
                currentUserId = findValidAdminUserId(conn);
                if (currentUserId == -1) {
                    System.err.println("❌ No valid admin user found in database!");
                    return new EventSaveResult(false, "No valid user found for event creation. Please contact administrator.", -1);
                }
                System.out.println("✅ Using fallback user ID: " + currentUserId);
                
                // Update the session with the valid user ID
                updateSessionWithValidUser(currentUserId);
            } else {
                System.out.println("✅ User ID " + currentUserId + " is valid");
            }
            
            // 1. Get or create venue
            int venueId = getOrCreateVenue(conn, location, capacity);
            
            // 2. Save the main event
            System.out.println("🔍 Creating event with user ID: " + currentUserId);
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
            
            // If we used a fallback user, update the session to use that user
            if (currentUserId != SessionManager.getCurrentUser().getUserId()) {
                System.out.println("⚠️ Warning: Used fallback user ID " + currentUserId + 
                                 " instead of current user ID " + SessionManager.getCurrentUser().getUserId());
                System.out.println("💡 Tip: The created event will be associated with the fallback user");
            }
            
            return new EventSaveResult(true, message, eventId);
            
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            System.err.println("❌ Error saving event: " + e.getMessage());
            e.printStackTrace();
            
            // Provide more specific error messages
            String errorMessage;
            if (e.getMessage().contains("FK_EventM_CreatedBy")) {
                errorMessage = "Database constraint error: Invalid user reference. Please log in again.";
            } else if (e.getMessage().contains("foreign key constraint")) {
                errorMessage = "Database constraint error: Invalid reference. Please check your data.";
            } else {
                errorMessage = "Failed to save event: " + e.getMessage();
            }
            
            return new EventSaveResult(false, errorMessage, -1);
            
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
        
        System.out.println("🔍 SQL: INSERT INTO EventM with CreatedByUserID = " + createdByUserId);
        
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
                    System.out.println("✅ Created event: " + title + " (ID: " + eventId + ", CreatedBy: " + createdByUserId + ")");
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
    
    /**
     * Check if a user ID exists in the database
     */
    private static boolean isValidUserId(Connection conn, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM UserM WHERE UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    /**
     * Find a valid admin user ID as fallback
     */
    private static int findValidAdminUserId(Connection conn) throws SQLException {
        String sql = "SELECT TOP 1 UserID FROM UserM WHERE RoleTypeID IN (1, 2) AND StatusTypeID = 1 ORDER BY UserID";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("UserID");
            }
        }
        return -1;
    }
    
    /**
     * Update the session with a valid user ID
     */
    private static void updateSessionWithValidUser(int validUserId) {
        try {
            // Get the user details from the database
            String sql = "SELECT * FROM UserM WHERE UserID = ?";
            try (Connection conn = Db.get();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, validUserId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    // Create a new User object with the valid ID
                    User validUser = new User();
                    validUser.setUserId(rs.getInt("UserID"));
                    validUser.setUsername(rs.getString("Username"));
                    validUser.setFirstName(rs.getString("FirstName"));
                    validUser.setLastName(rs.getString("LastName"));
                    validUser.setEmail(rs.getString("Email"));
                    validUser.setRoleTypeId(rs.getInt("RoleTypeID"));
                    validUser.setStatusTypeId(rs.getInt("StatusTypeID"));
                    
                    // Update the session
                    SessionManager.setCurrentUser(validUser);
                    System.out.println("✅ Updated session with valid user: " + validUser.getEmail() + " (ID: " + validUserId + ")");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error updating session with valid user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}