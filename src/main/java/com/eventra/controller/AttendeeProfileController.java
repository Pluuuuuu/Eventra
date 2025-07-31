package com.eventra.controller;

import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class AttendeeProfileController {
    
    @FXML private Text titleText;
    
    @FXML
    public void initialize() {
        System.out.println("Attendee Profile page loaded");
    }
    
    @FXML
    private void handleEditProfile() {
        System.out.println("Edit Profile clicked");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Feature Coming Soon");
        alert.setHeaderText("Profile Editor");
        alert.setContentText("Profile editing functionality will be implemented in the next version.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackToDashboard() {
        ViewUtil.switchTo("Dashboard", titleText.getScene().getWindow());
    }
} 