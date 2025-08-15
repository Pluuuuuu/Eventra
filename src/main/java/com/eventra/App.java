/**
 * Eventra - Main Application Entry Point
 * 
 * This is the MAIN CLASS that starts the entire Eventra application.
 * Think of this as the "front door" to your application - when you run the program,
 * this is the first code that gets executed.
 * 
 * WHAT THIS CLASS DOES:
 * - Starts the JavaFX application (the graphical user interface framework)
 * - Initializes the database connection
 * - Loads the first screen (Welcome page)
 * - Sets up the main window
 * - Handles application startup and shutdown
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Application: A framework for creating desktop applications with buttons, forms, etc.
 * - Stage: Think of this as the main window of your application
 * - Scene: The content inside the window (like a page)
 * - FXML: A way to design user interfaces using XML files (separates design from code)
 * - Database Initialization: Setting up the connection to your Azure SQL Database
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
 * of Application, plus some of my own."
 * 
 * The @Override annotation means "I'm replacing the default behavior with my own code"
 */
public class App extends Application {
    
    /**
     * start() method - This is called AUTOMATICALLY when the application starts
     * 
     * Think of this as the "setup" method - it runs once when you launch the app
     * and prepares everything for the user to see.
     * 
     * @param stage - This is the main window of your application
     *               Think of it like a theater stage where your app performs
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            // STEP 1: Initialize the database connection
            // This connects to your Azure SQL Database so the app can store/retrieve data
            // DatabaseInitializer is another class that handles database setup
            DatabaseInitializer.initializeDatabase();
            
            // STEP 2: Load the user interface (UI) from an FXML file
            // FXML is like a blueprint for your UI - it defines buttons, text fields, etc.
            // getClass().getResource() finds the FXML file in your project
            // "/fxml/Welcome.fxml" is the path to the Welcome screen design
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Welcome.fxml"));
            
            // STEP 3: Create a Scene (the content) from the loaded FXML
            // A Scene is like a page in your application
            // loader.load() reads the FXML and creates the actual UI components
            Scene scene = new Scene(loader.load());
            
            // STEP 4: Set the window title (what appears in the title bar)
            stage.setTitle("Eventra");
            
            // STEP 5: Put the Scene (content) into the Stage (window)
            stage.setScene(scene);
            
            // STEP 6: Make the window visible to the user
            stage.show();
            
        } catch (Exception e) {
            // ERROR HANDLING: If something goes wrong during startup
            // This prints error messages to help developers fix problems
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();  // Prints the full error details
            throw e;             // Re-throws the error to stop the application
        }
    }

    /**
     * stop() method - This is called AUTOMATICALLY when the application closes
     * 
     * Think of this as the "cleanup" method - it runs when the user closes the app
     * and makes sure resources are properly released (like closing database connections)
     */
    @Override
    public void stop() {
        // Close the database connection pool to free up resources
        // Db.close() is a static method that shuts down database connections
        Db.close();
    }

    /**
     * main() method - This is the ENTRY POINT that Java looks for when starting the program
     * 
     * When you double-click the JAR file or run "java -jar eventra.jar",
     * Java looks for this method and runs it first.
     * 
     * @param args - Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // launch() is a JavaFX method that starts the application
        // It automatically calls the start() method above
        // This is how JavaFX knows to begin running your app
        launch();
    }
}
