package com.eventra.controller;

import com.eventra.dao.UserDAO;
import com.eventra.model.User;
import com.eventra.util.ViewUtil;
import com.eventra.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class  LoginController {  
    
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private Button signInButton;
    @FXML private Label errorLabel;
    @FXML private Hyperlink signUpLink;
    @FXML private ImageView logoImage;
    @FXML private Button togglePasswordButton;
    @FXML private ImageView eyeIcon;
    @FXML private HBox passwordContainer;
    
    private boolean passwordVisible = false;
    private Image eyeOpenIcon;
    private Image eyeClosedIcon;
    
    @FXML
    public void initialize() {
        // Set up logo
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png.png"));
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
        String email = emailField.getText().trim();
        String password = passwordVisible ? visiblePasswordField.getText() : passwordField.getText();
        
        // Validate input
        if (!validateInput(email, password)) {
            return;
        }
        
        // Disable button during authentication
        signInButton.setDisable(true);
        signInButton.setText("Signing In...");
        
        try {
            // DEMO MODE: Bypass database for testing
            if (email.equals("demo@eventra.com") && password.equals("demo123")) {
                // Create demo admin user
                User demoUser = new User("demo", "Demo", "User", email, "");
                demoUser.setUserId(1);
                demoUser.setRoleTypeId(2); // Admin role
                
                SessionManager.setCurrentUser(demoUser);
                ViewUtil.switchTo("Dashboard", emailField.getScene().getWindow());
                return;
            }
            
            // DEMO MODE: Attendee user for testing
            if (email.equals("attendee@eventra.com") && password.equals("attendee123")) {
                // Create demo attendee user
                User demoAttendee = new User("attendee", "Demo", "Attendee", email, "");
                demoAttendee.setUserId(5);
                demoAttendee.setRoleTypeId(4); // Attendee role
                
                SessionManager.setCurrentUser(demoAttendee);
                ViewUtil.switchTo("AttendeeEvents", emailField.getScene().getWindow());
                return;
            }
            
            // DEMO MODE: User's test account
            if (email.equals("matcha123@gmail.com") && password.equals("matcha123@gmail.com")) {
                // Create demo attendee user for testing
                User testUser = new User("matcha123", "Matcha", "User", email, "");
                testUser.setUserId(6);
                testUser.setRoleTypeId(4); // Attendee role
                
                SessionManager.setCurrentUser(testUser);
                ViewUtil.switchTo("AttendeeEvents", emailField.getScene().getWindow());
                return;
            }
            
            // Try real authentication (when database is available)
            try {
                System.out.println("Attempting to authenticate user: " + email);
                Optional<User> userOpt = UserDAO.authenticateUser(email, password);
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    System.out.println("Authentication successful for user: " + user.getEmail() + " with role: " + user.getRoleTypeId());
                    SessionManager.setCurrentUser(user);
                    
                    // Redirect based on user role
                    if (user.getRoleTypeId() == 4) { // Attendee
                        System.out.println("Redirecting attendee to AttendeeEvents page");
                        ViewUtil.switchTo("AttendeeEvents", emailField.getScene().getWindow());
                    } else {
                        System.out.println("Redirecting user to Dashboard page");
                        ViewUtil.switchTo("Dashboard", emailField.getScene().getWindow());
                    }
                } else {
                    System.out.println("Authentication failed - invalid credentials");
                    showError("Invalid email or password. Please try again.");
                }
            } catch (Exception e) {
                // Database connection failed, show demo mode message
                System.err.println("Database connection failed: " + e.getMessage());
                e.printStackTrace();
                showError("Database connection failed. Use demo@eventra.com / demo123 for admin or attendee@eventra.com / attendee123 for attendee demo mode.");
            }
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            showError("An error occurred. Use demo@eventra.com / demo123 for admin or attendee@eventra.com / attendee123 for attendee demo mode.");
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