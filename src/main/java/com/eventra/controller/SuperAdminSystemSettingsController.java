package com.eventra.controller;

import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class SuperAdminSystemSettingsController {
    
    @FXML private Text titleText;
    
    @FXML
    public void initialize() {
        System.out.println("SuperAdmin System Settings page loaded");
    }
    
    @FXML
    private void handleDatabaseSettings() {
        System.out.println("Database Settings clicked");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Feature Coming Soon");
        alert.setHeaderText("Database Settings");
        alert.setContentText("Database configuration will be available in the next version.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackToDashboard() {
        ViewUtil.switchToRoleDashboard(titleText.getScene().getWindow());
    }
} 