package myMusicPlayer.AudioPlayer;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.media.Media;
import myMusicPlayer.Bean;
import myMusicPlayer.Utils.UtilsProperties;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * My Music Player
 */
public class MyClip extends MyAudioPlayerAbstract {

    private ObservableDoubleValue time;
    private Clip clip;
    private File currentMusic;
    private long pauseTime;
    private MyClip.Status status;

    /**
     * Default constructor
     *
     * @param mixerInfo (Mixer.Info) : Mixer info of the output port wanted
     */
    public MyClip(Mixer.Info mixerInfo, Bean bean) throws LineUnavailableException {
        super(bean);
        currentMusic = null;
        status = Status.NOTSTART;
        clip = AudioSystem.getClip(mixerInfo);
        bean.timeProperty().addListener(t -> {
            System.out.println(getTime());
            seek(bean.getTime());
        });
    }

    /**
     * Constructor with default mixer
     */
    public MyClip(Bean bean) throws LineUnavailableException {
        this(AudioSystem.getMixer(null).getMixerInfo(), bean);
    }



    /**
     * Change output mixer
     */
    public void changeOutput(Mixer.Info mixerInfo) {
        pause();
        Clip tmp = clip;
        try {
            clip = AudioSystem.getClip(mixerInfo);
            if (status != Status.NOTSTART) {
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            clip = tmp;
        }
        resume();
    }

    public String getMediaName() {
        Media media = new Media(currentMusic.toURI().toString());
        String name = currentMusic.getName().substring(0, currentMusic.getName().length() - 4);
        Object o = media.getMetadata().getOrDefault("title", null);
        return o == null ? name : o.toString();
    }

    /**
     * Play the music, resume if paused
     */
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
            if (status == Status.NOTSTART || status == Status.STOPPED) {
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

    /**
     * Stop music
     */
    @Override
    public void stop() {
        if (currentMusic != null) {
            clip.stop();
            pauseTime = 0l;
            seek(0);
            status = Status.STOPPED;
        }
    }

    /**
     * Pause music
     */
    @Override
    public void pause() {
        if (currentMusic != null && status == Status.PLAYING) {
            pauseTime = clip.getMicrosecondPosition();
            clip.stop();
            status = Status.PAUSED;
        }
    }

    /**
     * Resume music
     */
    public void resume() {
        if (currentMusic != null && status == Status.PAUSED) {
            clip.setMicrosecondPosition(pauseTime);
            clip.start();
            status = Status.PLAYING;
        }
    }

    /**
     * Play the next music
     */
    @Override
    public void next() {
        if (bean.getQueue().isEmpty()) {
            stop();
            clip.close();
            currentMusic = null;
        } else {
            stop();
            try {

                currentMusic = new File(bean.getQueue().remove(0));
                clip.close();
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            play();
        }
    }

    /**
     * Get the current time
     *
     * @return (double): current time in milliseconds
     */
    public double getTime() {
        return clip.getMicrosecondPosition()/1000;
    }

    /**
     * Get End time
     *
     * @return (double): end time in milliseconds
     */
    public double getDuration() {
        return getDurationFile(currentMusic);
    }


    /**
     * Go to time position
     *
     * @param time (long): time in milliseconds
     */
    public void seek(double time) {
        clip.setMicrosecondPosition((long) (time*1000));
    }

    @Override
    public void setMedia() {

    }

    public double getVolume() {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            return gainControl.getValue();
        } else {
            return -1f;
        }
    }

    /**
     * Value between 0 and 1
     *
     * @param volume (double): volume between 0 and 1
     */
    public void setVolume(double volume) {
        assert (volume >= 0 && volume <= 1);
        if (clip.isOpen()) {
            //FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            //gainControl.setValue((float) volume);
        }
    }

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
            return 0l;
        }
    }

    enum Status {
        PLAYING, STOPPED, PAUSED, NOTSTART;

    }
}
