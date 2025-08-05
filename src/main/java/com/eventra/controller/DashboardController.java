package com.eventra.controller;

import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import com.eventra.model.User;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DashboardController {
    
    @FXML private Text welcomeText;
    @FXML private Text emailText;
    @FXML private Text roleText;
    @FXML private VBox roleSpecificContent;
    @FXML private ImageView logoImageView;
    @FXML private ImageView profileImageView;
    
    @FXML
    public void initialize() {
        // Load logo
        loadLogo();
        
        // Get current user
        User currentUser = SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            // Display user information
            welcomeText.setText("Hello, " + currentUser.getFirstName() + "!");
            emailText.setText("Email: " + currentUser.getEmail());
            
            // Map role ID to role name
            String roleName = getRoleName(currentUser.getRoleTypeId());
            roleText.setText("Role: " + roleName);
            
            // Load profile picture
            loadProfilePicture(currentUser);
            
            // Show role-specific content
            showRoleSpecificContent(currentUser.getRoleTypeId());
        } else {
            // No user logged in, redirect to login
            ViewUtil.switchTo("Login", welcomeText.getScene().getWindow());
        }
    }
    
    private void showRoleSpecificContent(int roleTypeId) {
        // Clear existing content
        roleSpecificContent.getChildren().clear();
        
        switch (roleTypeId) {
            case 1: // SuperAdmin
                // Navigate directly to the new SuperAdmin Dashboard
                navigateToSuperAdminDashboard();
                return;
            case 2: // Admin
                createAdminContent();
                break;
            case 3: // Staff
                createStaffContent();
                break;
            case 4: // Attendee
                createAttendeeContent();
                break;
            default:
                createDefaultContent();
        }
    }
    
    private void navigateToSuperAdminDashboard() {
        System.out.println("Navigating to SuperAdmin Dashboard");
        ViewUtil.switchTo("SuperAdminDashboard", welcomeText.getScene().getWindow());
    }
    
    private void createSuperAdminContent() {
        // This method is now only kept for backward compatibility
        // SuperAdmins are redirected to the new dashboard view
        Button dashboardBtn = new Button("Go to Dashboard");
        dashboardBtn.setOnAction(e -> navigateToSuperAdminDashboard());
        dashboardBtn.getStyleClass().add("primary-button");
        
        roleSpecificContent.getChildren().add(dashboardBtn);
    }
    
    private void createAdminContent() {
        Button manageEventsBtn = new Button("Manage Events");
        manageEventsBtn.setOnAction(e -> handleManageEvents());
        manageEventsBtn.getStyleClass().add("primary-button");
        
        Button reportsBtn = new Button("View Reports");
        reportsBtn.setOnAction(e -> handleViewReports());
        reportsBtn.getStyleClass().add("secondary-button");
        
        roleSpecificContent.getChildren().addAll(manageEventsBtn, reportsBtn);
    }
    
    private void createStaffContent() {
        Button eventOperationsBtn = new Button("Event Operations");
        eventOperationsBtn.setOnAction(e -> handleEventOperations());
        eventOperationsBtn.getStyleClass().add("primary-button");
        
        Button attendeeManagementBtn = new Button("Attendee Management");
        attendeeManagementBtn.setOnAction(e -> handleAttendeeManagement());
        attendeeManagementBtn.getStyleClass().add("secondary-button");
        
        roleSpecificContent.getChildren().addAll(eventOperationsBtn, attendeeManagementBtn);
    }
    
    private void createAttendeeContent() {
        Button viewEventsBtn = new Button("View Events");
        viewEventsBtn.setOnAction(e -> handleViewEvents());
        viewEventsBtn.getStyleClass().add("primary-button");
        
        Button myProfileBtn = new Button("My Profile");
        myProfileBtn.setOnAction(e -> handleMyProfile());
        myProfileBtn.getStyleClass().add("secondary-button");
        
        roleSpecificContent.getChildren().addAll(viewEventsBtn, myProfileBtn);
    }
    
    private void createDefaultContent() {
        Button viewEventsBtn = new Button("View Events");
        viewEventsBtn.setOnAction(e -> handleViewEvents());
        viewEventsBtn.getStyleClass().add("primary-button");
        
        roleSpecificContent.getChildren().add(viewEventsBtn);
    }
    
    // Role-specific action handlers
    private void handleManageUsers() {
        System.out.println("Manage Users clicked - SuperAdmin functionality");
        ViewUtil.switchTo("SuperAdminManageUsers", welcomeText.getScene().getWindow());
    }
    
    private void handleSystemSettings() {
        System.out.println("System Settings clicked - SuperAdmin functionality");
        ViewUtil.switchTo("SuperAdminSystemSettings", welcomeText.getScene().getWindow());
    }
    
    private void handleManageEvents() {
        System.out.println("Manage Events clicked - Admin functionality");
        ViewUtil.switchTo("AdminManageEvents", welcomeText.getScene().getWindow());
    }
    
    private void handleViewReports() {
        System.out.println("View Reports clicked - Admin functionality");
        ViewUtil.switchTo("AdminReports", welcomeText.getScene().getWindow());
    }
    
    private void handleEventOperations() {
        System.out.println("Event Operations clicked - Staff functionality");
        ViewUtil.switchTo("StaffEventOperations", welcomeText.getScene().getWindow());
    }
    
    private void handleAttendeeManagement() {
        System.out.println("Attendee Management clicked - Staff functionality");
        ViewUtil.switchTo("StaffAttendeeManagement", welcomeText.getScene().getWindow());
    }
    
    @FXML
    private void handleViewEvents() {
        System.out.println("View Events clicked - Attendee functionality");
        ViewUtil.switchTo("AttendeeEvents", welcomeText.getScene().getWindow());
    }
    
    private void handleMyProfile() {
        System.out.println("My Profile clicked - Attendee functionality");
        ViewUtil.switchTo("AttendeeProfile", welcomeText.getScene().getWindow());
    }
    
    @FXML
    private void handleLogout() {
        // Clear session
        SessionManager.logout();
        
        // Navigate back to login
        ViewUtil.switchTo("Login", welcomeText.getScene().getWindow());
    }
    
    private String getRoleName(int roleTypeId) {
        switch (roleTypeId) {
            case 1:
                return "Super Administrator";
            case 2:
                return "Administrator";
            case 3:
                return "Staff";
            case 4:
                return "Attendee";
            default:
                return "Unknown";
        }
    }
    
    private void loadLogo() {
        try {
            // Load logo from resources
            String logoPath = getClass().getResource("/images/logo.png.png").toExternalForm();
            Image logoImage = new Image(logoPath);
            logoImageView.setImage(logoImage);
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
    }
    
    private void loadProfilePicture(User user) {
        try {
            if (user.getProfilePicUrl() != null && !user.getProfilePicUrl().trim().isEmpty()) {
                // Load user's profile picture
                File profilePicFile = new File(user.getProfilePicUrl());
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
        // Navigate to user info page
        ViewUtil.switchTo("UserInfo", profileImageView.getScene().getWindow());
    }
} 