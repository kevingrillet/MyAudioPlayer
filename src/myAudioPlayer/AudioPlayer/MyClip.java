package myAudioPlayer.AudioPlayer;

import javafx.scene.media.Media;
import myAudioPlayer.Bean;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * My Music Player
 */
public class MyClip extends MyAudioPlayerAbstract {
    private Clip clip;
    private File currentMusic;
    private long pauseTime;
    private MyClip.Status status;

    /**
     * Default constructor
     *
     * @param mixerInfo (Mixer.Info) : Mixer info of the output port wanted
     */
    public MyClip(Mixer.Info mixerInfo, Bean bean) {
        super(bean);
        currentMusic = null;
        status = Status.UNKNOWN;
        try {
            clip = AudioSystem.getClip(mixerInfo);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        bean.timeProperty().addListener(t -> seek(bean.getTime()));
    }

    /**
     * Constructor with default mixer
     */
    public MyClip(Bean bean) {
        this(AudioSystem.getMixer(null).getMixerInfo(), bean);
    }


    @Override
    public void setAudioOutput(Mixer.Info audioOutput) {
        pause();
        Clip tmp = clip;
        try {
            clip = AudioSystem.getClip(audioOutput);
            if (status != Status.UNKNOWN) {
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            clip = tmp;
        }
        resume();
    }

    @Override
    public String getMediaName() {
        Media media = new Media(currentMusic.toURI().toString());
        String name = currentMusic.getName().substring(0, currentMusic.getName().length() - 4);
        Object o = media.getMetadata().getOrDefault("title", null);
        return o == null ? name : o.toString();
    }

    @Override
    public void play() {
        if (!bean.getQueue().isEmpty() && currentMusic == null) {
            try {
                currentMusic = new File(bean.getQueue().get(0));
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (currentMusic != null) {
            if (status == Status.UNKNOWN || status == Status.STOPPED) {
                clip.start();
                status = Status.PLAYING;
            } else if (status == Status.PAUSED) {
                resume();
            }
        }
    }

    @Override
    public void previous() {
        seek(0);
    }

    @Override
    public void stop() {
        if (currentMusic != null) {
            clip.stop();
            pauseTime = 0L;
            seek(0);
            status = Status.STOPPED;
        }
    }

    @Override
    public void pause() {
        if (currentMusic != null && status == Status.PLAYING) {
            pauseTime = clip.getMicrosecondPosition();
            clip.stop();
            status = Status.PAUSED;
            bean.setTime(getTime());
        }
    }

    /**
     * Resume the music if Status.PAUSED
     */
    public void resume() {
        if (currentMusic != null && status == Status.PAUSED) {
            clip.setMicrosecondPosition(pauseTime);
            clip.start();
            status = Status.PLAYING;
        }
    }

    @Override
    public void next() {
        if (bean.getQueue().isEmpty()) {
            stop();
            clip.close();
            currentMusic = null;
        } else {
            stop();
            try {
                currentMusic = new File(bean.getQueue().get(1));
                clip.close();
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
            } catch (Exception e) {
                currentMusic = null;
                clip.close();
                System.out.println(e.getMessage());
            }
            bean.getQueue().remove(0);
            play();
        }
    }

    @Override
    public double getTime() {
        return clip.getMicrosecondPosition() / 1000.0;
    }

    @Override
    public double getDuration() {
        return getDurationFile(currentMusic);
    }

    @Override
    public void seek(double time) {
        clip.setMicrosecondPosition((long) (time * 1000));
    }

    @Override
    public void setMedia() {
        try {
            clip.open(AudioSystem.getAudioInputStream(new File(bean.getQueue().get(0))));
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getVolume() {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            return Math.pow(gainControl.getValue()/20, 10)/2;
        } else {
            return -1f;
        }
    }

    @Override
    public void setVolume(double volume) {
        assert (volume >= 0 && volume <= 1);
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)){
            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (Math.log10(volume*2) * 20));
        }
    }

    /**
     * Get the duration of the media from the file
     *
     * @param file (File):audio file
     * @return (long): duration of the file
     */
    private static long getDurationFile(File file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = file.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));
            return (long) durationInSeconds * 1000;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0L;
        }

    }

    enum Status {
        UNKNOWN, PLAYING, STOPPED, PAUSED
    }
}
