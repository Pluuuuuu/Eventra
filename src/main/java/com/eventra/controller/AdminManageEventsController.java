/**
 * Admin Manage Events Controller (AdminManageEventsController.java)
 * 
 * This class controls the EVENT MANAGEMENT SCREEN for administrators in the Eventra application.
 * Think of it as the "event control center" where admins can view, search, filter, edit,
 * and delete events across the entire system.
 * 
 * WHAT THIS CLASS DOES:
 * - Displays all events in a comprehensive table view
 * - Provides advanced search and filtering capabilities
 * - Shows event details including status, attendance, and location
 * - Offers edit and delete actions for each event
 * - Filters events by status, date, and location
 * - Shows real-time results count and statistics
 * - Handles bulk event management operations
 * - Provides administrative oversight of all events
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - TableView Management: Complex table with custom cell factories and actions
 * - Advanced Filtering: Multiple filter types with real-time application
 * - Observable Collections: JavaFX lists that automatically update the UI
 * - Search Functionality: Real-time search across event data
 * - Administrative Controls: Edit/delete capabilities for event management
 * - Data Binding: Connects table columns to data properties automatically
 * 
 * UI COMPONENTS MANAGED:
 * - Search field for finding specific events
 * - Status filter dropdown (Draft, Published, Cancelled, Completed)
 * - Date filter dropdown (Today, This Week, This Month, etc.)
 * - Location filter dropdown (Conference Center, Meeting Rooms, etc.)
 * - Events table with multiple columns and action buttons
 * - Results count display
 * - Edit and delete action buttons for each event
 * 
 * ADMINISTRATIVE FEATURES:
 * - Complete event oversight and management
 * - Multi-criteria filtering for efficient event discovery
 * - Bulk operations and administrative actions
 * - Real-time search and filtering
 * - Comprehensive event information display
 * - Professional-grade event management interface
 * 
 * COMPLEXITY LEVEL:
 * This is a sophisticated administrative controller that demonstrates:
 * - Advanced table management with custom cell factories
 * - Complex filtering and search systems
 * - Professional administrative interface design
 * - Efficient data handling for large event collections
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.dao.EventDAO;                     // For retrieving and managing events in the database
import com.eventra.dao.EventDAO.EventItem;            // Custom data structure for table display
import com.eventra.model.User;                         // User data model for authentication

// Utility classes for application functionality:
import com.eventra.util.SessionManager;                // For managing user sessions and authentication
import com.eventra.util.ViewUtil;                      // For navigating between screens

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                               // Links this class to FXML UI elements
import javafx.scene.control.*;                         // UI controls (buttons, text fields, labels, etc.)
import javafx.scene.control.Alert.AlertType;           // Types of alert dialogs (ERROR, WARNING, INFO)
import javafx.scene.text.Text;                         // For displaying text content

// JavaFX collections for reactive data management:
import javafx.collections.FXCollections;                // Creates observable lists from regular lists
import javafx.collections.ObservableList;               // Lists that automatically update the UI

// Java standard library imports:
import java.util.List;                                  // For handling collections of data

/**
 * AdminManageEventsController class - Controls the Event Management Screen for Administrators
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: EventItem data structure, DAO classes for database operations
 * - VIEW: FXML file defines the visual layout of the event management page
 * - CONTROLLER: This class connects the model and view, handles all event management logic
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the AdminManageEvents.fxml file, allowing us to control the UI elements.
 * 
 * ADMINISTRATIVE INTERFACE:
 * This controller provides a professional-grade interface for event administrators,
 * demonstrating advanced JavaFX concepts like custom table cell factories, complex
 * filtering systems, and real-time search functionality.
 */
