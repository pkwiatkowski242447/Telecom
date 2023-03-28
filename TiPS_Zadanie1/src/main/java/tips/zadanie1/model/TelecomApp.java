package tips.zadanie1.model;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TelecomApp extends Application {

    /*
        Klasa odpowiedzialna za uruchomienie interfejsu graficznego użytkownika.



     */

    /*
        Metoda powodująca odpowiedzialna za zbudowanie okna z graficznym interfejsem użytkownika.
     */

    @Override
    public void start(Stage stage) throws IOException {
        StageSetup.buildStage(stage, "main-form.fxml", "TiPS - Zadanie 1");
    }

    /*
        Metoda powodująca uruchomienie aplikacji, działa ona do momentu zakończenia wykonania programu
        przez użytkownika.
     */

    public static void main(String[] args) {
        launch();
    }
}