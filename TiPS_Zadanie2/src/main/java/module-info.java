module tips.zadanie2.tips_zadanie2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens tips.zadanie2.tips_zadanie2 to javafx.fxml;
    exports tips.zadanie2.tips_zadanie2;
}