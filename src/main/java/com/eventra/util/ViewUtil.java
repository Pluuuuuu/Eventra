package com.eventra.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Window;
import javafx.stage.Stage;

public class ViewUtil {
    public static void switchTo(String fxmlFile, Window window) {
        try {
            Parent pane = FXMLLoader.load(
                    ViewUtil.class.getResource("/fxml/" + fxmlFile + ".fxml"));
            window.getScene().setRoot(pane);
            
            // Resize window for login page to accommodate two-column design
            if (fxmlFile.equals("Login")) {
                Stage stage = (Stage) window;
                stage.setMinWidth(1000);
                stage.setMinHeight(600);
                stage.setWidth(1200);
                stage.setHeight(700);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
