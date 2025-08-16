package com.eventra.util;

import com.eventra.dao.EventDAO;
import com.eventra.model.Event;
import javafx.stage.Window;

/**
 * Route Manager (RouteManager.java)
 * 
 * This class handles navigation between different screens in the Eventra application.
 * Think of it like a "traffic controller" that directs users to the right places
 * when they click on events or navigate through the app.
 * 
 * WHAT THIS CLASS DOES:
 * - Takes users from event lists to detailed event pages
 * - Handles navigation errors gracefully with helpful messages
 * - Manages the flow between different parts of the application
 * - Ensures users always end up somewhere useful, even when things go wrong
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Navigation: Moving between different screens in the app
 * - Routing: Determining which screen to show based on user actions
 * - Error Handling: What happens when navigation fails
 * - Session Management: Remembering which event the user is looking at
 * 
 * USER EXPERIENCE BENEFITS:
 * - Smooth transitions between screens
 * - Helpful error messages when things go wrong
 * - Users never get stuck on broken pages
 * - Consistent navigation behavior throughout the app
 * 
 * WHEN THIS IS USED:
 * - When users click on event cards or event links
 * - When the app needs to show detailed event information
 * - When navigation errors occur and need to be handled
 * - When users need to be redirected to appropriate pages
 */
public class RouteManager {
    
    /**
     * navigateToEventDetails(int eventId, Window window) - Navigate using Event ID
     * 
     * This method takes users to the event details page when they only know
     * the event's ID number. It's like looking up a book by its catalog number
     * and then taking you to the book's detailed information page.
     * 
     * WHAT IT DOES:
     * - Takes an event ID number and finds the corresponding event
     * - Loads the event's complete information from the database
     * - Stores the event in the user's session for easy access
     * - Takes the user to the detailed event page
     * - Shows helpful error messages if something goes wrong
     * 
     * USER EXPERIENCE:
     * - Users click on an event and immediately see all the details
     * - If the event doesn't exist, they get a clear error message
     * - If there's a technical problem, they're safely redirected
     * - The navigation feels smooth and reliable
     * 
     * TECHNICAL PROCESS:
     * 1. Create a new EventDAO to access the database
     * 2. Look up the event using the provided ID
     * 3. If found: store it in session and navigate to details page
     * 4. If not found: show "event not found" error
     * 5. If error occurs: show navigation error and redirect safely
     * 
     * @param eventId The unique ID number of the event to display
     * @param window The current application window for navigation
     */
    public static void navigateToEventDetails(int eventId, Window window) {
        try {
            // 
            // STEP 1: FETCH EVENT FROM DATABASE
            // 
            // This creates a new EventDAO object to access the database and
            // looks up the event using the ID number provided.
            // 
            // THINK OF IT LIKE:
            // - Looking up a book in a library catalog using its call number
            // - Searching for a product in an online store using its SKU
            // - Finding a person in a phone book using their phone number
            // 
            EventDAO eventDAO = new EventDAO();
            Event event = eventDAO.getEventById(eventId);
            
            // 
            // STEP 2: CHECK IF EVENT EXISTS AND NAVIGATE
            // 
            // If we found the event, we store it in the user's session and
            // navigate to the details page. If not, we show an error.
            // 
            // THINK OF IT LIKE:
            // - If you find the book: take it off the shelf and show it to the user
            // - If you don't find the book: tell the user "sorry, that book doesn't exist"
            // 
            if (event != null) {
                // 
                // SUCCESS: Store event in session and navigate
                // 
                // We store the event in the session so other parts of the app
                // can access it without having to look it up again.
                // 
                // THINK OF IT LIKE:
                // - Putting a book on your desk so you can reference it easily
                // - Saving a webpage to your bookmarks for quick access
                // 
                SessionManager.setSelectedEvent(event);
                
                // 
                // NAVIGATE TO EVENT DETAILS PAGE
                // 
                // This takes the user to the detailed view of the event.
                // The ViewUtil.switchTo method handles the actual screen change.
                // 
                // THINK OF IT LIKE:
                // - Clicking a link to go to a detailed product page
                // - Opening a book to see its table of contents and chapters
                // 
                ViewUtil.switchTo("EventDetails", window);
            } else {
                // 
                // EVENT NOT FOUND: Show helpful error message
                // 
                // If the event ID doesn't exist in the database, we show
                // a clear error message and redirect the user to safety.
                // 
                // THINK OF IT LIKE:
                // - A librarian saying "I'm sorry, that book number doesn't exist"
                // - A website showing "404 - Page Not Found" with helpful links
                // 
                showEventNotFoundError(window);
            }
        } catch (Exception e) {
            // 
            // TECHNICAL ERROR: Handle unexpected problems gracefully
            // 
            // If something unexpected goes wrong (database error, network problem, etc.),
            // we log the error for developers and show a user-friendly message.
            // 
            // THINK OF IT LIKE:
            // - A car's check engine light that also tells you "drive to nearest service station"
            // - A website that says "something went wrong" instead of crashing
            // 
            System.err.println("Error navigating to event details: " + e.getMessage());
            e.printStackTrace();
            showNavigationError(window);
        }
    }
    
