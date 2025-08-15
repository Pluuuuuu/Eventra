/**
 * User Info Controller (UserInfoController.java)
 * 
 * This class controls the USER INFORMATION DISPLAY SCREEN of the Eventra application. Think of it as the
 * "user profile viewer" where users can see their account information, profile picture, and manage
 * their personal details like profile pictures.
 * 
 * WHAT THIS CLASS DOES:
 * - Displays the user's profile picture with fallback to default image
 * - Shows user account information (username, email, full name)
 * - Handles profile picture loading from file system
 * - Provides profile picture upload functionality via file chooser
 * - Manages profile picture updates in the database
 * - Handles authentication and session management
 * - Provides fallback images when profile pictures are missing
 * - Offers professional file selection interface for image uploads
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Profile Picture Management: Loading, displaying, and updating user images
 * - File System Operations: Reading images from local file system
 * - File Chooser Integration: Professional file selection dialog
 * - Error Handling: Graceful fallbacks for missing or corrupted images
 * - Database Updates: Saving profile picture changes to user records
 * - Session Management: Verifying user authentication and access
 * 
 * UI COMPONENTS MANAGED:
 * - Profile picture display with click-to-change functionality
 * - User information display (username, email, full name)
 * - File chooser dialog for profile picture selection
 * - Error handling and user feedback
 * - Professional image upload interface
 * 
 * PROFILE PICTURE FEATURES:
 * - Local file system image loading
 * - Default image fallback system
 * - Professional file selection dialog
 * - Image format validation (PNG, JPG, JPEG, GIF, BMP)
 * - Database persistence of image paths
 * - Real-time image updates
 * 
 * COMPLEXITY LEVEL:
 * This controller demonstrates several important concepts:
 * - File system operations and image handling
 * - Professional file selection interfaces
 * - Robust error handling with fallbacks
 * - Database integration for user updates
 * - User experience design for profile management
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.util.SessionManager;                 // For managing user sessions and getting current user
import com.eventra.util.ViewUtil;                       // For navigating between screens
import com.eventra.model.User;                           // User data model for current user
import com.eventra.dao.UserDAO;                          // For updating user profile information in database

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                                 // Links this class to FXML UI elements
import javafx.scene.text.Text;                           // For displaying text content
import javafx.scene.image.ImageView;                     // For displaying images in the UI
import javafx.scene.image.Image;                         // For loading and processing image files
import javafx.scene.control.Alert;                       // For showing user feedback and error messages
import javafx.scene.control.Alert.AlertType;             // Types of alert dialogs (ERROR, WARNING, INFO)

// JavaFX file handling and dialogs:
import javafx.stage.FileChooser;                         // Professional file selection dialog
import javafx.stage.Stage;                               // For managing application windows

// Java standard library imports for file operations:
import java.io.File;                                     // Represents files on the computer
import java.io.FileInputStream;                          // For reading file contents
import java.io.FileNotFoundException;                     // For handling missing files gracefully

/**
 * UserInfoController class - Controls the User Information Display Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: User model, DAO classes for database operations
 * - VIEW: FXML file defines the visual layout of the user info page
 * - CONTROLLER: This class connects the model and view, handles all user info logic
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the UserInfo.fxml file, allowing us to control the UI elements.
 * 
 * PROFILE MANAGEMENT:
 * This controller provides comprehensive profile picture management,
 * demonstrating how to handle file system operations, image processing,
 * and professional file selection interfaces in JavaFX applications.
 */
