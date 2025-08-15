/**
 * Welcome Controller (WelcomeController.java)
 * 
 * This class controls the WELCOME SCREEN of the Eventra application. Think of it as the
 * "brain" that handles everything that happens on the first screen users see.
 * 
 * WHAT THIS CLASS DOES:
 * - Manages the welcome screen when the app first opens
 * - Loads and displays the Eventra logo
 * - Handles the welcome button click to navigate to login
 * - Provides a smooth entry point to the application
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Event Handling: Responds to user actions (button clicks)
 * - Resource Loading: Loads images and other resources from the project
 * - Navigation: Uses ViewUtil to move between different screens
 * - Error Handling: Gracefully handles missing resources
 * 
 * UI COMPONENTS MANAGED:
 * - Logo display (ImageView)
 * - Welcome button (connected via FXML)
 * 
 * SCREEN FLOW:
 * Welcome Screen → Login Screen → Main Application
 * 
 * WHY THIS CONTROLLER EXISTS:
 * - Provides a professional first impression
 * - Ensures the logo loads correctly
 * - Handles the transition to the main application
 * - Demonstrates basic JavaFX controller concepts
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.util.ViewUtil;          // For navigating between screens
import javafx.event.ActionEvent;            // For handling button click events
import javafx.fxml.FXML;                   // Links this class to FXML UI elements
import javafx.scene.image.Image;           // For loading image files
import javafx.scene.image.ImageView;       // For displaying images

/**
 * WelcomeController class - Controls the Welcome Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: No specific data model needed for welcome screen
 * - VIEW: Welcome.fxml defines the visual layout
 * - CONTROLLER: This class handles the logic and user interactions
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the Welcome.fxml file, allowing us to control the UI elements.
 * 
 * This is a simple controller that demonstrates the basic concepts:
 * - Loading resources (images)
 * - Handling user interactions (button clicks)
 * - Navigating between screens
 * - Error handling for missing resources
 */
public class WelcomeController {
    
    /**
     * FXML INJECTED FIELD - Automatically connected to UI element
     * 
     * The @FXML annotation tells JavaFX to automatically connect this Java variable
     * to the corresponding element in the Welcome.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having a remote control for the logo image:
     * - You can change what image is displayed (setImage())
     * - You can make it clickable or interactive
     * - You can control its size, position, and other properties
     * 
     * CONNECTION DETAILS:
     * - This field connects to: <ImageView fx:id="logoView" ...> in Welcome.fxml
     * - The fx:id="logoView" in FXML must match the field name in Java
     * - JavaFX automatically creates this connection when the screen loads
     */
    @FXML private ImageView logoView;

    /**
     * initialize() method - Sets up the welcome screen when it first loads
     * 
     * This method is automatically called by JavaFX when the welcome screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Loads the Eventra logo image from the project resources
     * 2. Displays the logo in the logoView ImageView
     * 3. Handles any errors that might occur during image loading
     * 4. Ensures the app continues working even if the logo fails to load
     * 
     * RESOURCE LOADING EXPLANATION:
     * - getClass().getResourceAsStream("/images/logo.png.png") - Loads image from resources folder
     * - The "/images/" path refers to src/main/resources/images/ in the project
     * - This is the standard way to load resources in Java applications
     * 
     * ERROR HANDLING:
     * - Uses try-catch to handle missing or corrupted image files
     * - Logs the error for debugging purposes
     * - Continues running the app even if the logo can't be displayed
     * - This prevents the app from crashing due to missing resources
     * 
     * WHY IT'S IMPORTANT:
     * - Ensures the logo is displayed when the screen loads
     * - Provides professional appearance from the start
     * - Demonstrates proper resource management
     * - Shows how to handle errors gracefully
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        // Load logo.png from resources/images/ folder
        try {
            // Create an Image object from the logo file in the resources folder
            // The path "/images/logo.png.png" refers to src/main/resources/images/logo.png.png
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png.png"));
            
            // Display the loaded logo in the logoView ImageView
            logoView.setImage(logo);
            
        } catch (Exception e) {
            // If something goes wrong loading the logo, log the error but don't crash
            System.err.println("Warning: Could not load logo image: " + e.getMessage());
            // Continue without the logo - the app will still work
        }
    }

    /**
     * onWelcome() method - Handles the welcome button click event
     * 
     * This method is called when the user clicks the "Welcome" button on the welcome screen.
     * It's responsible for navigating the user from the welcome screen to the login screen.
     * 
     * HOW IT WORKS:
     * 1. User clicks the welcome button
     * 2. JavaFX automatically calls this method (because of onAction="#onWelcome" in FXML)
     * 3. The method uses ViewUtil to navigate to the Login screen
     * 4. The current window is replaced with the Login screen
     * 
     * NAVIGATION EXPLANATION:
     * - ViewUtil.switchTo("Login", ...) - Tells the app to show the Login screen
     * - "Login" refers to Login.fxml file in the resources/fxml folder
     * - logoView.getScene().getWindow() - Gets the current window to replace
     * - This creates a smooth transition between screens
     * 
     * EVENT HANDLING:
     * - ActionEvent event - Contains information about the button click
     * - This parameter is automatically provided by JavaFX
     * - Even though we don't use the event parameter, it's required for the method signature
     * 
     * SCREEN FLOW:
     * Welcome Screen → [User clicks Welcome button] → Login Screen
     * 
     * WHY THIS METHOD EXISTS:
     * - Provides user interaction functionality
     * - Enables navigation between screens
     * - Creates a logical flow through the application
     * - Demonstrates event handling in JavaFX
     * 
     * @param event - The ActionEvent that occurred when the button was clicked
     * @FXML annotation means this method can be called from the FXML file
     */
    @FXML
    private void onWelcome(ActionEvent event) {
        // Navigate to Login screen when the welcome button is clicked
        // The "Login" parameter refers to Login.fxml in the resources/fxml folder
        // logoView.getScene().getWindow() gets the current window to replace
        ViewUtil.switchTo("Login", logoView.getScene().getWindow());
    }
}
