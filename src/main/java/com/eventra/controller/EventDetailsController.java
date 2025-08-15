/**
 * Event Details Controller (EventDetailsController.java)
 * 
 * This class controls the EVENT DETAILS SCREEN of the Eventra application. Think of it as the
 * "event brochure" that shows comprehensive information about a specific event, including
 * speakers, sessions, registration status, and all the details attendees need to know.
 * 
 * WHAT THIS CLASS DOES:
 * - Displays complete event information (title, date, time, location, description)
 * - Shows event speakers and presenters with their details
 * - Lists all event sessions and their schedules
 * - Displays organizer information and contact details
 * - Shows available spots and registration status
 * - Handles event registration for attendees
 * - Provides search functionality within event details
 * - Manages error handling for missing or invalid events
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Data Loading: Fetches fresh data from multiple database tables
 * - Error Handling: Gracefully handles missing events and database errors
 * - Session Management: Gets selected event from user's session
 * - Dynamic UI Generation: Creates content based on event data
 * - Registration Management: Handles attendee sign-ups for events
 * 
 * UI COMPONENTS MANAGED:
 * - Event information display (title, date, time, location)
 * - Speakers container with presenter details
 * - Sessions container with schedule information
 * - Registration button and availability display
 * - Organizer information and contact details
 * - Search field for finding specific content
 * 
 * DATA SOURCES:
 * - EventDAO: Main event information
 * - PresenterDAO: Speaker and presenter details
 * - SessionDAO: Event sessions and schedules
 * - UserDAO: Organizer and user information
 * - EventRegistrationDAO: Registration status and management
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.dao.EventDAO;                  // For retrieving event information from database
import com.eventra.dao.PresenterDAO;              // For getting speaker and presenter details
import com.eventra.dao.SessionDAO;                // For retrieving event sessions and schedules
import com.eventra.dao.UserDAO;                   // For getting user/organizer information
import com.eventra.dao.EventRegistrationDAO;      // For managing event registrations

// Model classes for data representation:
import com.eventra.model.Event;                    // Event data model
import com.eventra.model.Presenter;                // Presenter/speaker data model
import com.eventra.model.Session;                  // Event session data model
import com.eventra.model.User;                     // User data model

// Utility classes for application functionality:
import com.eventra.util.SessionManager;            // For managing user sessions and selected events
import com.eventra.util.ViewUtil;                  // For navigating between screens

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                           // Links this class to FXML UI elements
import javafx.scene.control.*;                     // UI controls (buttons, text fields, labels, etc.)
import javafx.scene.image.ImageView;               // For displaying images
import javafx.scene.layout.*;                      // Layout containers (VBox, HBox, FlowPane, etc.)
import javafx.scene.text.Text;                     // For displaying text content
import javafx.scene.text.Font;                     // For customizing text appearance
import javafx.scene.text.FontWeight;               // For making text bold, normal, etc.

// JavaFX geometry and positioning:
import javafx.geometry.Insets;                     // For adding spacing around UI elements
import javafx.geometry.Pos;                        // For aligning content (left, center, right)

// Java standard library imports:
import java.time.format.DateTimeFormatter;         // For formatting dates and times nicely
import java.util.List;                             // For handling collections of data

/**
 * EventDetailsController class - Controls the Event Details Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: Event, Presenter, Session, User models, and various DAO classes
 * - VIEW: FXML file defines the visual layout of the event details page
 * - CONTROLLER: This class connects the model and view, handles all event detail logic
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the EventDetails.fxml file, allowing us to control the UI elements.
 * 
 * COMPLEX DATA INTEGRATION:
 * This controller demonstrates how to combine data from multiple database tables
 * to create a comprehensive view of an event. It's a great example of how
 * modern applications work with relational databases.
 */
