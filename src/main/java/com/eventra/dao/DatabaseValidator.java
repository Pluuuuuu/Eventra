package com.eventra.dao;

import com.eventra.Db;
import java.sql.*;

public class DatabaseValidator {
    
    public static void validateDatabase() {
        System.out.println("🔍 Validating database structure...");
        
        try (Connection conn = Db.get()) {
            
            // Check if UserM table exists and has data
            validateUserMTable(conn);
            
            // Check if EventM table exists and has proper constraints
            validateEventMTable(conn);
            
            // Check foreign key constraints
            validateForeignKeys(conn);
            
            System.out.println("✅ Database validation completed");
            
        } catch (Exception e) {
            System.err.println("❌ Database validation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void validateUserMTable(Connection conn) throws SQLException {
        System.out.println("🔍 Checking UserM table...");
        
        // Check if table exists
        String checkTableSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'UserM'";
        try (PreparedStatement stmt = conn.prepareStatement(checkTableSql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("✅ UserM table exists");
            } else {
                System.err.println("❌ UserM table does not exist!");
                return;
            }
        }
        
        // Check if table has data
        String countSql = "SELECT COUNT(*) FROM UserM";
        try (PreparedStatement stmt = conn.prepareStatement(countSql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("📊 UserM table has " + count + " users");
                
                if (count == 0) {
                    System.err.println("⚠️ Warning: UserM table is empty!");
                }
            }
        }
        
        // List all users
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
    
    private static void validateEventMTable(Connection conn) throws SQLException {
        System.out.println("🔍 Checking EventM table...");
        
        // Check if table exists
        String checkTableSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EventM'";
        try (PreparedStatement stmt = conn.prepareStatement(checkTableSql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("✅ EventM table exists");
            } else {
                System.err.println("❌ EventM table does not exist!");
                return;
            }
        }
        
        // Check foreign key constraints
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