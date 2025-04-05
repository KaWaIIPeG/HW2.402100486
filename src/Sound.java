    import javax.sound.sampled.AudioInputStream;
    import javax.sound.sampled.AudioSystem;
    import javax.sound.sampled.Clip;
    import java.io.File;

    public class Sound {
        Clip clip;
        String[] soundFilePaths = new String[1];
        long clipTimePosition = 0;

        public Sound() {
            soundFilePaths[0] = "sound/01 Courtesy.wav";
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
                clip.setMicrosecondPosition(clipTimePosition);
                clip.start();
            }
        }

        public void loop() {
            if (clip != null) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }

        public void pause() {
            if (clip != null && clip.isRunning()) {
                clipTimePosition = clip.getMicrosecondPosition();
                clip.stop();
            }
        }

        public void stop() {
            if (clip != null) {
                clipTimePosition = 0;
                clip.stop();
            }
        }
    }