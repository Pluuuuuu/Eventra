package com.eventra.controller;

import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import com.eventra.model.User;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class DashboardController {
    
    @FXML private Text welcomeText;
    @FXML private Text emailText;
    @FXML private Text roleText;
    @FXML private VBox roleSpecificContent;
    
    @FXML
    public void initialize() {
        // Get current user
        User currentUser = SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            // Display user information
            welcomeText.setText("Hello, " + currentUser.getFirstName() + "!");
            emailText.setText("Email: " + currentUser.getEmail());
            
            // Map role ID to role name
            String roleName = getRoleName(currentUser.getRoleTypeId());
            roleText.setText("Role: " + roleName);
            
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
                createSuperAdminContent();
                break;
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
    
    private void createSuperAdminContent() {
        Button manageUsersBtn = new Button("Manage Users");
        manageUsersBtn.setOnAction(e -> handleManageUsers());
        manageUsersBtn.getStyleClass().add("primary-button");
        
        Button systemSettingsBtn = new Button("System Settings");
        systemSettingsBtn.setOnAction(e -> handleSystemSettings());
        systemSettingsBtn.getStyleClass().add("secondary-button");
        
        roleSpecificContent.getChildren().addAll(manageUsersBtn, systemSettingsBtn);
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
} 