    /**
     * navigateToEventDetails(Event event, Window window) - Navigate using Event Object
     * 
     * This method takes users to the event details page when we already have
     * the complete event information. It's like already having a book in hand
     * and just needing to open it to the details page.
     * 
     * WHAT IT DOES:
     * - Takes a complete Event object (no database lookup needed)
     * - Stores the event in the user's session for easy access
     * - Takes the user directly to the detailed event page
     * - Shows helpful error messages if something goes wrong
     * 
     * USER EXPERIENCE:
     * - Faster navigation since no database lookup is needed
     * - Users see event details immediately
     * - Consistent behavior with the ID-based navigation method
     * - Reliable error handling if problems occur
     * 
     * WHEN TO USE THIS:
     * - When you already have the event data (like from a list or search)
     * - When you want faster navigation without database delays
     * - When the event information is already loaded in memory
     * 
     * @param event The complete Event object to display
     * @param window The current application window for navigation
     */
    public static void navigateToEventDetails(Event event, Window window) {
        try {
            // 
            // CHECK IF EVENT EXISTS AND NAVIGATE
            // 
            // Since we already have the event object, we just need to check
            // if it's valid and then navigate to the details page.
            // 
            // THINK OF IT LIKE:
            // - Already having a book in your hand - just open it
            // - Already having a product in your cart - just view its details
            // 
            if (event != null) {
                // 
                // SUCCESS: Store event in session and navigate
                // 
                // We store the event in the session so other parts of the app
                // can access it without having to look it up again.
                // 
                // THINK OF IT LIKE:
                // - Putting a book on your desk so you can reference it easily
                // - Saving a webpage to your bookmarks for quick access
                // 
                SessionManager.setSelectedEvent(event);
                
                // 
                // NAVIGATE TO EVENT DETAILS PAGE
                // 
                // This takes the user to the detailed view of the event.
                // The ViewUtil.switchTo method handles the actual screen change.
                // 
                // THINK OF IT LIKE:
                // - Clicking a link to go to a detailed product page
                // - Opening a book to see its table of contents and chapters
                // 
                ViewUtil.switchTo("EventDetails", window);
            } else {
                // 
                // EVENT IS NULL: Show helpful error message
                // 
                // If the event object is null (doesn't exist), we show
                // a clear error message and redirect the user to safety.
                // 
                // THINK OF IT LIKE:
                // - Someone handing you an empty box and saying "here's your book"
                // - A website showing "404 - Page Not Found" with helpful links
                // 
                showEventNotFoundError(window);
            }
        } catch (Exception e) {
            // 
            // TECHNICAL ERROR: Handle unexpected problems gracefully
            // 
            // If something unexpected goes wrong during navigation, we log
            // the error for developers and show a user-friendly message.
            // 
            // THINK OF IT LIKE:
            // - A car's check engine light that also tells you "drive to nearest service station"
            // - A website that says "something went wrong" instead of crashing
            // 
            System.err.println("Error navigating to event details: " + e.getMessage());
            e.printStackTrace();
            showNavigationError(window);
        }
    }
    
