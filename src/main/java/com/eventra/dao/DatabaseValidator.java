package com.eventra.dao;

import com.eventra.Db;
import java.sql.*;

/**
 * Database Validator (DatabaseValidator.java)
 * 
 * This class performs comprehensive health checks on the Eventra database
 * to ensure everything is working correctly. Think of it like a "system
 * doctor" that examines the database and reports on its health and status.
 * 
 * WHAT THIS CLASS DOES:
 * - Checks if all required database tables exist
 * - Verifies that tables contain the expected data
 * - Validates foreign key relationships and constraints
 * - Reports on database structure and content
 * - Identifies potential problems before they cause issues
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Database Validation: Checking if the database is properly set up
 * - Table Existence: Verifying that required tables are present
 * - Data Integrity: Ensuring tables contain the expected information
 * - Foreign Key Constraints: Checking relationships between tables
 * - System Health Monitoring: Proactively identifying problems
 * 
 * BUSINESS VALUE:
 * - Prevents application crashes due to database problems
 * - Helps developers identify and fix database issues
 * - Ensures data consistency and reliability
 * - Provides visibility into database health and status
 * - Supports system maintenance and troubleshooting
 * 
 * REAL-WORLD ANALOGY:
 * - Like a car's diagnostic system that checks all components
 * - Like a health checkup that examines all body systems
 * - Like a building inspector who verifies structural integrity
 * - Like a quality control system that checks product standards
 */
public class DatabaseValidator {
    
