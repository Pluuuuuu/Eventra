package com.eventra.controller;

import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class StaffAttendeeManagementController {
    
    @FXML private Text titleText;
    
    @FXML
    public void initialize() {
        System.out.println("Staff Attendee Management page loaded");
    }
    
    @FXML
    private void handleViewAttendees() {
        System.out.println("View Attendees clicked");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Feature Coming Soon");
        alert.setHeaderText("Attendee List");
        alert.setContentText("Attendee listing functionality will be implemented in the next version.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackToDashboard() {
        ViewUtil.switchTo("Dashboard", titleText.getScene().getWindow());
    }
} 