package tips.zadanie3.tips_zadanie3;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageSetup {

    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        StageSetup.primaryStage = stage;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static Parent fxmlFileLoad(String fxmlFromPath) throws IOException {
        return new FXMLLoader(StageSetup.class.getResource(fxmlFromPath)).load();
    }

    public static void buildStage(String fxmlFormPath) throws IOException {
        primaryStage.setScene(new Scene(fxmlFileLoad(fxmlFormPath)));
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void buildStage(String fxmlFormPath, String title) throws IOException {
        primaryStage.setScene(new Scene(fxmlFileLoad(fxmlFormPath)));
        primaryStage.setTitle(title);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void buildStage(Stage stage, String fxmlFormPath, String title) throws IOException {
        setStage(stage);
        primaryStage.setScene(new Scene(fxmlFileLoad(fxmlFormPath)));
        primaryStage.setTitle(title);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
