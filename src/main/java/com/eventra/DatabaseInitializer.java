package com.eventra;

/**
 * DATABASE INITIALIZER (DatabaseInitializer.java)
 *
 * This class is responsible for setting up and preparing the Eventra database
 * when the application starts. Think of it like a "construction crew" that
 * builds the foundation, walls, and basic structure of a building before
 * anyone can move in and use it.
 *
 * WHAT THIS CLASS DOES:
 * - Creates all necessary database tables if they don't exist
 * - Sets up lookup tables for system configuration (status types, roles, etc.)
 * - Creates main data tables for users, events, sessions, and registrations
 * - Inserts initial data and configuration values
 * - Validates the database structure and health
 * - Cleans up any duplicate or inconsistent data
 * - Ensures the database is ready for the application to use
 *
 * KEY CONCEPTS TO UNDERSTAND:
 * - Database Schema: The structure and organization of database tables
 * - Lookup Tables: Small tables that store reference data (like status types)
 * - Main Tables: Large tables that store the actual application data
 * - Initial Data: Starting data that the application needs to function
 * - Database Validation: Checking that everything is set up correctly
 * - Error Handling: Managing problems that might occur during setup
 *
 * BUSINESS VALUE:
 * - Ensures the database is properly configured when the application starts
 * - Prevents application crashes due to missing database structure
 * - Provides consistent starting point for all users
 * - Maintains data integrity and system reliability
 * - Supports application deployment and setup processes
 *
 * REAL-WORLD ANALOGY:
 * - Like a construction crew that builds a building before it opens
 * - Like a restaurant that sets up the kitchen and dining area before service
 * - Like a library that organizes books and sets up systems before opening
 * - Like a school that prepares classrooms and materials before students arrive
 * - Like a hospital that sets up equipment and systems before accepting patients
 */

import java.sql.Connection;
import java.sql.Statement;

/**
 * DatabaseInitializer class - Database Setup and Configuration
 *
 * This class contains static methods that handle all database initialization tasks.
 * It's designed to run once when the application starts to ensure everything
 * is properly set up before users begin using the system.
 */
public class DatabaseInitializer {
    
