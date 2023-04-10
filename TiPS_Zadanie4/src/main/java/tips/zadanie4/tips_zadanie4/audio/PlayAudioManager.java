package tips.zadanie4.tips_zadanie4.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class PlayAudioManager {

    private Clip clipToPlay;
    private AudioPlayer audioPlayer;

    public void playRecordedSound(String fileName) {
        audioPlayer = new AudioPlayer(fileName);
        audioPlayer.start();
    }

    public void closePlayedClip() {
        audioPlayer.interrupt();
    }

    class AudioPlayer extends Thread {

        private String fileName;
        private File recordedFile;

        public AudioPlayer(String fileName) {
            this.fileName = fileName;
            recordedFile = new File(fileName);
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public void run() {
            try {
                clipToPlay = AudioSystem.getClip();
                clipToPlay.open(AudioSystem.getAudioInputStream(recordedFile));
                clipToPlay.start();
                while(clipToPlay.getFramePosition() <= clipToPlay.getFrameLength() && clipToPlay.isOpen()) {

                }
                clipToPlay.stop();
                clipToPlay.close();
            } catch (UnsupportedAudioFileException | LineUnavailableException |
                     IOException exc) {
                exc.printStackTrace();
            }
        }

        @Override
        public void interrupt() {
            clipToPlay.stop();
            clipToPlay.close();
        }
    }
}
