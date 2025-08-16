/**
 * Login Controller (LoginController.java)
 * 
 * This class controls the LOGIN SCREEN of the Eventra application. Think of it as the
 * "brain" that handles everything that happens when a user tries to sign in.
 * 
 * WHAT THIS CLASS DOES:
 * - Manages the login form (email and password fields)
 * - Handles user authentication when they click "Sign In"
 * - Shows/hides password for better user experience
 * - Displays error messages if login fails
 * - Redirects users to appropriate screens based on their role
 * - Manages the visual elements (logo, icons, buttons)
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Event Handling: Responds to user actions (clicks, typing, etc.)
 * - Property Binding: Automatically syncs UI elements with data
 * - Error Handling: Gracefully handles problems and shows user-friendly messages
 * - Session Management: Creates user sessions after successful login
 * 
 * UI COMPONENTS MANAGED:
 * - Email input field
 * - Password input field (with show/hide toggle)
 * - Sign In button
 * - Error message display
 * - Sign Up link
 * - Logo and eye icons
 * 
 * SECURITY FEATURES:
 * - Password visibility toggle for user convenience
 * - Input validation before sending to database
 * - Secure password handling (never logged or displayed)
 * - Role-based navigation after successful login
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.dao.UserDAO;           // For user authentication (database operations)
import com.eventra.model.User;             // User data model
import com.eventra.util.ViewUtil;          // For navigating between screens
import com.eventra.util.SessionManager;    // For managing user sessions after login

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                  // Links this class to FXML UI elements
import javafx.scene.control.*;            // UI controls (buttons, text fields, labels, etc.)
import javafx.scene.image.ImageView;      // For displaying images (logo, icons)
import javafx.scene.image.Image;          // For loading image files
import javafx.scene.layout.HBox;          // Horizontal layout container

// Java standard library imports:
import java.util.Optional;                // Modern Java way to handle "maybe exists" values

/**
 * LoginController class - Controls the Login Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: User data and authentication logic (UserDAO, User)
 * - VIEW: FXML file defines the visual layout
 * - CONTROLLER: This class connects the model and view
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the Login.fxml file, allowing us to control the UI elements.
 */
public class LoginController {  
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the Login.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what the user typed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can respond to user actions (setOnAction())
     */
    @FXML private TextField emailField;           // Where user types their email address
    @FXML private PasswordField passwordField;    // Hidden password input (shows dots/asterisks)
    @FXML private TextField visiblePasswordField; // Visible password input (shows actual text)
    @FXML private Button signInButton;            // Button user clicks to attempt login
    @FXML private Label errorLabel;               // Displays error messages if login fails
    @FXML private Hyperlink signUpLink;           // Link to go to the sign-up page
    @FXML private ImageView logoImage;            // Displays the Eventra logo
    @FXML private Button togglePasswordButton;    // Button to show/hide password
    @FXML private ImageView eyeIcon;              // Eye icon that changes based on password visibility
    @FXML private HBox passwordContainer;         // Container that holds password-related elements
    
    /**
     * INSTANCE VARIABLES - Store state and data for this controller
     * 
     * These variables keep track of the current state of the login screen
     * and store resources like images that we load once and reuse.
     */
    private boolean passwordVisible = false;      // Tracks whether password is currently visible
    private Image eyeOpenIcon;                   // Image for when password is visible (👁️)
    private Image eyeClosedIcon;                 // Image for when password is hidden (🙈)
    
    /**
     * initialize() method - Sets up the login screen when it first loads
     * 
     * This method is automatically called by JavaFX when the login screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Loads and displays the Eventra logo
     * 2. Loads eye icons for the password visibility toggle
     * 3. Sets up keyboard shortcuts (Enter key to sign in)
     * 4. Connects text change listeners for real-time error clearing
     * 5. Synchronizes password fields for the show/hide functionality
     * 
     * WHY IT'S IMPORTANT:
     * - Ensures all UI elements are properly configured
     * - Loads resources (images) that might take time to load
     * - Sets up event handlers for user interactions
     * - Provides fallback behavior if resources fail to load
     * 
     * ERROR HANDLING:
     * - Gracefully handles missing logo or icon files
     * - Provides fallback text for missing icons
     * - Logs errors for debugging without crashing the app
     */
    @FXML
    public void initialize() {
        // Set up logo
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoImage.setImage(logo);
        } catch (Exception e) {
            System.err.println("Could not load logo: " + e.getMessage());
        }
        
        // Load eye icons for password toggle
        try {
            eyeOpenIcon = new Image(getClass().getResourceAsStream("/images/eye-open.png"));
            eyeClosedIcon = new Image(getClass().getResourceAsStream("/images/eye-closed.png"));
            eyeIcon.setImage(eyeClosedIcon);
            System.out.println("Eye icons loaded successfully!");
        } catch (Exception e) {
            System.err.println("Could not load eye icons: " + e.getMessage());
            e.printStackTrace();
            // Use Unicode characters as fallback
            togglePasswordButton.setText("👁");
            eyeIcon.setVisible(false);
            // Set default icons to null to avoid null pointer exceptions
            eyeOpenIcon = null;
            eyeClosedIcon = null;
        }
        
