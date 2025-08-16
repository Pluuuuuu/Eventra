package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Attendee;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Attendee Data Access Object (AttendeeDAO.java)
 * 
 * This class handles all database operations related to event attendees
 * in the Eventra system. Think of it like a "registration desk" that manages
 * all the information about people who attend events, including their profiles,
 * contact details, and account settings.
 * 
 * WHAT THIS CLASS DOES:
 * - Creates new attendee profiles in the database
 * - Checks if attendee information already exists
 * - Retrieves attendee details by email or other criteria
 * - Updates attendee information when it changes
 * - Manages attendee account status and settings
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Data Access Object (DAO): A class that handles database operations
 * - Database Operations: Creating, reading, updating, and deleting attendee records
 * - Data Validation: Checking for duplicate information and data integrity
 * - Account Management: Handling attendee profiles and settings
 * - Error Handling: Gracefully managing database problems
 * 
 * BUSINESS VALUE:
 * - Enables users to create attendee profiles for events
 * - Prevents duplicate attendee accounts
 * - Manages attendee information and preferences
 * - Supports event registration and attendance tracking
 * - Provides data for attendee management and communication
 * 
 * REAL-WORLD ANALOGY:
 * - Like a registration desk at a conference that creates attendee badges
 * - Like a membership system that manages member profiles
 * - Like a customer database that tracks customer information
 * - Like a student registration system that manages student records
 */
public class AttendeeDAO {

