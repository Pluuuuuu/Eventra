package com.eventra.controller;

import com.eventra.dao.UserDAO;
import com.eventra.model.User;
import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.format.DateTimeFormatter;

public class AttendeeProfileController {
    
    @FXML private ImageView profileImage;
    @FXML private ImageView logoImage;
    @FXML private Text profileName;
    @FXML private Text profileEmail;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private Button saveButton;
    @FXML private Text memberSinceText;
    @FXML private Text eventsAttendedText;
    @FXML private TextField searchField;
    
    private User currentUser;
    private boolean hasChanges = false;
    
    @FXML
    public void initialize() {
        loadImages();
        loadUserProfile();
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