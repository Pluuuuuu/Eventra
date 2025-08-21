package com.eventra;

// Import HikariCP connection pooling library for efficient database connections
// HikariCP is a high-performance JDBC connection pool that provides better performance
// than traditional connection pools by reducing overhead and improving connection management
import com.zaxxer.hikari.*;
// Import Java I/O classes for reading configuration files
// InputStream is used to read the config.properties file from the classpath
import java.io.InputStream;
// Import Java SQL classes for database operations
// Connection: Represents a connection to the database
// SQLException: Exception thrown for database-related errors
import java.sql.*;
// Import Java Properties class for configuration management
// Properties provides a way to store key-value pairs (like database settings)
import java.util.Properties;

/**
 * Database Connection Manager for Eventra Event Management System
 * 
 * This class manages database connections using HikariCP connection pooling.
 * It provides a centralized way to obtain database connections and ensures
 * efficient resource management by reusing connections instead of creating
 * new ones for each database operation.
 * 
 * Key Features:
 * - Connection pooling for better performance
 * - Configuration-based database settings
 * - Automatic connection validation and cleanup
 * - Graceful error handling for database unavailability
 * 
 * File Usage: Used by all DAO classes and database operations throughout the application
 * 
 * DESIGN PATTERN: This class implements the Singleton pattern for connection pool management
 * and the Factory pattern for providing database connections.
 */
public class Db {
    // Static connection pool that will be shared across all database operations
    // HikariDataSource is the main class that manages the connection pool
    // Static means this variable belongs to the class, not instances of the class
    // This ensures all parts of the application use the same connection pool
    private static HikariDataSource ds;
    
    // Static initialization block - runs once when the class is loaded
    // This sets up the database connection pool with configuration from properties file
    // Static blocks execute when the class is first loaded by the JVM
    // This ensures the connection pool is ready before any database operations
    static {
        // Try-with-resources ensures the InputStream is automatically closed
        // Db.class.getResourceAsStream() loads the config file from the classpath
        // "/config.properties" is the path relative to the resources directory
        try (InputStream in = Db.class.getResourceAsStream("/config.properties")) {
            // Load database configuration from the config.properties file
            // Properties class provides methods to load key-value pairs from a file
            Properties p = new Properties();
            // load() reads the properties file and populates the Properties object
            // This will contain database URL, username, password, etc.
            p.load(in);
            
            // Create HikariCP configuration object for connection pooling
            // HikariConfig holds all the settings for the connection pool
            // This is where we configure performance and behavior parameters
            HikariConfig cfg = new HikariConfig();
            
            // Set database connection parameters from configuration file
            // p.getProperty() retrieves values from the loaded properties file
            cfg.setJdbcUrl(p.getProperty("db.url"));        // Database URL (e.g., jdbc:sqlserver://localhost:1433)
            // The JDBC URL specifies the database type, host, port, and database name
            cfg.setUsername(p.getProperty("db.user"));      // Database username
            // Username for database authentication
            cfg.setPassword(p.getProperty("db.password"));  // Database password
            // Password for database authentication
            
            // Connection pool performance settings
            // These settings optimize the connection pool for the application's needs
            cfg.setConnectionTimeout(60000);     // 60 seconds - max time to wait for a connection
            // If no connection is available in the pool, wait up to 60 seconds
            cfg.setMaximumPoolSize(5);           // Maximum 5 connections in the pool
            // Limits the total number of connections to prevent database overload
            cfg.setMinimumIdle(1);               // Keep at least 1 connection idle
            // Maintains at least one connection ready for immediate use
            cfg.setIdleTimeout(300000);          // 5 minutes - close idle connections after this time
            // Closes connections that haven't been used for 5 minutes to save resources
            cfg.setMaxLifetime(1800000);         // 30 minutes - maximum lifetime of any connection
            // Forces connection replacement every 30 minutes to prevent stale connections
            cfg.setLeakDetectionThreshold(60000); // 1 minute - detect connection leaks
            // Warns if a connection is held for more than 1 minute without being returned
            cfg.setValidationTimeout(10000);     // 10 seconds - timeout for connection validation
            // Time limit for testing if a connection is still valid
            cfg.setConnectionTestQuery("SELECT 1"); // Simple query to test if connection is alive
            // This query is used to verify that a connection is still working
            
            // Create the connection pool with the configured settings
            // HikariDataSource creates and manages the actual connection pool
            // It uses the configuration we just set up
            ds = new HikariDataSource(cfg);
            
            // Confirm successful initialization
            // This message helps developers know the database is ready
            System.out.println("Database connection pool initialized successfully");
        } catch (Exception e) {
            // If database initialization fails, log the error but don't crash the application
            // This allows the application to start even if database is unavailable
            // This is important for development and testing scenarios
            System.err.println("Warning: Failed to initialize DB pool. Database features will be disabled.");
            // System.err outputs to the error stream (console/terminal)
            System.err.println("Error: " + e.getMessage());
            // Print the specific error message for debugging
            e.printStackTrace();
            // Print the full stack trace to help identify the root cause
            ds = null; // Set to null to indicate initialization failure
            // Setting to null allows other methods to detect that initialization failed
        }
    }

