package com.eventra.controller;

import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;

public class CreateEventController {
    
    // Basic Information Fields
    @FXML private TextField eventNameField;
    @FXML private ComboBox<String> eventTypeCombo;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField startTimeField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeField;
    @FXML private TextField locationField;
    @FXML private TextField capacityField;
    @FXML private TextField priceField;
    
    // Description Fields
    @FXML private TextField shortDescriptionField;
    @FXML private TextArea fullDescriptionArea;
    
    // Registration Settings
    @FXML private DatePicker regOpenDatePicker;
    @FXML private TextField regOpenTimeField;
    @FXML private DatePicker regCloseDatePicker;
    @FXML private TextField regCloseTimeField;
    @FXML private CheckBox requireApprovalCheck;
    @FXML private CheckBox waitlistEnabledCheck;
    
    // Contact Information
    @FXML private TextField contactPersonField;
    @FXML private TextField contactEmailField;
    @FXML private TextField contactPhoneField;
    @FXML private TextField websiteField;
    
    @FXML
    public void initialize() {
        System.out.println("Create Event form initialized");
        
        // Populate combo boxes
        setupComboBoxes();
        
        // Set default values
        setDefaultValues();
        
        // Add input validation
        setupValidation();
    }
    
    private void setupComboBoxes() {
        // Event Types
        eventTypeCombo.getItems().addAll(
            "Conference",
            "Workshop",
            "Seminar",
            "Webinar",
            "Networking",
            "Training",
            "Exhibition",
            "Social Event"
        );
        
        // Categories
        categoryCombo.getItems().addAll(
            "Technology",
            "Business",
            "Marketing",
            "Education",
            "Healthcare",
            "Finance",
            "Entertainment",
            "Sports",
            "Arts & Culture"
        );
    }
    
    private void setDefaultValues() {
        // Set default dates
        LocalDate today = LocalDate.now();
        startDatePicker.setValue(today.plusDays(7)); // Default to next week
        endDatePicker.setValue(today.plusDays(7));
        regOpenDatePicker.setValue(today);
        regCloseDatePicker.setValue(today.plusDays(6));
        
        // Set default times
        startTimeField.setText("09:00");
        endTimeField.setText("17:00");
        regOpenTimeField.setText("00:00");
        regCloseTimeField.setText("23:59");
        
        // Set default price
        priceField.setText("0.00");
    }
    
    private void setupValidation() {
        // Add numeric validation to capacity and price fields
        capacityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                capacityField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                priceField.setText(oldValue);
            }
        });
    }
    
    @FXML
    private void handleSaveDraft() {
        System.out.println("Save as Draft clicked");
        
        if (validateBasicFields()) {
            // TODO: Save event as draft to database
            showSuccessAlert("Draft Saved", "Event has been saved as draft successfully!");
            
            // Navigate back to manage events
            ViewUtil.switchToRoleDashboard(eventNameField.getScene().getWindow());
        }
    }
    
    @FXML
    private void handlePublishEvent() {
        System.out.println("Publish Event clicked");
        
        if (validateAllFields()) {
            // TODO: Save and publish event to database
            showSuccessAlert("Event Published", "Event has been published successfully!");
            
            // Navigate back to manage events
            ViewUtil.switchTo("AdminManageEvents", eventNameField.getScene().getWindow());
        }
    }
    
    @FXML
    private void handleCancel() {
        System.out.println("Cancel clicked");
        
        // Show confirmation dialog
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Discard Changes?");
        alert.setContentText("Are you sure you want to cancel? All unsaved changes will be lost.");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Navigate back to manage events
            ViewUtil.switchTo("AdminManageEvents", eventNameField.getScene().getWindow());
        }
    }
    
    private boolean validateBasicFields() {
        if (eventNameField.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Event name is required.");
            eventNameField.requestFocus();
            return false;
        }
        
        if (startDatePicker.getValue() == null) {
            showErrorAlert("Validation Error", "Start date is required.");
            startDatePicker.requestFocus();
            return false;
        }
        
        if (endDatePicker.getValue() == null) {
            showErrorAlert("Validation Error", "End date is required.");
            endDatePicker.requestFocus();
            return false;
        }
        
        if (locationField.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Location is required.");
            locationField.requestFocus();
            return false;
        }
        
        // Validate that end date is not before start date
        if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
            showErrorAlert("Validation Error", "End date cannot be before start date.");
            endDatePicker.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean validateAllFields() {
        if (!validateBasicFields()) {
            return false;
        }
        
        if (shortDescriptionField.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Short description is required for published events.");
            shortDescriptionField.requestFocus();
            return false;
        }
        
        if (eventTypeCombo.getValue() == null) {
            showErrorAlert("Validation Error", "Event type is required for published events.");
            eventTypeCombo.requestFocus();
            return false;
        }
        
        // Validate time format
        if (!isValidTimeFormat(startTimeField.getText())) {
            showErrorAlert("Validation Error", "Invalid start time format. Use HH:MM (24-hour format).");
            startTimeField.requestFocus();
            return false;
        }
        
        if (!isValidTimeFormat(endTimeField.getText())) {
            showErrorAlert("Validation Error", "Invalid end time format. Use HH:MM (24-hour format).");
            endTimeField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean isValidTimeFormat(String time) {
        return time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}