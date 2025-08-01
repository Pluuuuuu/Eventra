package com.eventra.util;

import com.eventra.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Window;

public class ViewUtil {
    public static void switchTo(String fxmlFile, Window window) {
        try {
            Parent pane = FXMLLoader.load(
                    ViewUtil.class.getResource("/fxml/" + fxmlFile + ".fxml"));
            window.getScene().setRoot(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Navigate to the appropriate dashboard based on user role
     */
    public static void switchToRoleDashboard(Window window) {
        User currentUser = SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            switch (currentUser.getRoleTypeId()) {
                case 1: // SuperAdmin
                    switchTo("SuperAdminDashboard", window);
                    break;
                case 2: // Admin
                    switchTo("AdminDashboard", window);
                    break;
                default: // Staff and Attendees
                    switchTo("Dashboard", window);
                    break;
            }
        } else {
            // No user logged in, redirect to login
            switchTo("Login", window);
        }
    }
}
