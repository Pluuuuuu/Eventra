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
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'StatusType') " +
            "CREATE TABLE StatusType (" +
            "StatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Role Type table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'RoleType') " +
            "CREATE TABLE RoleType (" +
            "RoleTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Event Status Type table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EventStatusType') " +
            "CREATE TABLE EventStatusType (" +
            "EventStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Session Status Type table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SessionStatusType') " +
            "CREATE TABLE SessionStatusType (" +
            "SessionStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Registration Status Type table
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
        
        // Attendee table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Attendee') " +
            "CREATE TABLE Attendee (" +
            "AttendeeID INT IDENTITY(1,1) PRIMARY KEY," +
            "UserID INT NOT NULL," +
            "FirstName NVARCHAR(100) NOT NULL," +
            "MiddleName NVARCHAR(100)," +
            "LastName NVARCHAR(100) NOT NULL," +
            "Email NVARCHAR(255) NOT NULL UNIQUE," +
            "Organization NVARCHAR(255) NOT NULL," +
            "Phone NVARCHAR(20)," +
            "Location NVARCHAR(255)," +
            "Gender NVARCHAR(10)," +
            "DateOfBirth DATE," +
            "ProfilePicUrl NVARCHAR(500)," +
            "Type NVARCHAR(50) NOT NULL DEFAULT 'Regular'," +
            "PasswordHash NVARCHAR(255) NOT NULL," +
            "StatusTypeID INT NOT NULL DEFAULT 1," +
            "PeriodCanLoginInMinutes INT NOT NULL DEFAULT 0," +
            "LastFailedLoginAt DATETIME," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "FOREIGN KEY (UserID) REFERENCES UserM(UserID)" +
            ")"
        );
        
        // EventM table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EventM') " +
            "CREATE TABLE EventM (" +
            "EventID INT IDENTITY(1,1) PRIMARY KEY," +
            "Title NVARCHAR(255) NOT NULL," +
            "Description NTEXT," +
            "StartDate DATETIME NOT NULL," +
            "EndDate DATETIME NOT NULL," +
            "CreatedByUserID INT NOT NULL," +
            "StatusTypeID INT NOT NULL DEFAULT 1," +
            "CreatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME NOT NULL DEFAULT GETDATE()," +
            "FOREIGN KEY (CreatedByUserID) REFERENCES UserM(UserID)," +
            "FOREIGN KEY (StatusTypeID) REFERENCES EventStatusType(EventStatusTypeID)" +
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
            "FOREIGN KEY (AttendeeID) REFERENCES Attendee(AttendeeID)," +
            "FOREIGN KEY (EventID) REFERENCES EventM(EventID)," +
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
        
        // Insert sample venues first
        String[] venueData = {
            "('Grand Conference Center', '1234 Main Street, City Center', 500, 5, 'https://maps.example.com/venue1')",
            "('Tech Hub Auditorium', '5678 Innovation Drive, Tech District', 300, 3, 'https://maps.example.com/venue2')",
            "('Business Plaza Hall', '9012 Corporate Ave, Downtown', 200, 4, 'https://maps.example.com/venue3')",
            "('Community Center', '3456 Community Blvd, Suburbs', 150, 2, 'https://maps.example.com/venue4')",
            "('Online Virtual Space', 'Virtual Meeting Platform', 1000, 1, 'https://platform.example.com/virtual')"
        };
        
        for (int i = 0; i < venueData.length; i++) {
            try {
                stmt.execute(
                    "INSERT INTO Venue (Name, Address, Capacity, Rooms, MapLink) " +
                    "VALUES " + venueData[i]
                );
                System.out.println("✅ Sample venue " + (i+1) + " created");
            } catch (Exception e) {
                // Venue already exists, ignore
                System.out.println("ℹ️ Sample venue " + (i+1) + " already exists, skipping");
            }
        }
        
        // Get the test admin user ID for sample events
        int testAdminUserId = 23; // Based on logs, this is the ID of test@eventra.com
        
        // Insert multiple sample events created by the test admin (now with VenueID)
        String[] eventData = {
            "('Tech Conference 2024', 'Annual technology conference', '2024-03-15 09:00:00', '2024-03-15 18:00:00', 1, " + testAdminUserId + ", 2)",
            "('Business Summit', 'Quarterly business meeting', '2024-02-20 10:00:00', '2024-02-20 16:00:00', 3, " + testAdminUserId + ", 2)",
            "('Innovation Workshop', 'Workshop on latest innovations', '2024-04-10 14:00:00', '2024-04-10 17:00:00', 2, " + testAdminUserId + ", 2)",
            "('Marketing Seminar', 'Digital marketing strategies', '2024-05-05 10:00:00', '2024-05-05 15:00:00', 2, " + testAdminUserId + ", 2)",
            "('Leadership Forum', 'Leadership development program', '2024-06-12 09:00:00', '2024-06-12 17:00:00', 1, " + testAdminUserId + ", 2)",
            "('Product Launch', 'New product unveiling event', '2024-07-18 11:00:00', '2024-07-18 16:00:00', 3, " + testAdminUserId + ", 1)",
            "('Networking Mixer', 'Professional networking event', '2024-08-22 18:00:00', '2024-08-22 21:00:00', 4, " + testAdminUserId + ", 2)",
            "('Training Bootcamp', 'Intensive skills training', '2024-09-30 08:00:00', '2024-10-02 18:00:00', 2, " + testAdminUserId + ", 1)",
            "('Annual Gala', 'Company annual celebration', '2024-11-15 19:00:00', '2024-11-15 23:00:00', 1, " + testAdminUserId + ", 2)",
            "('Year-End Review', 'Annual performance review meeting', '2024-12-20 13:00:00', '2024-12-20 17:00:00', 5, " + testAdminUserId + ", 2)"
        };
        
        for (int i = 0; i < eventData.length; i++) {
            try {
                stmt.execute(
                    "INSERT INTO EventM (Title, Description, StartDate, EndDate, VenueID, CreatedByUserID, StatusTypeID) " +
                    "VALUES " + eventData[i]
                );
                System.out.println("✅ Sample event " + (i+1) + " created for admin user " + testAdminUserId);
            } catch (Exception e) {
                // Event already exists, ignore
                System.out.println("ℹ️ Sample event " + (i+1) + " already exists, skipping");
            }
        }
        
        // Insert multiple sample registrations to create realistic data
        // Generate registrations for each attendee to multiple events
        for (int attendeeId = 1; attendeeId <= 10; attendeeId++) {
            for (int eventId = 1; eventId <= 10; eventId++) {
                // Not every attendee registers for every event - add some randomness
                if ((attendeeId + eventId) % 3 != 0) { // Skip some combinations to make it more realistic
                    try {
                        stmt.execute(
                            "INSERT INTO Registration (AttendeeID, EventID, RegistrationStatusTypeID) " +
                            "VALUES (" + attendeeId + ", " + eventId + ", " + 
                            (((attendeeId + eventId) % 4 == 0) ? "1" : "2") + ")" // Mix of Pending and Confirmed
                        );
                    } catch (Exception e) {
                        // Registration already exists, ignore
                    }
                }
            }
        }
    }
} 