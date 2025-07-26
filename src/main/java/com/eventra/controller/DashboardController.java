package com.eventra.controller;

import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import com.eventra.model.User;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DashboardController {
    
    @FXML private Text welcomeText;
    @FXML private Text emailText;
    @FXML private Text roleText;
    
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
        } else {
            // No user logged in, redirect to login
            ViewUtil.switchTo("Login", welcomeText.getScene().getWindow());
        }
    }
    
    @FXML
    private void handleViewEvents() {
        // TODO: Implement view events functionality
        System.out.println("View Events clicked - to be implemented");
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
                return "Administrator";
            case 2:
                return "User";
            case 3:
                return "Event Manager";
            default:
                return "Unknown";
        }
    }
} 