    /**
     * createAttendee(Attendee attendee) - Create a New Attendee Profile
     * 
     * This method creates a new attendee record in the database with all
     * the information provided. It's like filling out a complete registration
     * form and creating a new attendee profile in the system.
     * 
     * WHAT IT DOES:
     * - Takes a complete Attendee object with all profile information
     * - Inserts the attendee data into the database
     * - Automatically generates a unique attendee ID
     * - Sets creation and update timestamps
     * - Returns success/failure status
     * 
     * USER EXPERIENCE:
     * - New users can create complete attendee profiles
     * - Event organizers can add attendees to their system
     * - Attendee information is stored securely in the database
     * - Each attendee gets a unique identifier for tracking
     * 
     * TECHNICAL PROCESS:
     * 1. Prepares SQL insert statement with all attendee fields
     * 2. Connects to database safely
     * 3. Sets all attendee data as SQL parameters
     * 4. Executes the insert operation
     * 5. Retrieves the generated attendee ID
     * 6. Returns success/failure status
     * 
     * @param attendee The complete Attendee object with all profile information
     * @return true if attendee was created successfully, false otherwise
     */
    public static boolean createAttendee(Attendee attendee) {
        // 
        // SQL INSERT STATEMENT - Prepare to add new attendee to database
        // 
        // This SQL statement will insert all the attendee information into
        // the database, creating a new attendee record.
        // 
        // THINK OF IT LIKE:
        // - Filling out a complete registration form
        // - Creating a new entry in a contact database
        // - Adding a new member to a membership system
        // 
        String sql = "INSERT INTO Attendee (UserID, FirstName, MiddleName, LastName, Email, Organization, " +
                    "Phone, Location, Gender, DateOfBirth, ProfilePicUrl, Type, PasswordHash, " +
                    "StatusTypeID, PeriodCanLoginInMinutes, CreatedAt, UpdatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
        // 
        // DATABASE OPERATION - Execute the insert and handle results
        // 
        // This section safely connects to the database, executes the insert
        // statement, and handles the results including the generated attendee ID.
        // 
        // THINK OF IT LIKE:
        // - Submitting a completed registration form
        // - Processing a new membership application
        // - Creating a new customer account
        // 
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // 
            // SET ATTENDEE DATA - Fill in all the profile information
            // 
            // This section takes all the attendee information and sets it
            // as parameters in the SQL statement, ensuring data safety.
            // 
            // THINK OF IT LIKE:
            // - Filling in all the fields on a registration form
            // - Entering all the details for a new contact
            // - Providing complete information for a new account
            // 
            stmt.setInt(1, attendee.getUserId());
            stmt.setString(2, attendee.getFirstName());
            stmt.setString(3, attendee.getMiddleName());
            stmt.setString(4, attendee.getLastName());
            stmt.setString(5, attendee.getEmail());
            stmt.setString(6, attendee.getOrganization());
            stmt.setString(7, attendee.getPhone());
            stmt.setString(8, attendee.getLocation());
            stmt.setString(9, attendee.getGender());
            
            // 
            // HANDLE DATE CONVERSION - Convert Java date to database date format
            // 
            // This section safely converts the Java LocalDate object to a
            // database Date format, handling cases where the date might be null.
            // 
            // THINK OF IT LIKE:
            // - Converting between different calendar formats
            // - Translating date information to the right format
            // - Ensuring date data is properly formatted for storage
            // 
            if (attendee.getDateOfBirth() != null) {
                stmt.setDate(10, Date.valueOf(attendee.getDateOfBirth()));
            } else {
                stmt.setNull(10, Types.DATE);
            }
            
            // 
            // SET REMAINING FIELDS - Complete the attendee profile
            // 
            // This sets the remaining attendee information including profile
            // picture, type, password, status, and login preferences.
            // 
            // THINK OF IT LIKE:
            // - Completing the final sections of a registration form
            // - Adding the last details to a profile
            // - Finishing the account setup process
            // 
            stmt.setString(11, attendee.getProfilePicUrl());
            stmt.setString(12, attendee.getType());
            stmt.setString(13, attendee.getPasswordHash());
            stmt.setInt(14, attendee.getStatusTypeId());
            stmt.setInt(15, attendee.getPeriodCanLoginInMinutes());
            
            // 
            // EXECUTE INSERT - Add the attendee to the database
            // 
            // This runs the SQL insert statement and returns how many
            // database rows were affected by the operation.
            // 
            // THINK OF IT LIKE:
            // - Submitting a form and seeing if it was accepted
            // - Creating an account and checking if it was successful
            // - Adding a record and confirming it was saved
            // 
            int affectedRows = stmt.executeUpdate();
            
            // 
            // HANDLE SUCCESS - Get the generated attendee ID
            // 
            // If the insert was successful, we retrieve the automatically
            // generated attendee ID and update the attendee object.
            // 
            // THINK OF IT LIKE:
            // - Getting a confirmation number after successful registration
            // - Receiving an account ID after creating an account
            // - Getting a reference number after adding a record
            // 
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    attendee.setAttendeeId(generatedKeys.getInt(1));
                    return true;
                }
            }
            
        } catch (SQLException e) {
            // 
            // ERROR HANDLING - Log and report any database problems
            // 
            // If something goes wrong with the database operation, we log
            // detailed error information so developers can fix it.
            // 
            // THINK OF IT LIKE:
            // - A car's check engine light that shows detailed error codes
            // - A system that provides specific error information
            // - A detailed error report that helps solve problems
            // 
            System.err.println("Error creating attendee: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        
        // 
        // RETURN FAILURE - Indicate the operation was not successful
        // 
        // If we reach this point, something went wrong with the
        // attendee creation process.
        // 
        // THINK OF IT LIKE:
        // - Saying "sorry, we couldn't create your account"
        // - Indicating that the registration process failed
        // - Returning an error status when something goes wrong
        // 
        return false;
    }

    /**
     * emailExists(String email) - Check if Email is Already Registered
     * 
     * This method checks if an email address is already being used by
     * another attendee in the system. It's like checking if someone
     * has already registered with that email address.
     * 
     * WHAT IT DOES:
     * - Searches the database for the specified email address
     * - Returns true if the email is already registered
     * - Returns false if the email is available
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Prevents duplicate attendee accounts with the same email
     * - Helps users know if they need to use a different email
     * - Ensures each attendee has a unique email address
     * - Supports account recovery and login processes
     * 
     * TECHNICAL PROCESS:
     * 1. Connects to database safely
     * 2. Runs SQL query to count matching email addresses
     * 3. Returns true if count > 0, false otherwise
     * 4. Handles any database errors gracefully
     * 
     * @param email The email address to check for duplicates
     * @return true if email already exists, false if it's available
     */
    public static boolean emailExists(String email) {
        // 
        // SQL QUERY - Count how many attendees have this email
        // 
        // This SQL statement counts how many attendee records in the database
        // have the specified email address.
        // 
        // THINK OF IT LIKE:
        // - Looking through a contact list to see if an email is already there
        // - Checking a membership database for duplicate email addresses
        // - Searching for existing accounts with the same email
        // 
        String sql = "SELECT COUNT(*) FROM Attendee WHERE Email = ?";
        
        // 
        // DATABASE OPERATION - Execute the query and check results
        // 
        // This section safely connects to the database, runs the count query,
        // and processes the result to determine if the email exists.
        // 
        // THINK OF IT LIKE:
        // - Running a search to see if something already exists
        // - Checking a database for duplicate information
        // - Verifying if an email is already in use
        // 
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET EMAIL PARAMETER - Specify which email to search for
            // 
            // This replaces the "?" placeholder in the SQL query with the
            // actual email address we want to check.
            // 
            // THINK OF IT LIKE:
            // - Filling in a search form with a specific email address
            // - Replacing a placeholder with the actual value
            // - Specifying exactly which email to look for
            // 
            stmt.setString(1, email);
            
            // 
            // EXECUTE QUERY - Run the search and get the count
            // 
            // This runs the SQL query and returns a result set containing
            // the count of matching email addresses.
            // 
            // THINK OF IT LIKE:
            // - Submitting a search and getting back how many matches were found
            // - Running a check and seeing the results
            // - Looking up information and getting a count
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // CHECK RESULTS - See if any matching emails were found
            // 
            // This checks the count result and returns true if any
            // attendees have this email address.
            // 
            // THINK OF IT LIKE:
            // - Checking if your search found any matches
            // - Seeing if there are any duplicate email addresses
            // - Verifying whether the email is already in use
            // 
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            // 
            // ERROR HANDLING - Log and report any database problems
            // 
            // If something goes wrong with the database operation, we log
            // the error so developers can fix it, but don't crash the application.
            // 
            // THINK OF IT LIKE:
            // - A car's check engine light that alerts you to problems
            // - A website that shows an error message instead of crashing
            // - A system that gracefully handles problems and reports them
            // 
            System.err.println("Error checking if attendee email exists: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN FALSE ON ERROR - Assume email is available if there's a problem
        // 
        // If there was a database error, we return false to indicate
        // the email is available, allowing the process to continue.
        // 
        // THINK OF IT LIKE:
        // - Saying "let's assume it's okay" when we can't check
        // - Giving the benefit of the doubt when there are technical problems
        // - Allowing the process to continue even with checking errors
        // 
        return false;
    }

    /**
     * getAttendeeByEmail(String email) - Find Attendee by Email Address
     * 
     * This method finds an attendee in the database using their email address.
     * It's like looking up someone in a contact list by their email address
     * to get their complete profile information.
     * 
     * WHAT IT DOES:
     * - Searches the database for an attendee with the specified email
     * - Only returns active attendees (StatusTypeID = 1)
     * - Returns complete attendee profile information
     * - Returns null if no matching attendee is found
     * 
     * USER EXPERIENCE:
     * - Users can look up their attendee profile using their email
     * - Event organizers can find attendee information
     * - Supports login and account recovery processes
     * - Enables attendee profile updates and management
     * 
     * TECHNICAL PROCESS:
     * 1. Connects to database safely
     * 2. Runs SQL query to find attendee by email
     * 3. Filters for active attendees only
     * 4. Returns attendee object or null if not found
     * 
     * @param email The email address to search for
     * @return Attendee object if found, null if not found
     */
    public static Attendee getAttendeeByEmail(String email) {
        // 
        // SQL QUERY - Find attendee with matching email and active status
        // 
        // This SQL statement searches for an attendee with the specified
        // email address, but only returns active attendees.
        // 
        // THINK OF IT LIKE:
        // - Looking up a contact by email address
        // - Searching a membership database for an active member
        // - Finding a customer account using their email
        // 
        String sql = "SELECT * FROM Attendee WHERE Email = ? AND StatusTypeID = 1";
        
        // 
        // DATABASE OPERATION - Execute the query and get results
        // 
        // This section safely connects to the database, runs the search query,
        // and processes the result to find the attendee.
        // 
        // THINK OF IT LIKE:
        // - Running a search to find someone's profile
        // - Looking up information in a database
        // - Finding a specific record using search criteria
        // 
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET EMAIL PARAMETER - Specify which email to search for
            // 
            // This replaces the "?" placeholder in the SQL query with the
            // actual email address we want to find.
            // 
            // THINK OF IT LIKE:
            // - Filling in a search form with a specific email address
            // - Replacing a placeholder with the actual value
            // - Specifying exactly which email to look for
            // 
            stmt.setString(1, email);
            
            // 
            // EXECUTE QUERY - Run the search and get the result
            // 
            // This runs the SQL query and returns a result set containing
            // the attendee information if found.
            // 
            // THINK OF IT LIKE:
            // - Submitting a search and getting back the results
            // - Looking up information and finding a match
            // - Searching a database and getting back data
            // 
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Attendee attendee = new Attendee();
                attendee.setAttendeeId(rs.getInt("AttendeeID"));
                attendee.setUserId(rs.getInt("UserID"));
                attendee.setFirstName(rs.getString("FirstName"));
                attendee.setMiddleName(rs.getString("MiddleName"));
                attendee.setLastName(rs.getString("LastName"));
                attendee.setEmail(rs.getString("Email"));
                attendee.setOrganization(rs.getString("Organization"));
                attendee.setPhone(rs.getString("Phone"));
                attendee.setLocation(rs.getString("Location"));
                attendee.setGender(rs.getString("Gender"));
                
                Date dob = rs.getDate("DateOfBirth");
                if (dob != null) {
                    attendee.setDateOfBirth(dob.toLocalDate());
                }
                
                attendee.setProfilePicUrl(rs.getString("ProfilePicUrl"));
                attendee.setType(rs.getString("Type"));
                attendee.setPasswordHash(rs.getString("PasswordHash"));
                attendee.setStatusTypeId(rs.getInt("StatusTypeID"));
                attendee.setPeriodCanLoginInMinutes(rs.getInt("PeriodCanLoginInMinutes"));
                
                // Handle timestamp conversion
                Timestamp lastFailed = rs.getTimestamp("LastFailedLoginAt");
                if (lastFailed != null) {
                    attendee.setLastFailedLoginAt(lastFailed.toLocalDateTime());
                }
                
                Timestamp created = rs.getTimestamp("CreatedAt");
                if (created != null) {
                    attendee.setCreatedAt(created.toLocalDateTime());
                }
                
                Timestamp updated = rs.getTimestamp("UpdatedAt");
                if (updated != null) {
                    attendee.setUpdatedAt(updated.toLocalDateTime());
                }
                
                return attendee;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting attendee by email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Get attendee by ID
     */
    public static Attendee getAttendeeById(int attendeeId) {
        String sql = "SELECT * FROM Attendee WHERE AttendeeID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, attendeeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Attendee attendee = new Attendee();
                attendee.setAttendeeId(rs.getInt("AttendeeID"));
                attendee.setUserId(rs.getInt("UserID"));
                attendee.setFirstName(rs.getString("FirstName"));
                attendee.setMiddleName(rs.getString("MiddleName"));
                attendee.setLastName(rs.getString("LastName"));
                attendee.setEmail(rs.getString("Email"));
                attendee.setOrganization(rs.getString("Organization"));
                attendee.setPhone(rs.getString("Phone"));
                attendee.setLocation(rs.getString("Location"));
                attendee.setGender(rs.getString("Gender"));
                
                Date dob = rs.getDate("DateOfBirth");
                if (dob != null) {
                    attendee.setDateOfBirth(dob.toLocalDate());
                }
                
                attendee.setProfilePicUrl(rs.getString("ProfilePicUrl"));
                attendee.setType(rs.getString("Type"));
                attendee.setPasswordHash(rs.getString("PasswordHash"));
                attendee.setStatusTypeId(rs.getInt("StatusTypeID"));
                attendee.setPeriodCanLoginInMinutes(rs.getInt("PeriodCanLoginInMinutes"));
                
                // Handle timestamp conversion
                Timestamp lastFailed = rs.getTimestamp("LastFailedLoginAt");
                if (lastFailed != null) {
                    attendee.setLastFailedLoginAt(lastFailed.toLocalDateTime());
                }
                
                Timestamp created = rs.getTimestamp("CreatedAt");
                if (created != null) {
                    attendee.setCreatedAt(created.toLocalDateTime());
                }
                
                Timestamp updated = rs.getTimestamp("UpdatedAt");
                if (updated != null) {
                    attendee.setUpdatedAt(updated.toLocalDateTime());
                }
                
                return attendee;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting attendee by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Update attendee information
     */
    public static boolean updateAttendee(Attendee attendee) {
        String sql = "UPDATE Attendee SET FirstName = ?, MiddleName = ?, LastName = ?, " +
                    "Organization = ?, Phone = ?, Location = ?, Gender = ?, DateOfBirth = ?, " +
                    "ProfilePicUrl = ?, Type = ?, UpdatedAt = CURRENT_TIMESTAMP " +
                    "WHERE AttendeeID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, attendee.getFirstName());
            stmt.setString(2, attendee.getMiddleName());
            stmt.setString(3, attendee.getLastName());
            stmt.setString(4, attendee.getOrganization());
            stmt.setString(5, attendee.getPhone());
            stmt.setString(6, attendee.getLocation());
            stmt.setString(7, attendee.getGender());
            
            if (attendee.getDateOfBirth() != null) {
                stmt.setDate(8, Date.valueOf(attendee.getDateOfBirth()));
            } else {
                stmt.setNull(8, Types.DATE);
            }
            
            stmt.setString(9, attendee.getProfilePicUrl());
            stmt.setString(10, attendee.getType());
            stmt.setInt(11, attendee.getAttendeeId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating attendee: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
} 