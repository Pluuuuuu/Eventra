package com.eventra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://eventplanner-srv.database.windows.net:1433;databaseName=EventPlannerDB;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net";
        String user = "sqladmin@eventplanner-srv";
        String password = "Topshop3?";
        
        try {
            System.out.println("🔍 Testing connection to Azure SQL Database...");
            System.out.println("URL: " + url);
            System.out.println("User: " + user);
            
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connection successful!");
            
            // Test a simple query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM INFORMATION_SCHEMA.TABLES");
            
            if (rs.next()) {
                int tableCount = rs.getInt("count");
                System.out.println("📊 Number of tables in database: " + tableCount);
            }
            
            // Check if EventM table exists and count events
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM EventM");
            if (rs.next()) {
                int eventCount = rs.getInt("count");
                System.out.println("📅 Number of events in EventM table: " + eventCount);
            }
            
            // Check for admin users (organizers) - RoleTypeID = 2
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM UserM WHERE RoleTypeID = 2 AND StatusTypeID = 1");
            if (rs.next()) {
                int adminCount = rs.getInt("count");
                System.out.println("👥 Number of admin users (organizers): " + adminCount);
            }
            
            // List all admin users
            System.out.println("\n📋 Admin users (organizers) in database:");
            rs = stmt.executeQuery("SELECT UserID, Username, FirstName, LastName, Email FROM UserM WHERE RoleTypeID = 2 AND StatusTypeID = 1 ORDER BY FirstName, LastName");
            int adminNum = 1;
            while (rs.next()) {
                int userId = rs.getInt("UserID");
                String username = rs.getString("Username");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");
                System.out.println("  " + adminNum + ". " + firstName + " " + lastName + " (" + username + ") - " + email + " [ID: " + userId + "]");
                adminNum++;
            }
            
            // List all events with their organizers
            System.out.println("\n📅 Events with their organizers:");
            rs = stmt.executeQuery("SELECT e.EventID, e.Title, e.StartDate, e.EndDate, u.FirstName, u.LastName, u.Username FROM EventM e LEFT JOIN UserM u ON e.CreatedByUserID = u.UserID WHERE e.StatusTypeID = 1 ORDER BY e.StartDate");
            int eventNum = 1;
            while (rs.next()) {
                int eventId = rs.getInt("EventID");
                String title = rs.getString("Title");
                String startDate = rs.getString("StartDate");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String username = rs.getString("Username");
                String organizer = (firstName != null && lastName != null) ? firstName + " " + lastName : "Unknown";
                System.out.println("  " + eventNum + ". " + title + " - Organized by: " + organizer + " (" + username + ") [Event ID: " + eventId + "]");
                eventNum++;
            }
            
            conn.close();
            System.out.println("\n✅ Test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 