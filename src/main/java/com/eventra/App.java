package com.eventra;

// Import JavaFX application framework classes
// Application: The base class for JavaFX applications - provides lifecycle methods
import javafx.application.Application;
// FXMLLoader: Loads FXML files and creates the UI components defined in them
import javafx.fxml.FXMLLoader;
// Scene: Represents the content of a window - contains all UI elements
import javafx.scene.Scene;
// Stage: Represents a window in JavaFX - the top-level container
import javafx.stage.Stage;

/**
 * Main Application Class for Eventra Event Management System
 * 
 * This class serves as the entry point for the JavaFX application.
 * It extends Application to create a desktop GUI application and handles
 * the application lifecycle including startup and shutdown procedures.
 * 
 * Key Responsibilities:
 * - Initialize the database connection and schema
 * - Load the initial Welcome screen
 * - Set up the main application window
 * - Handle application shutdown and cleanup
 * 
 * File Usage: This is the primary entry point called by the JVM when starting the application
 * 
 * DESIGN PATTERN: This class follows the JavaFX Application lifecycle pattern
 * - Extends Application class to inherit JavaFX framework functionality
 * - Overrides start() method for application initialization
 * - Overrides stop() method for cleanup operations
 * - Uses main() method as the JVM entry point
 */
public class App extends Application {
    
    /**
     * Application startup method - called by JavaFX framework when application launches
     * This method is automatically called by the JavaFX framework after the Application
     * class is instantiated and before the UI is displayed to the user.
     * 
     * @param stage The primary stage (main window) provided by JavaFX framework
     *              This is the main window of the application that will contain all UI elements
     * @throws Exception If any error occurs during startup - this will prevent the application from starting
     * 
     * EXECUTION FLOW:
     * 1. Database initialization
     * 2. FXML loading
     * 3. Scene creation
     * 4. Stage configuration
     * 5. Window display
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Initialize database connection and create tables if they don't exist
            // This ensures the application has a working database before proceeding
            // This is a critical step - without a database, the application cannot function
            // The DatabaseInitializer will create all necessary tables and insert initial data
            DatabaseInitializer.initializeDatabase();
            
            // Create FXMLLoader to load the Welcome.fxml file from resources
            // This loads the initial user interface layout
            // getClass().getResource() looks for the FXML file in the classpath
            // The "/fxml/Welcome.fxml" path is relative to the resources directory
            // FXMLLoader will parse the XML and create JavaFX UI components
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Welcome.fxml"));
            
            // Create a new Scene with the loaded FXML content
            // A Scene represents the content of a window in JavaFX
            // loader.load() returns the root node of the FXML hierarchy
            // The Scene will contain all UI elements defined in the FXML file
            Scene scene = new Scene(loader.load());
            
            // Set the window title that appears in the title bar
            // This provides user identification of the application window
            // The title will appear in the operating system's window management
            stage.setTitle("Eventra");
            
            // Attach the scene to the stage (main window)
            // This connects the UI content (Scene) to the window container (Stage)
            // Without this, the window would be empty
            stage.setScene(scene);
            
            // Display the window to the user
            // This makes the window visible and brings it to the front
            // The application is now fully initialized and ready for user interaction
            stage.show();
        } catch (Exception e) {
            // Log any errors that occur during startup for debugging
            // System.err.println outputs to the error stream (console/terminal)
            // This helps developers identify what went wrong during startup
            System.err.println("Error starting application: " + e.getMessage());
            
            // Print the full stack trace for detailed debugging information
            // This shows the complete call stack when the error occurred
            e.printStackTrace();
            
            // Re-throw the exception to ensure application fails fast if startup fails
            // This prevents the application from running in an inconsistent state
            // The JavaFX framework will handle the exception and terminate the application
            throw e;
        }
    }

    /**
     * Application shutdown method - called by JavaFX framework when application is closing
     * This method is automatically called by the JavaFX framework when the user closes
     * the application window or when the application is terminated.
     * 
     * Performs cleanup operations like closing database connections
     * This is important to prevent resource leaks and ensure data integrity
     * 
     * LIFECYCLE: Called automatically when:
     * - User closes the main window
     * - System shutdown occurs
     * - Application is terminated programmatically
     */
    @Override
    public void stop() {
        // Close database connection to prevent resource leaks
        // This ensures that database connections are properly closed
        // Prevents memory leaks and frees up system resources
        // The Db.close() method will close the connection pool
        Db.close();
    }

    /**
     * Main method - entry point for the JVM
     * This is the traditional Java entry point that the JVM calls when starting the application
     * 
     * Launches the JavaFX application
     * The launch() method is a static method inherited from Application class
     * It handles the JavaFX framework initialization and calls the start() method
     * 
     * @param args Command line arguments (not used in this application)
     *             These are arguments passed when running the application from command line
     * 
     * EXECUTION SEQUENCE:
     * 1. JVM calls main() method
     * 2. launch() initializes JavaFX framework
     * 3. JavaFX creates Application instance
     * 4. JavaFX calls start() method
     * 5. Application runs until user closes it
     * 6. JavaFX calls stop() method for cleanup
     */
    public static void main(String[] args) {
        // Launch the JavaFX application
        // This method blocks until the application is closed
        // It handles all the JavaFX framework setup automatically
        // After this call, the JavaFX application thread takes over
        launch();
    }
}

/*
 * FILE SUMMARY: App.java
 * 
 * PRIMARY PURPOSE:
 * This is the main entry point for the Eventra event management system. It initializes
 * the JavaFX application, sets up the database, and launches the user interface.
 * 
 * ROLE WITHIN EVENTRA APPLICATION:
 * - Application lifecycle manager (startup and shutdown)
 * - Database initialization coordinator
 * - UI framework initializer
 * - Main window setup and configuration
 * 
 * KEY METHODS AND FUNCTIONALITIES:
 * - start(Stage stage): Initializes database, loads FXML, creates main window
 * - stop(): Performs cleanup operations (database connection closure)
 * - main(String[] args): JVM entry point that launches the JavaFX application
 * 
 * INTERACTIONS WITH OTHER SYSTEM COMPONENTS:
 * - Calls DatabaseInitializer.initializeDatabase() to set up database schema
 * - Loads Welcome.fxml to create the initial user interface
 * - Uses Db.close() for database cleanup during shutdown
 * - Integrates with JavaFX framework for application lifecycle management
 * 
 * DESIGN PATTERNS USED:
 * - JavaFX Application Lifecycle Pattern
 * - Resource Management Pattern (try-catch with cleanup)
 * - Dependency Injection (FXML loading with controller binding)
 * - Error Handling Pattern (exception logging and re-throwing)
 */
