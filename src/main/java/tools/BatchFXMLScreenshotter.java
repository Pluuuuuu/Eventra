package tools;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class BatchFXMLScreenshotter extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Path fxmlDir = Paths.get("src/main/resources/fxml"); // adjust if yours differs
        Path outDir  = Paths.get("screenshots");
        Files.createDirectories(outDir);

        List<Path> fxmlFiles = Files.walk(fxmlDir)
                .filter(p -> p.toString().endsWith(".fxml"))
                .collect(Collectors.toList());

        for (Path fxml : fxmlFiles) {
            Parent root = FXMLLoader.load(fxml.toUri().toURL());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show(); // needs to show before snapshot

            WritableImage img = scene.snapshot(null);
            String name = fxml.getFileName().toString().replace(".fxml", ".png");
            File out = outDir.resolve(name).toFile();
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", out);

            System.out.println("Saved: " + out.getAbsolutePath());
        }
        stage.close();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
} 