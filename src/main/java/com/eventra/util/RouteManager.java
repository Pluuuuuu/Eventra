package com.eventra.util;

import com.eventra.dao.EventDAO;
import com.eventra.model.Event;
import javafx.stage.Window;

public class RouteManager {
    
    /**
     * Navigate to event details page with event ID
     */
    public static void navigateToEventDetails(int eventId, Window window) {
        try {
            // Fetch the event by ID
            EventDAO eventDAO = new EventDAO();
            Event event = eventDAO.getEventById(eventId);
            
            if (event != null) {
                // Store the event in session
                SessionManager.setSelectedEvent(event);
                
                // Navigate to event details page
                ViewUtil.switchTo("EventDetails", window);
            } else {
                // Event not found - show 404 error
                showEventNotFoundError(window);
            }
        } catch (Exception e) {
            System.err.println("Error navigating to event details: " + e.getMessage());
            e.printStackTrace();
            showNavigationError(window);
        }
    }
    
    /**
     * Navigate to event details page with event object
     */
    public static void navigateToEventDetails(Event event, Window window) {
        try {
            if (event != null) {
                // Store the event in session
                SessionManager.setSelectedEvent(event);
                
                // Navigate to event details page
                ViewUtil.switchTo("EventDetails", window);
            } else {
                showEventNotFoundError(window);
            }
        } catch (Exception e) {
            System.err.println("Error navigating to event details: " + e.getMessage());
            e.printStackTrace();
            showNavigationError(window);
        }
    }
    
    /**
     * Show 404 error for event not found
     */
    private static void showEventNotFoundError(Window window) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Event Not Found");
        alert.setHeaderText("404 - Event Not Found");
        alert.setContentText("The requested event could not be found. It may have been removed or the link is invalid.");
        alert.showAndWait();
        
        // Navigate back to events list
        ViewUtil.switchTo("AttendeeEvents", window);
    }
    
    /**
     * Show navigation error
     */
    private static void showNavigationError(Window window) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Navigation Error");
        alert.setHeaderText("Could not load event details");
        alert.setContentText("An error occurred while loading the event details. Please try again.");
        alert.showAndWait();
        
        // Navigate back to events list
        ViewUtil.switchTo("AttendeeEvents", window);
    }
} 