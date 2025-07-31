package com.eventra.controller;

import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class AdminManageEventsController {
    
    @FXML private Text titleText;
    
    @FXML
    public void initialize() {
        System.out.println("Admin Manage Events page loaded");
    }
    
    @FXML
    private void handleCreateEvent() {
        System.out.println("Create New Event clicked");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Feature Coming Soon");
        alert.setHeaderText("Create Event");
        alert.setContentText("Event creation functionality will be implemented in the next version.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackToDashboard() {
        ViewUtil.switchTo("Dashboard", titleText.getScene().getWindow());
    }
} 