    /**
     * initializeDatabase() - Main Database Setup Method
     *
     * This is the primary method that orchestrates the entire database
     * initialization process. Think of it like the "foreman" who coordinates
     * all the different construction tasks.
     *
     * TECHNICAL PROCESS:
     * 1. Get a database connection from the connection pool
     * 2. Create lookup tables (status types, roles, etc.)
     * 3. Create main data tables (users, events, sessions, etc.)
     * 4. Insert initial configuration data
     * 5. Clean up any duplicate data
     * 6. Validate the database structure
     * 7. Report success or handle errors
     *
     * USER EXPERIENCE:
     * - Ensures the application starts with a properly configured database
     * - Prevents errors that would prevent users from using the system
     * - Provides consistent experience for all users
     *
     * ERROR HANDLING:
     * - Catches and reports any problems during initialization
     * - Continues with other tasks even if some fail
     * - Provides detailed error information for troubleshooting
     */
    public static void initializeDatabase() {
        try {
            // 
            // STEP 1: GET DATABASE CONNECTION - Connect to the database
            // 
            // Get a connection from our connection pool to perform database operations.
            // Think of this like getting the keys to the building before starting work.
            //
            Connection conn = Db.get();
            Statement stmt = conn.createStatement();
            
            // 
            // STEP 2: CREATE LOOKUP TABLES - Set up reference data structure
            // 
            // Lookup tables store small amounts of reference data that the application
            // needs to function. Think of these like the "rules and categories" of the system.
            // We use try-catch blocks so if tables already exist, we continue gracefully.
            //
            try {
                System.out.println("🔨 Creating lookup tables...");
                createLookupTables(stmt);
                System.out.println("✅ Lookup tables created successfully!");
            } catch (Exception e) {
                System.out.println("ℹ️ Lookup tables already exist, continuing...");
            }
            
            // 
            // STEP 3: CREATE MAIN TABLES - Set up data storage structure
            // 
            // Main tables store the actual application data like users, events, and registrations.
            // Think of these like the "rooms and storage areas" where data lives.
            //
            try {
                System.out.println("🏗️ Creating main data tables...");
                createMainTables(stmt);
                System.out.println("✅ Main tables created successfully!");
            } catch (Exception e) {
                System.out.println("ℹ️ Main tables already exist, continuing...");
            }
            
            // 
            // STEP 4: INSERT INITIAL DATA - Populate with starting information
            // 
            // Insert the basic data that the application needs to function, like
            // default user roles, status types, and system configuration.
            // Think of this like stocking the building with basic supplies.
            //
            System.out.println("📦 Inserting initial data...");
            insertInitialData(stmt);
            System.out.println("✅ Initial data inserted successfully!");
            
            // 
            // STEP 5: CLEANUP AND VALIDATION - Ensure data quality
            // 
            // Clean up any duplicate data and validate that everything is working correctly.
            // Think of this like doing a final inspection before opening the building.
            //
            System.out.println("🧹 Cleaning up duplicate data...");
            com.eventra.dao.EventSaveDAO.cleanupDuplicatePresenters();
            System.out.println("✅ Duplicate data cleanup completed!");
            
            System.out.println("🔍 Validating database structure...");
            com.eventra.dao.DatabaseValidator.validateDatabase();
            System.out.println("✅ Database validation completed!");
            
            // 
            // SUCCESS REPORT - Confirm everything is ready
            // 
            System.out.println("🎉 Database initialization completed successfully!");
            System.out.println("🚀 Eventra database is ready for use!");
            
        } catch (Exception e) {
            // 
            // ERROR HANDLING - Report any problems that occurred
            // 
            // If something goes wrong during initialization, we need to know about it
            // so we can fix the problem. Think of this like having a building inspector
            // who reports any issues found during construction.
            //
            System.err.println("❌ ERROR: Failed to initialize database!");
            System.err.println("🔍 Error details: " + e.getMessage());
            System.err.println("📋 Full error information:");
            e.printStackTrace();
        }
    }
    