        // Set up enter key handling
        passwordField.setOnAction(event -> handleSignIn());
        visiblePasswordField.setOnAction(event -> handleSignIn());
        
        // Clear error when user starts typing
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                hideError();
            }
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                hideError();
            }
            // Sync with visible password field
            if (!passwordVisible) {
                visiblePasswordField.setText(newValue);
            }
        });
        
        visiblePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                hideError();
            }
            // Sync with password field
            if (passwordVisible) {
                passwordField.setText(newValue);
            }
        });
    }
    
    /**
     * handleTogglePassword() method - Shows or hides the password
     * 
     * This method is called when the user clicks the eye icon button. It toggles
     * between showing the password as plain text and hiding it as dots/asterisks.
     * 
     * HOW IT WORKS:
     * 1. Toggles the passwordVisible flag (true becomes false, false becomes true)
     * 2. If showing password: hides the hidden field, shows the visible field
     * 3. If hiding password: shows the hidden field, hides the visible field
     * 4. Updates the eye icon to match the current state
     * 5. Focuses the appropriate password field for better user experience
     * 
     * WHY THIS FEATURE EXISTS:
     * - Users often make typos in passwords and can't see what they typed
     * - Helps users verify they entered the correct password
     * - Improves user experience and reduces login frustration
     * - Common feature in modern applications
     * 
     * SECURITY CONSIDERATIONS:
     * - Password is only visible when user explicitly requests it
     * - Hidden by default for security
     * - Both fields are synchronized so data isn't lost when switching
     * 
     * @FXML annotation means this method can be called from the FXML file
     * (when the user clicks the eye icon button)
     */
    @FXML
    private void handleTogglePassword() {
        passwordVisible = !passwordVisible;
        
        if (passwordVisible) {
            // Show password as text
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.requestFocus();
            if (eyeOpenIcon != null) {
                eyeIcon.setImage(eyeOpenIcon);
            } else {
                togglePasswordButton.setText("🙈");
            }
        } else {
            // Hide password
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            passwordField.setText(visiblePasswordField.getText());
            passwordField.requestFocus();
            if (eyeClosedIcon != null) {
                eyeIcon.setImage(eyeClosedIcon);
            } else {
                togglePasswordButton.setText("👁");
            }
        }
    }
    
    @FXML
    private void handleSignIn() {
        // Get user input and trim whitespace for better user experience
        String email = emailField.getText().trim();
        String password = passwordVisible ? visiblePasswordField.getText() : passwordField.getText();
        
        // Validate input before attempting authentication
        if (!validateInput(email, password)) {
            return;  // Stop if validation fails
        }
        
        // Disable sign-in button to prevent multiple submissions
        signInButton.setDisable(true);
        signInButton.setText("Signing In...");
        
        try {
            System.out.println("Attempting to authenticate user: " + email);
            
            // DEMO MODE: Bypass database for testing and demonstration purposes
            // This allows developers and users to test the system without a database
            if (email.equals("demo@eventra.com") && password.equals("demo123")) {
                // Create demo user with admin privileges for testing
                User demoUser = new User("demo", "Demo", "User", email, "");
                demoUser.setUserId(1);
                demoUser.setRoleTypeId(2); // Admin role (can manage events and users)
                
                // Create user session and redirect to admin dashboard
                SessionManager.setCurrentUser(demoUser);
                ViewUtil.switchTo("Dashboard", emailField.getScene().getWindow());
                return;
            }
            
            // SUPER ADMIN DEMO MODE: Direct access to SuperAdmin Dashboard
            // This provides access to system-wide management features
            if (email.equals("superadmin@eventra.com") && password.equals("admin123")) {
                // Create demo super admin user with full system access
                User superAdminUser = new User("superadmin", "Super", "Admin", email, "");
                superAdminUser.setUserId(0);
                superAdminUser.setRoleTypeId(1); // SuperAdmin role (can manage everything)
                
                // Create user session and redirect to super admin dashboard
                SessionManager.setCurrentUser(superAdminUser);
                ViewUtil.switchTo("SuperAdminDashboard", emailField.getScene().getWindow());
                return;
            }
            
            // REAL AUTHENTICATION: Attempt to authenticate against the database
            // This is the normal authentication flow for real users
            Optional<User> userOpt = UserDAO.authenticateUser(email, password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                System.out.println("Authentication successful for user: " + user.getEmail() + " with role: " + user.getRoleTypeId());
                
                // Create user session for the authenticated user
                SessionManager.setCurrentUser(user);
                
                // ROLE-BASED REDIRECTION: Send users to appropriate dashboards
                // This ensures users only see features they have permission to access
                if (user.getRoleTypeId() == 1) {
                    // SuperAdmin gets the specialized dashboard with system management
                    System.out.println("Redirecting SuperAdmin to SuperAdminDashboard page");
                    ViewUtil.switchTo("SuperAdminDashboard", emailField.getScene().getWindow());
                } else if (user.getRoleTypeId() == 2) {
                    // Admin gets the admin dashboard for event and user management
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
    
    private boolean isValidEmail(String email) {
        // Simple email validation
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    
    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
} 