public class EventDetailsController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the EventDetails.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what's displayed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can add/remove child elements (getChildren().add())
     */
    @FXML private Text eventTitle;                 // Displays the main event title/name
    @FXML private Text eventDate;                  // Shows the date when the event takes place
    @FXML private Text eventTime;                  // Displays the start time of the event
    @FXML private Text eventLocation;              // Shows the venue or location of the event
    @FXML private FlowPane speakersContainer;      // Container that holds all speaker/presenter information
    @FXML private VBox sessionsContainer;          // Container that holds all event sessions and schedules
    @FXML private Text spotsLeftText;              // Shows how many spots are still available for registration
    @FXML private Button registerButton;           // Button for attendees to register for the event
    @FXML private Text organizerName;              // Displays the name of the person organizing the event
    @FXML private TextField searchField;           // Input field for searching within event details
    
    /**
     * INSTANCE VARIABLES - Store state and data for this controller
     * 
     * These variables keep track of the current state of the event details screen
     * and store references to the data access objects (DAOs) and current event data.
     * 
     * DAO OBJECTS:
     * These are like "database assistants" that help us retrieve and manage data
     * from different tables in the database. Each DAO handles a specific type of data.
     */
    private EventDAO eventDAO;                     // Handles all event-related database operations
    private PresenterDAO presenterDAO;             // Handles speaker/presenter database operations
    private SessionDAO sessionDAO;                 // Handles event session database operations
    private UserDAO userDAO;                       // Handles user/organizer database operations
    private EventRegistrationDAO registrationDAO;  // Handles event registration database operations
    private Event currentEvent;                    // Stores the currently displayed event data
    
    /**
     * initialize() method - Sets up the event details screen when it first loads
     * 
     * This method is automatically called by JavaFX when the event details screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Logs successful page loading for debugging purposes
     * 2. Creates new instances of all DAO classes for database access
     * 3. Calls loadEventDetails() to populate the screen with event information
     * 4. Sets up the entire event details page with fresh data
     * 
     * DAO INITIALIZATION:
     * Each DAO is created as a new instance to ensure fresh database connections.
     * This is important for data integrity and preventing stale data issues.
     * 
     * ERROR HANDLING:
     * If anything goes wrong during initialization, the loadEventDetails() method
     * will handle errors gracefully and show appropriate error messages.
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        System.out.println("EventDetails page loaded successfully!");
        
        // Create new instances of all DAO classes for fresh database access
        eventDAO = new EventDAO();
        presenterDAO = new PresenterDAO();
        sessionDAO = new SessionDAO();
        userDAO = new UserDAO();
        registrationDAO = new EventRegistrationDAO();
        
        // Load and display all event details
        loadEventDetails();
    }
    
    /**
     * loadEventDetails() method - Main method that loads all event information
     * 
     * This is the CORE method that retrieves and displays all event details.
     * It's called during initialization and can be called again to refresh data.
     * 
     * HOW IT WORKS:
     * 1. Gets the selected event from the user's session (what they clicked on)
     * 2. Fetches fresh event data from the database to ensure accuracy
     * 3. Loads and displays all related information (speakers, sessions, organizer)
     * 4. Updates registration status and availability information
     * 5. Handles errors gracefully if anything goes wrong
     * 
     * DATA FRESHNESS:
     * Even though we have an event from the session, we fetch fresh data from
     * the database. This ensures users always see the most current information
     * (like updated times, locations, or availability).
     * 
     * COMPREHENSIVE LOADING:
     * This method loads ALL aspects of an event:
     * - Basic event information (title, date, time, location)
     * - Speakers and presenters
     * - Event sessions and schedules
     * - Organizer details
     * - Registration status and availability
     * 
     * ERROR HANDLING:
     * If anything fails (event not found, database error, etc.), the method
     * shows a user-friendly error message and redirects appropriately.
     */
    private void loadEventDetails() {
        try {
            // STEP 1: Get the selected event from user's session
            // This is the event the user clicked on from the events list
            currentEvent = SessionManager.getSelectedEvent();
            
            if (currentEvent == null) {
                // STEP 2A: No event selected - show error and redirect
                showEventNotFoundError();
                return;
            }
            
            // STEP 2B: Fetch fresh event data from database for accuracy
            // This ensures we have the most current information
            Event freshEvent = eventDAO.getEventById(currentEvent.getEventId());
            if (freshEvent != null) {
                currentEvent = freshEvent; // Use the fresh data
            }
            
            // STEP 3: Load and display all event components
            displayEventDetails();      // Basic event info (title, date, time, location)
            loadSpeakers();             // Event speakers and presenters
            loadSessions();             // Event sessions and schedules
            loadOrganizer();            // Event organizer information
            updateRegistrationInfo();   // Registration status and availability
            
        } catch (Exception e) {
            // STEP 4: Handle any errors that occur during loading
            System.err.println("Error loading event details: " + e.getMessage());
            e.printStackTrace(); // Log full error details for debugging
            showEventNotFoundError(); // Show user-friendly error message
        }
    }
    
    private void showEventNotFoundError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Event Not Found");
        alert.setHeaderText("404 - Event Not Found");
        alert.setContentText("The requested event could not be found. It may have been removed or the link is invalid.");
        alert.showAndWait();
        
        // Navigate back to events list
        ViewUtil.switchTo("AttendeeEvents", searchField.getScene().getWindow());
    }
    
    private void displayEventDetails() {
        eventTitle.setText(currentEvent.getTitle());
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        
        eventDate.setText(currentEvent.getStartDateTime().format(dateFormatter));
        eventTime.setText(currentEvent.getStartDateTime().format(timeFormatter) + " - " + 
                        currentEvent.getEndDateTime().format(timeFormatter));
        eventLocation.setText(currentEvent.getLocation());
    }
    
    private void loadSpeakers() {
        speakersContainer.getChildren().clear();
        
        try {
            List<Presenter> presenters = presenterDAO.getPresentersByEventId(currentEvent.getEventId());
            
            for (Presenter presenter : presenters) {
                VBox speakerCard = createSpeakerCard(presenter);
                speakersContainer.getChildren().add(speakerCard);
            }
        } catch (Exception e) {
            System.err.println("Error loading speakers: " + e.getMessage());
            // No demo speakers - show empty state
        }
    }
    
    private VBox createSpeakerCard(Presenter presenter) {
        VBox speakerCard = new VBox(10);
        speakerCard.setPrefWidth(200);
        speakerCard.setPrefHeight(250);
        speakerCard.setPadding(new Insets(15));
        speakerCard.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        // Speaker avatar
        ImageView avatar = new ImageView();
        avatar.setFitWidth(80);
        avatar.setFitHeight(80);
        avatar.setStyle("-fx-background-color: #4b3a8c; -fx-background-radius: 40;");
        
        // Speaker name
        Text name = new Text(presenter.getName());
        name.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        // Speaker bio
        Text bio = new Text(presenter.getBio() != null ? presenter.getBio() : "Expert speaker");
        bio.setWrappingWidth(170);
        bio.setStyle("-fx-font-size: 12; -fx-fill: #666;");
        
        speakerCard.getChildren().addAll(avatar, name, bio);
        return speakerCard;
    }
    
    private void loadSessions() {
        sessionsContainer.getChildren().clear();
        
        try {
            List<Session> sessions = sessionDAO.getSessionsByEventId(currentEvent.getEventId());
            
            for (Session session : sessions) {
                HBox sessionItem = createSessionItem(session);
                sessionsContainer.getChildren().add(sessionItem);
            }
        } catch (Exception e) {
            System.err.println("Error loading sessions: " + e.getMessage());
            // No demo sessions - show empty state
        }
    }
    
    private HBox createSessionItem(Session session) {
        HBox sessionItem = new HBox(15);
        sessionItem.setAlignment(Pos.CENTER_LEFT);
        sessionItem.setPadding(new Insets(15));
        sessionItem.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        
        // Time
        Text time = new Text(session.getStartTime().format(timeFormatter) + " - " + 
                           session.getEndTime().format(timeFormatter));
        time.setStyle("-fx-font-weight: bold; -fx-min-width: 120;");
        
        // Title
        Text title = new Text(session.getTitle());
        title.setStyle("-fx-font-weight: bold;");
        
        // Speaker (if available)
        if (session.getPresenterId() > 0) {
            try {
                Presenter presenter = presenterDAO.getPresenterById(session.getPresenterId());
                if (presenter != null) {
                    Text speaker = new Text(presenter.getName());
                    speaker.setStyle("-fx-fill: #666;");
                    sessionItem.getChildren().addAll(time, title, speaker);
                    return sessionItem;
                }
            } catch (Exception e) {
                System.err.println("Error getting presenter: " + e.getMessage());
            }
        }
        
        sessionItem.getChildren().addAll(time, title);
        return sessionItem;
    }
    
    private void loadOrganizer() {
        try {
            User organizer = userDAO.getUserById(currentEvent.getOrganizerId());
            if (organizer != null) {
                organizerName.setText(organizer.getFullName());
            } else {
                organizerName.setText("Unknown Organizer");
            }
        } catch (Exception e) {
            System.err.println("Error loading organizer: " + e.getMessage());
            organizerName.setText("Unknown Organizer");
        }
    }
    
    private void updateRegistrationInfo() {
        // Refresh the event data to get current attendee count
        try {
            Event freshEvent = eventDAO.getEventById(currentEvent.getEventId());
            if (freshEvent != null) {
                currentEvent = freshEvent;
            }
        } catch (Exception e) {
            System.err.println("Error refreshing event data: " + e.getMessage());
        }
        
        int availableSpots = currentEvent.getMaxAttendees() - currentEvent.getCurrentAttendees();
        
        // Check if user is already registered
        User currentUser = SessionManager.getCurrentUser();
        boolean alreadyRegistered = false;
        if (currentUser != null) {
            try {
                alreadyRegistered = registrationDAO.isUserRegisteredForEvent(
                    currentUser.getUserId(), currentEvent.getEventId());
            } catch (Exception e) {
                System.err.println("Error checking registration status: " + e.getMessage());
            }
        }
        
        if (alreadyRegistered) {
            spotsLeftText.setText("You are registered for this event!");
            registerButton.setText("Already Registered");
            registerButton.setDisable(true);
        } else if (availableSpots > 0) {
            spotsLeftText.setText("Only " + availableSpots + " spots left!");
            registerButton.setText("Register Now");
            registerButton.setDisable(false);
        } else {
            spotsLeftText.setText("Event is full");
            registerButton.setText("Event Full");
            registerButton.setDisable(true);
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser == null) {
                showAlert("Error", "Please log in to register for events.");
                return;
            }
            
            System.out.println("Attempting registration for user: " + currentUser.getUserId() + 
                             " (" + currentUser.getEmail() + ") to event: " + currentEvent.getEventId() + 
                             " (" + currentEvent.getTitle() + ")");
            
            // Check if already registered
            boolean alreadyRegistered = registrationDAO.isUserRegisteredForEvent(
                currentUser.getUserId(), currentEvent.getEventId());
            
            if (alreadyRegistered) {
                showAlert("Already Registered", "You are already registered for this event.");
                return;
            }
            
            // Check if event is full
            int availableSpots = currentEvent.getMaxAttendees() - currentEvent.getCurrentAttendees();
            if (availableSpots <= 0) {
                showAlert("Event Full", "Sorry, this event is already full.");
                return;
            }
            
            System.out.println("Event capacity: " + currentEvent.getMaxAttendees() + 
                             ", Current attendees: " + currentEvent.getCurrentAttendees() + 
                             ", Available spots: " + availableSpots);
            
            // Disable button during registration
            registerButton.setDisable(true);
            registerButton.setText("Registering...");
            
            // Register for event
            boolean success = registrationDAO.registerUserForEvent(
                currentUser.getUserId(), currentEvent.getEventId());
            
            if (success) {
                System.out.println("Registration successful!");
                showAlert("Success", "Successfully registered for " + currentEvent.getTitle() + "!");
                // Update the UI to reflect the new registration
                updateRegistrationInfo();
            } else {
                System.out.println("Registration failed!");
                showAlert("Registration Failed", 
                    "Failed to register for event. This could be due to:\n" +
                    "• User not found in database\n" +
                    "• Event not found in database\n" +
                    "• Database connection issues\n\n" +
                    "Please try again or contact support if the problem persists.");
            }
            
        } catch (Exception e) {
            System.err.println("Error registering for event: " + e.getMessage());
            e.printStackTrace();
            showAlert("System Error", 
                "An unexpected error occurred during registration:\n" +
                e.getMessage() + "\n\n" +
                "Please try again or contact support if the problem persists.");
        } finally {
            // Re-enable button
            registerButton.setDisable(false);
            registerButton.setText("Register Now");
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void handleMySchedule() {
        try {
            // Switch to MySchedule page
            ViewUtil.switchTo("MySchedule", searchField.getScene().getWindow());
            
            // Get the MyScheduleController and refresh it
            // Note: This will be handled by the MyScheduleController's initialize method
        } catch (Exception e) {
            System.err.println("Error navigating to My Schedule: " + e.getMessage());
            e.printStackTrace();
        }
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