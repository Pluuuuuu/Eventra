package com.eventra.controller;

import com.eventra.dao.UserDAO;
import com.eventra.model.User;
import com.eventra.util.ViewUtil;
import com.eventra.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class LoginController {
    
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button signInButton;
    @FXML private Label errorLabel;
    @FXML private Hyperlink signUpLink;
    
    @FXML
    public void initialize() {
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
        ViewUtil.switchTo("SignUp", emailField.getScene().getWindow());
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