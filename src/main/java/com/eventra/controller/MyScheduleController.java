package com.eventra.controller;

import com.eventra.dao.EventRegistrationDAO;
import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MyScheduleController {
    
    @FXML private FlowPane upcomingEventsContainer;
    @FXML private FlowPane pastEventsContainer;
    @FXML private VBox emptyStateContainer;
    @FXML private Text upcomingCountText;
    @FXML private Text pastCountText;
    @FXML private Text scheduleSubtitle;
    @FXML private TextField searchField;
    
    private EventRegistrationDAO registrationDAO;
    private List<com.eventra.model.Event> registeredEvents;
    
    @FXML
    public void initialize() {
        registrationDAO = new EventRegistrationDAO();
        
        loadUserSchedule();
        setupSearchFunctionality();
    }
    
    private void loadUserSchedule() {
        int currentUserId = SessionManager.getCurrentUserId();
        
        if (currentUserId == -1) {
            showEmptyState("Please log in to view your schedule");
            return;
        }
        
        registeredEvents = registrationDAO.getUserRegisteredEvents(currentUserId);
        
        if (registeredEvents.isEmpty()) {
            showEmptyState("No events in your schedule yet");
            return;
        }
        
        // Split events into upcoming and past
        LocalDateTime now = LocalDateTime.now();
        List<com.eventra.model.Event> upcomingEvents = registeredEvents.stream()
            .filter(event -> event.getStartDateTime().isAfter(now))
            .collect(Collectors.toList());
        
        List<com.eventra.model.Event> pastEvents = registeredEvents.stream()
            .filter(event -> event.getStartDateTime().isBefore(now))
            .collect(Collectors.toList());
        
        // Display events
        displayUpcomingEvents(upcomingEvents);
        displayPastEvents(pastEvents);
        
        // Update counts
        upcomingCountText.setText("(" + upcomingEvents.size() + ")");
        pastCountText.setText("(" + pastEvents.size() + ")");
        
        // Hide empty state
        emptyStateContainer.setVisible(false);
        emptyStateContainer.setManaged(false);
    }
    
    private void displayUpcomingEvents(List<com.eventra.model.Event> events) {
        upcomingEventsContainer.getChildren().clear();
        
        for (com.eventra.model.Event event : events) {
            VBox eventCard = createScheduleEventCard(event, true);
            upcomingEventsContainer.getChildren().add(eventCard);
        }
    }
    
    private void displayPastEvents(List<com.eventra.model.Event> events) {
        pastEventsContainer.getChildren().clear();
        
        for (com.eventra.model.Event event : events) {
            VBox eventCard = createScheduleEventCard(event, false);
            pastEventsContainer.getChildren().add(eventCard);
        }
    }
    
    private VBox createScheduleEventCard(com.eventra.model.Event event, boolean isUpcoming) {
        VBox card = new VBox(10);
        card.getStyleClass().add("schedule-event-card");
        card.setPrefWidth(300);
        card.setPrefHeight(350);
        card.setPadding(new Insets(15));
        
        // Event Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(270);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);
        
        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            try {
                imageView.setImage(new Image(event.getImageUrl()));
            } catch (Exception e) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default-event.png")));
            }
        } else {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default-event.png")));
        }
        
        // Status badge
        StackPane imageContainer = new StackPane();
        imageContainer.getChildren().add(imageView);
        
        Text statusBadge = new Text(isUpcoming ? "Upcoming" : "Past");
        statusBadge.getStyleClass().add(isUpcoming ? "upcoming-badge" : "past-badge");
        StackPane.setAlignment(statusBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(statusBadge, new Insets(10, 10, 0, 0));
        imageContainer.getChildren().add(statusBadge);
        
        card.getChildren().add(imageContainer);
        
        // Event Title
        Text title = new Text(event.getTitle());
        title.getStyleClass().add("event-title");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setWrappingWidth(260);
        
        // Event Date and Time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String dateTime = event.getStartDateTime().format(dateFormatter) + " • " + 
                         event.getStartDateTime().format(timeFormatter);
        Text dateTimeText = new Text(dateTime);
        dateTimeText.getStyleClass().add("event-datetime");
        
        // Event Location
        Text location = new Text(event.getLocation());
        location.getStyleClass().add("event-location");
        
        // Action Button
        Button actionButton = new Button(isUpcoming ? "View Details" : "View Details");
        actionButton.getStyleClass().add("view-details-button");
        actionButton.setOnAction(e -> handleEventClick(event));
        
        card.getChildren().addAll(title, dateTimeText, location, actionButton);
        
        return card;
    }
    
    private void showEmptyState(String message) {
        emptyStateContainer.setVisible(true);
        emptyStateContainer.setManaged(true);
        
        // Update the empty state message
        Text emptyStateTitle = (Text) emptyStateContainer.getChildren().get(1);
        emptyStateTitle.setText(message);
        
        // Hide the event containers
        upcomingEventsContainer.setVisible(false);
        upcomingEventsContainer.setManaged(false);
        pastEventsContainer.setVisible(false);
        pastEventsContainer.setManaged(false);
        
        // Clear counts
        upcomingCountText.setText("(0)");
        pastCountText.setText("(0)");
    }
    
    private void setupSearchFunctionality() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                loadUserSchedule(); // Reload all events
            } else {
                // Filter events based on search
                List<com.eventra.model.Event> filteredEvents = registeredEvents.stream()
                    .filter(event -> event.getTitle().toLowerCase().contains(newValue.toLowerCase()) ||
                                   event.getLocation().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());
                
                // Split filtered events
                LocalDateTime now = LocalDateTime.now();
                List<com.eventra.model.Event> upcomingEvents = filteredEvents.stream()
                    .filter(event -> event.getStartDateTime().isAfter(now))
                    .collect(Collectors.toList());
                
                List<com.eventra.model.Event> pastEvents = filteredEvents.stream()
                    .filter(event -> event.getStartDateTime().isBefore(now))
                    .collect(Collectors.toList());
                
                displayUpcomingEvents(upcomingEvents);
                displayPastEvents(pastEvents);
                
                upcomingCountText.setText("(" + upcomingEvents.size() + ")");
                pastCountText.setText("(" + pastEvents.size() + ")");
            }
        });
    }
    
    @FXML
    private void handleEventClick(com.eventra.model.Event event) {
        // Store the selected event and navigate to event details
        SessionManager.setSelectedEvent(event);
        ViewUtil.switchTo("EventDetails", searchField.getScene().getWindow());
    }
    
    @FXML
    private void handleMySchedule() {
        // Already on My Schedule page
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
        // Search functionality is handled by the text field listener
    }
} 