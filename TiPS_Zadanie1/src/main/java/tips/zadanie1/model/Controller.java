package tips.zadanie1.model;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import tips.zadanie1.exceptions.IOManagerReadException;
import tips.zadanie1.exceptions.IOManagerWriteException;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    /*
        Referencja do pola tekstowego przechowującego tekst do zakodowania z odpowiednią liczbą bitów parzystości.
        Użytkownik może wpisywać taki tekst samodzielnie lub też dokonać jego wczytania z pliku.
     */
    @FXML
    private TextArea textInput;

    /*
        Referencja do ChoiceBox'a przechowującego liczbę bitów parzystości, które są dodawane w procesie
        kodowania i które są usuwane w wyniku dekodowania.
     */
    @FXML
    private ChoiceBox parityBits;

    /*
        Referencja do instancji klasy, służącej do kodowania i dekodowania pola tekstowego, (tj. textInput) zawartości.
     */

    @FXML
    private CheckBox binaryContent;

    private MessageRepairClass MessRepair = new MessageRepairClass();

    /*
        Metoda wykorzystywana do wyświetlania użytkownikowi okna z wiadomością:

        @ typeOfAlert   -> typ wiadomości
        @ title         -> tytuł okna z wiadomością (pojawia się na pasku okna)
        @ header        -> nagłówek wiadomości
        @ content       -> właściwa treść wiadomości
     */

    private void throwAlert(Alert.AlertType typeOfAlert, String title, String header, String content) {
        Alert alert = new Alert(typeOfAlert);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    /*
        Metoda inizjalizująca niektóre pola formularza.
     */

    @FXML
    public void initialize() {
        List<String> options = new ArrayList<>();
        parityBits.getItems().addAll("4","8");
        textInput.setPromptText("Wprowadź tekst do zakodowania lub odkodowania...");

    }

    /*
        Metoda, przy pomocy której możliwe jest wczytanie zawartości z pliku, w postaci tekstu, do pola tekstowego
        w graficznym interfejsie użytkownika.

        Po naciśnięciu odpowiedniego przycisku (tj. "Wczytaj z pliku") wyświetlane jest okno, w którym użytkownik
        może dokonać wyboru pliku, z systemu plików, z którego zczytana zawartość zostanie wczytana do pola tekstowego.

        W przypadku błędów operacji odczytu użytkownikowi zostanie wyświetlony odpowiedni komunikat przy pomocy metody
        throwAlert().
     */

    @FXML
    public void readFile() {
        String pathToFile;
        byte[] someTextFromFile = null;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (!pathToFile.isEmpty() && pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            try {
                someTextFromFile = ioManager.readBytesFromFile(pathToFile);
                if (binaryContent.isSelected()) {
                    textInput.setText(new String(HelperClass.convertByteArrayToHex(someTextFromFile), StandardCharsets.UTF_8));
                } else {
                    textInput.setText(new String(someTextFromFile, StandardCharsets.UTF_8));
                }
            } catch (IOManagerReadException readExc) {
                String title = "Błąd";
                String header = "Błąd operacji odczytu z pliku";
                String content = readExc.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    /*
        Metoda, przy pomocy której możliwe jest zapisanie zawartości z pola tekstowego, w postaci tekstu,
        do pliku w systemie plików.

        Po naciśnięciu odpowiedniego przycisku (tj. "Zapisz do pliku") wyświetlane jest okno, w którym użytkownik
        może dokonać wyboru lokalizacji, w której zostanie utworzony plik, którego zawartość zczytana
        zostanie z pola tekstowego.

        W przypadku błędów operacji zapisu użytkownikowi zostanie wyświetlony odpowiedni komunikat przy pomocy metody
        throwAlert().
     */
    @FXML
    public void saveFile() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            byte[] textFromInput;
            if (binaryContent.isSelected()) {
                textFromInput = HelperClass.convertHexToByteArray(textInput.getText().getBytes(StandardCharsets.UTF_8));
            } else {
                textFromInput = textInput.getText().getBytes(StandardCharsets.UTF_8);
            }
            try {
                ioManager.saveBytesToFile(textFromInput);
            } catch (IOManagerWriteException writeExc) {
                String title = "Błąd";
                String header = "Błąd operacji zapisu pliku";
                String content = writeExc.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    /*
        Metoda wykorzystywana do binarnej edycji zawartości wskazanego pliku.

        Działanie tej metody polega na tym, że użytkownik w wyniku naciśnięcia przycisku "Edycja binarna" ma
        zaprezentowane okienko, gdzie z systemu plików może wybrać plik, którego zawartość jest wpisywana do
        pola tekstowego jako ciąg znaków binarnych.

        W sytuacji, w której dochodzi do błędu odczytania z pliku to wtedy użytkownik jest o nim powiadomiony poprzez
        wyświetlenie odpowiedniego okna z komunikatem.

        W sytuacji, w której plik zostanie zdekodowany z liczbą bitów parzystości nieprawidłową z liczbą bitów
        parzystosci wykorzystywaną do zakodowania plików, to zostanie on zdekodowany w sposób nieprawidłowy.
     */

    @FXML
    public void editBinaryString() {
        String pathToFile;
        byte[] someTextFromFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (!pathToFile.isEmpty() && pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            try {
                someTextFromFile = ioManager.readBytesFromFile(pathToFile);
                byte[] binaryRepresentation = MessRepair.stringTo8BitBinaryConversion(someTextFromFile);
                byte[] correctChars = MessRepair.changeNumbersToCharValues(binaryRepresentation);
                textInput.setText(new String(correctChars, StandardCharsets.US_ASCII));
            } catch (IOManagerReadException readExc) {
                String title = "Błąd";
                String header = "Błąd operacji odczytu pliku";
                String content = readExc.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    /*
        Metoda ta jest wykorzysytwana do zapisywania zmodyfikowanego ciągu znaków binarnych do plików.

        W tym przypadku kodowanie (tj. stosowana liczba bitów parzystości) jest zgodne z wybraną z menu liczbą
        bitów parzystości.

        W sytuacji wystąpienia błędów zapisu pliku, użytkownikowi zostanie wyświetlone okno z odpowiednim komunikatem.
     */

    @FXML
    public void saveBinaryChanges() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] textFromInput = textInput.getText().getBytes();
            byte[] correctNumbers = MessRepair.changeCharValuesToNumbers(textFromInput);
            byte[] stringRepresentation = MessRepair.eightBitBinaryToStringConversion(correctNumbers);
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            try {
                ioManager.saveBytesToFile(stringRepresentation);
            } catch (IOManagerWriteException writeExc) {
                String title = "Błąd";
                String header = "Błąd operacji zapisu do pliku";
                String content = writeExc.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    /*
        Metoda, przy pomocy której możliwe jest wczytanie zawartości z pliku, który został zakodowany z odpowiednią ilością
        bitów parzystości, w postaci tekstu, do pola tekstowego w graficznym interfejsie użytkownika.

        Po naciśnięciu odpowiedniego przycisku (tj. "Wczytaj zakodowany tekst") wyświetlane jest okno, w którym użytkownik
        może dokonać wyboru pliku, z systemu plików, z którego zczytana zawartość zostanie wczytana do pola tekstowego.
        Ważne jest to aby, przed naciśnięciem tej opcji wybrać ilość bitów parzystości, jakie zostały dodane do
        pliku w wyniku kodowania, gdyż w przeciwym przypadku wyświetlone zostanie okno z wiadomością dotycząca błedu.
        Wówczas zawartość z pliku zostanie odkodowna z usunięciem podanej liczby bitów parzystości.

        W przypadku błędów operacji odczytu użytkownikowi zostanie wyświetlony odpowiedni komunikat przy pomocy metody
        throwAlert().
     */

    @FXML
    public void readEncodedText() {
        String pathToFile;
        byte[] someTextFromFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (!pathToFile.isEmpty() && pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            try {
                someTextFromFile = ioManager.readBytesFromFile(pathToFile);
                if (parityBits.getSelectionModel().getSelectedItem() == null) {
                    throwAlert(Alert.AlertType.ERROR,"Brak wyboru liczby bitów parzystości", "Błąd","Proszę wybrać liczbę bitów parzystości.");
                } else if (parityBits.getSelectionModel().getSelectedItem() == "4") {
                    byte[] binaryRepresentation = MessRepair.stringTo8BitBinaryConversion(someTextFromFile);
                    byte[] decodedRepresentation = MessRepair.correctGivenMessage(binaryRepresentation, 4, ErrorsRepairClass.oneErrorMatrix);
                    byte[] textRepresentation = MessRepair.eightBitBinaryToStringConversion(decodedRepresentation);
                    if (binaryContent.isSelected()) {
                        textInput.setText(new String(HelperClass.convertByteArrayToHex(textRepresentation), StandardCharsets.US_ASCII));
                    } else {
                        textInput.setText(new String(textRepresentation, StandardCharsets.US_ASCII));
                    }
                } else {
                    byte[] binaryRepresentation = MessRepair.stringTo8BitBinaryConversion(someTextFromFile);
                    byte[] decodedRepresentation = MessRepair.correctGivenMessage(binaryRepresentation, 8, ErrorsRepairClass.twoErrorMatrix);
                    byte[] textRepresentation = MessRepair.eightBitBinaryToStringConversion(decodedRepresentation);
                    if (binaryContent.isSelected()) {
                        textInput.setText(new String(HelperClass.convertByteArrayToHex(textRepresentation), StandardCharsets.US_ASCII));
                    } else {
                        textInput.setText(new String(textRepresentation, StandardCharsets.US_ASCII));
                    }
                }
            } catch (IOManagerReadException readException) {
                String title = "Błąd";
                String header = "Błąd operacji odczytu pliku";
                String content = readException.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    /*
        Metoda, przy pomocy której możliwe jest zapisanie zawartości z pola tekstowego w graficznym interfejsie użytkownika
        do pliku, który został zakodowany z odpowiednią ilością bitów parzystości.

        Po naciśnięciu odpowiedniego przycisku (tj. "Zapisz zakodowany tekst") wyświetlane jest okno, w którym użytkownik
        może dokonać wyboru lokalizacji w systemie plików, w której zostanie utworzony plik, z zakodowaną wiadomością zczytaną
        z pola tekstowego w GUI. Ważne jest to aby, przed naciśnięciem tej opcji wybrać ilość bitów parzystości, jakie
        zostały dodane do pliku w wyniku kodowania, gdyż w przeciwym przypadku wyświetlone zostanie okno z wiadomością
        dotycząca błedu. Wówczas zawartość z pliku zostanie zakodowana z dodaniem podanej liczby bitów parzystości.

        W przypadku błędów operacji zapisu użytkownikowi zostanie wyświetlony odpowiedni komunikat przy pomocy metody
        throwAlert().
     */

    @FXML
    public void saveEncodedText() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            byte[] textFromInput;
            if (binaryContent.isSelected()) {
                textFromInput = HelperClass.convertHexToByteArray(textInput.getText().getBytes());
            } else {
                textFromInput = textInput.getText().getBytes();
            }
            if (parityBits.getSelectionModel().getSelectedItem() == null) {
                throwAlert(Alert.AlertType.ERROR, "Brak wyboru liczby bitów parzystości", "Błąd","Proszę wybrać liczbę bitów parzystości.");
            } else if (parityBits.getSelectionModel().getSelectedItem() == "4") {
                byte[] binaryRepresentation = MessRepair.stringTo8BitBinaryConversion(textFromInput);
                byte[] encodedRepresentation = MessRepair.add4ParityBits(binaryRepresentation);
                byte[] textRepresentation = MessRepair.eightBitBinaryToStringConversion(encodedRepresentation);
                try {
                    ioManager.saveBytesToFile(textRepresentation);
                } catch (IOManagerWriteException writeException) {
                    String title = "Błąd";
                    String header = "Błąd operacji zapisu do pliku";
                    String content = writeException.getMessage();
                    throwAlert(Alert.AlertType.ERROR, title, header, content);
                }
            } else {
                byte[] binaryRepresentation = MessRepair.stringTo8BitBinaryConversion(textFromInput);
                byte[] encodedRepresentation = MessRepair.add8ParityBits(binaryRepresentation);
                byte[] textRepresentation = MessRepair.eightBitBinaryToStringConversion(encodedRepresentation);
                try {
                    ioManager.saveBytesToFile(textRepresentation);
                } catch (IOManagerWriteException writeException) {
                    String title = "Błąd";
                    String header = "Błąd operacji zapisu do pliku";
                    String content = writeException.getMessage();
                    throwAlert(Alert.AlertType.ERROR, title, header, content);
                }
            }
        }
    }

    /*
        Metoda wykorzystywana do stworzenia wyskakującego okiena, w których znajdują się dane twórców
        programu tj. imię, nazwisko oraz numer indeksu.
     */

    @FXML
    public void showAuthors() {
        showPupUpWindow("Autorzy programu", "Aleksander Janicki 242405\nPiotr Kwiatkowski 242447");
    }

    /*
        Metoda wykorzysytwana do wyświetlania wyskakującego okienka.

        @ title     -> ciąg znaków wyświetlanych na pasku okienka
        @ content   -> zawartość przenoszonej wiadomości
     */

    private void showPupUpWindow(String title, String content) {
        Dialog<String> popUpWin = new Dialog<>();
        popUpWin.setTitle(title);
        popUpWin.setContentText(content);
        ButtonType closeWindow = new ButtonType("Zamknij", ButtonBar.ButtonData.CANCEL_CLOSE);
        popUpWin.getDialogPane().getButtonTypes().add(closeWindow);
        popUpWin.show();
    }
}