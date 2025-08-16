/**
 * EVENTRA MAIN APPLICATION (App.java)
 *
 * This is the MAIN CLASS that starts the entire Eventra application.
 * Think of this as the "front door" or "engine starter" of your application - 
 * when you run the program, this is the first code that gets executed and
 * sets up everything the application needs to function.
 *
 * WHAT THIS CLASS DOES:
 * - Starts the JavaFX application (the graphical user interface framework)
 * - Initializes the database connection to Azure SQL Database
 * - Loads the first screen (Welcome page) for users to see
 * - Sets up the main application window and configuration
 * - Handles application startup and shutdown processes
 * - Manages the application lifecycle from launch to closure
 * - Provides error handling for startup failures
 *
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Application: A framework for creating desktop applications with buttons, forms, and graphics
 * - Stage: Think of this as the main window of your application (like a theater stage)
 * - Scene: The content inside the window (like a page or scene in a play)
 * - FXML: A way to design user interfaces using XML files (separates design from code)
 * - Database Initialization: Setting up the connection to your Azure SQL Database
 * - Application Lifecycle: The complete process from startup to shutdown
 * - Error Handling: Managing problems that might occur during startup
 *
 * BUSINESS VALUE:
 * - Provides the foundation for the entire Eventra event management system
 * - Ensures proper application startup and user experience
 * - Manages critical resources like database connections
 * - Handles errors gracefully to prevent application crashes
 * - Creates a professional, reliable application launch experience
 *
 * REAL-WORLD ANALOGY:
 * - Like the ignition system of a car that starts the engine
 * - Like the main entrance of a building that welcomes visitors
 * - Like the conductor of an orchestra who starts the performance
 * - Like the manager of a store who opens the doors each morning
 * - Like the pilot who starts up an airplane before takeoff
 */
package com.eventra;

// These are IMPORT statements - they tell Java where to find the classes we want to use
// Think of imports like telling Java "I want to use these tools from the JavaFX toolbox"
import javafx.application.Application;    // The main JavaFX application class
import javafx.fxml.FXMLLoader;           // Loads FXML files (UI designs)
import javafx.scene.Scene;               // Represents the content area of a window
import javafx.stage.Stage;               // Represents the main application window

/**
 * App class extends Application - this means it INHERITS all the functionality
 * of a JavaFX application. Inheritance is like saying "I want all the features
 * of Application, plus some of my own custom behavior."
 * 
 * The @Override annotation means "I'm replacing the default behavior with my own code"
 * This gives us full control over how the application starts and behaves.
 */
public class App extends Application {
    
