/**
 * User Data Access Object (UserDAO.java)
 * 
 * This class handles ALL database operations related to users in the Eventra system.
 * Think of it as the "database manager" for user accounts - it's responsible for
 * creating, reading, updating, and deleting user information in the database.
 * 
 * WHAT THIS CLASS DOES:
 * - User authentication (login verification)
 * - User creation and registration
 * - User information updates
 * - Password management and security
 * - User search and retrieval
 * - Failed login tracking for security
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - DAO Pattern: Data Access Object - separates database logic from business logic
 * - Prepared Statements: Prevents SQL injection attacks
 * - Connection Pooling: Uses the Db class to get database connections
 * - BCrypt Hashing: Secure password storage and verification
 * - Optional Pattern: Modern Java way to handle "maybe exists" scenarios
 * - Transaction Management: Ensures database operations are atomic
 * 
 * SECURITY FEATURES:
 * - BCrypt password hashing with salt
 * - SQL injection prevention
 * - Failed login attempt tracking
 * - Automatic password upgrade from plain text to hashed
 * 
 * DATABASE OPERATIONS:
 * - SELECT: Retrieve user information
 * - INSERT: Create new user accounts
 * - UPDATE: Modify existing user data
 * - DELETE: Remove user accounts (if needed)
 */
package com.eventra.dao;

// These imports give us access to the tools we need:
import com.eventra.Db;                    // Database connection manager (our custom class)
import com.eventra.model.User;             // User data model class
import org.mindrot.jbcrypt.BCrypt;        // Secure password hashing library

// Java standard library imports for database and utility operations:
import java.sql.*;                         // Database operations (Connection, PreparedStatement, ResultSet, etc.)
import java.time.LocalDateTime;            // Modern date/time handling
import java.util.Optional;                 // Modern Java way to handle "maybe exists" values
import java.util.List;                     // Collection for storing multiple users
import java.util.ArrayList;                // Specific type of List implementation

/**
 * UserDAO class - Data Access Object for User Management
 * 
 * This class implements the DAO (Data Access Object) pattern, which is a design pattern
 * that separates database access logic from business logic. This makes the code:
 * - More organized and easier to maintain
 * - Easier to test (you can test database logic separately)
 * - More flexible (you can change how data is stored without changing business logic)
 * 
 * All methods are static, which means you don't need to create a UserDAO object
 * to use them. You can call them directly like: UserDAO.authenticateUser(email, password)
 */
public class UserDAO {
    
    /**
     * authenticateUser() method - Verifies user login credentials
     * 
     * This is the CORE method for user authentication. When someone tries to log in,
     * this method checks if their email and password are correct.
     * 
     * HOW IT WORKS:
     * 1. Look up the user by email in the database
     * 2. Check if the password matches (using secure BCrypt hashing)
     * 3. If password was plain text, automatically upgrade it to hashed version
     * 4. Track failed login attempts for security monitoring
     * 5. Return the user if authentication succeeds, or empty if it fails
     * 
     * SECURITY FEATURES:
     * - Uses prepared statements to prevent SQL injection
     * - BCrypt password verification (industry standard)
     * - Automatic password upgrade from plain text to hashed
     * - Failed login attempt tracking
     * - Only allows active users to log in (StatusTypeID = 1)
     * 
     * @param email - The email address the user is trying to log in with
     * @param password - The plain text password the user entered
     * @return Optional<User> - Contains user data if login succeeds, empty if it fails
     * @throws SQLException - If there's a database error during authentication
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
     * createUser() method - Creates a new user account in the database
     * 
     * This method is used when someone signs up for a new account. It takes all the
     * user information and stores it securely in the database.
     * 
     * HOW IT WORKS:
     * 1. Prepare an INSERT SQL statement with placeholders (?) for security
     * 2. Set all the user data (name, email, password hash, role, etc.)
     * 3. Execute the statement to create the user record
     * 4. Get the auto-generated user ID from the database
     * 5. Return true if successful, false if something goes wrong
     * 
     * SECURITY FEATURES:
     * - Uses prepared statements to prevent SQL injection
     * - Stores encrypted passwords (never plain text)
     * - Sets default role and status for new users
     * - Automatically sets creation and update timestamps
     * 
     * DATABASE OPERATION:
     * - INSERT: Creates a new row in the UserM table
     * - Statement.RETURN_GENERATED_KEYS: Gets the auto-generated user ID
     * - CURRENT_TIMESTAMP: Sets creation time automatically
     * 
     * @param user - User object containing all the account information
     * @return boolean - true if user was created successfully, false otherwise
     * @throws SQLException - If there's a database error during creation
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
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
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
     * Update user information
     */
    public static boolean updateUser(User user) {
        String sql = "UPDATE UserM SET Username = ?, FirstName = ?, MiddleName = ?, LastName = ?, " +
                    "Email = ?, ProfilePicUrl = ?, UpdatedAt = CURRENT_TIMESTAMP WHERE UserID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getMiddleName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getProfilePicUrl());
            stmt.setInt(7, user.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get user by ID
     */
    public static User getUserById(int userId) {
        String sql = "SELECT * FROM UserM WHERE UserID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get users by role
     */
    public static List<User> getUsersByRole(int roleTypeId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM UserM WHERE RoleTypeID = ? AND StatusTypeID = 1 ORDER BY FirstName, LastName";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roleTypeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users by role: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
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

    public void debugPrintAllUsers() {
        String sql = "SELECT UserID, Username, Email, RoleTypeID FROM UserM";
        System.out.println("🔍 Checking all users in database...");
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                int userId = rs.getInt("UserID");
                String username = rs.getString("Username");
                String email = rs.getString("Email");
                int roleId = rs.getInt("RoleTypeID");
                System.out.println("👤 User " + count + ": ID=" + userId + ", Username=" + username + ", Email=" + email + ", Role=" + roleId);
                count++;
            }
            System.out.println("📊 Total users in database: " + count);
        } catch (SQLException e) {
            System.err.println("❌ Error checking users: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 