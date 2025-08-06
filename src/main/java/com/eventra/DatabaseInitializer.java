package com.eventra;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try {
            Connection conn = Db.get();
            Statement stmt = conn.createStatement();
            
            // Create lookup tables (ignore if they exist)
            try {
                createLookupTables(stmt);
            } catch (Exception e) {
                System.out.println("Lookup tables already exist, continuing...");
            }
            
            // Create main tables (ignore if they exist)
            try {
                createMainTables(stmt);
            } catch (Exception e) {
                System.out.println("Main tables already exist, continuing...");
            }
            
            // Insert initial data (this is the important part)
            insertInitialData(stmt);
            
            System.out.println("Database initialization completed successfully!");
            
            // Clean up any duplicate presenters
            com.eventra.dao.EventSaveDAO.cleanupDuplicatePresenters();
            
            // Validate database structure
            com.eventra.dao.DatabaseValidator.validateDatabase();
            
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createLookupTables(Statement stmt) throws Exception {
        // StatusType table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'StatusType') " +
            "CREATE TABLE StatusType (" +
            "StatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // RoleType table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'RoleType') " +
            "CREATE TABLE RoleType (" +
            "RoleTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // EventStatusType table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EventStatusType') " +
            "CREATE TABLE EventStatusType (" +
            "EventStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // SessionStatusType table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SessionStatusType') " +
            "CREATE TABLE SessionStatusType (" +
            "SessionStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // RegistrationStatusType table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'RegistrationStatusType') " +
            "CREATE TABLE RegistrationStatusType (" +
            "RegistrationStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
    }
    
    private static void createMainTables(Statement stmt) throws Exception {
        // UserM table - SQL Server compatible
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
            "RoleTypeID INT NOT NULL DEFAULT 2," +
            "StatusTypeID INT NOT NULL DEFAULT 1," +
            "PeriodCanLoginInMinutes INT NOT NULL DEFAULT 0," +
            "LastFailedLoginAt DATETIME," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()" +
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