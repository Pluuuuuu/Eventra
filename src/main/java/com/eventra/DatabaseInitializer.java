package com.eventra;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try (Connection conn = Db.get();
             Statement stmt = conn.createStatement()) {
            
            // Create lookup tables
            createLookupTables(stmt);
            
            // Create main tables
            createMainTables(stmt);
            
            // Insert initial data
            insertInitialData(stmt);
            
            System.out.println("Database initialized successfully!");
            
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createLookupTables(Statement stmt) throws Exception {
        // Status Type table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS StatusType (" +
            "StatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Role Type table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS RoleType (" +
            "RoleTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Event Status Type table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS EventStatusType (" +
            "EventStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Session Status Type table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS SessionStatusType (" +
            "SessionStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Registration Status Type table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS RegistrationStatusType (" +
            "RegistrationStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
    }
    
    private static void createMainTables(Statement stmt) throws Exception {
        // UserM table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS UserM (" +
            "UserID INT AUTO_INCREMENT PRIMARY KEY," +
            "Username VARCHAR(100) NOT NULL UNIQUE," +
            "FirstName VARCHAR(100) NOT NULL," +
            "MiddleName VARCHAR(100)," +
            "LastName VARCHAR(100) NOT NULL," +
            "Email VARCHAR(255) NOT NULL UNIQUE," +
            "PasswordHash VARCHAR(255) NOT NULL," +
            "ProfilePicUrl VARCHAR(500)," +
            "RoleTypeID INT NOT NULL DEFAULT 2," +
            "StatusTypeID INT NOT NULL DEFAULT 1," +
            "PeriodCanLoginInMinutes INT NOT NULL DEFAULT 0," +
            "LastFailedLoginAt TIMESTAMP," +
            "CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "UpdatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
            ")"
        );
        
        // Attendee table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS Attendee (" +
            "AttendeeID INT AUTO_INCREMENT PRIMARY KEY," +
            "UserID INT NOT NULL," +
            "FirstName VARCHAR(100) NOT NULL," +
            "MiddleName VARCHAR(100)," +
            "LastName VARCHAR(100) NOT NULL," +
            "Email VARCHAR(255) NOT NULL UNIQUE," +
            "Organization VARCHAR(255) NOT NULL," +
            "Phone VARCHAR(20)," +
            "Location VARCHAR(255)," +
            "Gender VARCHAR(10)," +
            "DateOfBirth DATE," +
            "ProfilePicUrl VARCHAR(500)," +
            "Type VARCHAR(50) NOT NULL DEFAULT 'Regular'," +
            "PasswordHash VARCHAR(255) NOT NULL," +
            "StatusTypeID INT NOT NULL DEFAULT 1," +
            "PeriodCanLoginInMinutes INT NOT NULL DEFAULT 0," +
            "LastFailedLoginAt TIMESTAMP," +
            "CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "UpdatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (UserID) REFERENCES UserM(UserID)" +
            ")"
        );
        
        // Event table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS Event (" +
            "EventID INT AUTO_INCREMENT PRIMARY KEY," +
            "Title VARCHAR(255) NOT NULL," +
            "Description TEXT," +
            "StartDateTime TIMESTAMP NOT NULL," +
            "EndDateTime TIMESTAMP NOT NULL," +
            "Location VARCHAR(255) NOT NULL," +
            "ImageURL VARCHAR(500)," +
            "EventStatusTypeID INT NOT NULL DEFAULT 2," +
            "OrganizerID INT NOT NULL," +
            "MaxAttendees INT DEFAULT 0," +
            "CurrentAttendees INT DEFAULT 0," +
            "EventType VARCHAR(100)," +
            "CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "UpdatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (OrganizerID) REFERENCES UserM(UserID)," +
            "FOREIGN KEY (EventStatusTypeID) REFERENCES EventStatusType(EventStatusTypeID)" +
            ")"
        );
        
        // Presenter table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS Presenter (" +
            "PresenterID INT AUTO_INCREMENT PRIMARY KEY," +
            "FirstName VARCHAR(100) NOT NULL," +
            "LastName VARCHAR(100) NOT NULL," +
            "Email VARCHAR(255) NOT NULL UNIQUE," +
            "Bio TEXT," +
            "ProfilePicUrl VARCHAR(500)," +
            "Organization VARCHAR(255)," +
            "Expertise VARCHAR(255)," +
            "StatusTypeID INT NOT NULL DEFAULT 1," +
            "CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "UpdatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
            ")"
        );
        
        // Session table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS Session (" +
            "SessionID INT AUTO_INCREMENT PRIMARY KEY," +
            "EventID INT NOT NULL," +
            "Title VARCHAR(255) NOT NULL," +
            "Description TEXT," +
            "StartTime TIMESTAMP NOT NULL," +
            "EndTime TIMESTAMP NOT NULL," +
            "PresenterID INT," +
            "SessionStatusTypeID INT NOT NULL DEFAULT 2," +
            "Room VARCHAR(100)," +
            "CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "UpdatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (EventID) REFERENCES Event(EventID)," +
            "FOREIGN KEY (PresenterID) REFERENCES Presenter(PresenterID)," +
            "FOREIGN KEY (SessionStatusTypeID) REFERENCES SessionStatusType(SessionStatusTypeID)" +
            ")"
        );
        
        // EventRegistration table
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS EventRegistration (" +
            "RegistrationID INT AUTO_INCREMENT PRIMARY KEY," +
            "EventID INT NOT NULL," +
            "AttendeeID INT NOT NULL," +
            "RegistrationStatusTypeID INT NOT NULL DEFAULT 1," +
            "RegistrationDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "UpdatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (EventID) REFERENCES Event(EventID)," +
            "FOREIGN KEY (AttendeeID) REFERENCES Attendee(AttendeeID)," +
            "FOREIGN KEY (RegistrationStatusTypeID) REFERENCES RegistrationStatusType(RegistrationStatusTypeID)" +
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
        
        // Add the user's preferred test account
        try {
            stmt.execute(
                "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
                "VALUES ('matcha123', 'Matcha', 'User', 'matcha123@gmail.com', 'matcha123@gmail.com', 4, 1)"
            );
        } catch (Exception e) {
            // User already exists, ignore
        }
        
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
                "INSERT INTO Event (Title, Description, StartDateTime, EndDateTime, Location, OrganizerID, EventType) " +
                "VALUES ('Tech Conference 2024', 'Annual technology conference featuring the latest innovations', " +
                "'2024-12-15 09:00:00', '2024-12-15 17:00:00', 'Convention Center, Downtown', 2, 'Conference')"
            );
        } catch (Exception e) {
            // Event already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO Event (Title, Description, StartDateTime, EndDateTime, Location, OrganizerID, EventType) " +
                "VALUES ('Startup Networking', 'Connect with fellow entrepreneurs and investors', " +
                "'2024-12-20 18:00:00', '2024-12-20 21:00:00', 'Innovation Hub, Tech District', 2, 'Networking')"
            );
        } catch (Exception e) {
            // Event already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO Event (Title, Description, StartDateTime, EndDateTime, Location, OrganizerID, EventType) " +
                "VALUES ('AI Workshop', 'Hands-on workshop on artificial intelligence and machine learning', " +
                "'2024-12-25 10:00:00', '2024-12-25 16:00:00', 'Tech Institute, University Campus', 2, 'Workshop')"
            );
        } catch (Exception e) {
            // Event already exists, ignore
        }
        
        // Insert sample presenters
        try {
            stmt.execute(
                "INSERT INTO Presenter (FirstName, LastName, Email, Bio, Organization, Expertise) " +
                "VALUES ('Dr. Sarah', 'Johnson', 'sarah.johnson@tech.com', 'Leading AI researcher with 15 years of experience', 'TechCorp', 'Artificial Intelligence')"
            );
        } catch (Exception e) {
            // Presenter already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO Presenter (FirstName, LastName, Email, Bio, Organization, Expertise) " +
                "VALUES ('Mike', 'Chen', 'mike.chen@startup.com', 'Serial entrepreneur and startup mentor', 'StartupHub', 'Entrepreneurship')"
            );
        } catch (Exception e) {
            // Presenter already exists, ignore
        }
        
        // Insert sample sessions
        try {
            stmt.execute(
                "INSERT INTO Session (EventID, Title, Description, StartTime, EndTime, PresenterID, Room) " +
                "VALUES (1, 'Introduction to AI', 'Overview of artificial intelligence fundamentals', " +
                "'2024-12-15 09:00:00', '2024-12-15 10:30:00', 1, 'Main Hall')"
            );
        } catch (Exception e) {
            // Session already exists, ignore
        }
        
        try {
            stmt.execute(
                "INSERT INTO Session (EventID, Title, Description, StartTime, EndTime, PresenterID, Room) " +
                "VALUES (1, 'Building Successful Startups', 'Key strategies for startup success', " +
                "'2024-12-15 11:00:00', '2024-12-15 12:30:00', 2, 'Conference Room A')"
            );
        } catch (Exception e) {
            // Session already exists, ignore
        }
    }
} 