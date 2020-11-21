package myMP3Player;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * My Music Player
 */
public class MusicPlayer {

    private Clip clip;
    private Queue<String> musics;
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
    public void addMusic(String[] musics){
        this.musics.addAll(Arrays.asList(musics));
    }

    /**
     * Play the music
     */
    public void play(){
        if (!musics.isEmpty()) {
            if (currentMusic == null){
                try {
                    currentMusic = new File(musics.poll());
                    clip.open(AudioSystem.getAudioInputStream(currentMusic));
                    AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(currentMusic);
                    duration = (long) fileFormat.getProperty("duration");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if (currentMusic != null) {
            clip.start();
            status = Status.PLAYING;
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
        if (currentMusic != null){
            time = clip.getMicrosecondPosition();
            clip.stop();
            status = Status.PAUSED;
        }
    }

    /**
     * Resume music
     */
    public void resume(){
        if (currentMusic != null){
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
        } else {
            try {
                currentMusic = new File(musics.poll());
                clip.open(AudioSystem.getAudioInputStream(currentMusic));
            } catch (Exception e){
                System.out.println(e.getMessage());
                clip.stop();
            }
            try {
                AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(currentMusic);
                duration = (long) fileFormat.getProperty("duration");
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
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

    enum Status {
        PLAYING, STOPPED, PAUSED, NOTSTART;


    }
}
