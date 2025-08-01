package com.eventra.controller;

import com.eventra.dao.EventDAO;
import com.eventra.dao.EventRegistrationDAO;
import com.eventra.model.Event;
import com.eventra.util.SessionManager;
import com.eventra.util.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyScheduleController {
    
    @FXML private VBox registeredEventsContainer;
    @FXML private VBox pastEventsContainer;
    @FXML private TextField searchField;
    
    private EventDAO eventDAO;
    private EventRegistrationDAO registrationDAO;
    
    @FXML
    public void initialize() {
        System.out.println("MySchedule page loaded successfully!");
        
        eventDAO = new EventDAO();
        registrationDAO = new EventRegistrationDAO();
        
        loadUserSchedule();
    }
    
    public void refreshSchedule() {
        loadUserSchedule();
    }
    
    private void loadUserSchedule() {
        try {
            int currentUserId = SessionManager.getCurrentUserId();
            if (currentUserId == -1) {
                showEmptyState("Please log in to view your schedule.");
                return;
            }
            
            // Load registered events
            List<Event> registeredEvents = registrationDAO.getUserRegisteredEvents(currentUserId);
            
            if (registeredEvents.isEmpty()) {
                showEmptyState("No events in your schedule yet. Start exploring events and register for ones that interest you!");
                return;
            }
            
            // Separate upcoming and past events
            LocalDateTime now = LocalDateTime.now();
            List<Event> upcomingEvents = registeredEvents.stream()
                .filter(event -> event.getStartDateTime().isAfter(now))
                .toList();
            
            List<Event> pastEvents = registeredEvents.stream()
                .filter(event -> event.getStartDateTime().isBefore(now))
                .toList();
            
            // Display upcoming events
            displayEvents(upcomingEvents, registeredEventsContainer, "No upcoming events");
            
            // Display past events
            displayEvents(pastEvents, pastEventsContainer, "No past events");
            
        } catch (Exception e) {
            System.err.println("Error loading user schedule: " + e.getMessage());
            e.printStackTrace();
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
        
        // Action button
        Button viewButton = new Button("View Details");
        viewButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;");
        viewButton.setOnAction(e -> handleViewEvent(event));
        
        item.getChildren().addAll(dateText, info, viewButton);
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
        browseButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;");
        browseButton.setOnAction(e -> handleBrowseEvents());
        
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