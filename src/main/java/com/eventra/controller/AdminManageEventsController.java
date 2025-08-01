package com.eventra.controller;

import com.eventra.dao.EventDAO;
import com.eventra.dao.EventDAO.EventItem;
import com.eventra.model.User;
import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class AdminManageEventsController {
    
    // Search and Filter Components
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> dateFilter;
    @FXML private ComboBox<String> locationFilter;
    
    // Table Components
    @FXML private TableView<EventItem> eventsTable;
    @FXML private TableColumn<EventItem, String> eventNameColumn;
    @FXML private TableColumn<EventItem, String> dateTimeColumn;
    @FXML private TableColumn<EventItem, String> locationColumn;
    @FXML private TableColumn<EventItem, String> statusColumn;
    @FXML private TableColumn<EventItem, Void> actionsColumn;
    
    // Bottom Bar Components
    @FXML private Text resultsCountText;
    
    private ObservableList<EventItem> eventsList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        System.out.println("Admin Manage Events page loaded");
        
        setupFilters();
        setupTable();
        loadEvents();
        updateResultsCount();
    }
    
    private void setupFilters() {
        // Status Filter
        statusFilter.getItems().addAll(
            "All Status",
            "Draft",
            "Published",
            "Cancelled",
            "Completed"
        );
        statusFilter.setValue("All Status");
        
        // Date Filter
        dateFilter.getItems().addAll(
            "All Dates",
            "Today",
            "This Week",
            "This Month",
            "Upcoming",
            "Past Events"
        );
        dateFilter.setValue("All Dates");
        
        // Location Filter
        locationFilter.getItems().addAll(
            "All Locations",
            "Main Conference Center",
            "Meeting Room A",
            "Meeting Room B",
            "Online Event",
            "External Venue"
        );
        locationFilter.setValue("All Locations");
        
        // Add listeners for filter changes
        statusFilter.setOnAction(e -> applyFilters());
        dateFilter.setOnAction(e -> applyFilters());
        locationFilter.setOnAction(e -> applyFilters());  
        searchField.textProperty().addListener((obs, oldText, newText) -> applyFilters());
    }
    
    private void setupTable() {
        // Setup table columns
        eventNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        dateTimeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateTime()));
        locationColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));
        statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        
        // Setup actions column with edit/delete buttons
        actionsColumn.setCellFactory(col -> new TableCell<EventItem, Void>() {
            private final Button editBtn = new Button("✎");
            private final Button deleteBtn = new Button("🗑");
            private final javafx.scene.layout.HBox actionBox = new javafx.scene.layout.HBox(5, editBtn, deleteBtn);
            
            {
                editBtn.getStyleClass().add("table-action-button");
                deleteBtn.getStyleClass().add("table-action-button");
                editBtn.setOnAction(e -> handleEditEvent(getTableRow().getItem()));
                deleteBtn.setOnAction(e -> handleDeleteEvent(getTableRow().getItem()));
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        });
        
        eventsTable.setItems(eventsList);
    }
    
    private void loadEvents() {
        try {
            eventsList.clear();
            
            // Get current admin user
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser != null) {
                // Load events created by this admin from database
                List<EventItem> events = EventDAO.getEventsForAdmin(currentUser.getUserId());
                eventsList.addAll(events);
                
                System.out.println("✅ Loaded " + events.size() + " events for admin " + currentUser.getEmail());
            } else {
                System.err.println("❌ No current user found, cannot load events");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error loading events: " + e.getMessage());
            e.printStackTrace();
            
            // Add fallback demo data if database fails
            System.out.println("🔄 Using fallback demo data");
            eventsList.addAll(List.of(
                new EventItem(1, "Sample Event", "Jan 01, 2024 at 10:00 AM", "Main Hall", "Published")
            ));
        }
    }
    
    private void applyFilters() {
        // TODO: Implement filtering logic based on search text and filter selections
        updateResultsCount();
    }
    
    private void updateResultsCount() {
        int count = eventsList.size();
        resultsCountText.setText("Showing " + count + " event" + (count != 1 ? "s" : ""));
    }
    
    @FXML
    private void handleCreateEvent() {
        System.out.println("Create New Event clicked");
        ViewUtil.switchTo("CreateEvent", eventsTable.getScene().getWindow());
    }
    
    @FXML
    private void handleResetFilters() {
        System.out.println("Reset Filters clicked");
        statusFilter.setValue("All Status");
        dateFilter.setValue("All Dates");
        locationFilter.setValue("All Locations");
        searchField.clear();
        applyFilters();
    }
    
    @FXML
    private void handleExportEvents() {
        System.out.println("Export Events clicked");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Export Events");
        alert.setHeaderText("Export Functionality");
        alert.setContentText("Events export feature will be available in the next version.");
        alert.showAndWait();
    }
    
    private void handleEditEvent(EventItem event) {
        if (event != null) {
            System.out.println("Edit event: " + event.getTitle());
            // TODO: Navigate to edit event page
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Edit Event");
            alert.setHeaderText("Edit: " + event.getTitle());
            alert.setContentText("Event editing functionality will be available in the next version.");
            alert.showAndWait();
        }
    }
    
    private void handleDeleteEvent(EventItem event) {
        if (event != null) {
            System.out.println("Delete event: " + event.getTitle());
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Event");
            alert.setHeaderText("Delete: " + event.getTitle());
            alert.setContentText("Are you sure you want to delete this event? This action cannot be undone.");
            
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                eventsList.remove(event);
                updateResultsCount();
                System.out.println("Event deleted: " + event.getTitle());
            }
        }
    }
    
    @FXML
    private void handleBackToDashboard() {
        System.out.println("Back to Dashboard clicked");
        ViewUtil.switchToRoleDashboard(eventsTable.getScene().getWindow());
    }
}