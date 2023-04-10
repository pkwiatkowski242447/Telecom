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

    private float userInputSampleRate;
    private int userInputSampleSizeInBits;
    private int userInputNumberOfChannels;
    private RecordAudioManager recordAudioManager;
    private PlayAudioManager playAudioManager;

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

    private void updateSampleRate() {
        String sampleRateInString = sampleRate.getSelectionModel().getSelectedItem().toString();
        userInputSampleRate = Float.parseFloat(sampleRateInString);
    }

    private void updateSampleSizeInBits() {
        String sampleSizeInBitsInString = sampleSizeInBits.getSelectionModel().getSelectedItem().toString();
        userInputSampleSizeInBits = Integer.parseInt(sampleSizeInBitsInString);
    }

    private void updateNumberOfChannels() {
        String numberOfChannelsInString = numberOfChannels.getSelectionModel().getSelectedItem().toString();
        userInputNumberOfChannels = Integer.parseInt(numberOfChannelsInString);
    }

    private void updateAllData() {
        updateSampleRate();
        updateSampleSizeInBits();
        updateNumberOfChannels();
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