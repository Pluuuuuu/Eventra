package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserDAO {
    
    /**
     * Authenticate user by email and password
     */
    public static Optional<User> authenticateUser(String email, String password) {
        String sql = "SELECT * FROM UserM WHERE Email = ? AND StatusTypeID = 1";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("PasswordHash");
                
                // Check if password matches - handle both plain text and BCrypt hashes
                boolean passwordMatches = false;
                
                // First, try BCrypt verification (for properly hashed passwords)
                try {
                    passwordMatches = BCrypt.checkpw(password, storedHash);
                } catch (Exception e) {
                    // If BCrypt fails, it might be a plain text password
                    System.out.println("BCrypt verification failed, trying plain text comparison");
                }
                
                // If BCrypt failed, try plain text comparison (for legacy data)
                if (!passwordMatches) {
                    passwordMatches = password.equals(storedHash);
                }
                
                if (passwordMatches) {
                    User user = mapResultSetToUser(rs);
                    
                    // If the password was plain text, hash it and update the database
                    if (password.equals(storedHash)) {
                        System.out.println("Updating plain text password to BCrypt hash for user: " + email);
                        updatePasswordToHash(user.getUserId(), password);
                    }
                    
                    return Optional.of(user);
                } else {
                    // Update failed login attempt
                    updateFailedLoginAttempt(rs.getInt("UserID"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Create a new user
     */
    public static boolean createUser(User user) {
        String sql = "INSERT INTO UserM (Username, FirstName, MiddleName, LastName, Email, PasswordHash, " +
                    "ProfilePicUrl, RoleTypeID, StatusTypeID, PeriodCanLoginInMinutes, CreatedAt, UpdatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getMiddleName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPasswordHash());
            stmt.setString(7, user.getProfilePicUrl());
            stmt.setInt(8, user.getRoleTypeId());
            stmt.setInt(9, user.getStatusTypeId());
            stmt.setInt(10, user.getPeriodCanLoginInMinutes());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if email already exists
     */
    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM UserM WHERE Email = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if username already exists
     */
    public static boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM UserM WHERE Username = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get user by email
     */
    public static Optional<User> getUserByEmail(String email) {
        String sql = "SELECT * FROM UserM WHERE Email = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Update failed login attempt timestamp
     */
    private static void updateFailedLoginAttempt(int userId) {
        String sql = "UPDATE UserM SET LastFailedLoginAt = CURRENT_TIMESTAMP WHERE UserID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error updating failed login attempt: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Hash password using BCrypt
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Update password to BCrypt hash
     */
    private static void updatePasswordToHash(int userId, String password) {
        String sql = "UPDATE UserM SET PasswordHash = ? WHERE UserID = ?";
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating password to hash: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Map ResultSet to User object
     */
    private static User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserID"));
        user.setUsername(rs.getString("Username"));
        user.setFirstName(rs.getString("FirstName"));
        user.setMiddleName(rs.getString("MiddleName"));
        user.setLastName(rs.getString("LastName"));
        user.setEmail(rs.getString("Email"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setProfilePicUrl(rs.getString("ProfilePicUrl"));
        user.setRoleTypeId(rs.getInt("RoleTypeID"));
        user.setStatusTypeId(rs.getInt("StatusTypeID"));
        user.setPeriodCanLoginInMinutes(rs.getInt("PeriodCanLoginInMinutes"));
        
        Timestamp lastFailedLogin = rs.getTimestamp("LastFailedLoginAt");
        if (lastFailedLogin != null) {
            user.setLastFailedLoginAt(lastFailedLogin.toLocalDateTime());
        }
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
} 