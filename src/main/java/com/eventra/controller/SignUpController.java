package com.eventra.controller;

import com.eventra.dao.UserDAO;
import com.eventra.model.User;
import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SignUpController {
    
    @FXML private TextField usernameField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button signUpButton;
    @FXML private Label errorLabel;
    @FXML private Hyperlink signInLink;
    
    @FXML
    public void initialize() {
        // Set up enter key handling
        confirmPasswordField.setOnAction(event -> handleSignUp());
        
        // Clear error when user starts typing
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
    }
    
    @FXML
    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validate input
        if (!validateInput(username, firstName, lastName, email, password, confirmPassword)) {
            return;
        }
        
        // Disable button during registration
        signUpButton.setDisable(true);
        signUpButton.setText("Creating Account...");
        
        try {
            // Check if username already exists
            if (UserDAO.usernameExists(username)) {
                showError("Username already exists. Please choose a different username.");
                usernameField.requestFocus();
                return;
            }
            
            // Check if email already exists
            if (UserDAO.emailExists(email)) {
                showError("Email already registered. Please use a different email or sign in.");
                emailField.requestFocus();
                return;
            }
            
            // Create new user
            User newUser = new User(username, firstName, lastName, email, UserDAO.hashPassword(password));
            
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
                            ViewUtil.switchTo("Login", usernameField.getScene().getWindow())
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
        ViewUtil.switchTo("Login", usernameField.getScene().getWindow());
    }
    
    private boolean validateInput(String username, String firstName, String lastName, 
                                String email, String password, String confirmPassword) {
        
        if (username.isEmpty()) {
            showError("Please enter a username.");
            usernameField.requestFocus();
            return false;
        }
        
        if (username.length() < 3) {
            showError("Username must be at least 3 characters long.");
            usernameField.requestFocus();
            return false;
        }
        
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
            showError("Please enter a valid email address.");
            emailField.requestFocus();
            return false;
        }
        
        if (password.isEmpty()) {
            showError("Please enter a password.");
            passwordField.requestFocus();
            return false;
        }
        
        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            passwordField.requestFocus();
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            confirmPasswordField.requestFocus();
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
        usernameField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
} 