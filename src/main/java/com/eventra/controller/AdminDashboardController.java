package com.eventra.controller;

import com.eventra.dao.DashboardDAO;
import com.eventra.model.User;
import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.application.Platform;

public class AdminDashboardController {
    
    // Navigation buttons
    @FXML private Button dashboardNavBtn;
    @FXML private Button eventsNavBtn;
    @FXML private Button reportsNavBtn;
    @FXML private Button settingsNavBtn;
    
    // User info
    @FXML private Text userNameText;
    
    // Metric values
    @FXML private Text myEventsText;
    @FXML private Text totalRegistrationsText;
    @FXML private Text activeEventsText;
    
    @FXML
    public void initialize() {
        System.out.println("Admin Dashboard initialized");
        
        // Load user information
        loadUserInfo();
        
        // Load dashboard metrics
        loadDashboardMetrics();
        
        // Set default navigation state
        setActiveNavButton(dashboardNavBtn);
    }
    
    private void loadUserInfo() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getFirstName() + " " + currentUser.getLastName();
            userNameText.setText(displayName);
        } else {
            // No user logged in, redirect to login
            Platform.runLater(() -> {
                ViewUtil.switchTo("Login", userNameText.getScene().getWindow());
            });
        }
    }
    
    private void loadDashboardMetrics() {
        try {
            // Load Admin-specific metrics from database
            User currentUser = SessionManager.getCurrentUser();
            
            if (currentUser != null) {
                // Get real metrics specific to this admin's events using new DAO methods
                DashboardDAO.AdminDashboardMetrics metrics = DashboardDAO.getAdminDashboardMetrics(currentUser.getUserId());
                
                // Update UI with real data from database
                myEventsText.setText(String.valueOf(metrics.myEvents));
                totalRegistrationsText.setText(String.format("%,d", metrics.totalRegistrations));
                activeEventsText.setText(String.valueOf(metrics.activeEvents));
                
                System.out.println("✅ Admin dashboard metrics loaded from database successfully!");
                System.out.println("   - My Events: " + metrics.myEvents);
                System.out.println("   - Total Registrations: " + metrics.totalRegistrations); 
                System.out.println("   - Active Events: " + metrics.activeEvents);
                
            } else {
                throw new Exception("No current user found");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error loading admin dashboard metrics: " + e.getMessage());
            e.printStackTrace();
            
            // Use fallback demo values for Admin
            myEventsText.setText("0");
            totalRegistrationsText.setText("0");
            activeEventsText.setText("0");
            
            System.out.println("Using fallback demo data for admin dashboard metrics");
        }
    }
    
    // Navigation handlers
    @FXML
    private void handleDashboardNav() {
        System.out.println("Dashboard navigation clicked");
        setActiveNavButton(dashboardNavBtn);
        // Current view is already the dashboard, no need to navigate
    }
    
    @FXML
    private void handleEventsNav() {
        System.out.println("Events navigation clicked");
        setActiveNavButton(eventsNavBtn);
        ViewUtil.switchTo("AdminManageEvents", userNameText.getScene().getWindow());
    }
    
    @FXML
    private void handleReportsNav() {
        System.out.println("Reports navigation clicked");
        setActiveNavButton(reportsNavBtn);
        ViewUtil.switchTo("AdminReports", userNameText.getScene().getWindow());
    }
    
    @FXML
    private void handleSettingsNav() {
        System.out.println("Settings navigation clicked");
        setActiveNavButton(settingsNavBtn);
        showComingSoonAlert("Settings");
    }
    
    // Quick action handlers
    @FXML
    private void handleCreateEvent() {
        System.out.println("Create Event clicked");
        ViewUtil.switchTo("CreateEvent", userNameText.getScene().getWindow());
    }
    
    @FXML
    private void handleManageEvents() {
        System.out.println("Manage Events clicked");
        ViewUtil.switchTo("AdminManageEvents", userNameText.getScene().getWindow());
    }
    
    @FXML
    private void handleViewReports() {
        System.out.println("View Reports clicked");
        ViewUtil.switchTo("AdminReports", userNameText.getScene().getWindow());
    }
    
    @FXML
    private void handleExportData() {
        System.out.println("Export Data clicked");
        showComingSoonAlert("Data Export");
    }
    
    @FXML
    private void handleLogout() {
        System.out.println("Logout clicked");
        SessionManager.logout();
        ViewUtil.switchTo("Login", userNameText.getScene().getWindow());
    }
    
    // Utility methods
    private void setActiveNavButton(Button activeButton) {
        // Remove active class from all nav buttons
        dashboardNavBtn.getStyleClass().remove("nav-button-active");
        eventsNavBtn.getStyleClass().remove("nav-button-active");
        reportsNavBtn.getStyleClass().remove("nav-button-active");
        settingsNavBtn.getStyleClass().remove("nav-button-active");
        
        // Add active class to the selected button
        if (!activeButton.getStyleClass().contains("nav-button-active")) {
            activeButton.getStyleClass().add("nav-button-active");
        }
    }
    
    private void showComingSoonAlert(String feature) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Feature Coming Soon");
        alert.setHeaderText(feature + " Feature");
        alert.setContentText(feature + " functionality will be available in the next version.");
        alert.showAndWait();
    }
}