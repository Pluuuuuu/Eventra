package com.eventra.controller;

import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class StaffEventOperationsController {
    
    @FXML private Text titleText;
    
    @FXML
    public void initialize() {
        System.out.println("Staff Event Operations page loaded");
    }
    
    @FXML
    private void handleStartCheckin() {
        System.out.println("Start Check-in clicked");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Feature Coming Soon");
        alert.setHeaderText("Check-in System");
        alert.setContentText("Attendee check-in functionality will be implemented in the next version.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackToDashboard() {
        ViewUtil.switchToRoleDashboard(titleText.getScene().getWindow());
    }
} 