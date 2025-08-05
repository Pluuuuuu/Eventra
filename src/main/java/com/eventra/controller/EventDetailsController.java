package com.eventra.controller;

import com.eventra.dao.EventDAO;
import com.eventra.dao.PresenterDAO;
import com.eventra.dao.SessionDAO;
import com.eventra.dao.UserDAO;
import com.eventra.dao.EventRegistrationDAO;
import com.eventra.model.Event;
import com.eventra.model.Presenter;
import com.eventra.model.Session;
import com.eventra.model.User;
import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventDetailsController {
    
    @FXML private Text eventTitle;
    @FXML private Text eventDate;
    @FXML private Text eventTime;
    @FXML private Text eventLocation;
    @FXML private FlowPane speakersContainer;
    @FXML private VBox sessionsContainer;
    @FXML private Text spotsLeftText;
    @FXML private Button registerButton;
    @FXML private Text organizerName;
    @FXML private TextField searchField;
    
    private EventDAO eventDAO;
    private PresenterDAO presenterDAO;
    private SessionDAO sessionDAO;
    private UserDAO userDAO;
    private EventRegistrationDAO registrationDAO;
    private Event currentEvent;
    
    @FXML
    public void initialize() {
        System.out.println("EventDetails page loaded successfully!");
        
        eventDAO = new EventDAO();
        presenterDAO = new PresenterDAO();
        sessionDAO = new SessionDAO();
        userDAO = new UserDAO();
        registrationDAO = new EventRegistrationDAO();
        
        loadEventDetails();
    }
    
    private void loadEventDetails() {
        try {
            // Get the selected event from session
            currentEvent = SessionManager.getSelectedEvent();
            
            if (currentEvent == null) {
                // Try to get event from URL parameters
                showEventNotFoundError();
                return;
            }
            
            // Fetch fresh event data from database using the correct schema
            Event freshEvent = eventDAO.getEventById(currentEvent.getEventId());
            if (freshEvent != null) {
                currentEvent = freshEvent;
            }
            
            displayEventDetails();
            loadSpeakers();
            loadSessions();
            loadOrganizer();
            updateRegistrationInfo();
            
        } catch (Exception e) {
            System.err.println("Error loading event details: " + e.getMessage());
            e.printStackTrace();
            showEventNotFoundError();
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