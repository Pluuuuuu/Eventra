package com.eventra.controller;

import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class AdminReportsController {
    
    @FXML private Text titleText;
    
    @FXML
    public void initialize() {
        System.out.println("Admin Reports page loaded");
    }
    
    @FXML
    private void handleGenerateReport() {
        System.out.println("Generate Report clicked");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Feature Coming Soon");
        alert.setHeaderText("Report Generation");
        alert.setContentText("Report generation functionality will be implemented in the next version.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackToDashboard() {
        ViewUtil.switchTo("Dashboard", titleText.getScene().getWindow());
    }
} 