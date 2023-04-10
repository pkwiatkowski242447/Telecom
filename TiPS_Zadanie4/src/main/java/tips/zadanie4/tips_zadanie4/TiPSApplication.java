package tips.zadanie4.tips_zadanie4;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TiPSApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StageSetup.buildStage(stage, "main-form.fxml","TiPS - Zadanie 4.");
    }

    public static void main(String[] args) {
        launch();
    }
}