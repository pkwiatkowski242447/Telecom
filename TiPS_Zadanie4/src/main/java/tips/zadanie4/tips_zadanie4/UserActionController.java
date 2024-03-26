package tips.zadanie4.tips_zadanie4;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import tips.zadanie4.tips_zadanie4.audio.PlayAudioManager;
import tips.zadanie4.tips_zadanie4.audio.RecordAudioManager;
import tips.zadanie4.tips_zadanie4.exceptions.LineException;
import tips.zadanie4.tips_zadanie4.exceptions.LineWithGivenParametersNotSupported;

public class UserActionController {

    @FXML
    private ComboBox sampleRate;
    @FXML
    private ComboBox sampleSizeInBits;
    @FXML
    private ComboBox numberOfChannels;
    @FXML
    private Button startRecordingButton;
    @FXML
    private Button finishRecordingButton;
    @FXML
    private Button startPlayButton;
    @FXML
    private Button finishPlayButton;

    /*
        @ Above attributes are used for customizing GUI combo-boxes, disabling buttons and so on.
     */

    /*
        @ userInputSampleRate: Sample rate chosen by the user from combo-box. It is
        already converted to float, since it will be easier to create RecordAudioManager with it.
     */

    private float userInputSampleRate;

    /*
        @ userInputSampleSizeInBits: Sample size in bits chosen by the user from combo-box. It is
        already converted to integer, since it will be easier to create RecordAudioManager with it.
     */

    private int userInputSampleSizeInBits;

    /*
        @ userInputNumberOfChannels: Number of channels chosen by the user from combo-box. It is
        already converted to integer, since it will be easier to create RecordAudioManager with it.
     */

    private int userInputNumberOfChannels;

    /*
        @ recordAudioManager: Variable holding recordAudioManager object
        that will be used later for recording sound.
     */

    private RecordAudioManager recordAudioManager;

    /*
        @ playAudioManager: Variable holding recordAudioManager object
        that will be used later for playing recorded sound.
     */

    private PlayAudioManager playAudioManager;

    /*
        @ Method: initialize()

        @ Parameters: None

        @ Description: Method used for initializing combo-boxes content, disabling some button and so on.
     */

    @FXML
    public void initialize() {
        sampleRate.getItems().addAll("8000", "11025", "16000", "22050", "32000", "44100", "48000", "88200", "96000", "176400", "192000");
        sampleSizeInBits.getItems().setAll("8", "16", "24", "32");
        numberOfChannels.getItems().setAll("1", "2");

        sampleRate.getSelectionModel().select(6);
        sampleSizeInBits.getSelectionModel().select(1);
        numberOfChannels.getSelectionModel().select(0);

        finishRecordingButton.setDisable(true);
        finishPlayButton.setDisable(true);

        updateSampleRate();
        updateSampleSizeInBits();
        updateNumberOfChannels();
    }

    /*
        @ Method: updateSampleRate()

        @ Parameters: None

        @ Description: This method is used for updating sampleRate value. It is called by
        updateAllData() method.
     */

    private void updateSampleRate() {
        String sampleRateInString = sampleRate.getSelectionModel().getSelectedItem().toString();
        userInputSampleRate = Float.parseFloat(sampleRateInString);
    }

    /*
        @ Method: updateSampleSizeInBits()

        @ Parameters: None

        @ Description: This method is used for updating sampleSizeInBits value. It is called by
        updateAllData() method.
     */

    private void updateSampleSizeInBits() {
        String sampleSizeInBitsInString = sampleSizeInBits.getSelectionModel().getSelectedItem().toString();
        userInputSampleSizeInBits = Integer.parseInt(sampleSizeInBitsInString);
    }

    /*
        @ Method: updateNumberOfChannels()

        @ Parameters: None

        @ Description: This method is used for updating numberOfChannels value. It is called by
        updateAllData() method.
     */

    private void updateNumberOfChannels() {
        String numberOfChannelsInString = numberOfChannels.getSelectionModel().getSelectedItem().toString();
        userInputNumberOfChannels = Integer.parseInt(numberOfChannelsInString);
    }

