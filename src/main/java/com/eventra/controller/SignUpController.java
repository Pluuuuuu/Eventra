package com.eventra.controller;

import com.eventra.dao.UserDAO;
import com.eventra.model.User;
import com.eventra.util.ViewUtil;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SignUpController {
    
    @FXML private TextField firstNameField;
    @FXML private TextField middleNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField organizationField;
    @FXML private Button signUpButton;
    @FXML private Label errorLabel;
    @FXML private Hyperlink signInLink;
    
    private Timeline emailCheckTimeline;
    
    @FXML
    public void initialize() {
        // Organization field is now a text field - no initialization needed
        
        // Set up enter key handling
        confirmPasswordField.setOnAction(event -> handleSignUp());
        
        // Clear error when user starts typing
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        middleNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        // Special email field listener with real-time validation
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
            
            // Cancel any pending email check
            if (emailCheckTimeline != null) {
                emailCheckTimeline.stop();
            }
            
            // Start a new timeline to check email after user stops typing (500ms delay)
            emailCheckTimeline = new Timeline(new KeyFrame(Duration.millis(500), e -> checkEmailExists()));
            emailCheckTimeline.play();
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        organizationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
    }
    
    private void checkEmailExists() {
        String email = emailField.getText().trim();
        
        // Only check if email is not empty and has valid format
        if (!email.isEmpty() && isValidEmail(email)) {
            try {
                if (UserDAO.emailExists(email)) {
                    showError("This email already exists");
                    emailField.requestFocus();
                }
            } catch (Exception e) {
                System.err.println("Error checking email existence: " + e.getMessage());
                // Don't show error to user for database connectivity issues during typing
            }
        }
    }
    
    @FXML
    private void handleSignUp() {
        String firstName = firstNameField.getText().trim();
        String middleName = middleNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String organization = organizationField.getText().trim();
        
        // Generate username from email (part before @)
        String username = email.contains("@") ? email.substring(0, email.indexOf("@")) : email;
        
        // Validate input
        if (!validateInput(firstName, middleName, lastName, email, password, confirmPassword, organization)) {
            return;
        }
        
        // Disable button during registration
        signUpButton.setDisable(true);
        signUpButton.setText("Creating Account...");
        
        try {
            // Check if username already exists (generated from email)
            if (UserDAO.usernameExists(username)) {
                // If username exists, append a number to make it unique
                int counter = 1;
                String originalUsername = username;
                while (UserDAO.usernameExists(username)) {
                    username = originalUsername + counter;
                    counter++;
                }
            }
            
            // Double-check if email already exists during submission
            if (UserDAO.emailExists(email)) {
                showError("This email already exists. Please use a different email or sign in.");
                emailField.requestFocus();
                return;
            }
            
            // Create new user
            User newUser = new User(username, firstName, lastName, email, UserDAO.hashPassword(password));
            newUser.setMiddleName(middleName.isEmpty() ? null : middleName);
            
            if (UserDAO.createUser(newUser)) {
                // Registration successful
                showSuccess("Account created successfully! Please sign in.");
                
                // Clear form
                clearForm();
                
                // Navigate to login after a short delay
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(() -> 
                            ViewUtil.switchTo("Login", firstNameField.getScene().getWindow())
                        );
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
                
            } else {
                showError("Failed to create account. Please try again.");
            }
            
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            showError("An error occurred during registration. Please try again.");
        } finally {
            // Re-enable button
            signUpButton.setDisable(false);
            signUpButton.setText("Create Account");
        }
    }
    
    @FXML
    private void handleSignIn() {
        // Navigate to login page
        ViewUtil.switchTo("Login", firstNameField.getScene().getWindow());
    }
    
    private boolean validateInput(String firstName, String middleName, String lastName, 
                                String email, String password, String confirmPassword, String organization) {
        
        if (firstName.isEmpty()) {
            showError("Please enter your first name.");
            firstNameField.requestFocus();
            return false;
        }
        
        if (lastName.isEmpty()) {
            showError("Please enter your last name.");
            lastNameField.requestFocus();
            return false;
        }
        
        if (email.isEmpty()) {
            showError("Please enter your email address.");
            emailField.requestFocus();
            return false;
        }
        
        if (!isValidEmail(email)) {
            showError("Please enter a valid email address (e.g., user@example.com).");
            emailField.requestFocus();
            return false;
        }
        
        if (password.isEmpty()) {
            showError("Please enter a password.");
            passwordField.requestFocus();
            return false;
        }
        
        if (password.length() < 8) {
            showError("Password must be at least 8 characters long.");
            passwordField.requestFocus();
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            confirmPasswordField.requestFocus();
            return false;
        }
        
        if (organization == null || organization.isEmpty()) {
            showError("Please enter your organization.");
            organizationField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        // More comprehensive email validation
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Check for basic email structure
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        
        if (!email.matches(emailRegex)) {
            return false;
        }
        
        // Additional checks for common email format issues
        if (email.startsWith(".") || email.endsWith(".") || 
            email.contains("..") || email.startsWith("@") || 
            email.endsWith("@") || email.indexOf("@") != email.lastIndexOf("@")) {
            return false;
        }
        
        // Check domain part
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        
        String domain = parts[1];
        if (domain.startsWith(".") || domain.endsWith(".") || 
            domain.startsWith("-") || domain.endsWith("-") ||
            domain.contains("..") || domain.length() < 3) {
            return false;
        }
        
        return true;
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #d32f2f;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    
    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #388e3c;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    
    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
    
    private void clearForm() {
        firstNameField.clear();
        middleNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        organizationField.clear();
    }
} 