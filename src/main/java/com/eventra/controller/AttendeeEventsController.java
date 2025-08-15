/**
 * Attendee Events Controller (AttendeeEventsController.java)
 * 
 * This class controls the MAIN EVENTS BROWSING SCREEN for attendees in the Eventra application.
 * Think of it as the "events catalog" where users can browse, search, and discover events
 * they might want to attend.
 * 
 * WHAT THIS CLASS DOES:
 * - Displays "Coming Soon" events prominently at the top
 * - Shows all available events in a browseable grid layout
 * - Provides search functionality to find specific events
 * - Offers date filtering (today, tomorrow, weekend, custom date)
 * - Shows featured organizers and their events
 * - Creates interactive event cards with click-to-view functionality
 * - Updates event counts and availability in real-time
 * - Handles navigation to event details when events are clicked
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Dynamic UI Generation: Creates event cards programmatically based on data
 * - Search and Filtering: Real-time search and date-based filtering
 * - Event Cards: Custom UI components created for each event
 * - Responsive Layout: Adapts to different screen sizes and content
 * - Data Binding: Connects search results to display automatically
 * - Navigation: Routes users to detailed event views
 * 
 * UI COMPONENTS MANAGED:
 * - Coming Soon events container (prominent display)
 * - All events grid with searchable content
 * - Featured organizers showcase
 * - Search field with real-time filtering
 * - Date filter radio buttons (today, tomorrow, weekend, custom)
 * - Event count display
 * - Date picker for custom date selection
 * 
 * USER EXPERIENCE FEATURES:
 * - "Coming Soon" section highlights upcoming events
 * - Search works across event titles, descriptions, and organizers
 * - Date filtering helps users find events on specific days
 * - Event cards show key information at a glance
 * - Responsive design works on different screen sizes
 * - Clear navigation to event details
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.dao.EventDAO;                   // For retrieving events from the database
import com.eventra.dao.UserDAO;                     // For getting organizer information
import com.eventra.model.Event;                      // Event data model
import com.eventra.model.User;                       // User data model (for organizers)

// Utility classes for application functionality:
import com.eventra.util.SessionManager;              // For managing user sessions
import com.eventra.util.ViewUtil;                    // For navigating between screens
import com.eventra.util.RouteManager;                // For advanced routing functionality

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                             // Links this class to FXML UI elements
import javafx.scene.control.*;                       // UI controls (buttons, text fields, labels, etc.)
import javafx.scene.image.Image;                     // For loading image files
import javafx.scene.image.ImageView;                 // For displaying images
import javafx.scene.layout.*;                        // Layout containers (VBox, HBox, FlowPane, etc.)
import javafx.scene.text.Text;                       // For displaying text content
import javafx.scene.text.Font;                       // For customizing text appearance
import javafx.scene.text.FontWeight;                 // For making text bold, normal, etc.

// JavaFX geometry and styling:
import javafx.geometry.Insets;                       // For adding spacing around UI elements
import javafx.geometry.Pos;                          // For aligning content (left, center, right)
import javafx.scene.paint.Color;                     // For custom colors and styling

// Java time classes for date and time handling:
import java.time.LocalDateTime;                      // Modern Java date and time class
import java.time.LocalDate;                          // Modern Java date class
import java.time.format.DateTimeFormatter;           // For formatting dates and times nicely

// Java standard library imports:
import java.util.List;                               // For handling collections of data
import java.util.ArrayList;                          // For creating and managing lists
import java.util.Optional;                           // Modern Java way to handle "maybe exists" values

/**
 * AttendeeEventsController class - Controls the Main Events Browsing Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: Event and User models, DAO classes for database operations
 * - VIEW: FXML file defines the visual layout of the events browsing page
 * - CONTROLLER: This class connects the model and view, handles all events browsing logic
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the AttendeeEvents.fxml file, allowing us to control the UI elements.
 * 
 * COMPLEX UI MANAGEMENT:
 * This controller demonstrates advanced JavaFX concepts:
 * - Dynamic creation of event cards based on database data
 * - Real-time search and filtering functionality
 * - Responsive layout management
 * - Custom date filtering with radio buttons
 * - Interactive event browsing experience
 */
