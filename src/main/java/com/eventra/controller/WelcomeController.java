package com.eventra.controller;

import com.eventra.util.ViewUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WelcomeController {
    @FXML private ImageView logoView;

    @FXML
    public void initialize() {
        // load logo.png from resources/images/
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png.png"));
            logoView.setImage(logo);
        } catch (Exception e) {
            System.err.println("Warning: Could not load logo image: " + e.getMessage());
            // Continue without the logo
        }
    }

    @FXML
    private void onWelcome(ActionEvent event) {
        // navigate to Login screen (we'll create Login.fxml next)
        ViewUtil.switchTo("Login", logoView.getScene().getWindow());
    }
}
