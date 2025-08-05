package com.eventra.controller;

import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import com.eventra.model.User;
import com.eventra.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UserInfoController {
    
    @FXML private ImageView profileImageView;
    @FXML private Text usernameText;
    @FXML private Text emailText;
    @FXML private Text fullNameText;
    
    private User currentUser;
    
    @FXML
    public void initialize() {
        // Get current user
        currentUser = SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            // Display user information
            usernameText.setText(currentUser.getUsername());
            emailText.setText(currentUser.getEmail());
            fullNameText.setText(currentUser.getFullName());
            
            // Load profile picture
            loadProfilePicture();
        } else {
            // No user logged in, redirect to login
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