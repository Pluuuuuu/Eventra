/**
 * Session Manager (SessionManager.java)
 * 
 * This class manages the current user's session and application state throughout
 * the Eventra application. Think of it as the "memory" that keeps track of who
 * is logged in and what they're currently working with.
 * 
 * WHAT THIS CLASS DOES:
 * - Stores the currently logged-in user information
 * - Manages user authentication state
 * - Tracks selected events for navigation
 * - Provides easy access to current user data
 * - Handles user logout and session cleanup
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Static Methods: All methods are static, meaning you don't need to create
 *   a SessionManager object to use them
 * - Session State: Maintains application state during user's session
 * - User Context: Provides user information to other parts of the application
 * - Memory Management: Simple in-memory storage (not persistent across app restarts)
 * 
 * SECURITY FEATURES:
 * - Centralized user session management
 * - Easy session validation throughout the app
 * - Secure logout functionality
 * - Prevents unauthorized access to user-specific features
 * 
 * USAGE PATTERNS:
 * - Called after successful login to establish session
 * - Used throughout the app to check if user is logged in
 * - Referenced for user-specific operations and displays
 * - Called during logout to clear session data
 */
package com.eventra.util;

import com.eventra.model.User;
import com.eventra.model.Event;

public class SessionManager {
    /**
     * CURRENT USER SESSION - Stores the logged-in user's information
     * 
     * This static variable holds the User object for whoever is currently
     * logged into the application. It's null when no one is logged in.
     * 
     * ACCESS PATTERN:
     * - Set to User object after successful login
     * - Referenced throughout the app for user-specific operations
     * - Set to null during logout
     * 
     * SECURITY CONSIDERATIONS:
     * - Only stores user data in memory (not persistent)
     * - Cleared when application closes
     * - No sensitive data like passwords stored here
     */
    private static User currentUser;
    
    /**
     * setCurrentUser() - Establishes a user session after successful login
     * 
     * This method is called immediately after a user successfully logs in.
     * It stores the user's information so the rest of the application
     * knows who is currently using the system.
     * 
     * CALLING SCENARIOS:
     * - LoginController after successful authentication
     * - Demo mode login for testing purposes
     * - Session restoration (if implemented)
     * 
     * @param user - The User object representing the logged-in user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    /**
     * getCurrentUser() - Retrieves the currently logged-in user
     * 
     * This method provides access to the current user's information
     * throughout the application. It's used to display user details,
     * check permissions, and perform user-specific operations.
     * 
     * COMMON USE CASES:
     * - Display user name in navigation bars
     * - Check user role for access control
     * - Get user ID for database operations
     * - Show user profile information
     * 
     * @return User - The current user object, or null if no user is logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * isLoggedIn() - Checks if any user is currently logged in
     * 
     * This method provides a simple boolean check for authentication status.
     * It's commonly used to control access to protected features and
     * determine navigation options.
     * 
     * UI INTEGRATION:
     * - Show/hide login-specific buttons and menus
     * - Control access to user dashboard
     * - Determine if logout option should be displayed
     * - Redirect unauthenticated users to login page
     * 
     * @return boolean - true if a user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * logout() - Ends the current user session
     * 
     * This method clears the current user session, effectively logging
     * the user out of the application. It's called when the user
     * explicitly logs out or when their session expires.
     * 
     * SECURITY ACTIONS:
     * - Removes user data from memory
     * - Prevents access to protected features
     * - Clears any cached user information
     * - Prepares system for new login
     * 
     * POST-LOGOUT BEHAVIOR:
     * - User is redirected to login page
     * - All user-specific data is cleared
     * - Application returns to unauthenticated state
     */
    public static void logout() {
        currentUser = null;
    }
    
    /**
     * getCurrentUserId() - Gets the ID of the currently logged-in user
     * 
     * This convenience method extracts just the user ID from the current user.
     * It's commonly used for database operations that need to reference
     * the current user without loading the entire user object.
     * 
     * DATABASE OPERATIONS:
     * - Creating events (set organizer ID)
     * - Recording user actions (audit trails)
     * - User-specific queries and filters
     * - Foreign key relationships
     * 
     * RETURN VALUE:
     * - Returns user ID if logged in
     * - Returns -1 if no user is logged in (safe default)
     * 
     * @return int - The current user's ID, or -1 if no user is logged in
     */
    public static int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }
    
    /**
     * getCurrentUserName() - Gets the display name of the current user
     * 
     * This convenience method provides the user's full name for display purposes.
     * It's used throughout the UI to show who is currently logged in.
     * 
     * UI DISPLAY:
     * - Welcome messages and greetings
     * - User profile headers
     * - Navigation bar user information
     * - Event organizer displays
     * 
     * FALLBACK BEHAVIOR:
     * - Returns empty string if no user is logged in
     * - Prevents null pointer exceptions in UI code
     * 
     * @return String - The current user's full name, or empty string if not logged in
     */
    public static String getCurrentUserName() {
        return currentUser != null ? currentUser.getFullName() : "";
    }

    /**
     * SELECTED EVENT TRACKING - Manages event selection for navigation
     * 
     * This static variable stores the currently selected event when users
     * navigate between event-related screens. It allows the application
     * to maintain context about which event the user is working with.
     * 
     * USE CASES:
     * - Event details page navigation
     * - Event editing and management
     * - Session scheduling within events
     * - Event registration workflows
     */
    private static Event selectedEvent;
    
    /**
     * setSelectedEvent() - Marks an event as currently selected
     * 
     * This method is called when a user selects an event from a list
     * or navigates to an event-specific screen. It maintains the
     * context of which event the user is currently working with.
     * 
     * NAVIGATION SCENARIOS:
     * - User clicks on event in event list
     * - Navigation from dashboard to event details
     * - Event management operations
     * - Session creation within events
     * 
     * @param event - The Event object that is now selected
     */
    public static void setSelectedEvent(Event event) {
        selectedEvent = event;
    }
    
    /**
     * getSelectedEvent() - Retrieves the currently selected event
     * 
     * This method provides access to the selected event throughout
     * the application. It's used to display event details, manage
     * event operations, and maintain navigation context.
     * 
     * COMMON USES:
     * - Display event information in headers
     * - Pre-populate forms with event data
     * - Navigate to event-specific screens
     * - Perform event-related operations
     * 
     * @return Event - The currently selected event, or null if none selected
     */
    public static Event getSelectedEvent() {
        return selectedEvent;
    }
    
    /**
     * clearSelectedEvent() - Removes the currently selected event
     * 
     * This method clears the event selection, typically called when
     * the user navigates away from event-specific screens or when
     * the event context is no longer needed.
     * 
     * CLEANUP SCENARIOS:
     * - User returns to main dashboard
     * - Navigation to non-event screens
     * - Logout or session expiration
     * - Error conditions that invalidate selection
     */
    public static void clearSelectedEvent() {
        selectedEvent = null;
    }
} 