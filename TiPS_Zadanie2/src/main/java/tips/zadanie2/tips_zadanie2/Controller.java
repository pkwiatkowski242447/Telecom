package tips.zadanie2.tips_zadanie2;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.fazecast.jSerialComm.SerialPort;
import javafx.stage.FileChooser;
import tips.zadanie2.tips_zadanie2.exceptions.*;
import tips.zadanie2.tips_zadanie2.model.XModemClass;
import tips.zadanie2.tips_zadanie2.model.XModemDefinitions;
import tips.zadanie2.tips_zadanie2.utils.Converter;
import tips.zadanie2.tips_zadanie2.utils.IOManager;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private ComboBox portIdentifier;
    @FXML
    private ComboBox baudRate;
    @FXML
    private ComboBox dataBits;
    @FXML
    private ComboBox stopBits;
    @FXML
    private ComboBox parityBits;
    @FXML
    private CheckBox isContentBinary;
    @FXML
    private TextArea contentTextArea;
    @FXML
    private ComboBox checksumSelection;
    @FXML
    private Button openPortButton;
    @FXML
    private Button closePortButton;
    @FXML
    private TextField portStatus;

    /*
        @ Upper parameters are used to address combo-boxes, checkboxes, fieldTexts and much more in order
        to change their appearance or contents.
     */

    /*
        @ Attribute: selectedSerialPort

        @ Use: It main use is to be able to address serial port selected by the user without using any getters
        since it caused problem in previous version of this program.
     */
    private SerialPort selectedSerialPort;

    /*
        @ Attribute: withAddedCRC16

        @ Use: It is used in methods below in order to check whether user chose CRC checksum or
        algebraic modulo 256 checksum.
     */
    private boolean withAddedCRC16;

    /*
        @ Method: initialize()

        @ Parameters: None

        @ Description: This method is called automatically, to initialize for instance, combo-boxes in GUI.
     */

    @FXML
    private void initialize() {
        SerialPort[] serialPortArray = SerialPort.getCommPorts();
        for (SerialPort portId : serialPortArray) {
            portIdentifier.getItems().add(portId.getSystemPortName());
        }
        baudRate.getItems().addAll("110", "300", "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200");
        dataBits.getItems().addAll("6", "7", "8");
        stopBits.getItems().addAll("1", "2");
        parityBits.getItems().addAll("NO_PARITY", "EVEN_PARITY", "ODD_PARITY", "MARK_PARITY", "SPACE_PARITY");
        checksumSelection.getItems().addAll("CRC16", "Suma algebraiczna");

        baudRate.getSelectionModel().select(5);
        dataBits.getSelectionModel().select(2);
        stopBits.getSelectionModel().selectFirst();
        parityBits.getSelectionModel().selectFirst();
        checksumSelection.getSelectionModel().selectFirst();
        changeCurrentChecksum();
    }

    /*
        @ Method: readContentFromSecondHost()

        @ Parameters: None

        @ Description: This method is used to initialize serial port communication, by sending NAK for one minute
        every 10 seconds. Then, if other side responds with packet, this method reads it and check whether the packet
        is correct (that goal is achieved through calculating checksums and check if they are the same).
     */

    @FXML
    public void readContentFromSecondHost() throws InterruptedException {
        try {
            System.out.println("Start of the method");
            int lastReadPacket = -1;
            int numberOfIterations = 0;
            int operationResult;
            XModemClass xModem;
            byte[] byteToBeSent = new byte[1];
            byte[] inputPacket;
            List<byte[]> listOfReceivedPackets = new ArrayList<>();
            boolean typeCRC;

            // Loop starting communication process, signaling whether message should be sent with CRC
            // or algebraic checksum.

            do {
                System.out.println("Checking if CRC is selected");
                if (withAddedCRC16) {
                    System.out.println("Sending C from XModemDefinitions");
                    byteToBeSent[0] = XModemDefinitions.C;
                    selectedSerialPort.writeBytes(byteToBeSent, 1);
                    System.out.println("C symbol sent");
                } else {
                    System.out.println("Sending NAK from XModemDefinitions");
                    byteToBeSent[0] = XModemDefinitions.NAK;
                    selectedSerialPort.writeBytes(byteToBeSent, 1);
                    System.out.println("NAK symbol sent");
                }

                // Waiting for 10 seconds, according to XModem documentation.

                System.out.println("Before sleep");
                Thread.sleep(10000);
                System.out.println("After sleep");

                // Trying to read a packet according to selected checksum option.

                if (withAddedCRC16) {
                    System.out.println("Declaration for incoming packet with CRC");
                    inputPacket = new byte[XModemDefinitions.PACKET_SIZE_CRC];
                    System.out.println("Reading incoming packet with CRC");
                    operationResult = selectedSerialPort.readBytes(inputPacket, inputPacket.length);
                    System.out.println("Read bytes: " + operationResult);
                    System.out.println("First symbol: " + inputPacket[0]);
                    for (byte oneByte : inputPacket) {
                        System.out.print(oneByte + " ");
                    }
                    System.out.println(" ");
                    System.out.println("Packet with CRC read");
                    typeCRC = true;
                } else {
                    System.out.println("Declaration for incoming packet no CRC");
                    inputPacket = new byte[XModemDefinitions.PACKET_SIZE_NO_CRC];
                    System.out.println("Reading incoming packet no CRC");
                    operationResult = selectedSerialPort.readBytes(inputPacket, inputPacket.length);
                    System.out.println("Read bytes: " + operationResult);
                    for (byte oneByte : inputPacket) {
                        System.out.print(oneByte + " ");
                    }
                    System.out.println(" ");
                    System.out.println("Packet no CRC read");
                    typeCRC = false;
                }
                System.out.println("End of the first iteration");

                // Increment to go to next try (available total 6 tries, since XModem specifies that receiver starts
                // connection, sending NAK / C every 10 seconds).

                numberOfIterations++;
            } while ((inputPacket[0] != XModemDefinitions.SOH && inputPacket[0] != XModemDefinitions.C) && numberOfIterations < 6);

            // If SOH / C symbol found - exit loop (communication started).

            System.out.println("Creating XModemClass object");
            if (typeCRC) {
                System.out.println("typeCRC true");
            } else {
                System.out.println("typeCRC not true");
            }

            // Create an instance of XModemClass, for calculation of checksums, processing data and so on.

            xModem = new XModemClass(typeCRC);
            System.out.println("Object created");

            // Checking how packet looks like.

            System.out.println("Reading all the packets loop");
            System.out.println("Packet stats: ");
            System.out.println("Length: " + inputPacket.length);
            System.out.println("First symbol: " + inputPacket[0]);
            System.out.println("Packet number: " + inputPacket[1]);
            System.out.println("To 255: " + inputPacket[2]);
            for (byte oneByte : inputPacket) {
                System.out.print(oneByte + " ");
            }

            // Loop for reading next packets, send by the sender.

            do {
                // If
                System.out.println("Last read packet: " + lastReadPacket);
                System.out.println("Current packet: " + inputPacket[1]);
                if (inputPacket[1] != lastReadPacket && (inputPacket[0] == XModemDefinitions.SOH || inputPacket[0] == XModemDefinitions.C)) {
                    System.out.println("Check if packet is a valid packet");
                    System.out.println("Checking if checksum is correct");
                    if (xModem.checkIfCheckSumIsCorrect(inputPacket)) {
                        System.out.println("Checksum is correct");
                        byteToBeSent[0] = XModemDefinitions.ACK;
                        selectedSerialPort.writeBytes(byteToBeSent, 1);
                        byte[] readPacket = new byte[inputPacket.length];
                        System.arraycopy(inputPacket, 0, readPacket, 0, inputPacket.length);
                        listOfReceivedPackets.add(readPacket);
                        lastReadPacket = readPacket[1];
                        System.out.println("++++++++++++++++++++++++++++");
                        System.out.println("Number of read packet: " + readPacket[1]);
                        System.out.println("++++++++++++++++++++++++++++");
                        System.out.println("Writing ACK to output stream");
                    } else {
                        System.out.println("Checksum is not correct");
                        byteToBeSent[0] = XModemDefinitions.NAK;
                        selectedSerialPort.writeBytes(byteToBeSent, 1);
                        System.out.println("Writing NAK to output stream");
                    }
                }
                System.out.println("Reading next packet");
                selectedSerialPort.readBytes(inputPacket, inputPacket.length);
                System.out.println("Next packet read");
                System.out.println("End of iteration");

                System.out.println("Reading all the packets loop");
                System.out.println("Packet stats: ");
                System.out.println("Length: " + inputPacket.length);
                System.out.println("First symbol: " + inputPacket[0]);
                System.out.println("Packet number: " + inputPacket[1]);
                System.out.println("To 255: " + inputPacket[2]);
                for (byte oneByte : inputPacket) {
                    System.out.print(oneByte + " ");
                }
            } while (inputPacket[0] != XModemDefinitions.EOT);
            System.out.println("All packets sent. Sending ACK for sender EOT");
            byteToBeSent[0] = XModemDefinitions.ACK;
            selectedSerialPort.writeBytes(byteToBeSent, 1);
            System.out.println("Communication finished");
            int numberOfReceivedPackets = listOfReceivedPackets.size();
            System.out.println("Size: " + numberOfReceivedPackets);
            byte[] messageToDisplay = xModem.transformBackToArrayOfBytes(listOfReceivedPackets);
            if (isContentBinary.isSelected()) {
                contentTextArea.setText(new String(Converter.convertAsciiToHexadecimal(messageToDisplay), StandardCharsets.US_ASCII));
            } else {
                contentTextArea.setText(new String(messageToDisplay, StandardCharsets.UTF_8));
            }
            closeSerialPortMethod();
        } catch (NullPointerException nullPtrExc) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił błąd", "Nie rozpoznano portu szeregowego: " + selectedSerialPort.getSystemPortName());
        }
    }

    /*
        @ Method: sendContentToSecondHost()

        @ Parameters: None

        @ Description: This method is used for sending contextTextArea content to other side of the serial port.
     */

    @FXML
    public void sendContentToSecondHost() {
        try {
            System.out.println("Start of the method");
            int writtenBytes = 0;
            byte[] dataToBeSend;
            byte[] operationResult = new byte[1];
            byte[] endOfCommunication = {XModemDefinitions.EOT};
            byte[][] dataArrangedInPackets;
            System.out.println("Check if content is binary");

            // Checking if content in contextTextArea is binary, and respond accordingly.

            if (isContentBinary.isSelected()) {
                System.out.println("Content is binary");
                dataToBeSend = Converter.convertHexadecimalToAscii(contentTextArea.getText().getBytes(StandardCharsets.US_ASCII));
            } else {
                System.out.println("Content is not binary");
                dataToBeSend = contentTextArea.getText().getBytes(StandardCharsets.UTF_8);
            }
            System.out.println("Wait for receiver response.");

            // Wait for receiver response - that is whether checksum should be CRC or algebraic.

            do {
                selectedSerialPort.readBytes(operationResult, 1L);
                System.out.println("Response: " + operationResult[0]);
            } while (operationResult[0] != XModemDefinitions.C && operationResult[0] != XModemDefinitions.NAK);
            System.out.println("Checking if XModemDefinitions.C or NAK was sent.");

            // Check the result and respond accordingly.

            if (operationResult[0] == XModemDefinitions.C) {
                withAddedCRC16 = true;
            } else {
                withAddedCRC16 = false;
            }
            XModemClass xModem;
            System.out.println("Creating XModem with CRC set / not set.");

            // Creating XModemClass object with correct setting regarding CRC checksum.

            if (withAddedCRC16) {
                xModem = new XModemClass(true);
            } else {
                xModem = new XModemClass(false);
            }
            System.out.println("Data transformation to packets.");
            dataArrangedInPackets = xModem.transformToArrayOfPackets(dataToBeSend);
            System.out.println("Number of packages: " + dataArrangedInPackets.length);
            System.out.println("Size of package: " + dataArrangedInPackets[0].length);
            int numberOfSentPackages = 0;

            // Send all the packages to the receiver.

            do {
                System.out.println("Sending next packet.");
                System.out.println("Packet stats: ");
                System.out.println("Length: " + dataArrangedInPackets[numberOfSentPackages].length);
                System.out.println("First symbol: " + dataArrangedInPackets[numberOfSentPackages][0]);
                System.out.println("Packet number: " + dataArrangedInPackets[numberOfSentPackages][1]);
                System.out.println("To 255: " + dataArrangedInPackets[numberOfSentPackages][2]);
                System.out.println("");
                for (byte oneByte : dataArrangedInPackets[numberOfSentPackages]) {
                    System.out.print(oneByte + " ");
                }
                writtenBytes = selectedSerialPort.writeBytes(dataArrangedInPackets[numberOfSentPackages], dataArrangedInPackets[numberOfSentPackages].length);
                System.out.println("Written bytes: " + writtenBytes);
                System.out.println("Next packet is sent.");
                System.out.println("Reading operation result as long as it is not ACK or NAK.");
                do {
                    selectedSerialPort.readBytes(operationResult, 1L);
                } while (operationResult[0] != XModemDefinitions.ACK && operationResult[0] != XModemDefinitions.NAK);
                System.out.println("Checking if operation was successful.");
                if (operationResult[0] == XModemDefinitions.ACK) {
                    System.out.println("Going to next packet");
                    numberOfSentPackages++;
                }
                System.out.println("End of the iteration.");
                System.out.println("+++++++++++++++++++++");
                System.out.println("Number of sent packages: " + numberOfSentPackages);
                System.out.println("+++++++++++++++++++++");
            } while (numberOfSentPackages < dataArrangedInPackets.length);

            // Finish communication by sending EOT and wait for receiver ACK.

            do {
                System.out.println("Trying to finish communication - sending EOT");
                selectedSerialPort.writeBytes(endOfCommunication, 1);
                selectedSerialPort.readBytes(operationResult, 1L);
            } while (operationResult[0] != XModemDefinitions.ACK);
            System.out.println("Communication finished");
            closeSerialPortMethod();
        } catch (NullPointerException nullPtrExc) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił błąd", "Nie rozpoznano portu szeregowego: " + selectedSerialPort.getSystemPortName());
        }
    }

    /*
        @ Method: readContentFromAFile()

        @ Parameters: None

        @ Description: This method is used to read the content of a file, that is selected by the user, in filesystem.
        It also includes parsing from hexadecimal symbols to ascii codes, when checkbox isBinaryContent is selected.
     */

    @FXML
    public void readContentFromAFile() {
        String pathToFile;
        byte[] someTextFromFile;
        FileChooser chooseFile = new FileChooser();
        pathToFile = chooseFile.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (!pathToFile.isEmpty() && pathToFile != null) {
            try {
                someTextFromFile = IOManager.readBytesFromFile(pathToFile);
                if (isContentBinary.isSelected()) {
                    contentTextArea.setText(new String(Converter.convertAsciiToHexadecimal(someTextFromFile), StandardCharsets.UTF_8));
                } else {
                    contentTextArea.setText(new String(someTextFromFile, StandardCharsets.UTF_8));
                }
            } catch (IOManagerReadException readExc) {
                String title = "Błąd";
                String header = "Błąd operacji odczytu z pliku: " + pathToFile;
                String content = readExc.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    /*
        @ Method: saveContentToAFile()

        @ Parameters: None

        @ Description: This method is used to save content of the contextTextArea to file selected in the filesystem
        by the user. It does include hexadecimal to ascii codes parsing, when checkbox isBinaryContent is selected.
     */

    @FXML
    public void saveContentToAFile() {
        FileChooser chooseFile = new FileChooser();
        File file = chooseFile.showSaveDialog(StageSetup.getStage());
        if (file != null) {
            byte[] textFromInput;
            if (isContentBinary.isSelected()) {
                textFromInput = Converter.convertHexadecimalToAscii(contentTextArea.getText().getBytes(StandardCharsets.UTF_8));
            } else {
                textFromInput = contentTextArea.getText().getBytes(StandardCharsets.UTF_8);
            }
            try {
                IOManager.writeBytesToFile(file.getAbsolutePath(), textFromInput);
            } catch (IOManagerWriteException writeExc) {
                String title = "Błąd";
                String header = "Błąd operacji zapisu pliku: " + file.getName();
                String content = writeExc.getMessage();
                throwAlert(Alert.AlertType.ERROR, title, header, content);
            }
        }
    }

    /*
        @ Method: getSelectedPort()

        @ Parameters: None

        @ Description: This method is used to return SerialPort object of currently selected serial port.
        Used mainly in other method, not to repeat getting array of SerialPorts and so on.
     */

    private SerialPort getSelectedPort() {
        SerialPort[] arrayOfSerialPorts = SerialPort.getCommPorts();
        String selectedPort = portIdentifier.getSelectionModel().getSelectedItem().toString();
        for (int i = 0; i < arrayOfSerialPorts.length; i++) {
            if (selectedPort.equals(arrayOfSerialPorts[i].getSystemPortName())) {
                return arrayOfSerialPorts[i];
            }
        }
        return null;
    }

    /*
        @ Method: getNumberOfParityBits()

        @ Parameters: None

        @ Description: This method used for getting number of parity bits used in communication
        with COM port. It returns an integer, so it can be said that this method functions as somewhat
        of a parser.
     */

    private int getNumberOfParityBits() {
        String gotParityInString = parityBits.getSelectionModel().getSelectedItem().toString();
        int parityValue = 0;
        switch (gotParityInString) {
            case "NO_PARITY" -> parityValue = 0;
            case "ODD_PARITY" -> parityValue = 1;
            case "EVEN_PARITY" -> parityValue = 2;
            case "MARK_PARITY" -> parityValue = 3;
            case "SPACE_PARITY" -> parityValue = 4;
        }
        return parityValue;
    }

    /*
        @ Method: getNumberOfDataBits()

        @ Parameters: None

        @ Description: This method is used for getting a number of data bits selected by the user. As the previous
        method is functions as a parser, to parser the result from Combobox selected item to integer.
     */

    private int getNumberOfDataBits() {
        String numberOfDataBitsInString = dataBits.getSelectionModel().getSelectedItem().toString();
        return Integer.parseInt(numberOfDataBitsInString);
    }

    /*
        @ Method: getNumberOfStopBits()

        @ Parameters: None

        @ Description: This method is used for getting a number of stop bits selected by the user. As the previous
        method is functions as a parser, to parser the result from Combobox selected item to integer.
     */

    private int getNumberOfStopBits() {
        String numberOfStopBitsInString = stopBits.getSelectionModel().getSelectedItem().toString();
        return Integer.parseInt(numberOfStopBitsInString);
    }

    /*
        @ Method: getValueOfBaudRate()

        @ Parameters: None

        @ Description: This method is used for getting the value of baud rate, selected by the user. It is used in
        communication with serial port.
     */

    private int getValueOfBaudRate() {
        String baudRateInString = baudRate.getSelectionModel().getSelectedItem().toString();
        return Integer.parseInt(baudRateInString);
    }

    /*
        @ Method: getContextTextAreaContent()

        @ Parameters: None

        @ Description: This method is used for getting contents of contentTextArea, and parses the input to form of byte
        array, including parsing hexadecimal back to ascii codes. This method functionality can be summarized as improved
        getter of text inside textArea.
     */

    private byte[] getContextTextAreaContent() {
        if (!contentTextArea.getText().isEmpty() && contentTextArea.getText() != null) {
            if (isContentBinary.isSelected()) {
                return Converter.convertHexadecimalToAscii(contentTextArea.getText().getBytes(StandardCharsets.US_ASCII));
            } else {
                return contentTextArea.getText().getBytes(StandardCharsets.UTF_8);
            }
        } else {
            throw new NoContentAvailable("W polu tekstowym nie ma jakiejkolwiek zawartości.");
        }
    }

    /*
        @ Method: changeCurrentChecksum()

        @ Parameters: None

        @ Description: This method is used to set value of selectedChecksum
        when user choose one of the available checksums.
     */

    public void changeCurrentChecksum() {
        String checksumInString = checksumSelection.getSelectionModel().getSelectedItem().toString();
        if (checksumInString.equals("CRC16")) {
            withAddedCRC16 = true;
        } else {
            withAddedCRC16 = false;
        }
    }

    /*
        @ Method: openSerialPortMethod()

        @ Parameters: None

        @ Description: This method is used for opening serial port for later communication
        with the use of it.
     */

    @FXML
    private void openSerialPortMethod() {
        try {
            selectedSerialPort.setBaudRate(getValueOfBaudRate());
            selectedSerialPort.setNumDataBits(getNumberOfDataBits());
            selectedSerialPort.setNumStopBits(getNumberOfStopBits());
            selectedSerialPort.setParity(getNumberOfParityBits());
            selectedSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
            selectedSerialPort.openPort();
            portStatus.setText("Otwarty");
            openPortButton.setDisable(true);
            closePortButton.setDisable(false);
        } catch (NullPointerException nullPtrExc) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił krytyczny błąd", "Nie wybrano portu szeregowego do otwarcia.");
        }
    }

    /*
        @ Method: closeSerialPortMethod()

        @ Parameters: None

        @ Description: This method is used for closing serial port, after the communication
        took place and no data would be sent.
     */

    @FXML
    private void closeSerialPortMethod() {
        try {
            selectedSerialPort.closePort();
            portStatus.setText("Zamknięty");
            openPortButton.setDisable(false);
            closePortButton.setDisable(true);
        } catch (NullPointerException nullPtrExc) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił krytyczny błąd", "Nie wybrano portu szeregowego do zamknięcia.");
        }
    }

    /*
        @ Method: setDefaultValues()

        @ Parameters: None

        @ Description: Method used to set default values to boxes containing
        values chosen by the user - that is for parity, stop and data bits and for
        port status. This method is called when user changes COM port.
     */

    @FXML
    public void setDefaultValues() {
        try {
            if (selectedSerialPort != null && selectedSerialPort.isOpen()) {
                closeSerialPortMethod();
            }
            baudRate.getSelectionModel().select(5);
            dataBits.getSelectionModel().select(2);
            stopBits.getSelectionModel().select(0);
            parityBits.getSelectionModel().select(0);
            selectedSerialPort = getSelectedPort();
            if (selectedSerialPort.isOpen()) {
                portStatus.setText("Otwarty");
                openPortButton.setDisable(true);
                closePortButton.setDisable(false);
            } else {
                portStatus.setText("Zamknięty");
                openPortButton.setDisable(false);
                closePortButton.setDisable(true);
            }
        } catch (NullPointerException nullPtrExc) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Wystąpił krytyczny błąd", "Nie dokonano wyboru portu szeregowego.");
        }
    }

    /*
        @ Method: showAuthors()

        @ Parameters: None

        @ Description: This method is used for showing a popup with authors' names and their index number.
     */

    @FXML
    public void showAuthors() {
        showPupUpWindow("Autorzy programu", "Aleksander Janicki 242405\nPiotr Kwiatkowski 242447");
    }

    /*
        @ Method: showPopUpWindow()

        @ Parameters:

        title   -> text that is being shown on the bar of the window
        content -> text that is being shown inside the window

        @ Description: Method used for showing popup that is not necessarily connected to any error.
     */

    private void showPupUpWindow(String title, String content) {
        Dialog<String> popUpWin = new Dialog<>();
        popUpWin.setTitle(title);
        popUpWin.setContentText(content);
        ButtonType closeWindow = new ButtonType("Zamknij", ButtonBar.ButtonData.CANCEL_CLOSE);
        popUpWin.getDialogPane().getButtonTypes().add(closeWindow);
        popUpWin.show();
    }

    /*
        @ Method: throwAlert()

        @ Parameters:

        typeOfAlert   -> type of message
        title         -> text being show on the bar of the window
        header        -> main reason for seeing given message
        content       -> text explaining why the window is being shown

        @ Description: This method is used for showing alert windows - i.e. in situation where exception is thrown.
     */

    private void throwAlert(Alert.AlertType typeOfAlert, String title, String header, String content) {
        Alert alert = new Alert(typeOfAlert);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}