package tips.zadanie4.tips_zadanie4.audio;

import tips.zadanie4.tips_zadanie4.exceptions.LineException;
import tips.zadanie4.tips_zadanie4.exceptions.LineWithGivenParametersNotSupported;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class RecordAudioManager {

    private final float sampleRate;
    private final int sameSizeInBits;
    private final int numberOfChannels;

    private AudioFormat audioFormat;
    private DataLine.Info dataLineInfo;
    private TargetDataLine targetDataLine;

    public RecordAudioManager(float sampleRate, int sameSizeInBits, int numberOfChannels) throws LineException {
        this.sampleRate = sampleRate;
        this.sameSizeInBits = sameSizeInBits;
        this.numberOfChannels = numberOfChannels;
        try {
            audioFormat = new AudioFormat(sampleRate, sameSizeInBits, numberOfChannels, true, false);
            dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        } catch (LineUnavailableException lineUnavailableException) {
            throw new LineException("Linia komunikacyjna jest zajęta.");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new LineWithGivenParametersNotSupported("Linia komunikacyjna z takimi parametrami nie jest wspierana przez kartę dźwiękową.");
        }
    }

    /*
        @ Getters:
     */

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public TargetDataLine getTargetDataLine() {
        return targetDataLine;
    }

    public void recordSound(String fileName) {
        AudioRecorder audioRecorder = new AudioRecorder(fileName);
        audioRecorder.start();
    }

    public void closeTargetDataLine() {
        targetDataLine.stop();
        targetDataLine.close();
    }

    class AudioRecorder extends Thread {

        String fileName;

        public AudioRecorder(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public void run() {
            AudioFileFormat.Type fileFormat = AudioFileFormat.Type.WAVE;
            File recordedFile = new File(fileName);

            try {
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                AudioSystem.write(new AudioInputStream(targetDataLine), fileFormat, recordedFile);
            } catch (LineUnavailableException | IOException lineUnavailableException) {
                lineUnavailableException.printStackTrace();
            }

        }
    }
}
