package com.eventra;

import java.sql.*;

public class DatabaseDebugger {
    
    public static void main(String[] args) {
        System.out.println("🔍 Database Debugger Starting...");
        
        try {
            // Test connection
            Connection conn = Db.get();
            System.out.println("✅ Database connection successful!");
            
            // Check if tables exist
            checkTablesExist(conn);
            
            // Check UserM table
            checkUserMTable(conn);
            
            // Check EventM table
            checkEventMTable(conn);
            
            // Check Venue table
            checkVenueTable(conn);
            
            conn.close();
            
        } catch (Exception e) {
            System.err.println("❌ Database debug error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkTablesExist(Connection conn) throws SQLException {
        System.out.println("\n📋 Checking if tables exist...");
        
        String[] tables = {"UserM", "EventM", "Venue", "Registration", "Attendee"};
        
        for (String table : tables) {
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, table);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("   " + table + ": " + (count > 0 ? "✅ EXISTS" : "❌ NOT FOUND"));
                }
            }
        }
    }
    
    private static void checkUserMTable(Connection conn) throws SQLException {
        System.out.println("\n👤 Checking UserM table...");
        
        String sql = "SELECT UserID, Username, Email, RoleTypeID FROM UserM ORDER BY UserID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 0;
            while (rs.next()) {
                count++;
                int userId = rs.getInt("UserID");
                String username = rs.getString("Username");
                String email = rs.getString("Email");
                int roleId = rs.getInt("RoleTypeID");
                System.out.println("   User " + count + ": ID=" + userId + ", Username=" + username + ", Email=" + email + ", Role=" + roleId);
            }
            System.out.println("   Total users: " + count);
        }
    }
    
    private static void checkEventMTable(Connection conn) throws SQLException {
        System.out.println("\n🎫 Checking EventM table...");
        
        String sql = "SELECT EventID, Title, StartDate, EndDate, StatusTypeID, CreatedByUserID FROM EventM ORDER BY EventID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 0;
            while (rs.next()) {
                count++;
                int eventId = rs.getInt("EventID");
                String title = rs.getString("Title");
                Timestamp startDate = rs.getTimestamp("StartDate");
                Timestamp endDate = rs.getTimestamp("EndDate");
                int statusId = rs.getInt("StatusTypeID");
                int createdBy = rs.getInt("CreatedByUserID");
                System.out.println("   Event " + count + ": ID=" + eventId + ", Title=" + title + ", Status=" + statusId + ", CreatedBy=" + createdBy);
                System.out.println("      Start: " + startDate + ", End: " + endDate);
            }
            System.out.println("   Total events: " + count);
        }
    }
    
    private static void checkVenueTable(Connection conn) throws SQLException {
        System.out.println("\n🏢 Checking Venue table...");
        
        String sql = "SELECT VenueID, Name, Capacity FROM Venue ORDER BY VenueID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int count = 0;
            while (rs.next()) {
                count++;
                int venueId = rs.getInt("VenueID");
                String name = rs.getString("Name");
                int capacity = rs.getInt("Capacity");
                System.out.println("   Venue " + count + ": ID=" + venueId + ", Name=" + name + ", Capacity=" + capacity);
            }
            System.out.println("   Total venues: " + count);
        }
    }
} 