public class AttendeeEventsController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the AttendeeEvents.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what's displayed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can add/remove child elements (getChildren().add())
     */
    
    // MAIN CONTAINERS - Hold different sections of the events page
    @FXML private HBox comingSoonContainer;         // Horizontal container for "Coming Soon" events (prominent display)
    @FXML private FlowPane allEventsContainer;      // Flow layout for all events (automatically wraps to new rows)
    @FXML private VBox organizersContainer;         // Vertical container for featured organizers showcase
    @FXML private Text eventCountText;              // Shows total number of available events
    @FXML private TextField searchField;            // Input field for searching events by title, description, or organizer
    
    // DATE FILTER RADIO BUTTONS - Allow users to filter events by date
    @FXML private RadioButton todayRadio;           // Filter to show only today's events
    @FXML private RadioButton tomorrowRadio;        // Filter to show only tomorrow's events
    @FXML private RadioButton weekendRadio;         // Filter to show only weekend events
    @FXML private RadioButton pickDateRadio;        // Allow user to pick a custom date
    
    // CUSTOM DATE SELECTION - Appears when user selects "Pick Date" option
    @FXML private VBox selectedDateContainer;       // Container that holds the custom date picker
    @FXML private Text selectedDateText;            // Text showing the currently selected custom date
    
    /**
     * INSTANCE VARIABLES - Store state and data for this controller
     * 
     * These variables keep track of the current state of the events browsing screen
     * and store references to the data access objects (DAOs) and current data.
     * 
     * DAO OBJECTS:
     * These are like "database assistants" that help us retrieve and manage data
     * from different tables in the database. Each DAO handles a specific type of data.
     * 
     * STATE VARIABLES:
     * These track what the user is currently doing (searching, filtering, etc.)
     * and help maintain the correct display state.
     */
    private EventDAO eventDAO;                       // Handles all event-related database operations
    private UserDAO userDAO;                         // Handles user/organizer database operations
    private List<Event> allEvents;                   // Stores all available events from the database
    private List<User> organizers;                   // Stores featured organizers for the showcase
    private LocalDateTime selectedDate = null;       // Tracks the currently selected date for filtering
    private boolean isSearching = false;             // Tracks whether user is currently searching (for UI state management)
    
    /**
     * initialize() method - Sets up the events browsing screen when it first loads
     * 
     * This method is automatically called by JavaFX when the AttendeeEvents screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Logs successful page loading for debugging purposes
     * 2. Creates new instances of DAO classes for database access
     * 3. Runs debug check to verify database connectivity and data
     * 4. Loads all events and displays them in the appropriate containers
     * 5. Loads featured organizers for the showcase section
     * 6. Sets up search functionality with real-time filtering
     * 
     * COMPREHENSIVE SETUP:
     * This method handles the complete initialization of the events browsing experience:
     * - Database connectivity and data loading
     * - UI population with event data
     * - Search and filtering system setup
     * - Featured content loading
     * 
     * DEBUG FUNCTIONALITY:
     * The debugPrintAllUsers() call helps developers verify that the database
     * connection is working and contains the expected data.
     * 
     * USER EXPERIENCE:
     * By loading everything upfront, users can immediately start browsing
     * events without waiting for components to load or configure.
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        System.out.println("AttendeeEvents page loaded successfully!");
        
        // STEP 1: Create new instances of DAO classes for fresh database access
        eventDAO = new EventDAO();
        userDAO = new UserDAO();
        
        // STEP 2: Debug check to verify database connectivity and data
        userDAO.debugPrintAllUsers();
        
        // STEP 3: Load and display all events in the appropriate containers
        loadEvents();
        
        // STEP 4: Load featured organizers for the showcase section
        loadFeaturedOrganizers();
        
        // STEP 5: Set up search functionality with real-time filtering
        setupSearchFunctionality();
    }
    
    /**
     * loadEvents() method - Main method that loads all events from the database
     * 
     * This is the CORE method that retrieves and displays all events for attendees to browse.
     * It loads both "Coming Soon" events (prominently displayed) and all available events.
     * 
     * HOW IT WORKS:
     * 1. Loads "Coming Soon" events (events happening soon) and displays them prominently
     * 2. Loads all available events and displays them in the main grid
     * 3. Updates the event count display to show total available events
     * 4. Handles errors gracefully by showing empty state instead of crashing
     * 
     * TWO-TIER DISPLAY SYSTEM:
     * - Coming Soon Events: Displayed prominently at the top for immediate attention
     * - All Events: Complete catalog available in the main browsing area
     * This helps users discover both urgent and general events effectively.
     * 
     * ERROR HANDLING:
     * If anything goes wrong (database error, network issue, etc.), the method:
     * - Logs the error for debugging
     * - Shows empty containers instead of crashing
     * - Updates event count to 0 to indicate no events available
     * - Continues running so the app remains functional
     * 
     * PERFORMANCE CONSIDERATION:
     * Events are loaded once during initialization and stored in memory.
     * This provides fast browsing experience, though it means events won't
     * automatically update if changed elsewhere (would need refresh).
     */
    private void loadEvents() {
        try {
            // STEP 1: Load and display "Coming Soon" events prominently
            List<Event> comingSoonEvents = eventDAO.getComingSoonEvents();
            displayComingSoonEvents(comingSoonEvents);
            
            // STEP 2: Load and display all available events in the main grid
            allEvents = eventDAO.getAllEvents();
            displayAllEvents(allEvents);
            
            // STEP 3: Update the event count display
            updateEventCount(allEvents.size());
            
        } catch (Exception e) {
            // STEP 4: Handle any errors that occur during loading
            System.err.println("Error loading events: " + e.getMessage());
            e.printStackTrace(); // Log full error details for debugging
            
            // STEP 5: Show empty state instead of crashing
            allEvents = new ArrayList<>();
            displayComingSoonEvents(new ArrayList<>());
            displayAllEvents(new ArrayList<>());
            updateEventCount(0);
        }
    }
    
    private void displayComingSoonEvents(List<Event> events) {
        comingSoonContainer.getChildren().clear();
        
        for (Event event : events) {
            VBox eventCard = createEventCard(event, true);
            comingSoonContainer.getChildren().add(eventCard);
        }
    }
    
    private void displayAllEvents(List<Event> events) {
        allEventsContainer.getChildren().clear();
        
        for (Event event : events) {
            VBox eventCard = createEventCard(event, false);
            allEventsContainer.getChildren().add(eventCard);
        }
    }
    
    private VBox createEventCard(Event event, boolean isComingSoon) {
        VBox card = new VBox(10);
        card.getStyleClass().add("event-card");
        card.setPrefWidth(300);
        card.setPrefHeight(400);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        // Make the entire card clickable
        card.setOnMouseClicked(e -> handleEventClick(event));
        card.setCursor(javafx.scene.Cursor.HAND);
        
        // Event Image (placeholder)
        ImageView imageView = new ImageView();
        imageView.setFitWidth(270);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 4;");
        
        // Add a placeholder text for the image
        StackPane imageContainer = new StackPane();
        imageContainer.getChildren().add(imageView);
        
        Text placeholderText = new Text("Event Image");
        placeholderText.setStyle("-fx-fill: #666; -fx-font-size: 12;");
        StackPane.setAlignment(placeholderText, Pos.CENTER);
        imageContainer.getChildren().add(placeholderText);
        
        // New tag for coming soon events
        if (isComingSoon) {
            Text newTag = new Text("NEW");
            newTag.setStyle("-fx-fill: white; -fx-font-weight: bold; -fx-font-size: 10; -fx-background-color: #ff6b6b; -fx-padding: 4 8; -fx-background-radius: 4;");
            StackPane.setAlignment(newTag, Pos.TOP_LEFT);
            StackPane.setMargin(newTag, new Insets(10, 0, 0, 10));
            imageContainer.getChildren().add(newTag);
        }
        
        card.getChildren().add(imageContainer);
        
        // Event Title
        Text title = new Text(event.getTitle());
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setWrappingWidth(260);
        
        // Event Date and Time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String dateTime = event.getStartDateTime().format(dateFormatter) + " • " + 
                         event.getStartDateTime().format(timeFormatter);
        Text dateTimeText = new Text(dateTime);
        dateTimeText.setStyle("-fx-fill: #666; -fx-font-size: 12;");
        
        // Event Location
        Text location = new Text(event.getLocation());
        location.setStyle("-fx-fill: #666; -fx-font-size: 12;");
        
        // Button
        Button actionButton = new Button(isComingSoon ? "Learn More" : "View Details");
        actionButton.setStyle("-fx-background-color: #4b3a8c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand;");
        actionButton.setOnAction(e -> handleEventClick(event));
        
        // Add hover effect
        actionButton.setOnMouseEntered(e -> {
            actionButton.setStyle("-fx-background-color: #9d7abf; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(157, 122, 191, 0.4), 6, 0, 0, 3); -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        actionButton.setOnMouseExited(e -> {
            actionButton.setStyle("-fx-background-color: #4b3a8c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-transition: all 0.3s ease; -fx-cursor: hand;");
        });
        
        card.getChildren().addAll(title, dateTimeText, location, actionButton);
        
        return card;
    }
    
    private void loadFeaturedOrganizers() {
        organizersContainer.getChildren().clear();
        
        try {
            // Get admin users as organizers
            organizers = userDAO.getUsersByRole(User.ROLE_ADMIN);
            
            for (User organizer : organizers) {
                HBox organizerItem = createOrganizerItem(organizer);
                organizersContainer.getChildren().add(organizerItem);
            }
        } catch (Exception e) {
            System.err.println("Error loading organizers: " + e.getMessage());
            // No demo organizers - show empty state
        }
    }
    
    private HBox createOrganizerItem(User organizer) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 4;");
        
        // Organizer avatar
        ImageView avatar = new ImageView();
        avatar.setFitWidth(40);
        avatar.setFitHeight(40);
        avatar.setStyle("-fx-background-color: #4b3a8c; -fx-background-radius: 20;");
        
        // Organizer name
        Text name = new Text(organizer.getFullName());
        name.setStyle("-fx-font-weight: bold;");
        
        item.getChildren().addAll(avatar, name);
        
        return item;
    }
    
    private void setupSearchFunctionality() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                displayAllEvents(allEvents);
                updateEventCount(allEvents.size());
            } else {
                List<Event> filteredEvents = allEvents.stream()
                    .filter(event -> event.getTitle().toLowerCase().contains(newValue.toLowerCase()) ||
                                   event.getLocation().toLowerCase().contains(newValue.toLowerCase()))
                    .toList();
                displayAllEvents(filteredEvents);
                updateEventCount(filteredEvents.size());
            }
        });
    }
    
    private void updateEventCount(int count) {
        eventCountText.setText("(" + count + ")");
    }
    
    @FXML
    private void handleDateFilter() {
        if (isSearching) return; // Prevent multiple searches
        
        LocalDateTime filterDate = null;
        
        if (todayRadio.isSelected()) {
            filterDate = LocalDateTime.now();
        } else if (weekendRadio.isSelected()) {
            // Get next Saturday
            int daysUntilSaturday = 6 - LocalDateTime.now().getDayOfWeek().getValue();
            if (daysUntilSaturday <= 0) daysUntilSaturday += 7; // If today is Saturday, get next Saturday
            filterDate = LocalDateTime.now().plusDays(daysUntilSaturday);
        }
        
        performDateSearch(filterDate);
    }
    
    @FXML
    private void handleTomorrowToggle() {
        if (isSearching) return;
        
        if (selectedDate != null && selectedDate.toLocalDate().equals(LocalDateTime.now().plusDays(1).toLocalDate())) {
            // If tomorrow is already selected, clear it
            clearDateSelection();
        } else {
            // Select tomorrow
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
            performDateSearch(tomorrow);
        }
    }
    
    @FXML
    private void handlePickDate() {
        if (isSearching) return;
        
        // Create a date picker dialog
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Select Date");
        dialog.setHeaderText("Choose a date to filter events");
        dialog.getDialogPane().setContent(datePicker);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Set the result converter
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return datePicker.getValue();
            }
            return null;
        });
        
        // Show the dialog and handle the result
        Optional<LocalDate> result = dialog.showAndWait();
        result.ifPresent(date -> {
            LocalDateTime selectedDateTime = date.atStartOfDay();
            performDateSearch(selectedDateTime);
        });
    }
    
    @FXML
    private void handleClearDate() {
        clearDateSelection();
    }
    
    private void performDateSearch(LocalDateTime filterDate) {
        isSearching = true;
        selectedDate = filterDate;
        
        // Update UI to show selected date
        updateDateSelectionUI();
        
        // Disable search button during search
        searchField.setDisable(true);
        
        try {
            List<Event> filteredEvents;
            if (filterDate != null) {
                filteredEvents = eventDAO.getEventsByDate(filterDate);
            } else {
                filteredEvents = allEvents;
            }
            
            displayAllEvents(filteredEvents);
            updateEventCount(filteredEvents.size());
            
            // Show feedback
            if (filterDate != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Date Filter Applied");
                alert.setHeaderText("Events filtered by date");
                alert.setContentText("Showing events for: " + filterDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
                alert.showAndWait();
            }
            
        } catch (Exception e) {
            System.err.println("Error filtering events by date: " + e.getMessage());
            displayAllEvents(allEvents);
            updateEventCount(allEvents.size());
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filter Error");
            alert.setHeaderText("Could not filter events");
            alert.setContentText("Showing all events instead.");
            alert.showAndWait();
        } finally {
            isSearching = false;
            searchField.setDisable(false);
        }
    }
    
    private void clearDateSelection() {
        selectedDate = null;
        updateDateSelectionUI();
        
        // Reset radio buttons
        todayRadio.setSelected(true);
        tomorrowRadio.setSelected(false);
        weekendRadio.setSelected(false);
        pickDateRadio.setSelected(false);
        
        // Show all events
        displayAllEvents(allEvents);
        updateEventCount(allEvents.size());
    }
    
    private void updateDateSelectionUI() {
        if (selectedDate != null) {
            selectedDateText.setText("Selected: " + selectedDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
            selectedDateText.setFill(Color.web("#f2c33a")); // Set text color to #f2c33a
            selectedDateContainer.setVisible(true);
        } else {
            selectedDateContainer.setVisible(false);
        }
    }
    
    @FXML
    private void handleEventClick(Event event) {
        // Use RouteManager for deep-link navigation
        RouteManager.navigateToEventDetails(event, searchField.getScene().getWindow());
    }
    
    @FXML
    private void handleMySchedule() {
        ViewUtil.switchTo("MySchedule", searchField.getScene().getWindow());
    }
    
    @FXML
    private void handleBrowseEvents() {
        // Already on browse events page
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Browse Events");
        alert.setHeaderText("Browse Events");
        alert.setContentText("You are currently browsing all events.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleProfile() {
        ViewUtil.switchTo("AttendeeProfile", searchField.getScene().getWindow());
    }
    
    @FXML
    private void handleSearch() {
        // Search functionality is handled by the text field listener
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search");
        alert.setHeaderText("Search Results");
        alert.setContentText("Search functionality is working!");
        alert.showAndWait();
    }
    
    @FXML
    private void handleViewMore() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Date Filter");
        alert.setHeaderText("Advanced Date Filter");
        alert.setContentText("Advanced date filtering options will be implemented in the next version.");
        alert.showAndWait();
    }
    
    @FXML
    private void handleDiscoverOrganizers() {
        try {
            // Load all organizers (not just featured ones)
            List<User> allOrganizers = userDAO.getUsersByRole(User.ROLE_ADMIN);
            
            if (allOrganizers.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Organizers");
                alert.setHeaderText("No Organizers Found");
                alert.setContentText("No additional organizers are available at the moment.");
                alert.showAndWait();
                return;
            }
            
            // Create a dialog to show all organizers
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("All Organizers");
            dialog.setHeaderText("Discover All Event Organizers");
            
            // Create content
            VBox content = new VBox(10);
            content.setPadding(new Insets(20));
            content.setPrefWidth(400);
            content.setPrefHeight(300);
            
            ScrollPane scrollPane = new ScrollPane();
            VBox organizersList = new VBox(10);
            
            for (User organizer : allOrganizers) {
                HBox organizerItem = new HBox(10);
                organizerItem.setAlignment(Pos.CENTER_LEFT);
                organizerItem.setPadding(new Insets(10));
                organizerItem.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 4;");
                
                // Organizer avatar
                ImageView avatar = new ImageView();
                avatar.setFitWidth(40);
                avatar.setFitHeight(40);
                avatar.setStyle("-fx-background-color: #4b3a8c; -fx-background-radius: 20;");
                
                // Organizer info
                VBox info = new VBox(2);
                Text name = new Text(organizer.getFullName());
                name.setStyle("-fx-font-weight: bold;");
                Text email = new Text(organizer.getEmail());
                email.setStyle("-fx-font-size: 12; -fx-fill: #666;");
                info.getChildren().addAll(name, email);
                
                organizerItem.getChildren().addAll(avatar, info);
                organizersList.getChildren().add(organizerItem);
            }
            
            scrollPane.setContent(organizersList);
            content.getChildren().add(scrollPane);
            
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            
            dialog.showAndWait();
            
        } catch (Exception e) {
            System.err.println("Error loading all organizers: " + e.getMessage());
            e.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load organizers");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleBackToDashboard() {
        ViewUtil.switchToRoleDashboard(searchField.getScene().getWindow());
    }
} 