public class UserInfoController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the UserInfo.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what's displayed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can respond to user actions (setOnAction())
     */
    
    // PROFILE PICTURE - Main user image display with click functionality
    @FXML private ImageView profileImageView;           // Shows the user's profile picture, clickable for changes
    
    // USER INFORMATION DISPLAY - Show account details
    @FXML private Text usernameText;                    // Displays the user's username
    @FXML private Text emailText;                       // Shows the user's email address
    @FXML private Text fullNameText;                    // Displays the user's full name
    
    /**
     * INSTANCE VARIABLE - Store current user data for this controller
     * 
     * This variable keeps track of the currently logged-in user and their information.
     * It's used throughout the controller to access user data and make updates.
     * 
     * USER DATA MANAGEMENT:
     * This variable stores the complete user object, allowing us to:
     * - Display current user information
     * - Update profile pictures
     * - Save changes to the database
     * - Maintain user session state
     */
    private User currentUser;                            // Stores the currently logged-in user's complete data
    
    /**
     * initialize() method - Sets up the user info screen when it first loads
     * 
     * This method is automatically called by JavaFX when the UserInfo screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Gets the currently logged-in user from the session manager
     * 2. If user is logged in: displays their information and loads profile picture
     * 3. If no user is logged in: redirects to login screen for security
     * 4. Establishes the complete personalized user information experience
     * 
     * SECURITY CHECK:
     * The first thing this method does is verify that someone is actually logged in.
     * If no user is found, it immediately redirects to login - this prevents
     * unauthorized access to user information.
     * 
     * PERSONALIZED SETUP:
     * Once we know who the user is, we customize everything:
     * - Display their username, email, and full name
     * - Load their personal profile picture
     * - Set up the complete user profile view
     * 
     * USER EXPERIENCE:
     * By loading everything upfront, users can immediately see their
     * profile information without waiting for components to load or configure.
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        // STEP 1: Get the currently logged-in user from session management
        currentUser = SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            // STEP 2A: User is logged in - display their information
            usernameText.setText(currentUser.getUsername());
            emailText.setText(currentUser.getEmail());
            fullNameText.setText(currentUser.getFullName());
            
            // STEP 2B: Load and display the user's profile picture
            loadProfilePicture();
        } else {
            // STEP 2C: No user logged in - security redirect
            ViewUtil.switchTo("Login", profileImageView.getScene().getWindow());
        }
    }
    
    private void loadProfilePicture() {
        try {
            if (currentUser.getProfilePicUrl() != null && !currentUser.getProfilePicUrl().trim().isEmpty()) {
                // Load user's profile picture
                File profilePicFile = new File(currentUser.getProfilePicUrl());
                if (profilePicFile.exists()) {
                    Image profileImage = new Image(new FileInputStream(profilePicFile));
                    profileImageView.setImage(profileImage);
                } else {
                    loadDefaultProfilePicture();
                }
            } else {
                loadDefaultProfilePicture();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Profile picture file not found: " + e.getMessage());
            loadDefaultProfilePicture();
        } catch (Exception e) {
            System.err.println("Error loading profile picture: " + e.getMessage());
            loadDefaultProfilePicture();
        }
    }
    
    private void loadDefaultProfilePicture() {
        try {
            // Load default profile picture from resources
            String defaultImagePath = getClass().getResource("/images/logo.png.png").toExternalForm();
            Image defaultImage = new Image(defaultImagePath);
            profileImageView.setImage(defaultImage);
        } catch (Exception e) {
            System.err.println("Error loading default profile picture: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleProfilePictureClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        
        Stage stage = (Stage) profileImageView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            try {
                // Save the image path to user's profile
                currentUser.setProfilePicUrl(selectedFile.getAbsolutePath());
                
                // Update the database
                if (UserDAO.updateUser(currentUser)) {
                    // Update the displayed image
                    Image newImage = new Image(new FileInputStream(selectedFile));
                    profileImageView.setImage(newImage);
                    
                    // Update session
                    SessionManager.setCurrentUser(currentUser);
                    
                    showAlert("Success", "Profile picture updated successfully!", AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to update profile picture in database.", AlertType.ERROR);
                }
            } catch (Exception e) {
                System.err.println("Error updating profile picture: " + e.getMessage());
                showAlert("Error", "Failed to update profile picture: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleEditProfile() {
        // Navigate to edit profile page
        ViewUtil.switchTo("EditProfile", profileImageView.getScene().getWindow());
    }
    
    @FXML
    private void handleLogout() {
        // Clear session
        SessionManager.logout();
        
        // Navigate back to login
        ViewUtil.switchTo("Login", profileImageView.getScene().getWindow());
    }
    
    @FXML
    private void handleBackToDashboard() {
        // Navigate back to dashboard
        ViewUtil.switchTo("Dashboard", profileImageView.getScene().getWindow());
    }
    
    private void showAlert(String title, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 