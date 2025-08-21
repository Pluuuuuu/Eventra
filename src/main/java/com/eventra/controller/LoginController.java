package com.eventra.controller;

// Import DAO classes for database operations
import com.eventra.dao.UserDAO;
// Import model classes for data representation
import com.eventra.model.User;
// Import utility classes for view management and session handling
import com.eventra.util.ViewUtil;
import com.eventra.util.SessionManager;
// Import JavaFX classes for UI components and event handling
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

// Import Java utility classes
import java.util.Optional;

/**
 * Login Controller for Eventra Event Management System
 * 
 * This controller manages the login screen functionality. It handles user
 * authentication, password visibility toggling, and navigation to other screens.
 * The controller provides a secure login interface with proper error handling
 * and user feedback.
 * 
 * Key Features:
 * - User authentication with email and password
 * - Password visibility toggle for better UX
 * - Error handling and user feedback
 * - Navigation to signup and dashboard screens
 * - Session management after successful login
 * 
 * File Usage: Controls the Login.fxml view and is used by the application
 * to handle user authentication and login flow
 */
public class LoginController {  
    
    // FXML-injected UI components - these are automatically populated by JavaFX
    @FXML private TextField emailField;           // Input field for user email
    @FXML private PasswordField passwordField;    // Hidden password input field
    @FXML private TextField visiblePasswordField; // Visible password input field (for toggle)
    @FXML private Button signInButton;            // Button to submit login credentials
    @FXML private Label errorLabel;               // Label to display error messages
    @FXML private Hyperlink signUpLink;           // Link to navigate to signup screen
    @FXML private ImageView logoImage;            // Application logo display
    @FXML private Button togglePasswordButton;    // Button to toggle password visibility
    @FXML private ImageView eyeIcon;              // Eye icon for password visibility
    @FXML private HBox passwordContainer;         // Container for password fields
    
    // Controller state variables
    private boolean passwordVisible = false;      // Track password visibility state
    private Image eyeOpenIcon;                    // Icon for visible password state
    private Image eyeClosedIcon;                  // Icon for hidden password state
    
