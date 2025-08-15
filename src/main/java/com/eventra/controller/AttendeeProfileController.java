/**
 * Attendee Profile Controller (AttendeeProfileController.java)
 * 
 * This class controls the ATTENDEE PROFILE SCREEN of the Eventra application. Think of it as the
 * "personal profile page" where attendees can view and edit their account information,
 * see their profile picture, and manage their personal details.
 * 
 * WHAT THIS CLASS DOES:
 * - Displays the attendee's profile picture and personal information
 * - Shows account details like username, email, and member since date
 * - Allows editing of profile information with save functionality
 * - Loads and displays the Eventra logo and branding
 * - Tracks changes to profile fields for unsaved changes detection
 * - Provides search functionality within the profile
 * - Shows statistics like events attended and account activity
 * - Handles profile image loading with fallback to default avatar
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Profile Management: Personal information display and editing
 * - Image Loading: Profile pictures and logo with error handling
 * - Change Tracking: Detects when user has made unsaved changes
 * - Session Management: Gets current user information for personalization
 * - Error Handling: Gracefully handles missing images and data
 * - Date Formatting: Displays dates in user-friendly formats
 * 
 * UI COMPONENTS MANAGED:
 * - Profile picture display with fallback to default avatar
 * - Logo image display for branding
 * - Profile information display (name, email, username)
 * - Editable fields for profile updates
 * - Save button for applying changes
 * - Member since date and statistics display
 * - Search field for profile content
 * 
 * PROFILE FEATURES:
 * - Personal information management
 * - Profile picture handling
 * - Account statistics display
 * - Change tracking and save functionality
 * - Professional branding integration
 * - User-friendly date formatting
 * 
 * COMPLEXITY LEVEL:
 * This controller demonstrates several important concepts:
 * - Image resource management with fallbacks
 * - Form change detection and validation
 * - User session management and personalization
 * - Professional UI design with branding
 * - Robust error handling for missing resources
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.dao.UserDAO;                       // For updating user profile information in database
import com.eventra.model.User;                          // User data model for current attendee

// Utility classes for application functionality:
import com.eventra.util.SessionManager;                 // For managing user sessions and getting current user
import com.eventra.util.ViewUtil;                       // For navigating between screens

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                                // Links this class to FXML UI elements
import javafx.scene.control.*;                          // UI controls (buttons, text fields, labels, etc.)
import javafx.scene.image.Image;                        // For loading and displaying image files
import javafx.scene.image.ImageView;                    // For displaying images in the UI
import javafx.scene.layout.*;                           // Layout containers (VBox, HBox, etc.)
import javafx.scene.text.Text;                          // For displaying text content
import javafx.scene.text.Font;                          // For customizing text appearance
import javafx.scene.text.FontWeight;                    // For making text bold, normal, etc.

// JavaFX geometry and positioning:
import javafx.geometry.Insets;                          // For adding spacing around UI elements
import javafx.geometry.Pos;                             // For aligning content (left, center, right)

// Java time classes for date formatting:
import java.time.format.DateTimeFormatter;              // For formatting dates in user-friendly ways

/**
 * AttendeeProfileController class - Controls the Attendee Profile Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: User model, DAO classes for database operations
 * - VIEW: FXML file defines the visual layout of the profile page
 * - CONTROLLER: This class connects the model and view, handles all profile logic
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the AttendeeProfile.fxml file, allowing us to control the UI elements.
 * 
 * PERSONALIZED EXPERIENCE:
 * This controller creates a personalized profile experience for each attendee,
 * demonstrating how to handle user-specific data, profile management, and
 * professional UI design with proper error handling.
 */