    /**
     * Get a database connection from the connection pool
     * This method provides a pooled connection for database operations
     * 
     * @return Connection object for database operations
     *         This connection should be closed when no longer needed
     * @throws SQLException if connection pool is not initialized or connection fails
     *         This exception indicates a database connectivity problem
     * 
     * USAGE PATTERN: This method is called by DAO classes to get database connections
     * The returned connection should be used in a try-with-resources block or
     * explicitly closed to return it to the pool.
     */
    public static Connection get() throws SQLException {
        if (ds == null) {
            // If connection pool is not initialized, throw an exception
            // This prevents the application from trying to use a non-existent pool
            throw new SQLException("Database connection pool not initialized");
        }
        // Return a connection from the pool
        // getConnection() returns an available connection from the pool
        // If no connection is available, it will wait (up to the timeout we configured)
        return ds.getConnection();
    }

    /**
     * Clean up database resources when application shuts down
     * This method should be called during application shutdown to properly close connections
     * 
     * IMPORTANCE: Proper cleanup prevents resource leaks and ensures database connections
     * are properly closed. This is especially important in production environments.
     * 
     * CALLED BY: App.java in the stop() method during application shutdown
     */
    public static void close() {
        if (ds != null) {
            // Close the connection pool and release all database resources
            // This closes all connections in the pool and frees up system resources
            // It's important to call this to prevent memory leaks
            ds.close();
        }
    }
}

/*
 * FILE SUMMARY: Db.java
 * 
 * PRIMARY PURPOSE:
 * This class manages database connections for the Eventra application using HikariCP
 * connection pooling. It provides a centralized, efficient way to obtain database
 * connections and ensures proper resource management.
 * 
 * ROLE WITHIN EVENTRA APPLICATION:
 * - Database connection pool manager
 * - Configuration loader for database settings
 * - Resource cleanup coordinator
 * - Connection provider for all DAO classes
 * 
 * KEY METHODS AND FUNCTIONALITIES:
 * - Static initialization block: Sets up connection pool on class load
 * - get(): Provides database connections from the pool
 * - close(): Cleans up resources during application shutdown
 * - Configuration management: Loads database settings from properties file
 * 
 * INTERACTIONS WITH OTHER SYSTEM COMPONENTS:
 * - Used by all DAO classes (UserDAO, EventDAO, etc.) to get database connections
 * - Called by App.java during application shutdown for cleanup
 * - Reads configuration from /config.properties file
 * - Manages HikariCP connection pool for optimal performance
 * 
 * PERFORMANCE FEATURES:
 * - Connection pooling reduces database connection overhead
 * - Configurable pool size and timeouts for optimal performance
 * - Connection validation and leak detection
 * - Automatic connection lifecycle management
 * 
 * ERROR HANDLING:
 * - Graceful degradation if database is unavailable
 * - Detailed error logging for debugging
 * - Exception propagation for proper error handling
 * 
 * DESIGN PATTERNS USED:
 * - Singleton Pattern: Single connection pool instance
 * - Factory Pattern: Provides database connections
 * - Resource Management Pattern: Proper cleanup and resource handling
 * - Configuration Pattern: Externalized configuration management
 */
