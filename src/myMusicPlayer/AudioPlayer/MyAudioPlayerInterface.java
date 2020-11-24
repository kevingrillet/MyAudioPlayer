package myMusicPlayer.AudioPlayer;

import javax.sound.sampled.Mixer;
import java.util.Collection;
import java.util.List;

public interface MyAudioPlayerInterface {

    /**
     * Add music to the listening queue
     *
     * @param path (String) : Path to the music
     */
    void add(String path);

    /**
     * Add music to the listening queue
     *
     * @param paths (Collection<String>) : Collection of paths to the musics
     */
    void addAll(Collection<String> paths);

    /**
     * Get End time
     *
     * @return (double): end time in milliseconds
     */
    double getDuration();

    /**
     * Set duration of the current media
     *
     * @param duration total duration
     */
    void setDuration(double duration);

    /**
     * Get list of compatible formats
     *
     * @return List<String>: List of formats for FileChooser.ExtensionFilter
     */
    List<String> getFormats();

    /**
     * Get the name of the current media
     *
     * @return String: Name of the current media
     */
    String getMediaName();

    /**
     * Get the current time
     *
     * @return (double): current time in milliseconds
     */
    double getTime();

    /**
     * Return Volume of mediaPlayer.
     *
     * @return Return volume between 0 and 1. -1 is return if MediaPlayer is null.
     */
    double getVolume();

    /**
     * Value between 0 and 1
     *
     * @param volume (double): volume between 0 and 1
     */
    void setVolume(double volume);

    /**
     * Define the new Audio output
     *
     * @param audioOutput (Mixer.Info)
     */
    void setAudioOutput(Mixer.Info audioOutput);

    /**
     * Play the next music
     */
    void next();

    /**
     * Pause music
     */
    void pause();

    /**
     * Play the music, resume if paused
     */
    void play();

    /**
     * Get back to the beginning of the music
     */
    void previous();

    /**
     * Remove a music from the playlist
     *
     * @param index of the music to remove
     */
    void remove(int index);

    /**
     * Go to time position
     *
     * @param time (long): time in milliseconds
     */
    void seek(double time);

    /**
     * Select first media to play
     */
    void setMedia();

    /**
     * Stop music
     */
    void stop();
}
