package tips.zadanie3.tips_zadanie3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import tips.zadanie3.tips_zadanie3.exceptions.IOManagerReadException;
import tips.zadanie3.tips_zadanie3.exceptions.IOManagerWriteException;
import tips.zadanie3.tips_zadanie3.model.HuffmanCoding;
import tips.zadanie3.tips_zadanie3.utils.Converter;
import tips.zadanie3.tips_zadanie3.utils.IOManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class UserActionController {

    /*
        @ Attributes:
     */
    @FXML
    private TextArea decodedTextArea;
    @FXML
    private TextArea encodedTextArea;
    @FXML
    private CheckBox isBinaryContent;
    @FXML
    private CheckBox encodedContent;
    @FXML
    private ComboBox chooseFunction;
    @FXML
    private Button startButton;
    @FXML
    private Button finishButton;

    private boolean serverFunctionality;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private Socket serverSideSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    // Used for decoding.
    private Map<Short, Integer> occurrenceMap;

    /*
        @ Methods:
     */

    @FXML
    public void initialize() {
        chooseFunction.getItems().addAll("Klient", "Serwer");
        chooseFunction.getSelectionModel().selectFirst();
        serverFunctionality = false;
        finishButton.setDisable(true);
    }

    @FXML
    public void showAuthors() {
        showPupUpWindow("Autorzy programu", "Aleksander Janicki 242405\nPiotr Kwiatkowski 242447");
    }

    private void throwAlert(Alert.AlertType typeOfAlert, String title, String header, String content) {
        Alert alert = new Alert(typeOfAlert);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    private void showPupUpWindow(String title, String content) {
        Dialog<String> popUpWin = new Dialog<>();
        popUpWin.setTitle(title);
        popUpWin.setContentText(content);
        ButtonType closeWindow = new ButtonType("Zamknij", ButtonBar.ButtonData.CANCEL_CLOSE);
        popUpWin.getDialogPane().getButtonTypes().add(closeWindow);
        popUpWin.show();
    }

    @FXML
    public void encodeText() {
        byte[] textToEncode;
        if (isBinaryContent.isSelected()) {
            textToEncode = Converter.convertHexadecimalToAscii(decodedTextArea.getText().getBytes(StandardCharsets.US_ASCII));
        } else {
            textToEncode = decodedTextArea.getText().getBytes(StandardCharsets.UTF_8);
        }
        HuffmanCoding huffmanCode = new HuffmanCoding();
        occurrenceMap = huffmanCode.findOccurrenceMap(textToEncode);
        byte[] encodedMessage = huffmanCode.encodeWithHuffmanEncoding(textToEncode, occurrenceMap);
        encodedTextArea.setText(new String(Converter.convertAsciiToHexadecimal(encodedMessage), StandardCharsets.US_ASCII));
    }

    @FXML
    public void decodeText() {
        byte[] textToDecode = Converter.convertHexadecimalToAscii(encodedTextArea.getText().getBytes(StandardCharsets.US_ASCII));
        HuffmanCoding huffmanCode = new HuffmanCoding();
        byte[] decodedMessage = huffmanCode.decodeWithHuffmanEncoding(textToDecode, occurrenceMap);
        if (isBinaryContent.isSelected()) {
            decodedTextArea.setText(new String(Converter.convertAsciiToHexadecimal(decodedMessage), StandardCharsets.US_ASCII));
        } else {
            decodedTextArea.setText(new String(decodedMessage, StandardCharsets.UTF_8));
        }
    }

    @FXML
    public void readDecodedFromAFile() {
        String pathToFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (!pathToFile.isEmpty() && pathToFile != null) {
            try {
                byte[] someTextFromFile = IOManager.readBytesFromAFile(pathToFile);
                if (isBinaryContent.isSelected() && encodedContent.isSelected()) {
                    encodedTextArea.setText(new String(Converter.convertAsciiToHexadecimal(someTextFromFile), StandardCharsets.US_ASCII));
                } else if (isBinaryContent.isSelected() && !encodedContent.isSelected()) {
                    decodedTextArea.setText(new String(Converter.convertAsciiToHexadecimal(someTextFromFile), StandardCharsets.US_ASCII));
                } else if (!isBinaryContent.isSelected() && encodedContent.isSelected()) {
                    encodedTextArea.setText(new String(Converter.convertAsciiToHexadecimal(someTextFromFile), StandardCharsets.US_ASCII));
                } else {
                    decodedTextArea.setText(new String(someTextFromFile, StandardCharsets.UTF_8));
                }
            } catch (IOManagerReadException readExc) {
                String title = "Błąd";
                String header = "Błąd operacji odczytu z pliku: " + pathToFile;
                String content = readExc.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    @FXML
    public void saveDecodedToAFile() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] textFromInput;
            if (isBinaryContent.isSelected() && encodedContent.isSelected()) {
                textFromInput = Converter.convertHexadecimalToAscii(encodedTextArea.getText().getBytes(StandardCharsets.US_ASCII));
            } else if (isBinaryContent.isSelected() && !encodedContent.isSelected()) {
                textFromInput = Converter.convertHexadecimalToAscii(decodedTextArea.getText().getBytes(StandardCharsets.US_ASCII));
            } else if (!isBinaryContent.isSelected() && encodedContent.isSelected()) {
                textFromInput = Converter.convertHexadecimalToAscii(encodedContent.getText().getBytes(StandardCharsets.US_ASCII));
            } else {
                textFromInput = decodedTextArea.getText().getBytes(StandardCharsets.UTF_8);
            }
            try {
                IOManager.writeBytesToAFile(file.getAbsolutePath(), textFromInput);
            } catch (IOManagerWriteException writeExc) {
                String title = "Błąd";
                String header = "Błąd operacji zapisu pliku: " + file.getName();
                String content = writeExc.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    @FXML
    public void sendTextToOtherHost() {
        try {
            OutputStream outputStream;
            if (serverFunctionality) {
                outputStream = serverSideSocket.getOutputStream();
            } else {
                outputStream = clientSocket.getOutputStream();
            }
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            HuffmanCoding huffmanCode = new HuffmanCoding();
            byte[] textToEncode = null;
            if (isBinaryContent.isSelected()) {
                textToEncode = Converter.convertHexadecimalToAscii(decodedTextArea.getText().getBytes(StandardCharsets.US_ASCII));
            } else {
                textToEncode = decodedTextArea.getText().getBytes(StandardCharsets.UTF_8);
            }
            occurrenceMap = huffmanCode.findOccurrenceMap(textToEncode);
            byte[] encodedText = huffmanCode.encodeWithHuffmanEncoding(textToEncode, occurrenceMap);
            encodedTextArea.setText(new String(Converter.convertAsciiToHexadecimal(encodedText), StandardCharsets.US_ASCII));
            oos.writeObject(occurrenceMap);
            oos.writeObject(encodedText);
            oos.flush();
        } catch (IOException ioException) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Nie udało się przesłać danych do serwera.");
        }
    }

    @FXML
    public void startOperation() {
        if (!serverFunctionality) {
            TextInputDialog textInput = new TextInputDialog();
            textInput.setTitle("Wprowadź dane");
            textInput.setHeaderText("Wprowadź adres IP serwera, z którym się chcesz połączyć");
            textInput.setContentText("Adres IP: ");
            String serverIpAddress = textInput.showAndWait().get();
            textInput = new TextInputDialog();
            textInput.setTitle("Wprowadź dane");
            textInput.setHeaderText("Wprowadź numer portu, na którym działa serwera, z którym się chcesz połączyć");
            textInput.setContentText("Numer portu: ");
            String serverPortNumber = textInput.showAndWait().get();

            try {
                clientSocket = new Socket(serverIpAddress, Integer.parseInt(serverPortNumber));
                startButton.setDisable(true);
                finishButton.setDisable(false);
            } catch (IOException ioException) {
                throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Nie powiodło się otworzenie gniazda: " + serverIpAddress + ":" + serverPortNumber);
            } catch (IllegalArgumentException argExc) {
                throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Podany numer portu tj. " + serverPortNumber + " znajduje się poza zakresem dopuszczalnych numerów portów.");
            }
        } else {
            TextInputDialog textInput = new TextInputDialog();
            textInput.setTitle("Wprowadź dane");
            textInput.setHeaderText("Wprowadź dane potrzebne do nawiązania połączenia");
            textInput.setContentText("Wprowadź numer portu, na którym będzie działać serwer: ");
            String serverPortNumber = textInput.showAndWait().get();

            try {
                serverSocket = new ServerSocket(Integer.parseInt(serverPortNumber));
                startButton.setDisable(true);
                finishButton.setDisable(false);
                serverSideSocket = serverSocket.accept();
            } catch (IOException ioException) {
                throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Nie powiodło się otworzenie portu o numerze: " + serverPortNumber);
            } catch (IllegalArgumentException argExc) {
                throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Podany numer portu tj. " + serverPortNumber + " znajduje się poza zakresem dopuszczalnych numerów portów.");
            }
        }
    }

    @FXML
    public void finishOperation() {
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (serverFunctionality && serverSocket != null) {
                serverSocket.close();
            } else if (!serverFunctionality && clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException ioException) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Błąd krytyczny", "Nie powiodło się zamknięcie gniazda.");
        }
        startButton.setDisable(false);
        finishButton.setDisable(true);
    }

    @FXML
    public void changeFunction() {
        finishOperation();
        String function = chooseFunction.getSelectionModel().getSelectedItem().toString();
        if (function.equals("Klient")) {
            serverFunctionality = false;
        } else {
            serverFunctionality = true;
        }
        startButton.setDisable(false);
        finishButton.setDisable(true);
    }

    @FXML
    public void receiveDataFromSecondHost() {
        try {
            InputStream inputStream;
            if (serverFunctionality) {
                inputStream = serverSideSocket.getInputStream();
            } else {
                inputStream = clientSocket.getInputStream();
            };
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            occurrenceMap = (Map<Short, Integer>) ois.readObject();
            byte[] savedContent = (byte[]) ois.readObject();
            encodedTextArea.setText(new String(Converter.convertAsciiToHexadecimal(savedContent), StandardCharsets.US_ASCII));
            HuffmanCoding huffmanCode = new HuffmanCoding();
            byte[] decodedText = huffmanCode.decodeWithHuffmanEncoding(savedContent, occurrenceMap);
            if (isBinaryContent.isSelected()) {
                decodedTextArea.setText(new String(Converter.convertAsciiToHexadecimal(decodedText), StandardCharsets.US_ASCII));
            } else {
                decodedTextArea.setText(new String(decodedText, StandardCharsets.UTF_8));
            }
        } catch (IOException ioException) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Nie udało się odebrać danych od klienta.");
        } catch (ClassNotFoundException cnfExc) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Nie ma takiej klasy.");
        }
    }
}