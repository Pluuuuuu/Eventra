/**
 * View Utility (ViewUtil.java)
 * 
 * This class provides utility methods for managing screen navigation and view switching
 * in the Eventra application. Think of it as the "navigator" that helps users move
 * between different screens and ensures they see the appropriate content.
 * 
 * WHAT THIS CLASS DOES:
 * - Handles screen transitions between different FXML views
 * - Manages role-based navigation to appropriate dashboards
 * - Provides centralized view switching functionality
 * - Ensures consistent navigation behavior across the application
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - FXML Loading: Dynamically loads different screen layouts
 * - Scene Management: Updates the current window's content
 * - Role-Based Routing: Directs users to appropriate dashboards
 * - Error Handling: Gracefully handles navigation failures
 * 
 * NAVIGATION PATTERNS:
 * - Direct navigation to specific screens
 * - Role-based automatic routing
 * - Fallback navigation for unauthenticated users
 * - Seamless screen transitions
 * 
 * TECHNICAL IMPLEMENTATION:
 * - Uses JavaFX FXMLLoader for dynamic view loading
 * - Updates scene root for immediate screen changes
 * - Maintains window context during navigation
 * - Provides centralized navigation logic
 */
package com.eventra.util;

import com.eventra.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Window;

public class ViewUtil {
    
    /**
     * switchTo() - Navigates to a specific screen by FXML filename
     * 
     * This method loads a new FXML file and replaces the current screen content.
     * It's the primary method for screen navigation throughout the application.
     * 
     * HOW IT WORKS:
     * 1. Constructs the full path to the FXML file
     * 2. Loads the FXML using FXMLLoader
     * 3. Updates the current window's scene root
     * 4. Handles any loading errors gracefully
     * 
     * FXML FILE STRUCTURE:
     * - FXML files are stored in /src/main/resources/fxml/
     * - Method automatically adds .fxml extension
     * - Example: switchTo("Dashboard") loads Dashboard.fxml
     * 
     * ERROR HANDLING:
     * - Catches and logs any FXML loading errors
     * - Prevents application crashes from navigation failures
     * - Provides debugging information for developers
     * 
     * @param fxmlFile - The name of the FXML file (without .fxml extension)
     * @param window - The current application window to update
     */
    public static void switchTo(String fxmlFile, Window window) {
        try {
            // Load the FXML file from the resources directory
            // ViewUtil.class.getResource() finds the file relative to this class
            Parent pane = FXMLLoader.load(
                    ViewUtil.class.getResource("/fxml/" + fxmlFile + ".fxml"));
            
            // Replace the current scene content with the new FXML
            // This creates an immediate screen transition
            window.getScene().setRoot(pane);
            
        } catch (Exception e) {
            // Log the error for debugging purposes
            System.err.println("Error loading FXML file: " + fxmlFile);
            e.printStackTrace();
        }
    }
    
    /**
     * switchToRoleDashboard() - Automatically navigates to the appropriate dashboard
     * 
     * This method examines the current user's role and automatically redirects
     * them to the dashboard that matches their permission level. This ensures
     * users only see features they have access to.
     * 
     * ROLE-BASED ROUTING:
     * - SuperAdmin (Role 1): SuperAdminDashboard with system-wide management
     * - Admin (Role 2): AdminDashboard with event and user management
     * - Staff/Attendee (Role 3+): Regular Dashboard with limited features
     * 
     * SECURITY BENEFITS:
     * - Prevents unauthorized access to admin features
     * - Ensures users see appropriate functionality
     * - Maintains role-based access control
     * - Provides seamless user experience
     * 
     * FALLBACK BEHAVIOR:
     * - If no user is logged in, redirects to Login screen
     * - Handles edge cases gracefully
     * - Maintains application stability
     * 
     * @param window - The current application window to update
     */
    public static void switchToRoleDashboard(Window window) {
        // Get the currently logged-in user from the session manager
        User currentUser = SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            // User is logged in - route based on their role
            switch (currentUser.getRoleTypeId()) {
                case 1: // SuperAdmin - Full system access
                    System.out.println("Routing SuperAdmin to SuperAdminDashboard");
                    switchTo("SuperAdminDashboard", window);
                    break;
                    
                case 2: // Admin - Event and user management
                    System.out.println("Routing Admin to AdminDashboard");
                    switchTo("AdminDashboard", window);
                    break;
                    
                default: // Staff and Attendees - Limited features
                    System.out.println("Routing Staff/Attendee to regular Dashboard");
                    switchTo("Dashboard", window);
                    break;
            }
        } else {
            // No user logged in - redirect to login screen
            System.out.println("No user logged in, redirecting to Login");
            switchTo("Login", window);
        }
    }
}
