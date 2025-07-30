package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Attendee;

import java.sql.*;
import java.time.LocalDateTime;

public class AttendeeDAO {

    /**
     * Create a new attendee in the database
     */
    public static boolean createAttendee(Attendee attendee) {
        String sql = "INSERT INTO Attendee (UserID, FirstName, MiddleName, LastName, Email, Organization, " +
                    "Phone, Location, Gender, DateOfBirth, ProfilePicUrl, Type, PasswordHash, " +
                    "StatusTypeID, PeriodCanLoginInMinutes, CreatedAt, UpdatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETUTCDATE(), GETUTCDATE())";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, attendee.getUserId());
            stmt.setString(2, attendee.getFirstName());
            stmt.setString(3, attendee.getMiddleName());
            stmt.setString(4, attendee.getLastName());
            stmt.setString(5, attendee.getEmail());
            stmt.setString(6, attendee.getOrganization());
            stmt.setString(7, attendee.getPhone());
            stmt.setString(8, attendee.getLocation());
            stmt.setString(9, attendee.getGender());
            
            // Handle date conversion
            if (attendee.getDateOfBirth() != null) {
                stmt.setDate(10, Date.valueOf(attendee.getDateOfBirth()));
            } else {
                stmt.setNull(10, Types.DATE);
            }
            
            stmt.setString(11, attendee.getProfilePicUrl());
            stmt.setString(12, attendee.getType());
            stmt.setString(13, attendee.getPasswordHash());
            stmt.setInt(14, attendee.getStatusTypeId());
            stmt.setInt(15, attendee.getPeriodCanLoginInMinutes());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    attendee.setAttendeeId(generatedKeys.getInt(1));
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating attendee: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Check if attendee email already exists
     */
    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Attendee WHERE Email = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking if attendee email exists: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get attendee by email
     */
    public static Attendee getAttendeeByEmail(String email) {
        String sql = "SELECT * FROM Attendee WHERE Email = ? AND StatusTypeID = 1";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Attendee attendee = new Attendee();
                attendee.setAttendeeId(rs.getInt("AttendeeID"));
                attendee.setUserId(rs.getInt("UserID"));
                attendee.setFirstName(rs.getString("FirstName"));
                attendee.setMiddleName(rs.getString("MiddleName"));
                attendee.setLastName(rs.getString("LastName"));
                attendee.setEmail(rs.getString("Email"));
                attendee.setOrganization(rs.getString("Organization"));
                attendee.setPhone(rs.getString("Phone"));
                attendee.setLocation(rs.getString("Location"));
                attendee.setGender(rs.getString("Gender"));
                
                Date dob = rs.getDate("DateOfBirth");
                if (dob != null) {
                    attendee.setDateOfBirth(dob.toLocalDate());
                }
                
                attendee.setProfilePicUrl(rs.getString("ProfilePicUrl"));
                attendee.setType(rs.getString("Type"));
                attendee.setPasswordHash(rs.getString("PasswordHash"));
                attendee.setStatusTypeId(rs.getInt("StatusTypeID"));
                attendee.setPeriodCanLoginInMinutes(rs.getInt("PeriodCanLoginInMinutes"));
                
                // Handle timestamp conversion
                Timestamp lastFailed = rs.getTimestamp("LastFailedLoginAt");
                if (lastFailed != null) {
                    attendee.setLastFailedLoginAt(lastFailed.toLocalDateTime());
                }
                
                Timestamp created = rs.getTimestamp("CreatedAt");
                if (created != null) {
                    attendee.setCreatedAt(created.toLocalDateTime());
                }
                
                Timestamp updated = rs.getTimestamp("UpdatedAt");
                if (updated != null) {
                    attendee.setUpdatedAt(updated.toLocalDateTime());
                }
                
                return attendee;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting attendee by email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Get attendee by ID
     */
    public static Attendee getAttendeeById(int attendeeId) {
        String sql = "SELECT * FROM Attendee WHERE AttendeeID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, attendeeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Attendee attendee = new Attendee();
                attendee.setAttendeeId(rs.getInt("AttendeeID"));
                attendee.setUserId(rs.getInt("UserID"));
                attendee.setFirstName(rs.getString("FirstName"));
                attendee.setMiddleName(rs.getString("MiddleName"));
                attendee.setLastName(rs.getString("LastName"));
                attendee.setEmail(rs.getString("Email"));
                attendee.setOrganization(rs.getString("Organization"));
                attendee.setPhone(rs.getString("Phone"));
                attendee.setLocation(rs.getString("Location"));
                attendee.setGender(rs.getString("Gender"));
                
                Date dob = rs.getDate("DateOfBirth");
                if (dob != null) {
                    attendee.setDateOfBirth(dob.toLocalDate());
                }
                
                attendee.setProfilePicUrl(rs.getString("ProfilePicUrl"));
                attendee.setType(rs.getString("Type"));
                attendee.setPasswordHash(rs.getString("PasswordHash"));
                attendee.setStatusTypeId(rs.getInt("StatusTypeID"));
                attendee.setPeriodCanLoginInMinutes(rs.getInt("PeriodCanLoginInMinutes"));
                
                // Handle timestamp conversion
                Timestamp lastFailed = rs.getTimestamp("LastFailedLoginAt");
                if (lastFailed != null) {
                    attendee.setLastFailedLoginAt(lastFailed.toLocalDateTime());
                }
                
                Timestamp created = rs.getTimestamp("CreatedAt");
                if (created != null) {
                    attendee.setCreatedAt(created.toLocalDateTime());
                }
                
                Timestamp updated = rs.getTimestamp("UpdatedAt");
                if (updated != null) {
                    attendee.setUpdatedAt(updated.toLocalDateTime());
                }
                
                return attendee;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting attendee by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Update attendee information
     */
    public static boolean updateAttendee(Attendee attendee) {
        String sql = "UPDATE Attendee SET FirstName = ?, MiddleName = ?, LastName = ?, " +
                    "Organization = ?, Phone = ?, Location = ?, Gender = ?, DateOfBirth = ?, " +
                    "ProfilePicUrl = ?, Type = ?, UpdatedAt = GETUTCDATE() " +
                    "WHERE AttendeeID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, attendee.getFirstName());
            stmt.setString(2, attendee.getMiddleName());
            stmt.setString(3, attendee.getLastName());
            stmt.setString(4, attendee.getOrganization());
            stmt.setString(5, attendee.getPhone());
            stmt.setString(6, attendee.getLocation());
            stmt.setString(7, attendee.getGender());
            
            if (attendee.getDateOfBirth() != null) {
                stmt.setDate(8, Date.valueOf(attendee.getDateOfBirth()));
            } else {
                stmt.setNull(8, Types.DATE);
            }
            
            stmt.setString(9, attendee.getProfilePicUrl());
            stmt.setString(10, attendee.getType());
            stmt.setInt(11, attendee.getAttendeeId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating attendee: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
} 