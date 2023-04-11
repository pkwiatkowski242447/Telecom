module tips.zadanie2.tips_zadanie2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires org.apache.commons.lang3;
    exports tips.zadanie2.tips_zadanie2.utils;

    opens tips.zadanie2.tips_zadanie2 to javafx.fxml;
    exports tips.zadanie2.tips_zadanie2;
}