    /*
        @ Method: updateAllData()

        @ Parameters: None

        @ Description: This method is used for updating all the data selected by
        the user from the combo-boxes.
     */

    private void updateAllData() {
        updateSampleRate();
        updateSampleSizeInBits();
        updateNumberOfChannels();
    }

    /*
        @ Method: showAuthors()

        @ Parameters: None

        @ Description: This method is used to show authors' data.
     */

    @FXML
    public void showAuthors() {
        showPupUpWindow("Autorzy programu", "Aleksander Janicki 242405\nPiotr Kwiatkowski 242447");
    }


    /*
        @ Method: throwAlert()

        @ Parameters:

        * typeOfAlert   -> error of thrown alert
        * title         -> title of the window
        * header        -> header message inside the window
        * content       -> content inside the window - in most of the cases it is the reason for
        a certain exception being thrown

        @ Description: This method is used mainly to communicate to the user some problems
        that can occur through inappropriate use of the program.
     */

    private void throwAlert(Alert.AlertType typeOfAlert, String title, String header, String content) {
        Alert alert = new Alert(typeOfAlert);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    /*
        @ Method: showPupUpWindow()

        @ Parameters:

        * title -> title of the popup window.
        * content -> actual content of the popup - containing in this case authors' data.

        @ Description: Method for showing a windows with some text.
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
        @ Method: startRecordingSound()

        @ Parameters: None

        @ Description: This method is used for starting a recording of sound from user microphone.
     */

    @FXML
    public void startRecordingSound() {
        updateAllData();
        try {
            String fullFileName;
            FileChooser fileChooser = new FileChooser();
            fullFileName = fileChooser.showSaveDialog(StageSetup.getStage()).getAbsolutePath();
            if (fullFileName != null) {
                recordAudioManager = new RecordAudioManager(userInputSampleRate, userInputSampleSizeInBits, userInputNumberOfChannels);
                startRecordingButton.setDisable(true);
                finishRecordingButton.setDisable(false);
                recordAudioManager.recordSound(fullFileName);
            } else {
                throwAlert(Alert.AlertType.ERROR, "Bład", "Krytyczny błąd", "Nie podano nazwy pliku");
            }
        } catch (LineException lineException) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Powód: " + lineException.getMessage());
        } catch (LineWithGivenParametersNotSupported notSupported) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Nieprawidłowy dobór parametrów", notSupported.getMessage());
        }
    }

    /*
        @ Method: stopRecordingSound()

        @ Parameters: None

        @ Description: This method is used for cutting user recorded sound.
     */

    @FXML
    public void stopRecordingSound() {
        try {
            startRecordingButton.setDisable(false);
            finishRecordingButton.setDisable(true);
            recordAudioManager.closeTargetDataLine();
        } catch (NullPointerException nullPtrExc) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Nagrywanie zostało już zakończone.");
        }
    }

    /*
        @ Method: startPlayingRecordedSound()

        @ Parameters: None

        @ Description: This method is responsible for recording sound, recorded by user microphone.
     */

    @FXML
    public void startPlayingRecordedSound() {
        String fullFileName;
        FileChooser fileChooser = new FileChooser();
        fullFileName = fileChooser.showOpenDialog(StageSetup.getStage()).getAbsolutePath();
        if (fullFileName != null) {
            playAudioManager = new PlayAudioManager();
            startPlayButton.setDisable(true);
            finishPlayButton.setDisable(false);
            playAudioManager.playRecordedSound(fullFileName);
        } else {
            throwAlert(Alert.AlertType.ERROR, "Bład", "Krytyczny błąd", "Nie podano nazwy pliku");
        }
    }

    /*
        @ Method: finishPlayingRecordedSound()

        @ Parameters: None

        @ Description: This method is used for cutting played sound, before the end of it.
     */

    @FXML
    public void finishPlayingRecordedSound() {
        try {
            playAudioManager.closePlayedClip();
            startPlayButton.setDisable(false);
            finishPlayButton.setDisable(true);
        } catch (NullPointerException nullPtrExc) {
            throwAlert(Alert.AlertType.ERROR, "Błąd", "Krytyczny błąd", "Odtwarzanie nagrania zostało już zakończone");
        }
    }
}