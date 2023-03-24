package tips.zadanie1.model;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import tips.zadanie1.exceptions.IOManagerGeneralException;
import tips.zadanie1.exceptions.IOManagerReadException;
import tips.zadanie1.exceptions.IOManagerWriteException;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private TextArea textInput;

    @FXML
    private ChoiceBox parityBits;

    private MessageRepairClass MessRepair = new MessageRepairClass();

    private void throwAlert(Alert.AlertType typeOfAlert, String title, String header, String content) {
        Alert alert = new Alert(typeOfAlert);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    public void initialize() {
        List<String> options = new ArrayList<>();
        parityBits.getItems().addAll("4","8");
        textInput.setPromptText("Wprowadź tekst do zakodowania lub odkodowania...");

    }

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
                textInput.setText(new String(someTextFromFile, StandardCharsets.UTF_8));
            } catch (IOManagerReadException readExc) {
                String title = "Błąd";
                String header = "Błąd operacji odczytu z pliku";
                String content = readExc.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    @FXML
    public void saveFile() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            byte[] textFromInput = textInput.getText().getBytes(StandardCharsets.UTF_8);
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

    @FXML
    public void editBinaryString() {
        String pathToFile;
        byte[] someTextFromFile = null;
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

    @FXML
    public void readEncodedText() {
        String pathToFile;
        byte[] someTextFromFile = null;
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
                    textInput.setText(new String(textRepresentation, StandardCharsets.US_ASCII));
                } else {
                    byte[] binaryRepresentation = MessRepair.stringTo8BitBinaryConversion(someTextFromFile);
                    byte[] decodedRepresentation = MessRepair.correctGivenMessage(binaryRepresentation, 8, ErrorsRepairClass.twoErrorMatrix);
                    byte[] textRepresentation = MessRepair.eightBitBinaryToStringConversion(decodedRepresentation);
                    textInput.setText(new String(textRepresentation, StandardCharsets.US_ASCII));
                }
            } catch (IOManagerReadException readException) {
                String title = "Błąd";
                String header = "Błąd operacji odczytu pliku";
                String content = readException.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    @FXML
    public void saveEncodedText() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            byte[] textFromInput = textInput.getText().getBytes();
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

    @FXML
    public void showAuthors() {
        showPupUpWindow("Autorzy programu", "Aleksander Janicki 242405\nPiotr Kwiatkowski 242447");
    }

    private void showPupUpWindow(String title, String content) {
        Dialog<String> popUpWin = new Dialog<>();
        popUpWin.setTitle(title);
        popUpWin.setContentText(content);
        ButtonType closeWindow = new ButtonType("Zamknij", ButtonBar.ButtonData.CANCEL_CLOSE);
        popUpWin.getDialogPane().getButtonTypes().add(closeWindow);
        popUpWin.show();
    }
}