    /**
     * validateDatabase() - Perform Complete Database Health Check
     * 
     * This method runs a comprehensive validation of the entire database
     * structure and content. It's like running a full system diagnostic
     * to make sure everything is working properly.
     * 
     * WHAT IT DOES:
     * - Connects to the database safely
     * - Checks all major tables for existence and data
     * - Validates foreign key relationships
     * - Reports detailed findings to the console
     * - Handles any errors gracefully
     * 
     * USER EXPERIENCE:
     * - Developers can quickly identify database problems
     * - System administrators can monitor database health
     * - Users get reliable application performance
     * - Problems are caught before they affect users
     * 
     * TECHNICAL PROCESS:
     * 1. Establishes database connection
     * 2. Validates UserM table structure and content
     * 3. Validates EventM table structure and constraints
     * 4. Checks foreign key relationships
     * 5. Reports comprehensive results
     * 
     * USAGE:
     * - Call this method during system startup
     * - Run periodically to monitor database health
     * - Use for troubleshooting database issues
     * - Include in automated testing and deployment
     */
    public static void validateDatabase() {
        // 
        // START VALIDATION PROCESS - Begin comprehensive database check
        // 
        // This announces the start of the validation process and prepares
        // to check all aspects of the database.
        // 
        // THINK OF IT LIKE:
        // - Starting a comprehensive health checkup
        // - Beginning a system diagnostic procedure
        // - Launching a quality control inspection
        // 
        System.out.println("🔍 Validating database structure...");
        
        // 
        // DATABASE CONNECTION - Safely connect to the database
        // 
        // This establishes a connection to the database that will be used
        // for all validation checks. The try-with-resources ensures the
        // connection is properly closed when done.
        // 
        // THINK OF IT LIKE:
        // - Opening a door to access the database
        // - Establishing a communication line to the system
        // - Creating a secure pathway for validation
        // 
        try (Connection conn = Db.get()) {
            
            // 
            // VALIDATE USER TABLE - Check user management system
            // 
            // This validates the UserM table which stores all user account
            // information. It's a critical table for the application.
            // 
            // THINK OF IT LIKE:
            // - Checking if the user directory exists and has people in it
            // - Verifying that the membership system is properly set up
            // - Ensuring the user management foundation is solid
            // 
            validateUserMTable(conn);
            
            // 
            // VALIDATE EVENT TABLE - Check event management system
            // 
            // This validates the EventM table which stores all event
            // information. It's essential for the core functionality.
            // 
            // THINK OF IT LIKE:
            // - Checking if the event calendar system is properly set up
            // - Verifying that the event management foundation exists
            // - Ensuring events can be created and managed
            // 
            validateEventMTable(conn);
            
            // 
            // VALIDATE RELATIONSHIPS - Check table connections
            // 
            // This validates that tables are properly connected through
            // foreign key relationships, ensuring data consistency.
            // 
            // THINK OF IT LIKE:
            // - Checking if all the pieces of a puzzle fit together
            // - Verifying that all the connections in a network work
            // - Ensuring that related information stays synchronized
            // 
            validateForeignKeys(conn);
            
            // 
            // VALIDATION COMPLETE - Report successful completion
            // 
            // This announces that all validation checks have completed
            // successfully, indicating the database is healthy.
            // 
            // THINK OF IT LIKE:
            // - Getting a clean bill of health from a doctor
            // - Receiving a passing grade on a system inspection
            // - Confirming that everything is working properly
            // 
            System.out.println("✅ Database validation completed");
            
        } catch (Exception e) {
            // 
            // VALIDATION FAILED - Report and log any problems
            // 
            // If something goes wrong during validation, we log the error
            // so developers can identify and fix the problem.
            // 
            // THINK OF IT LIKE:
            // - A doctor reporting what went wrong during a checkup
            // - A system diagnostic that shows error details
            // - A quality control report that identifies problems
            // 
            System.err.println("❌ Database validation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * validateUserMTable(Connection conn) - Check User Management Table
     * 
     * This private method validates the UserM table which stores all
     * user account information. It's like checking if the user directory
     * exists and contains the expected information.
     * 
     * WHAT IT DOES:
     * - Verifies the UserM table exists in the database
     * - Counts how many user records are stored
     * - Lists all users with their basic information
     * - Reports on table health and content
     * 
     * USER EXPERIENCE:
     * - Developers can see if user accounts are properly set up
     * - System administrators can monitor user database health
     * - Problems with user management are quickly identified
     * - User data integrity is verified
     * 
     * TECHNICAL PROCESS:
     * 1. Checks if UserM table exists using system tables
     * 2. Counts total user records in the table
     * 3. Lists all users with their key information
     * 4. Reports findings to the console
     * 
     * @param conn The database connection to use for validation
     * @throws SQLException If there's an error during validation
     */
    private static void validateUserMTable(Connection conn) throws SQLException {
        // 
        // START USER TABLE VALIDATION - Begin checking user management system
        // 
        // This announces the start of UserM table validation and prepares
        // to check all aspects of the user management system.
        // 
        // THINK OF IT LIKE:
        // - Starting to check a specific part of a building
        // - Beginning an examination of a particular system
        // - Launching a focused inspection of user data
        // 
        System.out.println("🔍 Checking UserM table...");
        
        // 
        // CHECK TABLE EXISTENCE - Verify the table is present
        // 
        // This checks if the UserM table actually exists in the database
        // by querying the system's table registry.
        // 
        // THINK OF IT LIKE:
        // - Checking if a file exists in a filing cabinet
        // - Verifying that a room exists in a building
        // - Confirming that a component is present in a system
        // 
        String checkTableSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'UserM'";
        try (PreparedStatement stmt = conn.prepareStatement(checkTableSql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("✅ UserM table exists");
            } else {
                System.err.println("❌ UserM table does not exist!");
                return; // Can't continue validation if table is missing
            }
        }
        
        // 
        // COUNT USER RECORDS - Check how many users are stored
        // 
        // This counts the total number of user records in the UserM table
        // to ensure the system has user data to work with.
        // 
        // THINK OF IT LIKE:
        // - Counting how many people are in a directory
        // - Checking how many members are in a club
        // - Verifying how many accounts exist in a system
        // 
        String countSql = "SELECT COUNT(*) FROM UserM";
        try (PreparedStatement stmt = conn.prepareStatement(countSql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("📊 UserM table has " + count + " users");
                
                // 
                // WARN IF TABLE IS EMPTY - Alert about potential problems
                // 
                // If the table has no users, this could indicate a problem
                // with the system setup or data loading.
                // 
                // THINK OF IT LIKE:
                // - Warning that a directory is empty
                // - Alerting that a system has no users
                // - Notifying that a database is missing essential data
                // 
                if (count == 0) {
                    System.err.println("⚠️ Warning: UserM table is empty!");
                }
            }
        }
        
        // 
        // LIST ALL USERS - Show detailed user information
        // 
        // This retrieves and displays information about all users in the
        // system, providing visibility into the user database content.
        // 
        // THINK OF IT LIKE:
        // - Listing all people in a directory
        // - Showing all members of an organization
        // - Displaying all accounts in a system
        // 
        String listSql = "SELECT UserID, Username, Email, RoleTypeID FROM UserM ORDER BY UserID";
        try (PreparedStatement stmt = conn.prepareStatement(listSql)) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("👥 Users in database:");
            while (rs.next()) {
                int userId = rs.getInt("UserID");
                String username = rs.getString("Username");
                String email = rs.getString("Email");
                int roleId = rs.getInt("RoleTypeID");
                System.out.println("  - ID: " + userId + ", Username: " + username + ", Email: " + email + ", Role: " + roleId);
            }
        }
    }
    
    /**
     * validateEventMTable(Connection conn) - Check Event Management Table
     * 
     * This private method validates the EventM table which stores all
     * event information. It's like checking if the event calendar system
     * is properly set up and configured.
     * 
     * WHAT IT DOES:
     * - Verifies the EventM table exists in the database
     * - Checks for required foreign key constraints
     * - Validates table structure and relationships
     * - Reports on event table health
     * 
     * USER EXPERIENCE:
     * - Developers can see if event management is properly configured
     * - System administrators can monitor event database health
     * - Problems with event creation are quickly identified
     * - Event data integrity is verified
     * 
     * TECHNICAL PROCESS:
     * 1. Checks if EventM table exists using system tables
     * 2. Validates foreign key constraints to UserM table
     * 3. Reports findings to the console
     * 
     * @param conn The database connection to use for validation
     * @throws SQLException If there's an error during validation
     */
    private static void validateEventMTable(Connection conn) throws SQLException {
        // 
        // START EVENT TABLE VALIDATION - Begin checking event management system
        // 
        // This announces the start of EventM table validation and prepares
        // to check all aspects of the event management system.
        // 
        // THINK OF IT LIKE:
        // - Starting to check a specific part of a building
        // - Beginning an examination of a particular system
        // - Launching a focused inspection of event data
        // 
        System.out.println("🔍 Checking EventM table...");
        
        // 
        // CHECK TABLE EXISTENCE - Verify the table is present
        // 
        // This checks if the EventM table actually exists in the database
        // by querying the system's table registry.
        // 
        // THINK OF IT LIKE:
        // - Checking if a file exists in a filing cabinet
        // - Verifying that a room exists in a building
        // - Confirming that a component is present in a system
        // 
        String checkTableSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EventM'";
        try (PreparedStatement stmt = conn.prepareStatement(checkTableSql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("✅ EventM table exists");
            } else {
                System.err.println("❌ EventM table does not exist!");
                return; // Can't continue validation if table is missing
            }
        }
        
        // 
        // CHECK FOREIGN KEY CONSTRAINTS - Verify table relationships
        // 
        // This checks if the EventM table is properly connected to the
        // UserM table through foreign key constraints, ensuring data integrity.
        // 
        // THINK OF IT LIKE:
        // - Checking if two rooms are properly connected by a door
        // - Verifying that two systems are properly linked
        // - Ensuring that related information stays synchronized
        // 
        String checkFkSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS " +
                           "WHERE CONSTRAINT_NAME = 'FK_EventM_CreatedByUser'";
        try (PreparedStatement stmt = conn.prepareStatement(checkFkSql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("✅ FK_EventM_CreatedByUser constraint exists");
            } else {
                System.err.println("❌ FK_EventM_CreatedByUser constraint missing!");
            }
        }
    }
    
    private static void validateForeignKeys(Connection conn) throws SQLException {
        System.out.println("🔍 Checking foreign key constraints...");
        
        // Check all foreign key constraints
        String fkSql = "SELECT " +
                      "    fk.CONSTRAINT_NAME, " +
                      "    fk.TABLE_NAME, " +
                      "    fk.COLUMN_NAME, " +
                      "    fk.REFERENCED_TABLE_NAME, " +
                      "    fk.REFERENCED_COLUMN_NAME " +
                      "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE fk " +
                      "WHERE fk.REFERENCED_TABLE_NAME IS NOT NULL " +
                      "ORDER BY fk.TABLE_NAME, fk.CONSTRAINT_NAME";
        
        try (PreparedStatement stmt = conn.prepareStatement(fkSql)) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("🔗 Foreign key constraints:");
            while (rs.next()) {
                String constraintName = rs.getString("CONSTRAINT_NAME");
                String tableName = rs.getString("TABLE_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                String referencedTable = rs.getString("REFERENCED_TABLE_NAME");
                String referencedColumn = rs.getString("REFERENCED_COLUMN_NAME");
                
                System.out.println("  - " + constraintName + ": " + tableName + "." + columnName + 
                                 " -> " + referencedTable + "." + referencedColumn);
            }
        }
    }
} 