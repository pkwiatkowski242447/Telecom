package tips.zadanie1.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageSetup {

    /*
        Ta klasa wykorzystywana jest to budowania okna zawierającego graficzny interfejs użytkownika.

        Na klasę składaja się przeciążona metoda buildStage(), która przyjumje różną liczbę paramterów, w zależności
        od potrzeb.

        Z powodu, że dane jest okno, w którym mogą się użytkownikowi wyświetlać elementy interfejsu - to trzeba również
        zachować referencję do tego okna (w postaci primaryStage)

        Metoda fxmlFileLoad() odpowiada za załadowanie odpowiedniego formularza, zawierającego kontrolki, pola tekstowe
        itd.
     */

    private static Stage primaryStage;

    public static Stage getStage() {
        return primaryStage;
    }

    private static void setStage(Stage stage) {
        StageSetup.primaryStage = stage;
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
