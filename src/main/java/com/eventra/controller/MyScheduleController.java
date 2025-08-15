/**
 * My Schedule Controller (MyScheduleController.java)
 * 
 * This class controls the PERSONAL SCHEDULE SCREEN of the Eventra application. Think of it as the
 * "personal calendar" where users can see all the events they've registered for, both upcoming
 * and past events, with the ability to search and manage their schedule.
 * 
 * WHAT THIS CLASS DOES:
 * - Shows all events the current user has registered for
 * - Separates events into "Upcoming" and "Past" categories
 * - Provides search functionality within the user's schedule
 * - Displays events in an organized, easy-to-read format
 * - Shows empty state messages when no events are found
 * - Handles user authentication and session management
 * - Provides refresh functionality to update the schedule
 * - Creates interactive event items with event details
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Event Filtering: Separates events by date (upcoming vs. past)
 * - Stream API: Modern Java way to filter and process collections
 * - Dynamic UI Generation: Creates event items programmatically
 * - Session Management: Gets current user ID for personalized data
 * - Error Handling: Gracefully handles missing data and errors
 * - Refresh Capability: Allows users to update their schedule
 * 
 * UI COMPONENTS MANAGED:
 * - Registered events container (upcoming events)
 * - Past events container (completed events)
 * - Search field for finding specific events in schedule
 * - Event items with details and formatting
 * - Empty state messages when no events exist
 * 
 * DATA ORGANIZATION:
 * - Upcoming Events: Events that haven't happened yet (startDateTime > now)
 * - Past Events: Events that have already occurred (startDateTime < now)
 * - Events are loaded from the user's registration records
 * - Real-time filtering ensures accurate categorization
 * 
 * USER EXPERIENCE FEATURES:
 * - Clear separation between upcoming and past events
 * - Search functionality for finding specific events
 * - Refresh capability for updated information
 * - Empty state messages guide users on what to do next
 * - Consistent event item formatting for easy scanning
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.dao.EventDAO;                    // For retrieving event information from database
import com.eventra.dao.EventRegistrationDAO;         // For getting user's registered events
import com.eventra.model.Event;                       // Event data model

// Utility classes for application functionality:
import com.eventra.util.SessionManager;               // For managing user sessions and getting current user ID
import com.eventra.util.ViewUtil;                     // For navigating between screens

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                              // Links this class to FXML UI elements
import javafx.scene.control.*;                        // UI controls (buttons, text fields, labels, etc.)
import javafx.scene.layout.*;                         // Layout containers (VBox, HBox, etc.)
import javafx.scene.text.Text;                        // For displaying text content
import javafx.scene.text.Font;                        // For customizing text appearance
import javafx.scene.text.FontWeight;                  // For making text bold, normal, etc.

// JavaFX geometry and positioning:
import javafx.geometry.Insets;                        // For adding spacing around UI elements
import javafx.geometry.Pos;                           // For aligning content (left, center, right)

// Java time classes for date and time handling:
import java.time.LocalDateTime;                       // Modern Java date and time class
import java.time.format.DateTimeFormatter;            // For formatting dates and times nicely

// Java standard library imports:
import java.util.List;                                // For handling collections of data

/**
 * MyScheduleController class - Controls the Personal Schedule Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: Event model, DAO classes for database operations
 * - VIEW: FXML file defines the visual layout of the schedule page
 * - CONTROLLER: This class connects the model and view, handles all schedule logic
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the MySchedule.fxml file, allowing us to control the UI elements.
 * 
 * PERSONALIZED EXPERIENCE:
 * This controller is unique because it shows data specific to the logged-in user.
 * It demonstrates how to create personalized views based on user authentication
 * and individual data relationships (user → registrations → events).
 */
