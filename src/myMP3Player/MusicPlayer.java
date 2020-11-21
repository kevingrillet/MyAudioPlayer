package myMP3Player;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Mixer;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author David Rochinha (310280)
 */
public class MusicPlayer {

    private Clip clip;
    private Queue<String> musics;
    private File currentMusic;
    private Long time;

    /**
     * Default constructor
     * @param mixerInfo (Mixer.Info) : Mixer info of the output port wanted
     */
    public MusicPlayer(Mixer.Info mixerInfo){
        musics = new LinkedList<>();
        currentMusic = null;
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
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        clip.start();
    }

    /**
     * Stop music
     */
    public void stop(){
        if(currentMusic != null){
            clip.stop();
            time = 0l;
        }
    }

    /**
     * Pause music
     */
    public void pause(){
        if (currentMusic != null){
            time = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    /**
     * Resume music
     */
    public void resume(){
        if (currentMusic != null){
            clip.setMicrosecondPosition(time);
            clip.start();
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
            play();
        }
    }
    
    enum Status {
        PLAYING, STOPPED, PAUSED, NOTSTART
    }
}
