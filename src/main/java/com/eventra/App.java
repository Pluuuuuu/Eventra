package com.eventra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Welcome.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Eventra");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        Db.close();
    }

    public static void main(String[] args) {
        launch();
    }
}
