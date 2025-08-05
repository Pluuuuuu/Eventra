package com.eventra.controller;

import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import com.eventra.model.User;
import com.eventra.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class EditProfileController {
    
    @FXML private TextField usernameField;
    @FXML private TextField firstNameField;
    @FXML private TextField middleNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    
    private User currentUser;
    
    @FXML
    public void initialize() {
        // Get current user
        currentUser = SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            // Populate fields with current user data
            usernameField.setText(currentUser.getUsername());
            firstNameField.setText(currentUser.getFirstName());
            middleNameField.setText(currentUser.getMiddleName() != null ? currentUser.getMiddleName() : "");
            lastNameField.setText(currentUser.getLastName());
            emailField.setText(currentUser.getEmail());
        } else {
            // No user logged in, redirect to login
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