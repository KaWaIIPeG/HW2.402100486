import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {
    Clip clip;
    String[] soundFilePaths = new String[1];

    public Sound() {
        soundFilePaths[0] = "C:\\Users\\GS\\HW2.402100486\\sound\\01 Courtesy.wav";
    }

    public void setFile(int i) {
        try {
            File audioFile = new File(soundFilePaths[i]);
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}
