package tips.zadanie1.tips_zadanie1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import tips.zadanie1.tips_zadanie1.excpetions.IOManagerSaveException;

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

    private void throwAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    public void initialize() {
        List<String> options = new ArrayList<>();
        parityBits.getItems().addAll("4","8");
    }

    @FXML
    public void readFile() {
        String pathToFile;
        String someTextFromFile = "";
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (!pathToFile.isEmpty() && pathToFile != null) {
            IOManager ioManager = new IOManager(pathToFile);
            someTextFromFile = ioManager.readFromFile(pathToFile);
            textInput.setText(someTextFromFile);
        }
    }

    @FXML
    public void saveFile() throws IOManagerSaveException {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            IOManager ioManager = new IOManager(file.getAbsolutePath());
            String textFromInput = textInput.getText();
            ioManager.saveToFile(textFromInput);
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
            someTextFromFile = ioManager.readBytesFromFile(pathToFile);
            byte[] binaryRepresentation = MessRepair.stringTo8BitBinaryConversion(someTextFromFile);
            byte[] correctChars = MessRepair.changeNumbersToCharValues(binaryRepresentation);
            textInput.setText(new String(correctChars, StandardCharsets.US_ASCII));
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
            ioManager.saveBytesToFile(stringRepresentation);
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
            someTextFromFile = ioManager.readBytesFromFile(pathToFile);
            if (parityBits.getSelectionModel().getSelectedItem() == null) {
                throwAlert("Brak wyboru liczby bitów parzystości", "Błąd","Proszę wybrać liczbę bitów parzystości.");
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
                throwAlert("Brak wyboru liczby bitów parzystości", "Błąd","Proszę wybrać liczbę bitów parzystości.");
            } else if (parityBits.getSelectionModel().getSelectedItem() == "4") {
                byte[] binaryRepresentation = MessRepair.stringTo8BitBinaryConversion(textFromInput);
                byte[] encodedRepresentation = MessRepair.add4ParityBits(binaryRepresentation);
                byte[] textRepresentation = MessRepair.eightBitBinaryToStringConversion(encodedRepresentation);
                ioManager.saveBytesToFile(textRepresentation);
            } else {
                byte[] binaryRepresentation = MessRepair.stringTo8BitBinaryConversion(textFromInput);
                byte[] encodedRepresentation = MessRepair.add8ParityBits(binaryRepresentation);
                byte[] textRepresentation = MessRepair.eightBitBinaryToStringConversion(encodedRepresentation);
                ioManager.saveBytesToFile(textRepresentation);
            }
        }
    }

}