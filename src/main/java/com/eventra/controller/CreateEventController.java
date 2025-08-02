package com.eventra.controller;

import com.eventra.dao.EventDAO;
import com.eventra.dao.EventSaveDAO;
import com.eventra.model.Session;
import com.eventra.util.ViewUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CreateEventController {
    
    // Event Details Fields
    @FXML private TextField eventNameField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField startTimeField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeField;
    @FXML private TextField locationField;
    @FXML private TextField capacityField;
    @FXML private TextField tagsField;
    
    // Sessions Components
    @FXML private TableView<Session> sessionsTable;
    @FXML private TableColumn<Session, String> sessionColumn;
    @FXML private TableColumn<Session, String> dateColumn;
    @FXML private TableColumn<Session, String> dayColumn;
    @FXML private TableColumn<Session, String> timeColumn;
    @FXML private TableColumn<Session, String> locationColumn;
    @FXML private TableColumn<Session, String> presenterColumn;
    @FXML private TableColumn<Session, String> statusColumn;
    @FXML private TableColumn<Session, Void> actionsColumn;
    
    // Presenters Components
    @FXML private GridPane presentersGrid;
    
    // Data lists
    private ObservableList<Session> sessionsList = FXCollections.observableArrayList();
    private ObservableList<PresenterInfo> presentersList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        System.out.println("Create Event form initialized");
        
        // Setup sessions table
        setupSessionsTable();
        
        // Set default values
        setDefaultValues();
        
        // Add input validation
        setupValidation();
        
        // Load existing presenters from database
        loadPresentersFromDatabase();
    }
    
    private void setupSessionsTable() {
        // Setup table columns
        sessionColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        presenterColumn.setCellValueFactory(new PropertyValueFactory<>("presenter"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Setup actions column with edit/delete buttons
        actionsColumn.setCellFactory(col -> new TableCell<Session, Void>() {
            private final Button editBtn = new Button("✎");
            private final Button deleteBtn = new Button("🗑");
            private final javafx.scene.layout.HBox actionBox = new javafx.scene.layout.HBox(5, editBtn, deleteBtn);
            
            {
                editBtn.getStyleClass().add("table-action-button");
                deleteBtn.getStyleClass().add("table-action-button");
                editBtn.setOnAction(e -> handleEditSession(getTableRow().getItem()));
                deleteBtn.setOnAction(e -> handleDeleteSession(getTableRow().getItem()));
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        });
        
        sessionsTable.setItems(sessionsList);
    }
    
    private void setDefaultValues() {
        // Explicitly clear all form fields to ensure they're empty
        eventNameField.setText("");
        descriptionArea.setText("");
        locationField.setText("");
        capacityField.setText("");
        tagsField.setText("");
        
        // Clear date pickers
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        
        // Clear time fields
        startTimeField.setText("");
        endTimeField.setText("");
        
        // Clear sessions and presenters lists
        sessionsList.clear();
        presentersList.clear();
        
        // Clear presenters grid UI
        presentersGrid.getChildren().clear();
    }
    
    private void setupValidation() {
        // Add numeric validation to capacity field
        capacityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                capacityField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
    
    private void loadPresentersFromDatabase() {
        try {
            // Fetch all presenters from database
            List<PresenterInfo> dbPresenters = EventDAO.getAllPresenters();
            
            // Clear existing presenters
            presentersList.clear();
            presentersGrid.getChildren().clear();
            
            // Add each presenter to the list and UI
            for (PresenterInfo presenter : dbPresenters) {
                presentersList.add(presenter);
                displayPresenterCard(presenter);
            }
            
            System.out.println("✅ Loaded " + dbPresenters.size() + " presenters in Create Event form");
            
        } catch (Exception e) {
            System.err.println("❌ Error loading presenters: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleSaveDraft() {
        System.out.println("Save as Draft clicked");
        
        if (validateBasicFields()) {
            saveEventToDatabase(true); // Save as draft
        }
    }
    
    @FXML
    private void handlePublishEvent() {
        System.out.println("Publish Event clicked");
        
        if (validateAllFields()) {
            saveEventToDatabase(false); // Save as published
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
    
    private void saveEventToDatabase(boolean isDraft) {
        try {
            // Extract form data
            String title = eventNameField.getText().trim();
            String description = descriptionArea.getText().trim();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String location = locationField.getText().trim();
            
            // Parse capacity
            int capacity = 100; // Default
            try {
                if (!capacityField.getText().trim().isEmpty()) {
                    capacity = Integer.parseInt(capacityField.getText().trim());
                }
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Invalid capacity format, using default: 100");
            }
            
            // Save to database
            EventSaveDAO.EventSaveResult result = EventSaveDAO.saveEvent(
                title, description, startDate, endDate, location, capacity, 
                sessionsList, presentersList, isDraft
            );
            
            if (result.success) {
                showSuccessAlert(isDraft ? "Draft Saved" : "Event Published", result.message);
                
                // Navigate back to manage events
                if (isDraft) {
                    ViewUtil.switchToRoleDashboard(eventNameField.getScene().getWindow());
                } else {
                    ViewUtil.switchTo("AdminManageEvents", eventNameField.getScene().getWindow());
                }
            } else {
                showErrorAlert("Save Failed", result.message);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error saving event: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Save Failed", "An unexpected error occurred while saving the event.");
        }
    }
    
    // Session Management Methods
    @FXML
    private void handleAddSession() {
        System.out.println("Add Session clicked");
        
        // Create custom dialog for adding session
        Dialog<Session> dialog = new Dialog<>();
        dialog.setTitle("Add New Session");
        dialog.setHeaderText("Enter session details");
        
        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        // Create the form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Session Name");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        TextField timeField = new TextField();
        timeField.setPromptText("e.g., 10:00 AM - 11:00 AM");
        TextField locationField = new TextField();
        locationField.setPromptText("Room or Location");
        TextField presenterField = new TextField();
        presenterField.setPromptText("Presenter Name");
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Scheduled", "In Progress", "Completed", "Cancelled");
        statusCombo.setValue("Scheduled");
        
        grid.add(new javafx.scene.control.Label("Session Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Date:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new javafx.scene.control.Label("Time:"), 0, 2);
        grid.add(timeField, 1, 2);
        grid.add(new javafx.scene.control.Label("Location:"), 0, 3);
        grid.add(locationField, 1, 3);
        grid.add(new javafx.scene.control.Label("Presenter:"), 0, 4);
        grid.add(presenterField, 1, 4);
        grid.add(new javafx.scene.control.Label("Status:"), 0, 5);
        grid.add(statusCombo, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        
        // Enable/Disable add button depending on whether name is entered
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
        
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });
        
        // Convert the result to a Session when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String dayOfWeek = datePicker.getValue() != null ? 
                    datePicker.getValue().getDayOfWeek().toString() : "TBD";
                String dateStr = datePicker.getValue() != null ? 
                    datePicker.getValue().toString() : "TBD";
                
                return new Session(
                    nameField.getText(),
                    dateStr,
                    dayOfWeek,
                    timeField.getText().isEmpty() ? "TBD" : timeField.getText(),
                    locationField.getText().isEmpty() ? "TBD" : locationField.getText(),
                    presenterField.getText().isEmpty() ? "TBD" : presenterField.getText(),
                    statusCombo.getValue()
                );
            }
            return null;
        });
        
        Optional<Session> result = dialog.showAndWait();
        result.ifPresent(session -> {
            sessionsList.add(session);
            System.out.println("✅ New session added: " + session.getName());
        });
    }
    
    private void handleEditSession(Session session) {
        if (session != null) {
            System.out.println("Edit session: " + session.getName());
            
            // TODO: Show edit session dialog
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Edit Session");
            alert.setHeaderText("Edit: " + session.getName());
            alert.setContentText("Session editing will be available in the next version.");
            alert.showAndWait();
        }
    }
    
    private void handleDeleteSession(Session session) {
        if (session != null) {
            System.out.println("Delete session: " + session.getName());
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Session");
            alert.setHeaderText("Delete: " + session.getName());
            alert.setContentText("Are you sure you want to delete this session?");
            
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                sessionsList.remove(session);
                System.out.println("Session deleted: " + session.getName());
            }
        }
    }
    
    // Presenter Management Methods
    @FXML
    private void handleAddPresenter() {
        System.out.println("Add Presenter clicked");
        
        // Create custom dialog for adding presenter
        Dialog<PresenterInfo> dialog = new Dialog<>();
        dialog.setTitle("Add New Presenter");
        dialog.setHeaderText("Enter presenter details");
        
        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        // Create the form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField titleField = new TextField();
        titleField.setPromptText("Job Title/Position");
        TextField companyField = new TextField();
        companyField.setPromptText("Company/Organization");
        TextArea bioArea = new TextArea();
        bioArea.setPromptText("Brief bio or description");
        bioArea.setPrefRowCount(3);
        bioArea.setWrapText(true);
        
        grid.add(new javafx.scene.control.Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new javafx.scene.control.Label("Company:"), 0, 2);
        grid.add(companyField, 1, 2);
        grid.add(new javafx.scene.control.Label("Bio:"), 0, 3);
        grid.add(bioArea, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        // Enable/Disable add button depending on whether name is entered
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
        
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });
        
        // Convert the result to a PresenterInfo when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new PresenterInfo(
                    nameField.getText(),
                    titleField.getText().isEmpty() ? "Speaker" : titleField.getText(),
                    companyField.getText().isEmpty() ? "" : companyField.getText(),
                    bioArea.getText().isEmpty() ? "Professional speaker and expert in their field" : bioArea.getText()
                );
            }
            return null;
        });
        
        Optional<PresenterInfo> result = dialog.showAndWait();
        result.ifPresent(presenter -> {
            addPresenterCard(presenter);
            System.out.println("✅ New presenter added: " + presenter.getName());
        });
    }
    
    private void addPresenterCard(PresenterInfo presenter) {
        // Add presenter to the data list first
        presentersList.add(presenter);
        
        // Calculate the next position in the grid
        int currentPresenters = presentersGrid.getChildren().size();
        int column = currentPresenters % 3; // 3 presenters per row
        int row = currentPresenters / 3;
        
        // Create the presenter card VBox
        VBox presenterCard = new VBox();
        presenterCard.setSpacing(10);
        presenterCard.getStyleClass().add("presenter-card");
        
        // Create header HBox with avatar, name, title and edit button
        HBox header = new HBox();
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        header.setSpacing(10);
        
        // Avatar (emoji)
        Text avatar = new Text("👤");
        avatar.getStyleClass().add("presenter-avatar");
        
        // Name and title VBox
        VBox nameTitle = new VBox();
        nameTitle.setSpacing(2);
        
        Text nameText = new Text(presenter.getName());
        nameText.getStyleClass().add("presenter-name");
        
        String fullTitle = presenter.getCompany().isEmpty() ? 
            presenter.getTitle() : 
            presenter.getTitle() + " at " + presenter.getCompany();
        Text titleText = new Text(fullTitle);
        titleText.getStyleClass().add("presenter-title");
        
        nameTitle.getChildren().addAll(nameText, titleText);
        
        // Spacer region
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        // Edit button
        Button editButton = new Button("✎");
        editButton.getStyleClass().add("edit-presenter-button");
        editButton.setOnAction(e -> handleEditPresenter());
        
        header.getChildren().addAll(avatar, nameTitle, spacer, editButton);
        
        // Bio text
        Text bioText = new Text(presenter.getBio());
        bioText.getStyleClass().add("presenter-bio");
        bioText.setWrappingWidth(200); // Limit width for wrapping
        
        presenterCard.getChildren().addAll(header, bioText);
        
        // Add to grid
        presentersGrid.add(presenterCard, column, row);
        GridPane.setMargin(presenterCard, new javafx.geometry.Insets(5));
    }
    
    private void displayPresenterCard(PresenterInfo presenter) {
        // Calculate the next position in the grid (without adding to presentersList)
        int currentPresenters = presentersGrid.getChildren().size();
        int column = currentPresenters % 3; // 3 presenters per row
        int row = currentPresenters / 3;
        
        // Create the presenter card VBox
        VBox presenterCard = new VBox();
        presenterCard.setSpacing(10);
        presenterCard.getStyleClass().add("presenter-card");
        
        // Create header HBox with avatar, name, title and edit button
        HBox header = new HBox();
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        header.setSpacing(10);
        
        // Avatar (emoji)
        Text avatar = new Text("👤");
        avatar.getStyleClass().add("presenter-avatar");
        
        // Name and title VBox
        VBox nameTitle = new VBox();
        nameTitle.setSpacing(2);
        
        Text nameText = new Text(presenter.getName());
        nameText.getStyleClass().add("presenter-name");
        
        String fullTitle = presenter.getCompany().isEmpty() ? 
            presenter.getTitle() : 
            presenter.getTitle() + " at " + presenter.getCompany();
        Text titleText = new Text(fullTitle);
        titleText.getStyleClass().add("presenter-title");
        
        nameTitle.getChildren().addAll(nameText, titleText);
        
        // Spacer region
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        // Edit button
        Button editButton = new Button("✎");
        editButton.getStyleClass().add("edit-presenter-button");
        editButton.setOnAction(e -> handleEditPresenter());
        
        header.getChildren().addAll(avatar, nameTitle, spacer, editButton);
        
        // Bio text
        Text bioText = new Text(presenter.getBio());
        bioText.getStyleClass().add("presenter-bio");
        bioText.setWrappingWidth(200); // Limit width for wrapping
        
        presenterCard.getChildren().addAll(header, bioText);
        
        // Add to grid
        presentersGrid.add(presenterCard, column, row);
        GridPane.setMargin(presenterCard, new javafx.geometry.Insets(5));
    }
    
    // Helper class for presenter information
    public static class PresenterInfo {
        private String name;
        private String title;
        private String company;
        private String bio;
        
        public PresenterInfo(String name, String title, String company, String bio) {
            this.name = name;
            this.title = title;
            this.company = company;
            this.bio = bio;
        }
        
        public String getName() { return name; }
        public String getTitle() { return title; }
        public String getCompany() { return company; }
        public String getBio() { return bio; }
    }
    
    @FXML
    private void handleEditPresenter() {
        System.out.println("Edit Presenter clicked");
        
        // TODO: Show edit presenter dialog
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Edit Presenter");
        alert.setHeaderText("Edit Presenter");
        alert.setContentText("Presenter editing will be available in the next version.");
        alert.showAndWait();
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
        
        if (descriptionArea.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Description is required for published events.");
            descriptionArea.requestFocus();
            return false;
        }
        
        // Validate time format
        if (!isValidTimeFormat(startTimeField.getText())) {
            showErrorAlert("Validation Error", "Invalid start time format. Use HH:MM AM/PM.");
            startTimeField.requestFocus();
            return false;
        }
        
        if (!isValidTimeFormat(endTimeField.getText())) {
            showErrorAlert("Validation Error", "Invalid end time format. Use HH:MM AM/PM.");
            endTimeField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean isValidTimeFormat(String time) {
        // Accept both 24-hour and 12-hour format
        return time.matches("^(1[0-2]|0?[1-9]):[0-5][0-9]\\s?(AM|PM|am|pm)$") ||
               time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
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