    /**
     * Initialize method called by JavaFX after FXML loading
     * Sets up the login screen with proper UI configuration and event handlers
     */
    @FXML
    public void initialize() {
        // Set up application logo display
        try {
            // Load logo image from resources
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoImage.setImage(logo);
        } catch (Exception e) {
            // Log error if logo cannot be loaded
            System.err.println("Could not load logo: " + e.getMessage());
        }
        
        // Load eye icons for password visibility toggle functionality
        try {
            // Load open and closed eye icons from resources
            eyeOpenIcon = new Image(getClass().getResourceAsStream("/images/eye-open.png"));
            eyeClosedIcon = new Image(getClass().getResourceAsStream("/images/eye-closed.png"));
            eyeIcon.setImage(eyeClosedIcon); // Start with closed eye (password hidden)
            System.out.println("Eye icons loaded successfully!");
        } catch (Exception e) {
            // If icons cannot be loaded, use Unicode fallback
            System.err.println("Could not load eye icons: " + e.getMessage());
            e.printStackTrace();
            // Use Unicode eye character as fallback
            togglePasswordButton.setText("👁");
            eyeIcon.setVisible(false); // Hide the image view
            // Set default icons to null to avoid null pointer exceptions
            eyeOpenIcon = null;
            eyeClosedIcon = null;
        }
        
        // Set up keyboard event handling for login submission
        // Allow users to press Enter to submit login form
        passwordField.setOnAction(event -> handleSignIn());
        visiblePasswordField.setOnAction(event -> handleSignIn());
        
        // Set up real-time error clearing when user starts typing
        // This provides immediate feedback and better UX
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                hideError(); // Clear error when user starts typing in email field
            }
        });
        
        // Sync password fields and clear errors when password changes
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                hideError(); // Clear error when user starts typing in password field
            }
            // Sync with visible password field when password is hidden
            if (!passwordVisible) {
                visiblePasswordField.setText(newValue);
            }
        });
        
        // Sync password fields and clear errors when visible password changes
        visiblePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                hideError(); // Clear error when user starts typing in visible password field
            }
            // Sync with hidden password field when password is visible
            if (passwordVisible) {
                passwordField.setText(newValue);
            }
        });
    }
    
    /**
     * Toggle password visibility between hidden and visible states
     * This method switches between password field types and updates the UI accordingly
     */
    @FXML
    private void handleTogglePassword() {
        passwordVisible = !passwordVisible; // Toggle the visibility state
        
        if (passwordVisible) {
            // Show password as plain text for better user experience
            passwordField.setVisible(false);      // Hide the password field
            passwordField.setManaged(false);      // Remove from layout management
            visiblePasswordField.setVisible(true); // Show the visible password field
            visiblePasswordField.setManaged(true); // Add to layout management
            visiblePasswordField.setText(passwordField.getText()); // Sync text
            visiblePasswordField.requestFocus(); // Request focus to the visible field
            if (eyeOpenIcon != null) {
                eyeIcon.setImage(eyeOpenIcon); // Set eye icon to open
            } else {
                togglePasswordButton.setText("🙈"); // Fallback to Unicode
            }
        } else {
            // Hide password
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            passwordField.setText(visiblePasswordField.getText()); // Sync text
            passwordField.requestFocus(); // Request focus to the password field
            if (eyeClosedIcon != null) {
                eyeIcon.setImage(eyeClosedIcon); // Set eye icon to closed
            } else {
                togglePasswordButton.setText("👁"); // Fallback to Unicode
            }
        }
    }
    
    /**
     * Handle the login submission process.
     * This method validates input, performs authentication, and navigates
     * to the appropriate dashboard based on the user's role.
     */
    @FXML
    private void handleSignIn() {
        String email = emailField.getText().trim();
        String password = passwordVisible ? visiblePasswordField.getText() : passwordField.getText();
        
        // Validate input
        if (!validateInput(email, password)) {
            return;
        }
        
        signInButton.setDisable(true);
        signInButton.setText("Signing In...");
        
        try {
            System.out.println("Attempting to authenticate user: " + email);
            
            // DEMO MODE: Bypass database for testing
            if (email.equals("demo@eventra.com") && password.equals("demo123")) {
                // Create demo user
                User demoUser = new User("demo", "Demo", "User", email, "");
                demoUser.setUserId(1);
                demoUser.setRoleTypeId(2); // Admin role
                
                SessionManager.setCurrentUser(demoUser);
                ViewUtil.switchTo("Dashboard", emailField.getScene().getWindow());
                return;
            }
            
            // SUPER ADMIN DEMO MODE: Direct access to SuperAdmin Dashboard
            if (email.equals("superadmin@eventra.com") && password.equals("admin123")) {
                // Create demo super admin user
                User superAdminUser = new User("superadmin", "Super", "Admin", email, "");
                superAdminUser.setUserId(0);
                superAdminUser.setRoleTypeId(1); // SuperAdmin role
                
                SessionManager.setCurrentUser(superAdminUser);
                ViewUtil.switchTo("SuperAdminDashboard", emailField.getScene().getWindow());
                return;
            }
            
            // NORMAL USER DEMO MODE: For testing normal user functionality
            if (email.equals("matcha123@gmail.com") && password.equals("matcha123@gmail.com")) {
                // Create demo normal user
                User normalUser = new User("matcha123", "Matcha", "User", email, "");
                normalUser.setUserId(100);
                normalUser.setRoleTypeId(4); // Attendee role
                
                SessionManager.setCurrentUser(normalUser);
                ViewUtil.switchTo("AttendeeEvents", emailField.getScene().getWindow());
                return;
            }
            
            // Try real authentication
            Optional<User> userOpt = UserDAO.authenticateUser(email, password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                System.out.println("Authentication successful for user: " + user.getEmail() + " with role: " + user.getRoleTypeId());
                SessionManager.setCurrentUser(user);
                
                // Redirect based on user role
                if (user.getRoleTypeId() == 1) {
                    // SuperAdmin gets the specialized dashboard
                    System.out.println("Redirecting SuperAdmin to SuperAdminDashboard page");
                    ViewUtil.switchTo("SuperAdminDashboard", emailField.getScene().getWindow());
                } else if (user.getRoleTypeId() == 2) {
                    // Admin gets the admin dashboard
                    System.out.println("Redirecting Admin to AdminDashboard page");
                    ViewUtil.switchTo("AdminDashboard", emailField.getScene().getWindow());
                } else if (user.getRoleTypeId() == 4) {
                    // Attendee
                    System.out.println("Redirecting attendee to AttendeeEvents page");
                    ViewUtil.switchTo("AttendeeEvents", emailField.getScene().getWindow());
                } else {
                    // Staff and other roles get the generic dashboard
                    System.out.println("Redirecting user to Dashboard page");
                    ViewUtil.switchTo("Dashboard", emailField.getScene().getWindow());
                }
            } else {
                System.out.println("Authentication failed - invalid credentials");
                showError("Invalid email or password. Please try again.");
            }
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            showError("An error occurred during login. Please try again. Use demo@eventra.com / demo123 for demo mode.");
        } finally {
            // Re-enable button
            signInButton.setDisable(false);
            signInButton.setText("Sign In");
        }
    }
    
    /**
     * Navigate to the signup page.
     * This method is triggered when the user clicks the "Sign Up" hyperlink.
     */
    @FXML
    private void handleSignUp() {
        // Navigate to sign up page
        System.out.println("Sign Up button clicked! Attempting to navigate to SignUp page...");
        try {
            ViewUtil.switchTo("SignUp", emailField.getScene().getWindow());
            System.out.println("Successfully navigated to SignUp page");
        } catch (Exception e) {
            System.err.println("Error navigating to SignUp page: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Validate the user's input for email and password.
     * This method checks if the email and password fields are not empty and
     * if the email is in a valid format.
     * @param email The email address entered by the user.
     * @param password The password entered by the user.
     * @return true if input is valid, false otherwise.
     */
    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            showError("Please enter your email address.");
            emailField.requestFocus();
            return false;
        }
        
        if (!isValidEmail(email)) {
            showError("Please enter a valid email address.");
            emailField.requestFocus();
            return false;
        }
        
        if (password.isEmpty()) {
            showError("Please enter your password.");
            passwordField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Check if the provided email address is in a valid format.
     * This is a simple regex-based validation.
     * @param email The email address to validate.
     * @return true if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        // Simple email validation using regex
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Display an error message in the error label.
     * This method sets the text of the error label and makes it visible.
     * @param message The error message to display.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    
    /**
     * Hide the error message from the error label.
     * This method makes the error label invisible and removes it from layout.
     */
    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
} 