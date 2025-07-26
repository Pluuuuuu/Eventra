package com.eventra.util;

import com.eventra.model.User;

public class SessionManager {
    private static User currentUser;
    
    /**
     * Set the current logged-in user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    /**
     * Get the current logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if a user is currently logged in
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Clear the current user session (logout)
     */
    public static void logout() {
        currentUser = null;
    }
    
    /**
     * Get the current user's ID
     */
    public static int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }
    
    /**
     * Get the current user's full name
     */
    public static String getCurrentUserName() {
        return currentUser != null ? currentUser.getFullName() : "";
    }
} 