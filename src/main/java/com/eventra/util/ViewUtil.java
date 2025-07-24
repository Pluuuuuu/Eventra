package com.eventra.util;

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
}
