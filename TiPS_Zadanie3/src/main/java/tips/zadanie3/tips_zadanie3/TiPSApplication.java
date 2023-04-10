package tips.zadanie3.tips_zadanie3;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TiPSApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StageSetup.buildStage(stage, "main-form.fxml", "TiPS - Zadanie 3.");
    }

    public static void main(String[] args) {
        launch();
    }
}