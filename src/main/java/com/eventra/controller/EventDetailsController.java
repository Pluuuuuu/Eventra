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
                // Fallback to demo event
                currentEvent = createDemoEvent();
            }
            
            displayEventDetails();
            loadSpeakers();
            loadSessions();
            loadOrganizer();
            updateRegistrationInfo();
            
        } catch (Exception e) {
            System.err.println("Error loading event details: " + e.getMessage());
            e.printStackTrace();
            // Show demo data
            showDemoEventDetails();
        }
    }
    
    private Event createDemoEvent() {
        Event demoEvent = new Event("Tech Conference 2024", 
            "Annual technology conference featuring the latest innovations in AI, machine learning, and software development. " +
            "Join industry experts for three days of insightful talks, hands-on workshops, and networking opportunities.",
            java.time.LocalDateTime.now().plusDays(10), 
            java.time.LocalDateTime.now().plusDays(10).plusHours(8), 
            "Convention Center, Downtown", 2);
        demoEvent.setEventId(1);
        demoEvent.setMaxAttendees(200);
        demoEvent.setCurrentAttendees(43);
        return demoEvent;
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
            // Add demo speakers
            addDemoSpeakers();
        }
    }
    
    private void addDemoSpeakers() {
        // Create demo speaker cards
        for (int i = 1; i <= 3; i++) {
            VBox speakerCard = new VBox(10);
            speakerCard.setPrefWidth(200);
            speakerCard.setPrefHeight(250);
            speakerCard.setPadding(new Insets(15));
            speakerCard.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8;");
            
            // Speaker avatar
            ImageView avatar = new ImageView();
            avatar.setFitWidth(80);
            avatar.setFitHeight(80);
            avatar.setStyle("-fx-background-color: #007bff; -fx-background-radius: 40;");
            
            // Speaker name
            Text name = new Text("Demo Speaker " + i);
            name.setFont(Font.font("System", FontWeight.BOLD, 14));
            
            // Speaker bio
            Text bio = new Text("Expert in technology and innovation with years of experience in the industry.");
            bio.setWrappingWidth(170);
            bio.setStyle("-fx-font-size: 12; -fx-fill: #666;");
            
            speakerCard.getChildren().addAll(avatar, name, bio);
            speakersContainer.getChildren().add(speakerCard);
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
        avatar.setStyle("-fx-background-color: #007bff; -fx-background-radius: 40;");
        
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
            // Add demo sessions
            addDemoSessions();
        }
    }
    
    private void addDemoSessions() {
        // Create demo session items
        for (int i = 1; i <= 3; i++) {
            HBox sessionItem = new HBox(15);
            sessionItem.setAlignment(Pos.CENTER_LEFT);
            sessionItem.setPadding(new Insets(15));
            sessionItem.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
            
            // Time
            Text time = new Text("9:00 AM - 10:30 AM");
            time.setStyle("-fx-font-weight: bold; -fx-min-width: 120;");
            
            // Title
            Text title = new Text("Demo Session " + i);
            title.setStyle("-fx-font-weight: bold;");
            
            // Speaker
            Text speaker = new Text("Demo Speaker " + i);
            speaker.setStyle("-fx-fill: #666;");
            
            sessionItem.getChildren().addAll(time, title, speaker);
            sessionsContainer.getChildren().add(sessionItem);
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
                organizerName.setText("Demo Organizer");
            }
        } catch (Exception e) {
            System.err.println("Error loading organizer: " + e.getMessage());
            organizerName.setText("Demo Organizer");
        }
    }
    
    private void updateRegistrationInfo() {
        int availableSpots = currentEvent.getMaxAttendees() - currentEvent.getCurrentAttendees();
        if (availableSpots > 0) {
            spotsLeftText.setText("Only " + availableSpots + " spots left!");
            registerButton.setDisable(false);
        } else {
            spotsLeftText.setText("Event is full");
            registerButton.setDisable(true);
        }
    }
    
    private void showDemoEventDetails() {
        currentEvent = createDemoEvent();
        displayEventDetails();
        addDemoSpeakers();
        addDemoSessions();
        organizerName.setText("Demo Organizer");
        updateRegistrationInfo();
    }
    
    @FXML
    private void handleRegister() {
        try {
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser == null) {
                showAlert("Error", "Please log in to register for events.");
                return;
            }
            
            // Check if already registered
            boolean alreadyRegistered = registrationDAO.isUserRegisteredForEvent(
                currentUser.getUserId(), currentEvent.getEventId());
            
            if (alreadyRegistered) {
                showAlert("Already Registered", "You are already registered for this event.");
                return;
            }
            
            // Register for event
            boolean success = registrationDAO.registerUserForEvent(
                currentUser.getUserId(), currentEvent.getEventId());
            
            if (success) {
                showAlert("Success", "Successfully registered for " + currentEvent.getTitle() + "!");
                // Update attendee count
                currentEvent.setCurrentAttendees(currentEvent.getCurrentAttendees() + 1);
                updateRegistrationInfo();
            } else {
                showAlert("Error", "Failed to register for event. Please try again.");
            }
            
        } catch (Exception e) {
            System.err.println("Error registering for event: " + e.getMessage());
            showAlert("Error", "An error occurred during registration. Please try again.");
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
        ViewUtil.switchTo("MySchedule", searchField.getScene().getWindow());
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