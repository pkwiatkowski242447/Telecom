package tips.zadanie1.tips_zadanie1;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TelecomApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StageSetup.buildStage(stage, "main-form.fxml", "TiPS - Zadanie 1");
    }

    public static void main(String[] args) {
        launch();
    }
}