public class AttendeeProfileController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the AttendeeProfile.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what's displayed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can respond to user actions (setOnAction())
     */
    
    // IMAGE COMPONENTS - Display profile picture and branding
    @FXML private ImageView profileImage;              // Shows the attendee's profile picture
    @FXML private ImageView logoImage;                  // Displays the Eventra logo for branding
    
    // PROFILE INFORMATION DISPLAY - Show current user details
    @FXML private Text profileName;                     // Displays the attendee's full name
    @FXML private Text profileEmail;                    // Shows the attendee's email address
    
    // EDITABLE PROFILE FIELDS - Allow user to update their information
    @FXML private TextField usernameField;              // Editable field for username
    @FXML private TextField emailField;                  // Editable field for email address
    @FXML private Button saveButton;                    // Button to save profile changes
    
    // ACCOUNT STATISTICS - Show user activity and membership information
    @FXML private Text memberSinceText;                 // Shows when the user joined (e.g., "January 2024")
    @FXML private Text eventsAttendedText;              // Shows how many events the user has attended
    
    // SEARCH FUNCTIONALITY - Allow searching within profile content
    @FXML private TextField searchField;                // Input field for searching profile information
    
    /**
     * INSTANCE VARIABLES - Store state and data for this controller
     * 
     * These variables keep track of the current state of the profile screen
     * and store references to the current user and form state.
     * 
     * STATE MANAGEMENT:
     * These variables help manage the profile editing experience and
     * track whether the user has made unsaved changes.
     */
    private User currentUser;                           // Stores the currently logged-in user's data
    private boolean hasChanges = false;                 // Tracks whether user has made unsaved changes to profile
    
    /**
     * initialize() method - Sets up the attendee profile screen when it first loads
     * 
     * This method is automatically called by JavaFX when the AttendeeProfile screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Loads and displays the Eventra logo and branding images
     * 2. Loads the current user's profile information and displays it
     * 3. Sets up listeners to track changes in profile fields
     * 4. Establishes the complete personalized profile experience
     * 
     * COMPREHENSIVE SETUP:
     * This method handles the complete initialization of the profile management interface:
     * - Branding and logo display
     * - User profile data loading and display
     * - Change tracking for form validation
     * - Professional profile management experience
     * 
     * PERSONALIZED EXPERIENCE:
     * Unlike general screens, this provides a completely personalized view:
     * - User's own profile picture and information
     * - Personal account statistics and history
     * - Customizable profile fields
     * - Individual branding and preferences
     * 
     * USER EXPERIENCE:
     * By setting up everything in advance, users can immediately view and edit
     * their profile without waiting for components to load or configure.
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        // STEP 1: Load and display branding images (logo, etc.)
        loadImages();
        
        // STEP 2: Load and display current user's profile information
        loadUserProfile();
        
        // STEP 3: Set up listeners to track changes in profile fields
        setupFieldListeners();
    }
    
    private void loadImages() {
        try {
            // Load logo
            String logoPath = getClass().getResource("/images/logo.elon.png").toExternalForm();
            Image logoImage = new Image(logoPath);
            this.logoImage.setImage(logoImage);
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
            // Set logo to null if it can't be loaded
            this.logoImage.setImage(null);
        }
    }
    
    private void loadUserProfile() {
        currentUser = SessionManager.getCurrentUser();
        
        if (currentUser == null) {
            showError("No user logged in");
            return;
        }
        
        // Load profile image
        if (currentUser.getProfilePicUrl() != null && !currentUser.getProfilePicUrl().isEmpty()) {
            try {
                profileImage.setImage(new Image(currentUser.getProfilePicUrl()));
            } catch (Exception e) {
                // Try to load default avatar, if not available, set to null
                try {
                    profileImage.setImage(new Image(getClass().getResourceAsStream("/images/default-avatar.png")));
                } catch (Exception ex) {
                    // If no default avatar, just leave the image view empty
                    profileImage.setImage(null);
                }
            }
        } else {
            // Try to load default avatar, if not available, set to null
            try {
                profileImage.setImage(new Image(getClass().getResourceAsStream("/images/default-avatar.png")));
            } catch (Exception e) {
                // If no default avatar, just leave the image view empty
                profileImage.setImage(null);
            }
        }
        
        // Load user information
        profileName.setText(currentUser.getFullName());
        profileEmail.setText(currentUser.getEmail());
        usernameField.setText(currentUser.getUsername());
        emailField.setText(currentUser.getEmail());
        
        // Load additional information
        if (currentUser.getCreatedAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            memberSinceText.setText(currentUser.getCreatedAt().format(formatter));
        } else {
            memberSinceText.setText("Unknown");
        }
        
        // TODO: Get events attended count from database
        eventsAttendedText.setText("5"); // Placeholder
    }
    
    private void setupFieldListeners() {
        // Listen for changes in text fields
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(currentUser.getUsername())) {
                hasChanges = true;
                saveButton.setDisable(false);
            } else {
                checkForChanges();
            }
        });
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(currentUser.getEmail())) {
                hasChanges = true;
                saveButton.setDisable(false);
            } else {
                checkForChanges();
            }
        });
    }
    
    private void checkForChanges() {
        hasChanges = !usernameField.getText().equals(currentUser.getUsername()) ||
                    !emailField.getText().equals(currentUser.getEmail());
        saveButton.setDisable(!hasChanges);
    }
    
    @FXML
    private void handleEditUsername() {
        usernameField.setEditable(true);
        usernameField.requestFocus();
        usernameField.selectAll();
    }
    
    @FXML
    private void handleEditEmail() {
        emailField.setEditable(true);
        emailField.requestFocus();
        emailField.selectAll();
    }
    
    @FXML
    private void handleSaveChanges() {
        if (!hasChanges) {
            return;
        }
        
        String newUsername = usernameField.getText().trim();
        String newEmail = emailField.getText().trim();
        
        // Validate input
        if (newUsername.isEmpty()) {
            showError("Username cannot be empty");
            return;
        }
        
        if (newEmail.isEmpty() || !isValidEmail(newEmail)) {
            showError("Please enter a valid email address");
            return;
        }
        
        // Check if username or email already exists (if changed)
        if (!newUsername.equals(currentUser.getUsername()) && UserDAO.usernameExists(newUsername)) {
            showError("Username already exists");
            return;
        }
        
        if (!newEmail.equals(currentUser.getEmail()) && UserDAO.emailExists(newEmail)) {
            showError("Email already exists");
            return;
        }
        
        // Update user object
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        
        // Save to database
        boolean success = UserDAO.updateUser(currentUser);
        
        if (success) {
            // Update session
            SessionManager.setCurrentUser(currentUser);
            
            // Update display
            profileName.setText(currentUser.getFullName());
            profileEmail.setText(currentUser.getEmail());
            
            // Reset change tracking
            hasChanges = false;
            saveButton.setDisable(true);
            
            showSuccess("Profile updated successfully");
        } else {
            showError("Failed to update profile");
        }
    }
    
    @FXML
    private void handleCancel() {
        // Reset fields to original values
        usernameField.setText(currentUser.getUsername());
        emailField.setText(currentUser.getEmail());
        
        // Reset change tracking
        hasChanges = false;
        saveButton.setDisable(true);
    }
    
    @FXML
    private void handleMySchedule() {
        ViewUtil.switchTo("MySchedule", searchField.getScene().getWindow());
    }
    
    @FXML
    private void handleBrowseEvents() {
        ViewUtil.switchTo("AttendeeEvents", searchField.getScene().getWindow());
    }
    
    @FXML
    private void handleProfile() {
        // Already on profile page
    }
    
    @FXML
    private void handleSearch() {
        // Search functionality
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Profile Update");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Profile Update Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackToDashboard() {
        ViewUtil.switchToRoleDashboard(searchField.getScene().getWindow());
    }
} 