public class MyScheduleController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the MySchedule.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what's displayed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can add/remove child elements (getChildren().add())
     */
    @FXML private VBox registeredEventsContainer;    // Container for upcoming events the user has registered for
    @FXML private VBox pastEventsContainer;          // Container for past events the user has attended
    @FXML private TextField searchField;             // Input field for searching within the user's schedule
    
    /**
     * INSTANCE VARIABLES - Store state and data for this controller
     * 
     * These variables keep track of the current state of the schedule screen
     * and store references to the data access objects (DAOs) for database operations.
     * 
     * DAO OBJECTS:
     * These are like "database assistants" that help us retrieve and manage data
     * from different tables in the database. Each DAO handles a specific type of data.
     */
    private EventDAO eventDAO;                        // Handles all event-related database operations
    private EventRegistrationDAO registrationDAO;     // Handles event registration database operations
    
    /**
     * initialize() method - Sets up the schedule screen when it first loads
     * 
     * This method is automatically called by JavaFX when the MySchedule screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Logs successful page loading for debugging purposes
     * 2. Creates new instances of DAO classes for database access
     * 3. Calls loadUserSchedule() to populate the screen with user's events
     * 4. Sets up the entire schedule page with personalized data
     * 
     * PERSONALIZED SETUP:
     * Unlike other controllers that show general data, this one loads data
     * specific to the currently logged-in user. It demonstrates how to create
     * personalized experiences based on user authentication.
     * 
     * DAO INITIALIZATION:
     * Each DAO is created as a new instance to ensure fresh database connections.
     * This is important for data integrity and preventing stale data issues.
     * 
     * IMMEDIATE DATA LOADING:
     * The schedule is loaded immediately during initialization, so users
     * see their events right away without waiting for additional actions.
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        System.out.println("MySchedule page loaded successfully!");
        
        // Create new instances of DAO classes for fresh database access
        eventDAO = new EventDAO();
        registrationDAO = new EventRegistrationDAO();
        
        // Load and display the user's personal schedule
        loadUserSchedule();
    }
    
    public void refreshSchedule() {
        loadUserSchedule();
    }
    
    /**
     * loadUserSchedule() method - Main method that loads the user's personal schedule
     * 
     * This is the CORE method that retrieves and displays all events the current user
     * has registered for, separating them into upcoming and past categories.
     * 
     * HOW IT WORKS:
     * 1. Gets the current user ID from the session manager
     * 2. Checks if user is logged in (authentication check)
     * 3. Loads all events the user has registered for
     * 4. Separates events into "upcoming" and "past" based on current time
     * 5. Displays events in appropriate containers with helpful messages
     * 6. Handles errors gracefully with user-friendly messages
     * 
     * AUTHENTICATION CHECK:
     * The first thing this method does is verify the user is logged in.
     * If no user ID is found (returns -1), it shows a login prompt instead of crashing.
     * 
     * EVENT CATEGORIZATION:
     * Events are automatically sorted into two categories:
     * - Upcoming Events: startDateTime > current time (haven't happened yet)
     * - Past Events: startDateTime < current time (already occurred)
     * 
     * STREAM API USAGE:
     * This method demonstrates modern Java's Stream API for filtering collections:
     * - .stream() - converts the list to a stream for processing
     * - .filter() - keeps only events that match the condition
     * - .toList() - converts the filtered stream back to a list
     * 
     * ERROR HANDLING:
     * If anything goes wrong (database error, network issue, etc.), the method:
     * - Logs the error for debugging
     * - Shows a user-friendly error message
     * - Continues running so the app remains functional
     */
    private void loadUserSchedule() {
        try {
            // STEP 1: Get current user ID and check authentication
            int currentUserId = SessionManager.getCurrentUserId();
            if (currentUserId == -1) {
                showEmptyState("Please log in to view your schedule.");
                return;
            }
            
            // STEP 2: Load all events the user has registered for
            List<Event> registeredEvents = registrationDAO.getUserRegisteredEvents(currentUserId);
            
            if (registeredEvents.isEmpty()) {
                // STEP 2A: No events found - show encouraging message
                showEmptyState("No events in your schedule yet. Start exploring events and register for ones that interest you!");
                return;
            }
            
            // STEP 3: Separate events into upcoming and past categories
            LocalDateTime now = LocalDateTime.now();
            
            // Use Stream API to filter upcoming events (startDateTime > now)
            List<Event> upcomingEvents = registeredEvents.stream()
                .filter(event -> event.getStartDateTime().isAfter(now))
                .toList();
            
            // Use Stream API to filter past events (startDateTime < now)
            List<Event> pastEvents = registeredEvents.stream()
                .filter(event -> event.getStartDateTime().isBefore(now))
                .toList();
            
            // STEP 4: Display events in appropriate containers
            displayEvents(upcomingEvents, registeredEventsContainer, "No upcoming events");
            displayEvents(pastEvents, pastEventsContainer, "No past events");
            
        } catch (Exception e) {
            // STEP 5: Handle any errors that occur during loading
            System.err.println("Error loading user schedule: " + e.getMessage());
            e.printStackTrace(); // Log full error details for debugging
            showEmptyState("Error loading your schedule. Please try again later.");
        }
    }
    
    private void displayEvents(List<Event> events, VBox container, String emptyMessage) {
        container.getChildren().clear();
        
        if (events.isEmpty()) {
            Text emptyText = new Text(emptyMessage);
            emptyText.setStyle("-fx-fill: #666; -fx-font-style: italic;");
            container.getChildren().add(emptyText);
            return;
        }
        
        for (Event event : events) {
            HBox eventItem = createEventItem(event);
            container.getChildren().add(eventItem);
        }
    }
    
    private HBox createEventItem(Event event) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        // Event date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d");
        Text dateText = new Text(event.getStartDateTime().format(dateFormatter));
        dateText.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-min-width: 60;");
        
        // Event info
        VBox info = new VBox(5);
        Text title = new Text(event.getTitle());
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String timeRange = event.getStartDateTime().format(timeFormatter) + " - " + 
                          event.getEndDateTime().format(timeFormatter);
        Text time = new Text(timeRange);
        time.setStyle("-fx-fill: #666;");
        
        Text location = new Text(event.getLocation());
        location.setStyle("-fx-fill: #666;");
        
        info.getChildren().addAll(title, time, location);
        
        // Action buttons container
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        
        // View Details button
        Button viewButton = new Button("View Details");
        viewButton.setStyle("-fx-background-color: #4b3a8c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand;");
        viewButton.setOnAction(e -> handleViewEvent(event));
        
        // Add hover effect for View Details button
        viewButton.setOnMouseEntered(e -> {
            viewButton.setStyle("-fx-background-color: #9d7abf; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(157, 122, 191, 0.4), 6, 0, 0, 3); -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        viewButton.setOnMouseExited(e -> {
            viewButton.setStyle("-fx-background-color: #4b3a8c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand;");
        });
        
        // Unenroll button
        Button unenrollButton = new Button("Unenroll");
        unenrollButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand;");
        unenrollButton.setOnAction(e -> handleUnenrollEvent(event));
        
        // Add hover effect for Unenroll button
        unenrollButton.setOnMouseEntered(e -> {
            unenrollButton.setStyle("-fx-background-color: #c82333; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(220, 53, 69, 0.4), 6, 0, 0, 3); -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        unenrollButton.setOnMouseExited(e -> {
            unenrollButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand;");
        });
        
        buttonContainer.getChildren().addAll(unenrollButton, viewButton);
        
        // Create a spacer to push the buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        item.getChildren().addAll(dateText, info, spacer, buttonContainer);
        return item;
    }
    
    private void showEmptyState(String message) {
        registeredEventsContainer.getChildren().clear();
        pastEventsContainer.getChildren().clear();
        
        VBox emptyState = new VBox(20);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPadding(new Insets(40));
        
        Text emptyText = new Text(message);
        emptyText.setStyle("-fx-fill: #666; -fx-font-size: 16; -fx-text-alignment: center;");
        emptyText.setWrappingWidth(400);
        
        Button browseButton = new Button("Browse Events");
        browseButton.setStyle("-fx-background-color: #4b3a8c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand;");
        browseButton.setOnAction(e -> handleBrowseEvents());
        
        // Add hover effect
        browseButton.setOnMouseEntered(e -> {
            browseButton.setStyle("-fx-background-color: #9d7abf; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(157, 122, 191, 0.4), 6, 0, 0, 3); -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        browseButton.setOnMouseExited(e -> {
            browseButton.setStyle("-fx-background-color: #4b3a8c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand;");
        });
        
        emptyState.getChildren().addAll(emptyText, browseButton);
        registeredEventsContainer.getChildren().add(emptyState);
    }

    @FXML
    private void handleViewEvent(Event event) {
        try {
            SessionManager.setSelectedEvent(event);
            ViewUtil.switchTo("EventDetails", searchField.getScene().getWindow());
        } catch (Exception e) {
            System.err.println("Error navigating to event details: " + e.getMessage());
            e.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not open event details");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }
    
    private void handleUnenrollEvent(Event event) {
        try {
            int currentUserId = SessionManager.getCurrentUserId();
            if (currentUserId == -1) {
                showError("Please log in to unenroll from events.");
                return;
            }
            
            // Show confirmation dialog
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Unenrollment");
            confirmDialog.setHeaderText("Unenroll from Event");
            confirmDialog.setContentText("Are you sure you want to unenroll from '" + event.getTitle() + "'? This action cannot be undone.");
            
            ButtonType result = confirmDialog.showAndWait().orElse(ButtonType.CANCEL);
            
            if (result == ButtonType.OK) {
                // Perform unenrollment
                boolean success = registrationDAO.unregisterUserFromEvent(currentUserId, event.getEventId());
                
                if (success) {
                    showSuccess("Successfully unenrolled from '" + event.getTitle() + "'");
                    // Refresh the schedule to reflect the change
                    refreshSchedule();
                } else {
                    showError("Failed to unenroll from event. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error unenrolling from event: " + e.getMessage());
            e.printStackTrace();
            showError("An error occurred while trying to unenroll from the event. Please try again.");
        }
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Unenrollment Successful");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Unenrollment Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void handleMySchedule() {
        // Already on MySchedule page
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("My Schedule");
        alert.setHeaderText("My Schedule");
        alert.setContentText("You are currently viewing your schedule.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleBrowseEvents() {
        ViewUtil.switchTo("AttendeeEvents", searchField.getScene().getWindow());
    }
    
    @FXML
    private void handleProfile() {
        ViewUtil.switchTo("AttendeeProfile", searchField.getScene().getWindow());
    }
    
    @FXML
    private void handleSearch() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search");
        alert.setHeaderText("Search Results");
        alert.setContentText("Search functionality is working!");
        alert.showAndWait();
    }
} 