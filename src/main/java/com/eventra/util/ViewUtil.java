package com.eventra.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Window;
import javafx.stage.Stage;

public class ViewUtil {
    public static void switchTo(String fxmlFile, Window window) {
        try {
            System.out.println("ViewUtil: Loading FXML file: " + fxmlFile);
            Parent pane = FXMLLoader.load(
                    ViewUtil.class.getResource("/fxml/" + fxmlFile + ".fxml"));
            System.out.println("ViewUtil: FXML loaded successfully, setting root...");
            window.getScene().setRoot(pane);
            System.out.println("ViewUtil: Root set successfully");
            
            // Resize window for login page to accommodate two-column design
            if (fxmlFile.equals("Login")) {
                Stage stage = (Stage) window;
                stage.setMinWidth(1000);
                stage.setMinHeight(600);
                stage.setWidth(1200);
                stage.setHeight(700);
            }
        } catch (Exception e) {
            System.err.println("ViewUtil: Error switching to " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
