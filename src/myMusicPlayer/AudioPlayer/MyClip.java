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
public class MyClip implements MyAudioPlayerInterface {
// TODO [implements MyAudioPlayerInterface] -> [extends MyAudioPlayerAbstract]
// TODO bean.Queue
    private final static String formats = "*.aif, *.aiff, *.aifc, *.wav, *.WAV";
    private final Queue<String> musics;
    private ObservableDoubleValue time;
    private Clip clip;
    private File currentMusic;
    private long pauseTime;
    private MyClip.Status status;
    private long duration;

    /**
     * Default constructor
     *
     * @param mixerInfo (Mixer.Info) : Mixer info of the output port wanted
     */
    public MyClip(Mixer.Info mixerInfo, Bean bean) throws LineUnavailableException {
        musics = new LinkedList<>();
        currentMusic = null;
        status = Status.NOTSTART;
        duration = 0l;
        clip = AudioSystem.getClip(mixerInfo);
        time = new SimpleDoubleProperty((double) clip.getMicrosecondLength()); //TODO
        time.addListener(t -> bean.setTime(time.get()));
        bean.timeProperty().addListener(t -> seek(bean.getTime()));
    }

    /**
     * Constructor with default mixer
     */
    public MyClip(Bean bean) throws LineUnavailableException {
        this(AudioSystem.getMixer(null).getMixerInfo(), bean);
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

    @Override
    public void add(String path) {
        musics.add(path);
    }

    /**
     * Add music to the listening queue
     *
     * @param paths (Collection<String>) : Collection of paths to the musics
     */
    @Override
    public void addAll(Collection<String> paths) {
        this.musics.addAll(paths);
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
        if (!musics.isEmpty() && currentMusic == null) {
            try {
                currentMusic = new File(musics.poll());
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
                duration = getDurationFile(currentMusic);
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

    @Override
    public void remove(int index) {
        if (!musics.isEmpty()) {
            ((LinkedList) musics).remove(index);
        }
    }

    /**
     * Stop music
     */
    @Override
    public void stop() {
        if (currentMusic != null) {
            clip.stop();
            pauseTime = 0l;
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
        if (musics.isEmpty()) {
            stop();
            clip.close();
            currentMusic = null;
        } else {
            try {
                currentMusic = new File(musics.poll());
                clip.close();
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                clip.stop();
            }
            duration = getDurationFile(currentMusic);
            stop();
            play();
        }
    }

    /**
     * Get the current time
     *
     * @return (double): current time in milliseconds
     */
    public double getTime() {
        return clip.getMicrosecondPosition();
    }

    /**
     * Get End time
     *
     * @return (double): end time in milliseconds
     */
    public double getDuration() {
        return duration;
    }

    @Override
    public List<String> getFormats() {
        return UtilsProperties.readFormats(formats);
    }

    /**
     * Tells if a music has ended
     *
     * @return (if the current music has ended)
     */
    public boolean hasEnded() {
        return clip.getMicrosecondPosition() >= duration;
    }

    /**
     * Go to time position
     *
     * @param time (long): time in milliseconds
     */
    public void seek(double time) {
        clip.setMicrosecondPosition((long) time);
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
        //FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        //gainControl.setValue((20f *  (float) Math.log10(volume)));
    }

    enum Status {
        PLAYING, STOPPED, PAUSED, NOTSTART;

    }
}
