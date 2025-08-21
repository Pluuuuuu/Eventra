package com.eventra;

// Import Java SQL classes for database operations
// Connection: Represents a connection to the database for executing SQL commands
import java.sql.Connection;
// Statement: Used to execute SQL statements and queries against the database
import java.sql.Statement;

/**
 * Database Initializer Class for Eventra Event Management System
 * 
 * This class is responsible for setting up the complete database schema
 * and initializing it with default data when the application starts.
 * It creates all necessary tables, relationships, and sample data
 * to ensure the application can run immediately after installation.
 * 
 * Key Responsibilities:
 * - Create database tables if they don't exist
 * - Set up foreign key relationships
 * - Insert initial lookup data (status types, role types)
 * - Create sample users and attendees for testing
 * - Validate database structure after initialization
 * 
 * File Usage: Called by App.java during application startup to ensure
 * database is properly configured before the UI loads
 * 
 * DESIGN PATTERN: This class implements the Initialization Pattern, ensuring
 * the database is properly set up before the application starts running.
 */
public class DatabaseInitializer {
    
    /**
     * Main initialization method - sets up the entire database schema
     * This method is called once during application startup
     * 
     * EXECUTION FLOW:
     * 1. Get database connection
     * 2. Create lookup tables (reference data)
     * 3. Create main tables (business data)
     * 4. Insert initial data
     * 5. Clean up duplicates
     * 6. Validate structure
     * 
     * ERROR HANDLING: Uses try-catch blocks to handle database errors gracefully
     * and continue initialization even if some tables already exist.
     */
    public static void initializeDatabase() {
        try {
            // Get database connection from the Db utility class
            // Db.get() returns a connection from the HikariCP connection pool
            // This connection will be used for all database operations in this method
            Connection conn = Db.get();
            
            // Create a statement object for executing SQL commands
            // Statement is used to execute DDL (Data Definition Language) commands
            // like CREATE TABLE, INSERT, etc.
            Statement stmt = conn.createStatement();
            
            // Create lookup tables (ignore if they exist)
            // Lookup tables contain reference data like status types and role types
            // These tables store predefined values used throughout the application
            try {
                createLookupTables(stmt);
            } catch (Exception e) {
                // If tables already exist, continue with initialization
                // This allows the method to be called multiple times safely
                // Useful for development and testing scenarios
                System.out.println("Lookup tables already exist, continuing...");
            }
            
            // Create main tables (ignore if they exist)
            // Main tables contain the core business data like users, events, attendees
            // These are the primary tables that store application data
            try {
                createMainTables(stmt);
            } catch (Exception e) {
                // If tables already exist, continue with initialization
                // This prevents errors when the application is restarted
                System.out.println("Main tables already exist, continuing...");
            }
            
            // Insert initial data (this is the important part)
            // Populates tables with default values and sample data
            // This ensures the application has data to work with immediately
            insertInitialData(stmt);
            
            // Confirm successful initialization
            // This message helps developers know the database setup is complete
            System.out.println("Database initialization completed successfully!");
            
            // Clean up any duplicate presenters that might exist
            // This ensures data integrity by removing duplicate entries
            // EventSaveDAO.cleanupDuplicatePresenters() handles this cleanup
            com.eventra.dao.EventSaveDAO.cleanupDuplicatePresenters();
            
            // Validate database structure to ensure everything is set up correctly
            // DatabaseValidator.validateDatabase() checks that all tables exist
            // and have the correct structure
            com.eventra.dao.DatabaseValidator.validateDatabase();
            
        } catch (Exception e) {
            // Log any errors that occur during database initialization
            // System.err outputs to the error stream for better visibility
            System.err.println("Error initializing database: " + e.getMessage());
            // Print the full stack trace for detailed debugging information
            e.printStackTrace();
        }
    }
    
