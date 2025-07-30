package com.eventra.controller;

import com.eventra.dao.UserDAO;
import com.eventra.model.User;
import com.eventra.util.ViewUtil;
import com.eventra.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.util.Optional;

public class  LoginController {  
    
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button signInButton;
    @FXML private Label errorLabel;
    @FXML private Hyperlink signUpLink;
    @FXML private ImageView logoImage;
    
    @FXML
    public void initialize() {
        // Set up logo
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png.png"));
            logoImage.setImage(logo);
        } catch (Exception e) {
            System.err.println("Could not load logo: " + e.getMessage());
        }
        
        // Set up enter key handling
        passwordField.setOnAction(event -> handleSignIn());
        
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
        });
    }
    
    @FXML
    private void handleSignIn() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
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
                // Create demo user
                User demoUser = new User("demo", "Demo", "User", email, "");
                demoUser.setUserId(1);
                demoUser.setRoleTypeId(2);
                
                SessionManager.setCurrentUser(demoUser);
                ViewUtil.switchTo("Dashboard", emailField.getScene().getWindow());
                return;
            }
            
            // Real authentication (when database is available)
            Optional<User> userOpt = UserDAO.authenticateUser(email, password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // Store current user in session (you can create a SessionManager class)
                SessionManager.setCurrentUser(user);
                
                // Navigate to dashboard
                ViewUtil.switchTo("Dashboard", emailField.getScene().getWindow());
            } else {
                showError("Invalid email or password. Please try again.");
            }
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            showError("Database not available. Use demo@eventra.com / demo123 for demo mode.");
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