package com.eventra.controller;

import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class AttendeeEventsController {
    
    @FXML private Text titleText;
    
    @FXML
    public void initialize() {
        System.out.println("Attendee Events page loaded");
    }
    
    @FXML
    private void handleSearchEvents() {
        System.out.println("Search Events clicked");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Feature Coming Soon");
        alert.setHeaderText("Event Search");
        alert.setContentText("Event search and registration functionality will be implemented in the next version.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackToDashboard() {
        ViewUtil.switchTo("Dashboard", titleText.getScene().getWindow());
    }
} 