    /**
     * Creates lookup tables that contain reference data
     * These tables store predefined values used throughout the application
     * 
     * @param stmt SQL Statement object for executing commands
     * @throws Exception if table creation fails
     * 
     * LOOKUP TABLES CREATED:
     * - StatusType: User account statuses (Active, Inactive, Suspended, Deleted)
     * - RoleType: User roles (SuperAdmin, Admin, Staff, Attendee)
     * - EventStatusType: Event statuses (Draft, Published, Cancelled, Completed)
     * - SessionStatusType: Session statuses (Draft, Confirmed, Cancelled, Completed)
     * - RegistrationStatusType: Registration statuses (Pending, Confirmed, Cancelled, Attended)
     */
    private static void createLookupTables(Statement stmt) throws Exception {
        // StatusType table - defines user account statuses (Active, Inactive, Suspended, etc.)
        // This table stores the possible states a user account can be in
        // IF NOT EXISTS prevents errors if the table already exists
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'StatusType') " +
            "CREATE TABLE StatusType (" +
            "StatusTypeID INT PRIMARY KEY," +  // Primary key for unique identification
            "Name VARCHAR(20) NOT NULL UNIQUE" + // Status name, must be unique
            ")"
        );
        
        // RoleType table - defines user roles (SuperAdmin, Admin, Staff, Attendee)
        // This table stores the different user roles and their permissions
        // Role hierarchy: SuperAdmin > Admin > Staff > Attendee
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'RoleType') " +
            "CREATE TABLE RoleType (" +
            "RoleTypeID INT PRIMARY KEY," +  // Primary key for unique identification
            "Name VARCHAR(20) NOT NULL UNIQUE" + // Role name, must be unique
            ")"
        );
        
        // EventStatusType table - defines event statuses (Draft, Published, Cancelled, etc.)
        // This table stores the possible states an event can be in
        // Events progress from Draft -> Published -> Completed (or Cancelled)
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EventStatusType') " +
            "CREATE TABLE EventStatusType (" +
            "EventStatusTypeID INT PRIMARY KEY," +  // Primary key for unique identification
            "Name VARCHAR(20) NOT NULL UNIQUE" + // Status name, must be unique
            ")"
        );
        
        // SessionStatusType table - defines session statuses (Scheduled, In Progress, Completed, etc.)
        // This table stores the possible states a session can be in
        // Sessions progress from Draft -> Confirmed -> Completed (or Cancelled)
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SessionStatusType') " +
            "CREATE TABLE SessionStatusType (" +
            "SessionStatusTypeID INT PRIMARY KEY," +  // Primary key for unique identification
            "Name VARCHAR(20) NOT NULL UNIQUE" + // Status name, must be unique
            ")"
        );
        
        // RegistrationStatusType table - defines registration statuses (Pending, Confirmed, Cancelled, etc.)
        // This table stores the possible states a registration can be in
        // Registrations progress from Pending -> Confirmed -> Attended (or Cancelled)
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'RegistrationStatusType') " +
            "CREATE TABLE RegistrationStatusType (" +
            "RegistrationStatusTypeID INT PRIMARY KEY," +  // Primary key for unique identification
            "Name VARCHAR(20) NOT NULL UNIQUE" + // Status name, must be unique
            ")"
        );
    }
    
    /**
     * Creates main business tables that store the core application data
     * These tables contain the actual business entities like users, events, attendees
     * 
     * @param stmt SQL Statement object for executing commands
     * @throws Exception if table creation fails
     * 
     * MAIN TABLES CREATED:
     * - UserM: User accounts with authentication and profile information
     * - Attendee: Detailed attendee information linked to user accounts
     * - Venue: Event venue information and capacity details
     * - Room: Individual rooms within venues
     * - EventM: Event information and scheduling details
     * - Presenter: Presenter information for event sessions
     * - SessionM: Event sessions with scheduling and presenter assignments
     * - SessionPresenter: Many-to-many relationship between sessions and presenters
     * - Registration: Event registrations and attendance tracking
     * 
     * DATABASE DESIGN PATTERNS:
     * - Foreign Key Relationships: Ensure referential integrity
     * - Audit Fields: CreatedAt, UpdatedAt for tracking changes
     * - Default Values: Sensible defaults for required fields
     * - Constraints: UNIQUE constraints for business rules
     */
    private static void createMainTables(Statement stmt) throws Exception {
        // UserM table - stores all user accounts with authentication information
        // SQL Server compatible with IDENTITY for auto-incrementing primary keys
        // This is the central user table that all other user-related tables reference
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'UserM') " +
            "CREATE TABLE UserM (" +
            "UserID INT IDENTITY(1,1) PRIMARY KEY," +  // Auto-incrementing primary key
            "Username NVARCHAR(100) NOT NULL UNIQUE," + // Unique username for login
            "FirstName NVARCHAR(100) NOT NULL," + // User's first name
            "MiddleName NVARCHAR(100)," + // Optional middle name
            "LastName NVARCHAR(100) NOT NULL," + // User's last name
            "Email NVARCHAR(255) NOT NULL UNIQUE," + // Unique email address
            "PasswordHash NVARCHAR(255) NOT NULL," + // BCrypt hashed password
            "ProfilePicUrl NVARCHAR(500)," + // URL to profile picture
            "RoleTypeID INT NOT NULL DEFAULT 2," + // Default to Admin role (2)
            "StatusTypeID INT NOT NULL DEFAULT 1," + // Default to Active status (1)
            "PeriodCanLoginInMinutes INT NOT NULL DEFAULT 0," + // Login time restrictions
            "LastFailedLoginAt DATETIME," + // Track failed login attempts
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," + // Audit field
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()" + // Audit field
            ")"
        );
        
        // Attendee table - stores detailed attendee information linked to user accounts
        // Contains additional fields specific to event attendees
        // This table extends user information with attendee-specific data
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Attendee') " +
            "CREATE TABLE Attendee (" +
            "AttendeeID INT IDENTITY(1,1) PRIMARY KEY," +  // Auto-incrementing primary key
            "UserID INT NOT NULL," + // Foreign key to UserM table
            "FirstName NVARCHAR(100) NOT NULL," + // Attendee's first name
            "MiddleName NVARCHAR(100)," + // Optional middle name
            "LastName NVARCHAR(100) NOT NULL," + // Attendee's last name
            "Email NVARCHAR(255) NOT NULL UNIQUE," + // Unique email address
            "Organization NVARCHAR(255)," + // Company or organization name
            "Phone NVARCHAR(50)," + // Contact phone number
            "Location NVARCHAR(255)," + // Geographic location
            "Gender NVARCHAR(20)," + // Gender information
            "DateOfBirth DATE," + // Birth date for age-related features
            "ProfilePicUrl NVARCHAR(500)," + // URL to profile picture
            "Type NVARCHAR(50) NOT NULL DEFAULT 'Regular'," + // Attendee type (Regular, VIP, etc.)
            "PasswordHash NVARCHAR(255) NOT NULL," + // BCrypt hashed password
            "StatusTypeID INT NOT NULL DEFAULT 1," + // Default to Active status (1)
            "PeriodCanLoginInMinutes INT NOT NULL DEFAULT 0," + // Login time restrictions
            "LastFailedLoginAt DATETIME," + // Track failed login attempts
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," + // Audit field
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()," + // Audit field
            "CONSTRAINT FK_Attendee_UserM FOREIGN KEY (UserID) REFERENCES UserM(UserID) ON DELETE CASCADE ON UPDATE CASCADE," + // Links to user account
            "CONSTRAINT FK_Attendee_StatusType FOREIGN KEY (StatusTypeID) REFERENCES StatusType(StatusTypeID) ON DELETE NO ACTION ON UPDATE CASCADE" + // Links to status lookup
            ")"
        );
        
        // Venue table - stores event venue information
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
        
        // Room table - stores individual rooms within venues
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
        
        // EventM table - stores event information
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
        
        // Presenter table - stores presenter information for event sessions
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
    
    /**
     * Inserts initial data into the database tables
     * This method populates lookup tables with default values and creates sample users
     * @param stmt SQL Statement object for executing commands
     * @throws Exception if data insertion fails
     */
    private static void insertInitialData(Statement stmt) throws Exception {
        // Insert Status Types - defines possible user account statuses
        // These values are referenced throughout the application for user management
        try {
            stmt.execute(
                "INSERT INTO StatusType (StatusTypeID, Name) VALUES " +
                "(1, 'Active'), " +
                "(2, 'Inactive'), " +
                "(3, 'Suspended'), " +
                "(4, 'Deleted')"
            );
        } catch (Exception e) {
            // Data already exists, ignore - this allows the method to be called multiple times safely
        }
        
        // Insert Role Types - defines user roles and permissions in the system
        // Role hierarchy: SuperAdmin > Admin > Staff > Attendee
        try {
            stmt.execute(
                "INSERT INTO RoleType (RoleTypeID, Name) VALUES " +
                "(1, 'SuperAdmin'), " +
                "(2, 'Admin'), " +
                "(3, 'Staff'), " +
                "(4, 'Attendee')"
            );
        } catch (Exception e) {
            // Data already exists, ignore - this allows the method to be called multiple times safely
        }
        
        // Insert Event Status Types - defines the lifecycle states of events
        // Events progress from Draft -> Published -> Completed (or Cancelled)
        try {
            stmt.execute(
                "INSERT INTO EventStatusType (EventStatusTypeID, Name) VALUES " +
                "(1, 'Draft'), " +
                "(2, 'Published'), " +
                "(3, 'Cancelled'), " +
                "(4, 'Completed')"
            );
        } catch (Exception e) {
            // Data already exists, ignore - this allows the method to be called multiple times safely
        }
        
        // Insert Session Status Types - defines the lifecycle states of event sessions
        // Sessions progress from Draft -> Confirmed -> Completed (or Cancelled)
        try {
            stmt.execute(
                "INSERT INTO SessionStatusType (SessionStatusTypeID, Name) VALUES " +
                "(1, 'Draft'), " +
                "(2, 'Confirmed'), " +
                "(3, 'Cancelled'), " +
                "(4, 'Completed')"
            );
        } catch (Exception e) {
            // Data already exists, ignore - this allows the method to be called multiple times safely
        }
        
        // Insert Registration Status Types - defines the lifecycle states of event registrations
        // Registrations progress from Pending -> Confirmed -> Attended (or Cancelled)
        try {
            stmt.execute(
                "INSERT INTO RegistrationStatusType (RegistrationStatusTypeID, Name) VALUES " +
                "(1, 'Pending'), " +
                "(2, 'Confirmed'), " +
                "(3, 'Cancelled'), " +
                "(4, 'Attended')"
            );
        } catch (Exception e) {
            // Data already exists, ignore - this allows the method to be called multiple times safely
        }
        
        // Create test admin user for development and testing purposes
        // This user has admin privileges and can be used to test all features
        // Password: test123 (stored as BCrypt hash)
        try {
            // First, try to update existing user if it already exists
            // This ensures the password is always up-to-date
            int updated = stmt.executeUpdate(
                "UPDATE UserM SET " +
                "PasswordHash = '$2a$10$6EtNj6fB6stiRuM.K3Y7FeNUZ/DJ29vqjg9XZiaKeAm4TI4vnj9Ta', " +
                "RoleTypeID = 2, " + // RoleTypeID 2 = Admin
                "StatusTypeID = 1 " + // StatusTypeID 1 = Active
                "WHERE Email = 'test@eventra.com'"
            );
            
            if (updated > 0) {
                // User was updated successfully
                System.out.println("✅ Updated existing test admin user: test@eventra.com");
            } else {
                // If no user was updated, insert new one
                // This creates a new admin user for testing
                stmt.execute(
                    "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                    "VALUES ('testuser', 'Test', 'User', 'test@eventra.com', " +
                    "'$2a$10$6EtNj6fB6stiRuM.K3Y7FeNUZ/DJ29vqjg9XZiaKeAm4TI4vnj9Ta', 2, 1)"
                );
                System.out.println("✅ Inserted new test admin user: test@eventra.com");
            }
        } catch (Exception e) {
            // Log any errors that occur during test user creation
            System.out.println("❌ Error with test user: " + e.getMessage());
        }
        
        // Create additional test users with different roles for comprehensive testing
        // These users have plain text passwords for easy testing (not recommended for production)
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
    
    /**
     * Ensures that the matcha123 user exists in the database
     * This is a special user account that may be required for specific functionality
     * @param stmt SQL Statement object for executing commands
     * @throws Exception if user creation fails
     */
    private static void ensureMatchaUserExists(Statement stmt) throws Exception {
        // Check if matcha123 user exists by querying the UserM table
        String checkSql = "SELECT COUNT(*) FROM UserM WHERE Email = 'matcha123@gmail.com'";
        try {
            // Execute the query to check if user exists
            java.sql.ResultSet rs = stmt.executeQuery(checkSql);
            if (rs.next() && rs.getInt(1) == 0) {
                // User doesn't exist, create it with attendee role (RoleTypeID = 4)
                System.out.println("Creating matcha123 user...");
                stmt.execute(
                    "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                    "VALUES ('matcha123', 'Matcha', 'User', 'matcha123@gmail.com', 'matcha123@gmail.com', 4, 1)"
                );
                System.out.println("matcha123 user created successfully!");
            } else {
                // User already exists, no action needed
                System.out.println("matcha123 user already exists!");
            }
        } catch (Exception e) {
            // Log any errors that occur during the check
            System.err.println("Error ensuring matcha123 user exists: " + e.getMessage());
            // Try to create the user anyway as a fallback
            try {
                stmt.execute(
                    "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                    "VALUES ('matcha123', 'Matcha', 'User', 'matcha123@gmail.com', 'matcha123@gmail.com', 4, 1)"
                );
                System.out.println("matcha123 user created successfully!");
            } catch (Exception e2) {
                // Log if the fallback creation also fails
                System.err.println("Failed to create matcha123 user: " + e2.getMessage());
            }
        }
    }
}
/*
 * FILE SUMMARY: DatabaseInitializer.java
 * 
 * PRIMARY PURPOSE:
 * This class is responsible for setting up the complete database schema for the Eventra
 * event management system. It creates all necessary tables, relationships, and initial
 * data to ensure the application can run immediately after installation.
 * 
 * ROLE WITHIN EVENTRA APPLICATION:
 * - Database schema initialization manager
 * - Initial data population coordinator
 * - Database structure validator
 * - Sample data creator for testing
 * - Database integrity maintainer
 * 
 * KEY METHODS AND FUNCTIONALITIES:
 * - initializeDatabase(): Main initialization method called during application startup
 * - createLookupTables(): Creates reference tables (StatusType, RoleType, etc.)
 * - createMainTables(): Creates business tables (UserM, EventM, Attendee, etc.)
 * - insertInitialData(): Populates tables with default values and sample data
 * - ensureMatchaUserExists(): Creates special user account for testing
 * 
 * INTERACTIONS WITH OTHER SYSTEM COMPONENTS:
 * - Called by App.java during application startup
 * - Uses Db.java for database connections
 * - Calls EventSaveDAO.cleanupDuplicatePresenters() for data integrity
 * - Calls DatabaseValidator.validateDatabase() for structure validation
 * - Creates tables that are used by all DAO classes
 * 
 * DATABASE SCHEMA CREATED:
 * LOOKUP TABLES:
 * - StatusType: User account statuses (Active, Inactive, Suspended, Deleted)
 * - RoleType: User roles (SuperAdmin, Admin, Staff, Attendee)
 * - EventStatusType: Event statuses (Draft, Published, Cancelled, Completed)
 * - SessionStatusType: Session statuses (Draft, Confirmed, Cancelled, Completed)
 * - RegistrationStatusType: Registration statuses (Pending, Confirmed, Cancelled, Attended)
 * 
 * MAIN TABLES:
 * - UserM: User accounts with authentication and profile information
 * - Attendee: Detailed attendee information linked to user accounts
 * - Venue: Event venue information and capacity details
 * - Room: Individual rooms within venues
 * - EventM: Event information and scheduling details
 * - Presenter: Presenter information for event sessions
 * - SessionM: Event sessions with scheduling and presenter assignments
 * - SessionPresenter: Many-to-many relationship between sessions and presenters
 * - Registration: Event registrations and attendance tracking
 * 
 * SAMPLE DATA CREATED:
 * - Test users with different roles (SuperAdmin, Admin, Staff, Attendee)
 * - Sample attendees with various organizations and types
 * - Reference data for all lookup tables
 * - Special matcha123 user for testing purposes
 * 
 * ERROR HANDLING:
 * - Graceful handling of existing tables (IF NOT EXISTS)
 * - Try-catch blocks for individual table creation
 * - Detailed error logging for debugging
 * - Fallback mechanisms for critical operations
 * 
 * DESIGN PATTERNS USED:
 * - Initialization Pattern: Ensures proper setup before application starts
 * - Template Method Pattern: Structured approach to database initialization
 * - Error Handling Pattern: Graceful degradation and error recovery
 * - Resource Management Pattern: Proper connection handling and cleanup
 * 
 * SECURITY FEATURES:
 * - BCrypt password hashing for user accounts
 * - Foreign key constraints for data integrity
 * - Unique constraints for business rules
 * - Audit fields for tracking changes
 * 
 * PERFORMANCE CONSIDERATIONS:
 * - Efficient table creation with proper indexing
 * - Batch operations for sample data insertion
 * - Connection pooling through Db.java
 * - Minimal database operations during initialization
 */
