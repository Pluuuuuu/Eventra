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
            
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createLookupTables(Statement stmt) throws Exception {
        // StatusType table
        stmt.execute(
            "CREATE TABLE StatusType (" +
            "StatusTypeID TINYINT PRIMARY KEY," +
            "Name NVARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // RoleType table
        stmt.execute(
            "CREATE TABLE RoleType (" +
            "RoleTypeID TINYINT PRIMARY KEY," +
            "Name NVARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // EventStatusType table
        stmt.execute(
            "CREATE TABLE EventStatusType (" +
            "EventStatusTypeID TINYINT PRIMARY KEY," +
            "Name NVARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // SessionStatusType table
        stmt.execute(
            "CREATE TABLE SessionStatusType (" +
            "SessionStatusTypeID TINYINT PRIMARY KEY," +
            "Name NVARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // RegistrationStatusType table
        stmt.execute(
            "CREATE TABLE RegistrationStatusType (" +
            "RegistrationStatusTypeID TINYINT PRIMARY KEY," +
            "Name NVARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
    }
    
    private static void createMainTables(Statement stmt) throws Exception {
        // UserM table
        stmt.execute(
            "CREATE TABLE UserM (" +
            "UserID INT IDENTITY(1,1) PRIMARY KEY," +
            "Username NVARCHAR(100) NOT NULL UNIQUE," +
            "FirstName NVARCHAR(100) NOT NULL," +
            "MiddleName NVARCHAR(100)," +
            "LastName NVARCHAR(100) NOT NULL," +
            "Email NVARCHAR(255) NOT NULL UNIQUE," +
            "PasswordHash NVARCHAR(255) NOT NULL," +
            "ProfilePicUrl NVARCHAR(500)," +
            "RoleTypeID TINYINT NOT NULL DEFAULT 2," +
            "StatusTypeID TINYINT NOT NULL DEFAULT 1," +
            "PeriodCanLoginInMinutes TINYINT NOT NULL DEFAULT 0," +
            "LastFailedLoginAt DATETIME2," +
            "CreatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()," +
            "UpdatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()" +
            ")"
        );
        
        // Attendee table (with UserID field as expected by Java code)
        stmt.execute(
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
            "Type NVARCHAR(50) NOT NULL," +
            "PasswordHash NVARCHAR(255) NOT NULL," +
            "StatusTypeID TINYINT NOT NULL DEFAULT 1," +
            "PeriodCanLoginInMinutes TINYINT NOT NULL DEFAULT 0," +
            "LastFailedLoginAt DATETIME2," +
            "CreatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()," +
            "UpdatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()," +
            "CONSTRAINT FK_Attendee_UserM FOREIGN KEY (UserID) REFERENCES UserM(UserID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "CONSTRAINT FK_Attendee_StatusType FOREIGN KEY (StatusTypeID) REFERENCES StatusType(StatusTypeID) ON DELETE NO ACTION ON UPDATE CASCADE" +
            ")"
        );
        
        // Venue table
        stmt.execute(
            "CREATE TABLE Venue (" +
            "VenueID INT IDENTITY(1,1) PRIMARY KEY," +
            "Name NVARCHAR(255) NOT NULL," +
            "Address NVARCHAR(500)," +
            "Capacity INT," +
            "Rooms INT," +
            "MapLink NVARCHAR(500)," +
            "CreatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()," +
            "UpdatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()" +
            ")"
        );
        
        // Room table
        stmt.execute(
            "CREATE TABLE Room (" +
            "RoomID INT IDENTITY(1,1) PRIMARY KEY," +
            "VenueID INT NOT NULL," +
            "Name NVARCHAR(100) NOT NULL," +
            "Capacity INT," +
            "CreatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()," +
            "UpdatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()" +
            ")"
        );
        
        // EventM table
        stmt.execute(
            "CREATE TABLE EventM (" +
            "EventID INT IDENTITY(1,1) PRIMARY KEY," +
            "Title NVARCHAR(255) NOT NULL," +
            "Description NVARCHAR(MAX)," +
            "StartDate DATETIME2 NOT NULL," +
            "EndDate DATETIME2 NOT NULL," +
            "VenueID INT NOT NULL," +
            "CreatedByUserID INT NOT NULL," +
            "StatusTypeID TINYINT NOT NULL DEFAULT 1," +
            "CreatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()," +
            "UpdatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()" +
            ")"
        );
        
        // Presenter table
        stmt.execute(
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
            "CreatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()," +
            "UpdatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()" +
            ")"
        );
        
        // SessionM table
        stmt.execute(
            "CREATE TABLE SessionM (" +
            "SessionID INT IDENTITY(1,1) PRIMARY KEY," +
            "EventID INT NOT NULL," +
            "CreatedByUserID INT NOT NULL," +
            "Track NVARCHAR(100) NOT NULL," +
            "Role NVARCHAR(50)," +
            "Title NVARCHAR(255) NOT NULL," +
            "Abstract NVARCHAR(MAX)," +
            "StartAt DATETIME2 NOT NULL," +
            "EndAt DATETIME2 NOT NULL," +
            "RoomID INT," +
            "StatusTypeID TINYINT NOT NULL DEFAULT 1," +
            "CreatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()," +
            "UpdatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()" +
            ")"
        );
        
        // SessionPresenter table
        stmt.execute(
            "CREATE TABLE SessionPresenter (" +
            "SessionID INT NOT NULL," +
            "PresenterID INT NOT NULL," +
            "PRIMARY KEY (SessionID, PresenterID)" +
            ")"
        );
        
        // Registration table
        stmt.execute(
            "CREATE TABLE Registration (" +
            "RegistrationID INT IDENTITY(1,1) PRIMARY KEY," +
            "AttendeeID INT NOT NULL," +
            "EventID INT NOT NULL," +
            "RegistrationStatusTypeID TINYINT NOT NULL DEFAULT 1," +
            "CreatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()," +
            "UpdatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()" +
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
        
        // COMMENTED OUT: Sample data insertion to prevent overwriting Azure data
        // Uncomment these sections if you need to initialize with sample data
        
        /*
        // Insert sample venues and rooms
        try {
            stmt.execute(
                "INSERT INTO Venue (Name, Address, Capacity, Rooms, MapLink) " +
                "VALUES ('Conference Center', '123 Main Street, Downtown', 2000, 10, 'https://maps.example.com/venue1')"
            );
        } catch (Exception e) {
            // Venue already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO Room (VenueID, Name, Capacity) " +
                "VALUES (1, 'Main Hall', 1000), (1, 'Conference Room A', 200)"
            );
        } catch (Exception e) {
            // Rooms already exist, ignore
        }
        
        // Insert test user (password: test123)
        try {
            stmt.execute(
                "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                "VALUES ('testuser', 'Test', 'User', 'test@eventra.com', " +
                "'$2a$10$TToB9tKSXwz5Ub9mOByFMekz.ygcmX2ZMgiDmpP.QigvnhUtjVXrK', 2, 1)"
            );
        } catch (Exception e) {
            // User already exists, ignore
        }
        */
        
        // COMMENTED OUT: Sample data insertion to prevent overwriting Azure data
        // Uncomment these sections if you need to initialize with sample data
        
        /*
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
        
        // Insert sample events
        try {
            stmt.execute(
                "INSERT INTO EventM (Title, Description, StartDate, EndDate, VenueID, CreatedByUserID, StatusTypeID) " +
                "VALUES ('Tech Conference 2024', 'Annual technology conference featuring the latest innovations', " +
                "'2024-12-15 09:00:00', '2024-12-15 17:00:00', 1, 2, 2)"
            );
        } catch (Exception e) {
            // Event already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO EventM (Title, Description, StartDate, EndDate, VenueID, CreatedByUserID, StatusTypeID) " +
                "VALUES ('Startup Networking', 'Connect with fellow entrepreneurs and investors', " +
                "'2024-12-20 18:00:00', '2024-12-20 21:00:00', 1, 2, 2)"
            );
        } catch (Exception e) {
            // Event already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO EventM (Title, Description, StartDate, EndDate, VenueID, CreatedByUserID, StatusTypeID) " +
                "VALUES ('AI Workshop', 'Hands-on workshop on artificial intelligence and machine learning', " +
                "'2024-12-25 10:00:00', '2024-12-25 16:00:00', 1, 2, 2)"
            );
        } catch (Exception e) {
            // Event already exists, ignore
        }
        
        // Insert sample presenters
        try {
            stmt.execute(
                "INSERT INTO Presenter (FirstName, MiddleName, LastName, Bio, Email, PhotoUrl, ContactInfo, CreatedByUserID) " +
                "VALUES ('Dr. Sarah', 'Johnson', 'Johnson', 'Leading AI researcher with 15 years of experience', 'sarah.johnson@tech.com', 'https://example.com/sarah.jpg', 'sarah.johnson@tech.com', 2)"
            );
        } catch (Exception e) {
            // Presenter already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO Presenter (FirstName, MiddleName, LastName, Bio, Email, PhotoUrl, ContactInfo, CreatedByUserID) " +
                "VALUES ('Mike', 'Chen', 'Chen', 'Serial entrepreneur and startup mentor', 'mike.chen@startup.com', 'https://example.com/mike.jpg', 'mike.chen@startup.com', 2)"
            );
        } catch (Exception e) {
            // Presenter already exists, ignore
        }
        
        // Insert sample sessions
        try {
            stmt.execute(
                "INSERT INTO SessionM (EventID, CreatedByUserID, Track, Role, Title, Abstract, StartAt, EndAt, RoomID, StatusTypeID) " +
                "VALUES (1, 2, 'AI & Machine Learning', 'Speaker', 'Introduction to AI', 'Overview of artificial intelligence fundamentals', '2024-12-15 09:00:00', '2024-12-15 10:30:00', 1, 2)"
            );
        } catch (Exception e) {
            // Session already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO SessionM (EventID, CreatedByUserID, Track, Role, Title, Abstract, StartAt, EndAt, RoomID, StatusTypeID) " +
                "VALUES (1, 2, 'Startup Strategies', 'Panel', 'Building Successful Startups', 'Key strategies for startup success', '2024-12-15 11:00:00', '2024-12-15 12:30:00', 2, 2)"
            );
        } catch (Exception e) {
            // Session already exists, ignore
        }
        */
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