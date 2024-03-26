package tips.zadanie2.tips_zadanie2;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TiPSApplication extends Application {

    /*
        @ Method: start()

        @ Parameters:

        * stage -> stage connected to window in operating system.

        @ Description: This method is used for starting entire application in GUI, by loading
        correct FXML file, and giving it correct title.
     */

    @Override
    public void start(Stage stage) throws IOException {
        StageSetup.buildStage(stage, "main-form.fxml", "TiPS - Zadanie 2.");
    }

    /*
        @ Method: main()

        @ Parameters:

        * args -> starting parameters of the program.

        @ Description: This method is used for launching GUI program.
     */

    public static void main(String[] args) {
        launch();
    }
}