    /**
     * start() method - This is called AUTOMATICALLY when the application starts
     *
     * Think of this as the "setup" method - it runs once when you launch the app
     * and prepares everything for the user to see. It's like the opening sequence
     * of a movie that sets up the story.
     *
     * TECHNICAL PROCESS:
     * 1. Initialize database connection
     * 2. Load the Welcome screen UI from FXML
     * 3. Create the main application window
     * 4. Display the application to the user
     * 5. Handle any errors that occur during startup
     *
     * @param stage - This is the main window of your application
     *               Think of it like a theater stage where your app performs
     *               JavaFX automatically creates this for us
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            // 
            // STEP 1: DATABASE INITIALIZATION - Connect to Azure SQL Database
            // 
            // This connects to your Azure SQL Database so the app can store/retrieve data.
            // DatabaseInitializer is another class that handles all the database setup,
            // including creating tables, inserting initial data, and validating the structure.
            // Think of this like checking that all the systems are working before opening
            // the doors to customers.
            //
            System.out.println("🚀 Starting Eventra application...");
            System.out.println("📊 Initializing database connection...");
            DatabaseInitializer.initializeDatabase();
            System.out.println("✅ Database connection established successfully!");
            
            // 
            // STEP 2: LOAD USER INTERFACE - Get the Welcome screen design
            // 
            // FXML is like a blueprint for your UI - it defines buttons, text fields, layouts, etc.
            // getClass().getResource() finds the FXML file in your project resources.
            // "/fxml/Welcome.fxml" is the path to the Welcome screen design file.
            // Think of this like loading the floor plan before building a house.
            //
            System.out.println("🎨 Loading user interface...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Welcome.fxml"));
            
            // 
            // STEP 3: CREATE SCENE - Build the actual UI components
            // 
            // A Scene is like a page in your application - it contains all the UI elements.
            // loader.load() reads the FXML and creates the actual buttons, text fields, etc.
            // Think of this like assembling the furniture according to the blueprint.
            //
            Scene scene = new Scene(loader.load());
            System.out.println("✅ User interface loaded successfully!");
            
            // 
            // STEP 4: CONFIGURE MAIN WINDOW - Set up the application window
            // 
            // Set the window title that appears in the title bar.
            // Think of this like putting a sign on the front of a building.
            //
            stage.setTitle("Eventra - Event Management System");
            
            // 
            // STEP 5: ASSEMBLE THE APPLICATION - Put everything together
            // 
            // Put the Scene (content) into the Stage (window).
            // Think of this like putting the furniture into the house.
            //
            stage.setScene(scene);
            
            // 
            // STEP 6: LAUNCH THE APPLICATION - Make it visible to users
            // 
            // Make the window visible to the user.
            // Think of this like opening the doors and welcoming customers in.
            //
            stage.show();
            System.out.println("🎉 Eventra application launched successfully!");
            System.out.println("👋 Welcome to Eventra - Your Event Management Solution!");
            
        } catch (Exception e) {
            // 
            // ERROR HANDLING - If something goes wrong during startup
            // 
            // This section handles any problems that occur during application startup.
            // It prints detailed error messages to help developers identify and fix issues.
            // Think of this like having a backup plan if the main entrance is blocked.
            //
            System.err.println("❌ ERROR: Failed to start Eventra application!");
            System.err.println("🔍 Error details: " + e.getMessage());
            System.err.println("📋 Full error information:");
            e.printStackTrace();  // Prints the full error details for debugging
            
            // Re-throw the error to stop the application
            // This prevents the app from running with critical problems
            throw e;
        }
    }

    /**
     * stop() method - This is called AUTOMATICALLY when the application closes
     *
     * Think of this as the "cleanup" method - it runs when the user closes the app
     * and makes sure resources are properly released. It's like the closing sequence
     * of a movie that wraps up loose ends.
     *
     * TECHNICAL PROCESS:
     * 1. Close database connection pool
     * 2. Release system resources
     * 3. Perform any final cleanup tasks
     *
     * USER EXPERIENCE:
     * - Ensures the application closes cleanly
     * - Prevents resource leaks
     * - Maintains system stability
     */
    @Override
    public void stop() {
        try {
            // 
            // DATABASE CLEANUP - Close database connections
            // 
            // Close the database connection pool to free up system resources.
            // Db.close() is a static method that shuts down all database connections.
            // Think of this like turning off the lights and locking up when closing
            // a building for the day.
            //
            System.out.println("🔒 Closing database connections...");
            Db.close();
            System.out.println("✅ Database connections closed successfully!");
            
            // 
            // APPLICATION SHUTDOWN - Final cleanup
            // 
            // Perform any additional cleanup tasks needed before the application exits.
            // This ensures a clean shutdown and prevents resource leaks.
            //
            System.out.println("👋 Eventra application shutting down...");
            System.out.println("✅ Shutdown completed successfully!");
            
        } catch (Exception e) {
            // 
            // SHUTDOWN ERROR HANDLING - Handle cleanup errors
            // 
            // Even during shutdown, we want to handle errors gracefully.
            // This prevents the application from hanging during closure.
            //
            System.err.println("⚠️ Warning: Error during application shutdown: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * main() method - This is the ENTRY POINT that Java looks for when starting the program
     *
     * Think of this as the "launch button" - when you double-click the application
     * or run it from the command line, Java looks for this method to know where to start.
     * It's like the main entrance to a building that everyone uses.
     *
     * TECHNICAL PROCESS:
     * 1. Java finds this main() method
     * 2. Calls Application.launch() to start JavaFX
     * 3. JavaFX automatically calls our start() method
     * 4. Our application begins running
     *
     * USER EXPERIENCE:
     * - Provides the standard way to launch Java applications
     * - Ensures proper JavaFX initialization
     * - Creates a professional application launch experience
     *
     * @param args - Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // 
        // LAUNCH THE JAVAFX APPLICATION
        // 
        // Application.launch() starts the JavaFX framework and automatically calls
        // our start() method. This is the standard way to launch JavaFX applications.
        // Think of this like pressing the "start" button on a machine.
        //
        System.out.println("🚀 Launching Eventra JavaFX application...");
        Application.launch(args);
    }
}
