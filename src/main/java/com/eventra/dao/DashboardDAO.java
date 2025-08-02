package com.eventra.dao;

import com.eventra.Db;
import java.sql.*;

public class DashboardDAO {
    
    /**
     * Get total count of events from EventM table
     */
    public static int getTotalEventsCount() {
        String sql = "SELECT COUNT(*) FROM EventM WHERE StatusTypeID IN (1, 2)"; // Draft and Published
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total events count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get total count of users from UserM table
     */
    public static int getTotalUsersCount() {
        String sql = "SELECT COUNT(*) FROM UserM WHERE StatusTypeID = 1"; // Active users only
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total users count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get total count of registrations from Registration table
     */
    public static int getTotalRegistrationsCount() {
        String sql = "SELECT COUNT(*) FROM Registration WHERE RegistrationStatusTypeID IN (1, 2)"; // Pending and Confirmed
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total registrations count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get total count of attendees from Attendee table
     */
    public static int getTotalAttendeesCount() {
        String sql = "SELECT COUNT(*) FROM Attendee WHERE StatusTypeID = 1"; // Active attendees only
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total attendees count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get registrations count for current month
     */
    public static int getRegistrationsThisMonth() {
        String sql = "SELECT COUNT(*) FROM Registration " +
                    "WHERE MONTH(CreatedAt) = MONTH(CURRENT_DATE) " +
                    "AND YEAR(CreatedAt) = YEAR(CURRENT_DATE) " +
                    "AND RegistrationStatusTypeID IN (1, 2)"; // Pending and Confirmed
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting registrations this month: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get active events count (Published events)
     */
    public static int getActiveEventsCount() {
        String sql = "SELECT COUNT(*) FROM EventM WHERE StatusTypeID = 2"; // Published events
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active events count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get comprehensive dashboard metrics
     */
    public static DashboardMetrics getDashboardMetrics() {
        DashboardMetrics metrics = new DashboardMetrics();
        
        try {
            metrics.totalEvents = getActiveEventsCount();
            metrics.totalUsers = getTotalUsersCount();
            metrics.totalRegistrations = getRegistrationsThisMonth();
            metrics.totalAttendees = getTotalAttendeesCount();
            
            System.out.println("Dashboard metrics loaded: Events=" + metrics.totalEvents + 
                             ", Users=" + metrics.totalUsers + 
                             ", Registrations=" + metrics.totalRegistrations + 
                             ", Attendees=" + metrics.totalAttendees);
            
        } catch (Exception e) {
            System.err.println("Error loading dashboard metrics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return metrics;
    }
    
    // ADMIN-SPECIFIC METRICS (based on CreatedByUserID)
    
    /**
     * Get count of events created by a specific admin user
     */
    public static int getMyEventsCount(int adminUserId) {
        String sql = "SELECT COUNT(*) FROM EventM WHERE CreatedByUserID = ? AND StatusTypeID IN (1, 2)"; // Draft and Published
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, adminUserId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting my events count for admin " + adminUserId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get count of registrations for events created by a specific admin user
     */
    public static int getRegistrationsForMyEvents(int adminUserId) {
        String sql = "SELECT COUNT(*) FROM Registration r " +
                    "INNER JOIN EventM e ON r.EventID = e.EventID " +
                    "WHERE e.CreatedByUserID = ? AND r.RegistrationStatusTypeID IN (1, 2)"; // Pending and Confirmed
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, adminUserId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting registrations for admin " + adminUserId + " events: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get count of active (published) events created by a specific admin user  
     */
    public static int getActiveEventsForAdmin(int adminUserId) {
        String sql = "SELECT COUNT(*) FROM EventM WHERE CreatedByUserID = ? AND StatusTypeID = 2"; // Published events only
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, adminUserId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active events for admin " + adminUserId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get comprehensive admin-specific dashboard metrics
     */
    public static AdminDashboardMetrics getAdminDashboardMetrics(int adminUserId) {
        AdminDashboardMetrics metrics = new AdminDashboardMetrics();
        
        try {
            metrics.myEvents = getMyEventsCount(adminUserId);
            metrics.totalRegistrations = getRegistrationsForMyEvents(adminUserId);
            metrics.activeEvents = getActiveEventsForAdmin(adminUserId);
            
            System.out.println("Admin dashboard metrics loaded for user " + adminUserId + ": " + 
                             "MyEvents=" + metrics.myEvents + 
                             ", Registrations=" + metrics.totalRegistrations + 
                             ", ActiveEvents=" + metrics.activeEvents);
            
        } catch (Exception e) {
            System.err.println("Error loading admin dashboard metrics for user " + adminUserId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return metrics;
    }

    /**
     * Inner class to hold dashboard metrics
     */
    public static class DashboardMetrics {
        public int totalEvents = 0;
        public int totalUsers = 0;
        public int totalRegistrations = 0;
        public int totalAttendees = 0;
    }
    
    /**
     * Inner class to hold admin-specific dashboard metrics
     */
    public static class AdminDashboardMetrics {
        public int myEvents = 0;
        public int totalRegistrations = 0;
        public int activeEvents = 0;
    }
}