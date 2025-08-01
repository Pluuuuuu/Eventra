package com.eventra.controller;

import com.eventra.dao.UserDAO;
import com.eventra.dao.DashboardDAO;
import com.eventra.model.User;
import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.application.Platform;

public class SuperAdminDashboardController {
    
    // Navigation buttons
    @FXML private Button dashboardNavBtn;
    @FXML private Button analyticsNavBtn;
    @FXML private Button reportsNavBtn;
    @FXML private Button settingsNavBtn;
    
    // User info
    @FXML private Text userNameText;
    
    // Metric values
    @FXML private Text totalEventsText;
    @FXML private Text totalUsersText;
    @FXML private Text totalRegistrationsText;
    @FXML private Text totalAttendeesText;
    
    @FXML
    public void initialize() {
        System.out.println("SuperAdmin Dashboard initialized");
        
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
            // Load real data from database using DashboardDAO
            DashboardDAO.DashboardMetrics metrics = DashboardDAO.getDashboardMetrics();
            
            // Update UI with actual database data
            totalEventsText.setText(String.valueOf(metrics.totalEvents));
            totalUsersText.setText(String.format("%,d", metrics.totalUsers));
            totalRegistrationsText.setText(String.valueOf(metrics.totalRegistrations));
            totalAttendeesText.setText(String.format("%,d", metrics.totalAttendees));
            
            System.out.println("Dashboard metrics updated from database successfully!");
            
        } catch (Exception e) {
            System.err.println("Error loading dashboard metrics from database: " + e.getMessage());
            e.printStackTrace();
            
            // Use fallback demo values if database is not available
            totalEventsText.setText("24");
            totalUsersText.setText("1,245");
            totalRegistrationsText.setText("128");
            totalAttendeesText.setText("7,890");
            
            System.out.println("Using fallback demo data for dashboard metrics");
        }
    }
    
    // These methods are now handled by DashboardDAO
    // Keeping them for backward compatibility if needed
    private int getTotalEventsCount() {
        return DashboardDAO.getActiveEventsCount();
    }
    
    private int getTotalUsersCount() {
        return DashboardDAO.getTotalUsersCount();
    }
    
    private int getTotalRegistrationsCount() {
        return DashboardDAO.getRegistrationsThisMonth();
    }
    
    private int getTotalAttendeesCount() {
        return DashboardDAO.getTotalAttendeesCount();
    }
    
    // Navigation handlers
    @FXML
    private void handleDashboardNav() {
        System.out.println("Dashboard navigation clicked");
        setActiveNavButton(dashboardNavBtn);
        // Current view is already the dashboard, no need to navigate
    }
    
    @FXML
    private void handleAnalyticsNav() {
        System.out.println("Analytics navigation clicked");
        setActiveNavButton(analyticsNavBtn);
        // TODO: Navigate to analytics view or show analytics content
        showComingSoonAlert("Analytics");
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
        ViewUtil.switchTo("SuperAdminSystemSettings", userNameText.getScene().getWindow());
    }
    
    // Quick action handlers
    @FXML
    private void handleCreateEvent() {
        System.out.println("Create Event clicked");
        ViewUtil.switchTo("AdminManageEvents", userNameText.getScene().getWindow());
    }
    
    @FXML
    private void handleManageUsers() {
        System.out.println("Manage Users clicked");
        ViewUtil.switchTo("SuperAdminManageUsers", userNameText.getScene().getWindow());
    }
    
    @FXML
    private void handleViewReports() {
        System.out.println("View Reports clicked");
        ViewUtil.switchTo("AdminReports", userNameText.getScene().getWindow());
    }
    
    @FXML
    private void handleSystemSettings() {
        System.out.println("System Settings clicked");
        ViewUtil.switchTo("SuperAdminSystemSettings", userNameText.getScene().getWindow());
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
        analyticsNavBtn.getStyleClass().remove("nav-button-active");
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