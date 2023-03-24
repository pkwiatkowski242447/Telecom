module tips.zadanie1.tips_zadanie1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens tips.zadanie1.model to javafx.fxml;
    exports tips.zadanie1.model;
}