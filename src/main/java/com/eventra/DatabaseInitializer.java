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
            "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='StatusType' AND xtype='U') " +
            "CREATE TABLE StatusType (" +
            "StatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Role Type table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='RoleType' AND xtype='U') " +
            "CREATE TABLE RoleType (" +
            "RoleTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Event Status Type table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='EventStatusType' AND xtype='U') " +
            "CREATE TABLE EventStatusType (" +
            "EventStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Session Status Type table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='SessionStatusType' AND xtype='U') " +
            "CREATE TABLE SessionStatusType (" +
            "SessionStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
        
        // Registration Status Type table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='RegistrationStatusType' AND xtype='U') " +
            "CREATE TABLE RegistrationStatusType (" +
            "RegistrationStatusTypeID INT PRIMARY KEY," +
            "Name VARCHAR(20) NOT NULL UNIQUE" +
            ")"
        );
    }
    
    private static void createMainTables(Statement stmt) throws Exception {
        // UserM table
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='UserM' AND xtype='U') " +
            "CREATE TABLE UserM (" +
            "UserID INT IDENTITY(1,1) PRIMARY KEY," +
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
            "LastFailedLoginAt DATETIME2," +
            "CreatedAt DATETIME2 NOT NULL DEFAULT GETDATE()," +
            "UpdatedAt DATETIME2 NOT NULL DEFAULT GETDATE()" +
            ")"
        );
    }
    
    private static void insertInitialData(Statement stmt) throws Exception {
        // Insert Status Types
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM StatusType WHERE StatusTypeID = 1) " +
            "INSERT INTO StatusType (StatusTypeID, Name) VALUES " +
            "(1, 'Active'), " +
            "(2, 'Inactive'), " +
            "(3, 'Suspended'), " +
            "(4, 'Deleted')"
        );
        
        // Insert Role Types
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM RoleType WHERE RoleTypeID = 1) " +
            "INSERT INTO RoleType (RoleTypeID, Name) VALUES " +
            "(1, 'Administrator'), " +
            "(2, 'User'), " +
            "(3, 'Event Manager'), " +
            "(4, 'Presenter')"
        );
        
        // Insert Event Status Types
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM EventStatusType WHERE EventStatusTypeID = 1) " +
            "INSERT INTO EventStatusType (EventStatusTypeID, Name) VALUES " +
            "(1, 'Draft'), " +
            "(2, 'Published'), " +
            "(3, 'Cancelled'), " +
            "(4, 'Completed')"
        );
        
        // Insert Session Status Types
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM SessionStatusType WHERE SessionStatusTypeID = 1) " +
            "INSERT INTO SessionStatusType (SessionStatusTypeID, Name) VALUES " +
            "(1, 'Draft'), " +
            "(2, 'Confirmed'), " +
            "(3, 'Cancelled'), " +
            "(4, 'Completed')"
        );
        
        // Insert Registration Status Types
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM RegistrationStatusType WHERE RegistrationStatusTypeID = 1) " +
            "INSERT INTO RegistrationStatusType (RegistrationStatusTypeID, Name) VALUES " +
            "(1, 'Pending'), " +
            "(2, 'Confirmed'), " +
            "(3, 'Cancelled'), " +
            "(4, 'Attended')"
        );
        
        // Insert test user (password: test123)
        stmt.execute(
            "IF NOT EXISTS (SELECT * FROM UserM WHERE Email = 'test@eventra.com') " +
            "INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID) " +
            "VALUES ('testuser', 'Test', 'User', 'test@eventra.com', " +
            "'$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 2, 1)"
        );
    }
} 