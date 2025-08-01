package com.eventra.controller;

import com.eventra.dao.EventDAO;
import com.eventra.dao.UserDAO;
import com.eventra.model.Event;
import com.eventra.model.User;
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
import java.util.ArrayList;

public class AttendeeEventsController {
    
    @FXML private HBox comingSoonContainer;
    @FXML private FlowPane allEventsContainer;
    @FXML private VBox organizersContainer;
    @FXML private Text eventCountText;
    @FXML private TextField searchField;
    
    @FXML private RadioButton todayRadio;
    @FXML private RadioButton tomorrowRadio;
    @FXML private RadioButton weekendRadio;
    @FXML private RadioButton pickDateRadio;
    
    private EventDAO eventDAO;
    private UserDAO userDAO;
    private List<Event> allEvents;
    private List<User> organizers;
    
    @FXML
    public void initialize() {
        System.out.println("AttendeeEvents page loaded successfully!");
        
        eventDAO = new EventDAO();
        userDAO = new UserDAO();
        
        loadEvents();
        loadFeaturedOrganizers();
        setupSearchFunctionality();
    }
    
    private void loadEvents() {
        try {
            // Load coming soon events
            List<Event> comingSoonEvents = eventDAO.getComingSoonEvents();
            displayComingSoonEvents(comingSoonEvents);
            
            // Load all events
            allEvents = eventDAO.getAllEvents();
            displayAllEvents(allEvents);
            updateEventCount(allEvents.size());
        } catch (Exception e) {
            System.err.println("Error loading events: " + e.getMessage());
            e.printStackTrace();
            // Fallback to demo data
            showDemoEvents();
        }
    }
    
    private void showDemoEvents() {
        // Create demo events if database fails
        allEvents = new ArrayList<>();
        
        Event demoEvent1 = new Event("Tech Conference 2024", "Annual technology conference", 
            LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(8), 
            "Convention Center", 2);
        demoEvent1.setEventId(1);
        
        Event demoEvent2 = new Event("Startup Networking", "Connect with entrepreneurs", 
            LocalDateTime.now().plusDays(15), LocalDateTime.now().plusDays(15).plusHours(3), 
            "Innovation Hub", 2);
        demoEvent2.setEventId(2);
        
        allEvents.add(demoEvent1);
        allEvents.add(demoEvent2);
        
        displayComingSoonEvents(allEvents.subList(0, Math.min(2, allEvents.size())));
        displayAllEvents(allEvents);
        updateEventCount(allEvents.size());
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
        actionButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;");
        actionButton.setOnAction(e -> handleEventClick(event));
        
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
            // Add demo organizers
            addDemoOrganizers();
        }
    }
    
    private void addDemoOrganizers() {
        // Create demo organizer items
        for (int i = 1; i <= 3; i++) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(10));
            item.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 4;");
            
            // Demo avatar
            ImageView avatar = new ImageView();
            avatar.setFitWidth(40);
            avatar.setFitHeight(40);
            avatar.setStyle("-fx-background-color: #007bff; -fx-background-radius: 20;");
            
            // Demo name
            Text name = new Text("Demo Organizer " + i);
            name.setStyle("-fx-font-weight: bold;");
            
            item.getChildren().addAll(avatar, name);
            organizersContainer.getChildren().add(item);
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
        avatar.setStyle("-fx-background-color: #007bff; -fx-background-radius: 20;");
        
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
        LocalDateTime filterDate = null;
        
        if (todayRadio.isSelected()) {
            filterDate = LocalDateTime.now();
        } else if (tomorrowRadio.isSelected()) {
            filterDate = LocalDateTime.now().plusDays(1);
        } else if (weekendRadio.isSelected()) {
            // Get next Saturday
            int daysUntilSaturday = 6 - LocalDateTime.now().getDayOfWeek().getValue();
            if (daysUntilSaturday <= 0) daysUntilSaturday += 7; // If today is Saturday, get next Saturday
            filterDate = LocalDateTime.now().plusDays(daysUntilSaturday);
        }
        
        if (filterDate != null) {
            try {
                List<Event> filteredEvents = eventDAO.getEventsByDate(filterDate);
                displayAllEvents(filteredEvents);
                updateEventCount(filteredEvents.size());
                
                // Show feedback
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Date Filter Applied");
                alert.setHeaderText("Events filtered by date");
                alert.setContentText("Showing events for: " + filterDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
                alert.showAndWait();
            } catch (Exception e) {
                System.err.println("Error filtering events by date: " + e.getMessage());
                displayAllEvents(allEvents);
                updateEventCount(allEvents.size());
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Filter Error");
                alert.setHeaderText("Could not filter events");
                alert.setContentText("Showing all events instead.");
                alert.showAndWait();
            }
        } else {
            displayAllEvents(allEvents);
            updateEventCount(allEvents.size());
        }
    }
    
    @FXML
    private void handleEventClick(Event event) {
        try {
            // Store the selected event in session and navigate to event details
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
                avatar.setStyle("-fx-background-color: #007bff; -fx-background-radius: 20;");
                
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
} 