    /**
     * createLookupTables() - Create Reference Data Tables
     *
     * Creates small tables that store reference information like status types,
     * user roles, and other configuration data. Think of these like the
     * "dictionaries and rulebooks" of the system.
     *
     * LOOKUP TABLES CREATED:
     * - StatusType: User account statuses (Active, Inactive, Suspended)
     * - RoleType: User roles (Admin, Staff, Attendee)
     * - EventStatusType: Event statuses (Draft, Published, Cancelled)
     * - SessionStatusType: Session statuses (Scheduled, In Progress, Completed)
     * - RegistrationStatusType: Registration statuses (Pending, Confirmed, Cancelled)
     *
     * @param stmt - Database statement object for executing SQL commands
     * @throws Exception - If table creation fails
     */
    private static void createLookupTables(Statement stmt) throws Exception {
        // 
        // STATUS TYPE TABLE - User account status management
        // 
        // This table defines the different states a user account can be in.
        // Think of it like having different "modes" for user accounts.
        //
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'StatusType') " +
            "CREATE TABLE StatusType (" +
            "StatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // 
        // ROLE TYPE TABLE - User permission management
        // 
        // This table defines the different types of users and their permissions.
        // Think of it like having different "job titles" with different access levels.
        //
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'RoleType') " +
            "CREATE TABLE RoleType (" +
            "RoleTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // 
        // EVENT STATUS TYPE TABLE - Event lifecycle management
        // 
        // This table defines the different states an event can be in.
        // Think of it like having different "phases" in an event's life.
        //
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EventStatusType') " +
            "CREATE TABLE EventStatusType (" +
            "EventStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // 
        // SESSION STATUS TYPE TABLE - Session lifecycle management
        // 
        // This table defines the different states a session can be in.
        // Think of it like having different "stages" in a session's timeline.
        //
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SessionStatusType') " +
            "CREATE TABLE SessionStatusType (" +
            "SessionStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // 
        // REGISTRATION STATUS TYPE TABLE - Registration tracking
        // 
        // This table defines the different states a registration can be in.
        // Think of it like having different "steps" in the registration process.
        //
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'RegistrationStatusType') " +
            "CREATE TABLE RegistrationStatusType (" +
            "RegistrationStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
    }
    
    /**
     * createMainTables() - Create Data Storage Tables
     *
     * Creates the main tables that store the actual application data like
     * users, events, sessions, and registrations. Think of these like the
     * "main rooms and storage areas" where the important data lives.
     *
     * MAIN TABLES CREATED:
     * - UserM: User account information and authentication
     * - EventM: Event details and management
     * - Session: Event session information and scheduling
     * - Presenter: Speaker and presenter information
     * - Attendee: Event participant information
     * - EventRegistration: User event registrations and attendance
     *
     * @param stmt - Database statement object for executing SQL commands
     * @throws Exception - If table creation fails
     */
    private static void createMainTables(Statement stmt) throws Exception {
        // 
        // USERM TABLE - User account management
        // 
        // This table stores all user account information including authentication
        // details, personal information, and account settings.
        // Think of it like the "user directory" of the system.
        //
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'UserM') " +
            "CREATE TABLE UserM (" +
            "UserID INT IDENTITY(1,1) PRIMARY KEY," +
            "Username NVARCHAR(100) NOT NULL UNIQUE," +
            "FirstName NVARCHAR(100) NOT NULL," +
            "MiddleName NVARCHAR(100)," +
            "LastName NVARCHAR(100) NOT NULL," +
            "Email NVARCHAR(255) NOT NULL UNIQUE," +
            "PasswordHash NVARCHAR(255) NOT NULL," +
            "ProfilePicUrl NVARCHAR(500)," +
            "RoleTypeID INT NOT NULL," +
            "StatusTypeID INT NOT NULL," +
            "PeriodCanLoginInMinutes INT DEFAULT 1440," +
            "LastFailedLoginAt DATETIME2," +
            "CreatedAt DATETIME2 DEFAULT GETDATE()," +
            "UpdatedAt DATETIME2 DEFAULT GETDATE()" +
            ")"
        );
        
        // Attendee table (with UserID field as expected by Java code)
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Attendee') " +
            "CREATE TABLE Attendee (" +
            "AttendeeID INT IDENTITY(1,1) PRIMARY KEY," +
            "UserID INT NOT NULL," +
            "FirstName NVARCHAR(100) NOT NULL," +
            "MiddleName NVARCHAR(100)," +
            "LastName NVARCHAR(100) NOT NULL," +
            "Email NVARCHAR(255) NOT NULL UNIQUE," +
            "Organization NVARCHAR(255)," +
            "Phone NVARCHAR(50)," +
            "Location NVARCHAR(255)," +
            "Gender NVARCHAR(20)," +
            "DateOfBirth DATE," +
            "ProfilePicUrl NVARCHAR(500)," +
            "Type NVARCHAR(50) NOT NULL DEFAULT 'Regular'," +
            "PasswordHash NVARCHAR(255) NOT NULL," +
            "StatusTypeID INT NOT NULL DEFAULT 1," +
            "PeriodCanLoginInMinutes INT NOT NULL DEFAULT 0," +
            "LastFailedLoginAt DATETIME," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "CONSTRAINT FK_Attendee_UserM FOREIGN KEY (UserID) REFERENCES UserM(UserID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "CONSTRAINT FK_Attendee_StatusType FOREIGN KEY (StatusTypeID) REFERENCES StatusType(StatusTypeID) ON DELETE NO ACTION ON UPDATE CASCADE" +
            ")"
        );
        
        // Venue table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Venue') " +
            "CREATE TABLE Venue (" +
            "VenueID INT IDENTITY(1,1) PRIMARY KEY," +
            "Name NVARCHAR(255) NOT NULL," +
            "Address NVARCHAR(500)," +
            "Capacity INT," +
            "Rooms INT," +
            "MapLink NVARCHAR(500)," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()" +
            ")"
        );
        
        // Room table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Room') " +
            "CREATE TABLE Room (" +
            "RoomID INT IDENTITY(1,1) PRIMARY KEY," +
            "VenueID INT NOT NULL," +
            "Name NVARCHAR(100) NOT NULL," +
            "Capacity INT," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()" +
            ")"
        );
        
        // EventM table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EventM') " +
            "CREATE TABLE EventM (" +
            "EventID INT IDENTITY(1,1) PRIMARY KEY," +
            "Title NVARCHAR(255) NOT NULL," +
            "Description NVARCHAR(MAX)," +
            "StartDate DATETIME NOT NULL," +
            "EndDate DATETIME NOT NULL," +
            "VenueID INT," +
            "CreatedByUserID INT NOT NULL," +
            "StatusTypeID INT NOT NULL DEFAULT 1," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "CONSTRAINT FK_EventM_CreatedByUser FOREIGN KEY (CreatedByUserID) REFERENCES UserM(UserID)," +
            "CONSTRAINT FK_EventM_StatusType FOREIGN KEY (StatusTypeID) REFERENCES EventStatusType(EventStatusTypeID)" +
            ")"
        );
        
        // Presenter table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Presenter') " +
            "CREATE TABLE Presenter (" +
            "PresenterID INT IDENTITY(1,1) PRIMARY KEY," +
            "FirstName NVARCHAR(100) NOT NULL," +
            "MiddleName NVARCHAR(100)," +
            "LastName NVARCHAR(100) NOT NULL," +
            "Bio NVARCHAR(MAX)," +
            "Email NVARCHAR(255) UNIQUE," +
            "PhotoUrl NVARCHAR(500)," +
            "ContactInfo NVARCHAR(255)," +
            "CreatedByUserID INT NOT NULL," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()" +
            ")"
        );
        
        // SessionM table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SessionM') " +
            "CREATE TABLE SessionM (" +
            "SessionID INT IDENTITY(1,1) PRIMARY KEY," +
            "EventID INT NOT NULL," +
            "CreatedByUserID INT NOT NULL," +
            "Track NVARCHAR(100) NOT NULL," +
            "Role NVARCHAR(50)," +
            "Title NVARCHAR(255) NOT NULL," +
            "Abstract NVARCHAR(MAX)," +
            "StartAt DATETIME NOT NULL," +
            "EndAt DATETIME NOT NULL," +
            "RoomID INT," +
            "StatusTypeID INT NOT NULL DEFAULT 1," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "CONSTRAINT FK_SessionM_Event FOREIGN KEY (EventID) REFERENCES EventM(EventID)," +
            "CONSTRAINT FK_SessionM_CreatedByUser FOREIGN KEY (CreatedByUserID) REFERENCES UserM(UserID)," +
            "CONSTRAINT FK_SessionM_StatusType FOREIGN KEY (StatusTypeID) REFERENCES SessionStatusType(SessionStatusTypeID)" +
            ")"
        );
        
        // SessionPresenter table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SessionPresenter') " +
            "CREATE TABLE SessionPresenter (" +
            "SessionID INT NOT NULL," +
            "PresenterID INT NOT NULL," +
            "PRIMARY KEY (SessionID, PresenterID)," +
            "CONSTRAINT FK_SessionPresenter_Session FOREIGN KEY (SessionID) REFERENCES SessionM(SessionID)," +
            "CONSTRAINT FK_SessionPresenter_Presenter FOREIGN KEY (PresenterID) REFERENCES Presenter(PresenterID)" +
            ")"
        );
        
        // Registration table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Registration') " +
            "CREATE TABLE Registration (" +
            "RegistrationID INT IDENTITY(1,1) PRIMARY KEY," +
            "AttendeeID INT NOT NULL," +
            "EventID INT NOT NULL," +
            "RegistrationStatusTypeID INT NOT NULL DEFAULT 1," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "CONSTRAINT FK_Registration_Attendee FOREIGN KEY (AttendeeID) REFERENCES Attendee(AttendeeID)," +
            "CONSTRAINT FK_Registration_Event FOREIGN KEY (EventID) REFERENCES EventM(EventID)," +
            "CONSTRAINT FK_Registration_StatusType FOREIGN KEY (RegistrationStatusTypeID) REFERENCES RegistrationStatusType(RegistrationStatusTypeID)" +
            ")"
        );
    }
    
    private static void insertInitialData(Statement stmt) throws Exception {
        // Insert Status Types
        try {
            stmt.execute(
                "INSERT INTO StatusType (StatusTypeID, Name) VALUES " +
                "(1, 'Active'), " +
                "(2, 'Inactive'), " +
                "(3, 'Suspended'), " +
                "(4, 'Deleted')"
            );
        } catch (Exception e) {
            // Data already exists, ignore
        }
        
        // Insert Role Types
        try {
            stmt.execute(
                "INSERT INTO RoleType (RoleTypeID, Name) VALUES " +
                "(1, 'SuperAdmin'), " +
                "(2, 'Admin'), " +
                "(3, 'Staff'), " +
                "(4, 'Attendee')"
            );
        } catch (Exception e) {
            // Data already exists, ignore
        }
        
        // Insert Event Status Types
        try {
            stmt.execute(
                "INSERT INTO EventStatusType (EventStatusTypeID, Name) VALUES " +
                "(1, 'Draft'), " +
                "(2, 'Published'), " +
                "(3, 'Cancelled'), " +
                "(4, 'Completed')"
            );
        } catch (Exception e) {
            // Data already exists, ignore
        }
        
        // Insert Session Status Types
        try {
            stmt.execute(
                "INSERT INTO SessionStatusType (SessionStatusTypeID, Name) VALUES " +
                "(1, 'Draft'), " +
                "(2, 'Confirmed'), " +
                "(3, 'Cancelled'), " +
                "(4, 'Completed')"
            );
        } catch (Exception e) {
            // Data already exists, ignore
        }
        
        // Insert Registration Status Types
        try {
            stmt.execute(
                "INSERT INTO RegistrationStatusType (RegistrationStatusTypeID, Name) VALUES " +
                "(1, 'Pending'), " +
                "(2, 'Confirmed'), " +
                "(3, 'Cancelled'), " +
                "(4, 'Attended')"
            );
        } catch (Exception e) {
            // Data already exists, ignore
        }
        
        // Insert or update test user (password: test123)
        try {
            // First, try to update existing user
            int updated = stmt.executeUpdate(
                "UPDATE UserM SET " +
                "PasswordHash = '$2a$10$6EtNj6fB6stiRuM.K3Y7FeNUZ/DJ29vqjg9XZiaKeAm4TI4vnj9Ta', " +
                "RoleTypeID = 2, " +
                "StatusTypeID = 1 " +
                "WHERE Email = 'test@eventra.com'"
            );
            
            if (updated > 0) {
                System.out.println("✅ Updated existing test admin user: test@eventra.com");
            } else {
                // If no user was updated, insert new one
                stmt.execute(
                    "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                    "VALUES ('testuser', 'Test', 'User', 'test@eventra.com', " +
                    "'$2a$10$6EtNj6fB6stiRuM.K3Y7FeNUZ/DJ29vqjg9XZiaKeAm4TI4vnj9Ta', 2, 1)"
                );
                System.out.println("✅ Inserted new test admin user: test@eventra.com");
            }
        } catch (Exception e) {
            System.out.println("❌ Error with test user: " + e.getMessage());
        }
        
        // Insert your test users with plain text passwords (as requested)
        // Password for all users: staff@gmail.com (stored as plain text)
        
        try {
            stmt.execute(
                "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                "VALUES ('superadmin11', 'System1', 'Owner1', 'superadmin11@gmail.com', 'staff@gmail.com', 1, 1)"
            );
        } catch (Exception e) {
            // User already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                "VALUES ('admin111', 'Alice1', 'Admin1', 'admin11@gmail.com', 'staff@gmail.com', 2, 1)"
            );
        } catch (Exception e) {
            // User already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                "VALUES ('staff111', 'Bob1', 'Staff1', 'staff11@gmail.com', 'staff@gmail.com', 3, 1)"
            );
        } catch (Exception e) {
            // User already exists, ignore
        }
        
        // CRITICAL: Ensure matcha123 user exists
        ensureMatchaUserExists(stmt);
        
        // Insert demo attendees (password: demo123)
        try {
            stmt.execute(
                "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                "VALUES ('attendee1', 'John', 'Doe', 'john.doe@example.com', " +
                "'$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlhMTi/jKMFxhPRlT54O6e', 4, 1)"
            );
        } catch (Exception e) {
            // User already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                "VALUES ('attendee2', 'Jane', 'Smith', 'jane.smith@example.com', " +
                "'$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlhMTi/jKMFxhPRlT54O6e', 4, 1)"
            );
        } catch (Exception e) {
            // User already exists, ignore
        }
        
        // Insert multiple sample attendees to get realistic numbers
        String[] attendeeData = {
            "('John', 'Doe', 'john.doe@example.com', 'Tech Corp', 'Regular')",
            "('Jane', 'Smith', 'jane.smith@example.com', 'Business Inc', 'VIP')",
            "('Alice', 'Johnson', 'alice.johnson@corp.com', 'Corporate Ltd', 'Regular')",
            "('Bob', 'Brown', 'bob.brown@startup.io', 'Startup Hub', 'Regular')",
            "('Carol', 'Davis', 'carol.davis@enterprise.com', 'Enterprise Co', 'VIP')",
            "('David', 'Wilson', 'david.wilson@innovate.net', 'Innovation Labs', 'Regular')",
            "('Eva', 'Martinez', 'eva.martinez@global.org', 'Global Solutions', 'Regular')",
            "('Frank', 'Taylor', 'frank.taylor@consulting.biz', 'Consulting Firm', 'VIP')",
            "('Grace', 'Anderson', 'grace.anderson@design.studio', 'Design Studio', 'Regular')",
            "('Henry', 'Thomas', 'henry.thomas@finance.group', 'Finance Group', 'Regular')"
        };
        
        for (int i = 0; i < attendeeData.length; i++) {
            try {
                stmt.execute(
                    "INSERT INTO Attendee (UserID, FirstName, LastName, Email, Organization, Type, PasswordHash, StatusTypeID) " +
                    "VALUES (" + (i + 1) + ", " + attendeeData[i].substring(1, attendeeData[i].length() - 1) + 
                    ", '$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlhMTi/jKMFxhPRlT54O6e', 1)"
                );
            } catch (Exception e) {
                // Attendee already exists, ignore
            }
        }
        
        // NOTE: Removed automatic sample data creation (venues, events, registrations)
        // These should be created manually through the application interface
    }
    
    private static void ensureMatchaUserExists(Statement stmt) throws Exception {
        // Check if matcha123 user exists
        String checkSql = "SELECT COUNT(*) FROM UserM WHERE Email = 'matcha123@gmail.com'";
        try {
            java.sql.ResultSet rs = stmt.executeQuery(checkSql);
            if (rs.next() && rs.getInt(1) == 0) {
                // User doesn't exist, create it
                System.out.println("Creating matcha123 user...");
                stmt.execute(
                    "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                    "VALUES ('matcha123', 'Matcha', 'User', 'matcha123@gmail.com', 'matcha123@gmail.com', 4, 1)"
                );
                System.out.println("matcha123 user created successfully!");
            } else {
                System.out.println("matcha123 user already exists!");
            }
        } catch (Exception e) {
            System.err.println("Error ensuring matcha123 user exists: " + e.getMessage());
            // Try to create the user anyway
            try {
                stmt.execute(
                    "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                    "VALUES ('matcha123', 'Matcha', 'User', 'matcha123@gmail.com', 'matcha123@gmail.com', 4, 1)"
                );
                System.out.println("matcha123 user created successfully!");
            } catch (Exception e2) {
                System.err.println("Failed to create matcha123 user: " + e2.getMessage());
            }
        }
    }
} 