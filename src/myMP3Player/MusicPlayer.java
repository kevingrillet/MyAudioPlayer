package myMP3Player;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * My Music Player
 */
public class MusicPlayer {

    private Clip clip;
    private final Queue<String> musics;
    private File currentMusic;
    private long time;
    private MusicPlayer.Status status;
    private long duration;

    /**
     * Default constructor
     * @param mixerInfo (Mixer.Info) : Mixer info of the output port wanted
     */
    public MusicPlayer(Mixer.Info mixerInfo){
        musics = new LinkedList<>();
        currentMusic = null;
        status = Status.NOTSTART;
        duration = 0l;
        try {
            clip = AudioSystem.getClip(mixerInfo);
        } catch (Exception e){
            clip = null;
        }
    }

    /**
     * Constructor with default mixer
     */
    public MusicPlayer(){
        this(AudioSystem.getMixer(null).getMixerInfo());
    }

    /**
     * Add music to the listening queue
     * @param musics (String[]) : Array of path to the musics
     */
    public void addMusic(Collection<String> musics){
        this.musics.addAll(musics);
    }

    /**
     * Change output mixer
     */
    public void changeOutput(Mixer.Info mixerInfo){
        pause();
        try {
            clip = AudioSystem.getClip(mixerInfo);
            if (status != Status.NOTSTART) {
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        resume();
    }
    /**
     * Play the music, resume if paused
     */
    public void play(){
        if (!musics.isEmpty() && currentMusic == null) {
            try {
                currentMusic = new File(musics.poll());
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
                duration = getDuration(currentMusic);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (currentMusic != null){
            if(status == Status.NOTSTART || status == Status.STOPPED) {
                clip.start();
                status = Status.PLAYING;
            } else if (status == Status.PAUSED){
                resume();
            }
        }
    }

    /**
     * Stop music
     */
    public void stop(){
        if(currentMusic != null){
            clip.stop();
            time = 0l;
            status = Status.STOPPED;
        }
    }

    /**
     * Pause music
     */
    public void pause(){
        if (currentMusic != null && status == Status.PLAYING){
            time = clip.getMicrosecondPosition();
            clip.stop();
            status = Status.PAUSED;
        }
    }

    /**
     * Resume music
     */
    public void resume(){
        if (currentMusic != null && status == Status.PAUSED){
            clip.setMicrosecondPosition(time);
            clip.start();
            status = Status.PLAYING;
        }
    }

    /**
     * Play the next music
     */
    public void next(){
        if (musics.isEmpty()){
            stop();
            clip.close();
            currentMusic = null;
        } else {
            try {
                currentMusic = new File(musics.poll());
                clip.close();
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
            } catch (Exception e){
                System.out.println(e.getMessage());
                clip.stop();
            }
            duration = getDuration(currentMusic);
            stop();
            play();
        }
    }

    /**
     * Get the current time
     */
    public long getTime(){
        return clip.getMicrosecondPosition();
    }

    /**
     *Tells if a music has ended
     */
    public boolean hasEnded() {
        return clip.getMicrosecondPosition() >= duration;
    }

    /**
     * Return current status
     */
    public Status getStatus() {
        return status;
    }


    private static long getDuration(File file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = file.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));
            return (long) durationInSeconds*1000;

        } catch (Exception e){
            System.out.println(e.getMessage());
            return 0l;
        }
    }

    enum Status {
        PLAYING, STOPPED, PAUSED, NOTSTART;

    }
}