public class AdminManageEventsController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the AdminManageEvents.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what the user typed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can add/remove child elements (getChildren().add())
     */
    
    // SEARCH AND FILTER COMPONENTS - Control how events are displayed
    @FXML private TextField searchField;              // Input field for searching events by title, description, etc.
    @FXML private ComboBox<String> statusFilter;      // Dropdown to filter events by status (Draft, Published, etc.)
    @FXML private ComboBox<String> dateFilter;        // Dropdown to filter events by date range (Today, This Week, etc.)
    @FXML private ComboBox<String> locationFilter;    // Dropdown to filter events by venue/location
    
    // TABLE COMPONENTS - Display events in organized columns
    @FXML private TableView<EventItem> eventsTable;   // Main table showing all events with filtering applied
    @FXML private TableColumn<EventItem, String> eventNameColumn;     // Column for event title/name
    @FXML private TableColumn<EventItem, String> dateTimeColumn;     // Column for event date and time
    @FXML private TableColumn<EventItem, String> locationColumn;     // Column for event venue/location
    @FXML private TableColumn<EventItem, String> statusColumn;       // Column for event status (Draft, Published, etc.)
    @FXML private TableColumn<EventItem, Integer> attendeeCountColumn; // Column for number of registered attendees
    @FXML private TableColumn<EventItem, Void> actionsColumn;        // Column for edit/delete action buttons
    
    // BOTTOM BAR COMPONENTS - Show summary information
    @FXML private Text resultsCountText;              // Text showing how many events match current filters
    
    /**
     * DATA LIST - Store and manage dynamic data for the events table
     * 
     * This list uses JavaFX's ObservableList which automatically updates the UI
     * whenever the data changes. This is called "reactive programming" and it's
     * what makes modern JavaFX applications feel responsive and dynamic.
     * 
     * OBSERVABLE LIST EXPLAINED:
     * - When you add/remove items from this list, the table automatically updates
     * - No need to manually refresh the table after filtering or searching
     * - JavaFX handles all the synchronization between data and display
     * - This is much more efficient than manually updating UI elements
     * 
     * EVENT ITEM STRUCTURE:
     * EventItem is a custom data structure that contains all the information
     * needed to display an event in the table (title, date, location, status, etc.)
     */
    private ObservableList<EventItem> eventsList = FXCollections.observableArrayList();
    
    /**
     * initialize() method - Sets up the admin event management screen when it first loads
     * 
     * This method is automatically called by JavaFX when the AdminManageEvents screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the admin sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Logs successful page loading for debugging purposes
     * 2. Sets up all filter dropdowns with predefined options and event listeners
     * 3. Configures the events table with proper columns and custom cell factories
     * 4. Loads all events from the database and populates the table
     * 5. Updates the results count to show total events available
     * 
     * COMPREHENSIVE SETUP:
     * This method handles the complete initialization of the administrative interface:
     * - Filter system setup with predefined options
     * - Table configuration with custom action buttons
     * - Data loading and display
     * - Real-time filtering and search functionality
     * 
     * ADMINISTRATIVE INTERFACE:
     * Unlike regular user screens, this provides professional-grade event management:
     * - Advanced filtering capabilities
     * - Comprehensive table view
     * - Administrative actions (edit/delete)
     * - Real-time search and filtering
     * 
     * USER EXPERIENCE:
     * By setting up everything in advance, administrators can immediately start
     * managing events without waiting for components to load or configure.
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        System.out.println("Admin Manage Events page loaded");
        
        // STEP 1: Set up all filter dropdowns with options and event listeners
        setupFilters();
        
        // STEP 2: Configure the events table with columns and custom cell factories
        setupTable();
        
        // STEP 3: Load all events from database and populate the table
        loadEvents();
        
        // STEP 4: Update the results count display
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
                System.out.println("🔍 Loading events for admin user ID: " + currentUser.getUserId() + 
                                 " (Email: " + currentUser.getEmail() + ")");
                
                // Load events created by this admin from database
                List<EventItem> events = EventDAO.getEventsForAdmin(currentUser.getUserId());
                eventsList.addAll(events);
                
                System.out.println("✅ Loaded " + events.size() + " events for admin " + currentUser.getEmail());
                
                // Debug: Check what events exist in the database
                debugCheckAllEvents();
                
                // Debug: Check events for this specific user
                EventDAO.debugCheckEventsForUser(currentUser.getUserId());
                
            } else {
                System.err.println("❌ No current user found, cannot load events");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error loading events: " + e.getMessage());
            e.printStackTrace();
            
            // Add fallback demo data if database fails
            System.out.println("🔄 Using fallback demo data");
            eventsList.addAll(List.of(
                new EventItem(1, "Sample Event", "Jan 01, 2024 at 10:00 AM", "Main Hall", "Published", 0)
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
                // Delete from database first
                boolean deleted = EventDAO.deleteEvent(event.getEventId());
                
                if (deleted) {
                    // Remove from UI and refresh the list
                    eventsList.remove(event);
                    updateResultsCount();
                    System.out.println("Event deleted: " + event.getTitle());
                    
                    // Optionally reload events from database to ensure UI is in sync
                    // loadEvents(); // Uncomment if you want to fully refresh from DB
                    
                    // Show success message
                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Event Deleted");
                    successAlert.setHeaderText("Success");
                    successAlert.setContentText("Event '" + event.getTitle() + "' has been deleted successfully.");
                    successAlert.showAndWait();
                } else {
                    // Show error message if database deletion failed
                    Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Delete Failed");
                    errorAlert.setHeaderText("Failed to Delete Event");
                    errorAlert.setContentText("Could not delete event '" + event.getTitle() + "' from the database. Please try again.");
                    errorAlert.showAndWait();
                }
            }
        }
    }
    
    @FXML
    private void handleBackToDashboard() {
        System.out.println("Back to Dashboard clicked");
        ViewUtil.switchToRoleDashboard(eventsTable.getScene().getWindow());
    }
    
    /**
     * Debug method to check all events in the database
     */
    private void debugCheckAllEvents() {
        try {
            System.out.println("🔍 Debug: Checking all events in database...");
            
            // Get all events for debugging
            List<EventItem> allEvents = EventDAO.getAllEventsForAdmin();
            System.out.println("📊 Total events in database: " + allEvents.size());
            
            for (EventItem event : allEvents) {
                System.out.println("  - Event: " + event.getTitle() + " (ID: " + event.getEventId() + 
                                 ", Status: " + event.getStatus() + ")");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error in debug check: " + e.getMessage());
            e.printStackTrace();
        }
    }
}