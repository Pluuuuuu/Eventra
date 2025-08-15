/**
 * Edit Profile Controller (EditProfileController.java)
 * 
 * This class controls the PROFILE EDITING SCREEN of the Eventra application. Think of it as the
 * "profile editor" where users can modify their account information like username, name, and email,
 * with validation and database persistence.
 * 
 * WHAT THIS CLASS DOES:
 * - Provides editable form fields for user profile information
 * - Pre-populates fields with current user data
 * - Validates user input for required fields and data integrity
 * - Checks for username and email uniqueness to prevent conflicts
 * - Updates user profile in the database
 * - Refreshes user session with updated information
 * - Provides user feedback through alert dialogs
 * - Handles navigation between profile screens
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Form Validation: Ensures data quality and prevents errors
 * - Database Updates: Persists changes to user records
 * - Session Management: Keeps user session synchronized with changes
 * - Input Sanitization: Cleans user input (trimming whitespace)
 * - Conflict Detection: Prevents duplicate usernames and emails
 * - User Experience: Provides clear feedback and navigation
 * 
 * UI COMPONENTS MANAGED:
 * - Editable text fields for all profile information
 * - Save and cancel buttons for form actions
 * - Validation feedback through alert dialogs
 * - Form state management and error handling
 * - Professional form layout and user interaction
 * 
 * PROFILE EDITING FEATURES:
 * - Complete profile information editing
 * - Real-time validation and error checking
 * - Duplicate username/email prevention
 * - Professional form design and layout
 * - Immediate feedback and confirmation
 * - Session synchronization after updates
 * 
 * COMPLEXITY LEVEL:
 * This controller demonstrates several important concepts:
 * - Form validation and data integrity
 * - Database update operations
 * - Session management and synchronization
 * - Professional error handling and user feedback
 * - Clean navigation and user experience design
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.util.SessionManager;                 // For managing user sessions and getting current user
import com.eventra.util.ViewUtil;                       // For navigating between screens
import com.eventra.model.User;                           // User data model for current user
import com.eventra.dao.UserDAO;                          // For updating user profile information in database

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                                 // Links this class to FXML UI elements
import javafx.scene.control.TextField;                   // For editable text input fields
import javafx.scene.control.Alert;                       // For showing user feedback and error messages
import javafx.scene.control.Alert.AlertType;             // Types of alert dialogs (ERROR, WARNING, INFO)

/**
 * EditProfileController class - Controls the Profile Editing Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: User model, DAO classes for database operations
 * - VIEW: FXML file defines the visual layout of the profile editing page
 * - CONTROLLER: This class connects the model and view, handles all profile editing logic
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the EditProfile.fxml file, allowing us to control the UI elements.
 * 
 * PROFILE MANAGEMENT:
 * This controller provides comprehensive profile editing capabilities,
 * demonstrating how to handle form validation, database updates, and
 * professional user experience design in JavaFX applications.
 */
public class EditProfileController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the EditProfile.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what the user typed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can respond to user actions (setOnAction())
     */
    
    // PROFILE EDITING FIELDS - Allow users to modify their account information
    @FXML private TextField usernameField;              // Editable field for username (must be unique)
    @FXML private TextField firstNameField;             // Editable field for first name (required)
    @FXML private TextField middleNameField;            // Editable field for middle name (optional)
    @FXML private TextField lastNameField;              // Editable field for last name (required)
    @FXML private TextField emailField;                 // Editable field for email address (must be unique)
    
    /**
     * INSTANCE VARIABLE - Store current user data for this controller
     * 
     * This variable keeps track of the currently logged-in user and their information.
     * It's used throughout the controller to access user data and make updates.
     * 
     * USER DATA MANAGEMENT:
     * This variable stores the complete user object, allowing us to:
     * - Display current user information in form fields
     * - Compare old vs. new values for validation
     * - Update user data in the database
     * - Maintain user session state during editing
     */
    private User currentUser;                            // Stores the currently logged-in user's complete data
    
    /**
     * initialize() method - Sets up the profile editing screen when it first loads
     * 
     * This method is automatically called by JavaFX when the EditProfile screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Gets the currently logged-in user from the session manager
     * 2. If user is logged in: populates form fields with current profile data
     * 3. If no user is logged in: redirects to login screen for security
     * 4. Establishes the complete profile editing experience
     * 
     * SECURITY CHECK:
     * The first thing this method does is verify that someone is actually logged in.
     * If no user is found, it immediately redirects to login - this prevents
     * unauthorized access to profile editing.
     * 
     * FORM PRE-POPULATION:
     * Once we know who the user is, we fill all form fields with their current data:
     * - Username field shows current username
     * - Name fields show current name information
     * - Email field shows current email address
     * - This gives users a starting point for their edits
     * 
     * USER EXPERIENCE:
     * By pre-populating fields, users can see their current information
     * and make targeted changes without having to re-enter everything.
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        // STEP 1: Get the currently logged-in user from session management
        currentUser = SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            // STEP 2A: User is logged in - populate form fields with current data
            usernameField.setText(currentUser.getUsername());
            firstNameField.setText(currentUser.getFirstName());
            middleNameField.setText(currentUser.getMiddleName() != null ? currentUser.getMiddleName() : "");
            lastNameField.setText(currentUser.getLastName());
            emailField.setText(currentUser.getEmail());
        } else {
            // STEP 2B: No user logged in - security redirect
            ViewUtil.switchTo("Login", usernameField.getScene().getWindow());
        }
    }
    
    @FXML
    private void handleSaveChanges() {
        if (currentUser == null) {
            showAlert("Error", "No user logged in.", AlertType.ERROR);
            return;
        }
        
        // Validate required fields
        if (usernameField.getText().trim().isEmpty() || 
            firstNameField.getText().trim().isEmpty() || 
            lastNameField.getText().trim().isEmpty() || 
            emailField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.", AlertType.ERROR);
            return;
        }
        
        // Check if username is already taken by another user
        if (!usernameField.getText().equals(currentUser.getUsername()) && 
            UserDAO.usernameExists(usernameField.getText())) {
            showAlert("Error", "Username is already taken.", AlertType.ERROR);
            return;
        }
        
        // Check if email is already taken by another user
        if (!emailField.getText().equals(currentUser.getEmail()) && 
            UserDAO.emailExists(emailField.getText())) {
            showAlert("Error", "Email is already registered.", AlertType.ERROR);
            return;
        }
        
        try {
            // Update user object
            currentUser.setUsername(usernameField.getText().trim());
            currentUser.setFirstName(firstNameField.getText().trim());
            currentUser.setMiddleName(middleNameField.getText().trim());
            currentUser.setLastName(lastNameField.getText().trim());
            currentUser.setEmail(emailField.getText().trim());
            
            // Save to database
            if (UserDAO.updateUser(currentUser)) {
                // Update session
                SessionManager.setCurrentUser(currentUser);
                
                showAlert("Success", "Profile updated successfully!", AlertType.INFORMATION);
                
                // Navigate back to user info page
                ViewUtil.switchTo("UserInfo", usernameField.getScene().getWindow());
            } else {
                showAlert("Error", "Failed to update profile in database.", AlertType.ERROR);
            }
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
            showAlert("Error", "Failed to update profile: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleCancel() {
        // Navigate back to user info page without saving
        ViewUtil.switchTo("UserInfo", usernameField.getScene().getWindow());
    }
    
    @FXML
    private void handleBackToProfile() {
        // Navigate back to user info page
        ViewUtil.switchTo("UserInfo", usernameField.getScene().getWindow());
    }
    
    private void showAlert(String title, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 