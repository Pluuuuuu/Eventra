/**
 * Create Event Controller (CreateEventController.java)
 * 
 * This class controls the EVENT CREATION SCREEN of the Eventra application. Think of it as the
 * "event planning desk" where administrators and organizers can create comprehensive events
 * with multiple sessions, presenters, and detailed scheduling information.
 * 
 * WHAT THIS CLASS DOES:
 * - Provides a comprehensive form for creating new events
 * - Manages event details (name, description, dates, times, location, capacity)
 * - Handles multiple event sessions with individual scheduling
 * - Manages event presenters and speakers
 * - Provides real-time validation and error checking
 * - Offers a table view for managing sessions with edit/delete capabilities
 * - Handles complex event scheduling and capacity management
 * - Saves events to the database with all related information
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - TableView Management: Complex table with custom cell factories and actions
 * - Observable Collections: JavaFX lists that automatically update the UI
 * - Form Validation: Real-time input checking and error prevention
 * - Dynamic UI Generation: Creates and manages UI elements programmatically
 * - Data Binding: Connects form fields to data models automatically
 * - Session Management: Handles multiple related data entities (Event + Sessions)
 * 
 * UI COMPONENTS MANAGED:
 * - Event details form (name, description, dates, times, location, capacity)
 * - Sessions table with edit/delete actions
 * - Presenters grid for managing speakers
 * - Validation feedback and error messages
 * - Save/cancel buttons with confirmation dialogs
 * 
 * COMPLEXITY LEVEL:
 * This is one of the most complex controllers in the application because it handles:
 * - Multiple form fields with validation
 * - Dynamic table management
 * - Complex data relationships (Event → Sessions → Presenters)
 * - Real-time UI updates
 * - Database transactions with multiple tables
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.dao.EventDAO;                   // For saving events to the database
import com.eventra.dao.EventSaveDAO;                // For specialized event saving operations
import com.eventra.model.Session;                   // Session data model for event sessions

// Utility classes for application functionality:
import com.eventra.util.ViewUtil;                   // For navigating between screens

// JavaFX collections for reactive data management:
import javafx.collections.FXCollections;            // Creates observable lists from regular lists
import javafx.collections.ObservableList;           // Lists that automatically update the UI

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                            // Links this class to FXML UI elements
import javafx.scene.Node;                           // Base class for all UI elements
import javafx.scene.control.*;                      // UI controls (buttons, text fields, labels, etc.)
import javafx.scene.control.Alert.AlertType;        // Types of alert dialogs (ERROR, WARNING, INFO)
import javafx.scene.control.cell.PropertyValueFactory; // Connects table columns to data properties

// JavaFX layout containers:
import javafx.scene.layout.GridPane;                // Grid layout for organizing presenters
import javafx.scene.layout.HBox;                    // Horizontal layout container
import javafx.scene.layout.Region;                  // Base class for layout containers
import javafx.scene.layout.VBox;                    // Vertical layout container
import javafx.scene.text.Text;                      // For displaying text content

// Java standard library imports:
import java.time.LocalDate;                         // Modern Java date class
import java.util.List;                              // For handling collections of data
import java.util.Optional;                          // Modern Java way to handle "maybe exists" values

/**
 * CreateEventController class - Controls the Event Creation Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: Session model, DAO classes for database operations
 * - VIEW: FXML file defines the visual layout of the event creation form
 * - CONTROLLER: This class connects the model and view, handles all event creation logic
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the CreateEvent.fxml file, allowing us to control the UI elements.
 * 
 * ADVANCED FEATURES:
 * This controller demonstrates several advanced JavaFX concepts:
 * - Custom table cell factories with action buttons
 * - Observable collections for reactive UI updates
 * - Dynamic form validation and error handling
 * - Complex data relationships and management
 * - Professional-grade form design and user experience
 */
public class CreateEventController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the CreateEvent.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what the user typed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can add/remove child elements (getChildren().add())
     */
    
    // EVENT DETAILS FIELDS - Basic event information
    @FXML private TextField eventNameField;         // Where user types the event name/title
    @FXML private TextArea descriptionArea;         // Large text area for event description
    @FXML private DatePicker startDatePicker;       // Calendar picker for event start date
    @FXML private TextField startTimeField;         // Text field for event start time (e.g., "2:30 PM")
    @FXML private DatePicker endDatePicker;         // Calendar picker for event end date
    @FXML private TextField endTimeField;           // Text field for event end time
    @FXML private TextField locationField;          // Where user types the event venue/location
    @FXML private TextField capacityField;          // Maximum number of attendees allowed
    @FXML private TextField tagsField;              // Keywords/tags to help people find the event
    
    // SESSIONS COMPONENTS - Table for managing event sessions
    @FXML private TableView<Session> sessionsTable; // Main table showing all event sessions
    @FXML private TableColumn<Session, String> sessionColumn;   // Session name column
    @FXML private TableColumn<Session, String> dateColumn;      // Session date column
    @FXML private TableColumn<Session, String> dayColumn;       // Day of week column
    @FXML private TableColumn<Session, String> timeColumn;      // Session time column
    @FXML private TableColumn<Session, String> locationColumn;  // Session location column
    @FXML private TableColumn<Session, String> presenterColumn; // Presenter/speaker column
    @FXML private TableColumn<Session, String> statusColumn;    // Session status column
    @FXML private TableColumn<Session, Void> actionsColumn;     // Edit/delete buttons column
    
    // PRESENTERS COMPONENTS - Grid for managing event speakers
    @FXML private GridPane presentersGrid;          // Grid layout for organizing presenter information
    
    /**
     * DATA LISTS - Store and manage dynamic data for the form
     * 
     * These lists use JavaFX's ObservableList which automatically updates the UI
     * whenever the data changes. This is called "reactive programming" and it's
     * what makes modern JavaFX applications feel responsive and dynamic.
     * 
     * OBSERVABLE LISTS EXPLAINED:
     * - When you add/remove items from these lists, the UI automatically updates
     * - No need to manually refresh tables or grids
     * - JavaFX handles all the synchronization between data and display
     * - This is much more efficient than manually updating UI elements
     */
    private ObservableList<Session> sessionsList = FXCollections.observableArrayList();      // List of event sessions
    private ObservableList<PresenterInfo> presentersList = FXCollections.observableArrayList(); // List of event presenters
    
    /**
     * initialize() method - Sets up the event creation form when it first loads
     * 
     * This method is automatically called by JavaFX when the create event screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Logs successful form initialization for debugging purposes
     * 2. Sets up the sessions table with proper columns and functionality
     * 3. Clears all form fields to ensure a clean starting state
     * 4. Adds input validation to prevent errors and improve user experience
     * 5. Loads existing presenters from the database for reuse
     * 
     * COMPREHENSIVE SETUP:
     * This method handles the most complex initialization in the application:
     * - Table setup with custom cell factories and action buttons
     * - Form validation with real-time error checking
     * - Database integration for presenter management
     * - Clean state management for new event creation
     * 
     * USER EXPERIENCE:
     * By setting up everything in advance, users can immediately start
     * creating events without waiting for components to load or configure.
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        System.out.println("Create Event form initialized");
        
        // STEP 1: Setup the sessions table with columns and action buttons
        setupSessionsTable();
        
        // STEP 2: Clear all form fields to ensure clean starting state
        setDefaultValues();
        
        // STEP 3: Add real-time input validation for better user experience
        setupValidation();
        
        // STEP 4: Load existing presenters from database for reuse
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