    /**
     * showEventNotFoundError(Window window) - Handle Missing Event Errors
     * 
     * This method shows a helpful error message when a user tries to view
     * an event that doesn't exist. It's like a helpful librarian who tells
     * you "that book doesn't exist" and suggests where else to look.
     * 
     * WHAT IT DOES:
     * - Shows a clear "Event Not Found" error message
     * - Explains what happened in user-friendly language
     * - Automatically redirects users to the events list page
     * - Prevents users from getting stuck on broken pages
     * 
     * USER EXPERIENCE:
     * - Clear understanding of what went wrong
     * - Helpful explanation of the problem
     * - Automatic redirection to a working page
     * - No confusion or frustration about where to go next
     * 
     * ERROR MESSAGE CONTENT:
     * - Title: "Event Not Found" (clear and direct)
     * - Header: "404 - Event Not Found" (technical context)
     * - Content: Helpful explanation with possible reasons
     * 
     * @param window The current application window for navigation
     */
    private static void showEventNotFoundError(Window window) {
        // 
        // CREATE ERROR ALERT - Show user-friendly error message
        // 
        // This creates a popup dialog that clearly explains what went wrong.
        // The AlertType.ERROR makes it visually clear this is an error message.
        // 
        // THINK OF IT LIKE:
        // - A helpful error message on a website that explains the problem
        // - A librarian who says "I'm sorry, that book doesn't exist" instead of just shrugging
        // 
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        
        // 
        // SET ALERT CONTENT - Clear and helpful error information
        // 
        // We set three levels of information to help users understand the problem:
        // - Title: What type of error this is
        // - Header: Technical context (404 status)
        // - Content: Human-readable explanation with helpful suggestions
        // 
        // THINK OF IT LIKE:
        // - A book with a clear title, chapter heading, and detailed explanation
        // - A website error page that tells you exactly what went wrong
        // 
        alert.setTitle("Event Not Found");
        alert.setHeaderText("404 - Event Not Found");
        alert.setContentText("The requested event could not be found. It may have been removed or the link is invalid.");
        
        // 
        // SHOW ALERT AND WAIT - Ensure user sees the message
        // 
        // showAndWait() displays the error message and waits for the user
        // to acknowledge it before continuing. This ensures they see the error.
        // 
        // THINK OF IT LIKE:
        // - A popup that stays open until you click "OK"
        // - A notification that requires your attention before you can continue
        // 
        alert.showAndWait();
        
        // 
        // SAFE REDIRECTION - Take user to working page
        // 
        // After showing the error, we automatically redirect the user to
        // the events list page where they can browse other events.
        // 
        // THINK OF IT LIKE:
        // - A librarian who says "that book doesn't exist, but let me show you our new arrivals"
        // - A website that redirects you to a working page instead of leaving you stranded
        // 
        ViewUtil.switchTo("AttendeeEvents", window);
    }
    
    /**
     * showNavigationError(Window window) - Handle Technical Navigation Errors
     * 
     * This method shows a helpful error message when something goes wrong
     * during the navigation process. It's like a helpful system administrator
     * who explains technical problems in simple terms.
     * 
     * WHAT IT DOES:
     * - Shows a clear "Navigation Error" message
     * - Explains that a technical problem occurred
     * - Suggests what the user can do about it
     * - Automatically redirects users to a working page
     * 
     * USER EXPERIENCE:
     * - Understanding that it's a technical problem, not their fault
     * - Clear guidance on what to do next
     * - Automatic redirection to safety
     * - No confusion about whether to wait or try again
     * 
     * WHEN THIS IS USED:
     * - Database connection problems
     * - Network or server errors
     * - Unexpected application crashes
     * - Any technical issue during navigation
     * 
     * @param window The current application window for navigation
     */
    private static void showNavigationError(Window window) {
        // 
        // CREATE ERROR ALERT - Show technical error message
        // 
        // This creates a popup dialog that explains a technical problem
        // occurred during navigation. It's different from "event not found"
        // because this is a system problem, not a missing data problem.
        // 
        // THINK OF IT LIKE:
        // - A computer error message that says "something went wrong with the system"
        // - A website that says "we're experiencing technical difficulties"
        // 
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        
        // 
        // SET ALERT CONTENT - Technical problem explanation
        // 
        // We explain that this is a technical problem and suggest what
        // the user can do about it. The message is encouraging, not frustrating.
        // 
        // THINK OF IT LIKE:
        // - A helpful IT person who says "the system is having trouble, let me help you"
        // - A website that says "we're working on it, please try again"
        // 
        alert.setTitle("Navigation Error");
        alert.setHeaderText("Could not load event details");
        alert.setContentText("An error occurred while loading the event details. Please try again.");
        
        // 
        // SHOW ALERT AND WAIT - Ensure user sees the message
        // 
        // showAndWait() displays the error message and waits for the user
        // to acknowledge it before continuing. This ensures they see the error.
        // 
        // THINK OF IT LIKE:
        // - A popup that stays open until you click "OK"
        // - A notification that requires your attention before you can continue
        // 
        alert.showAndWait();
        
        // 
        // SAFE REDIRECTION - Take user to working page
        // 
        // After showing the error, we automatically redirect the user to
        // the events list page where they can try again or browse other events.
        // 
        // THINK OF IT LIKE:
        // - A system that takes you back to a working page instead of crashing
        // - A website that redirects you to safety when technical problems occur
        // 
        ViewUtil.switchTo("AttendeeEvents", window);
    }
} 