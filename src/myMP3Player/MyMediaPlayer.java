package myMP3Player;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class MyMediaPlayer implements MyAudioPlayer{
    private Duration duration;
    private Queue<String> queue;
    private MediaPlayer mediaPlayer;

    public MyMediaPlayer() {
        queue = new LinkedList<>();
    }

    @Override
    public void add(String path) {
        queue.add(path);
    }

    @Override
    public void addAll(Collection<String> paths) {
        queue.addAll(paths);
    }

    @Override
    public void next() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING
                    || mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
                    || mediaPlayer.getStatus() == MediaPlayer.Status.READY
                    || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                mediaPlayer.seek(mediaPlayer.getTotalDuration());
            }
        }
    }

    @Override
    public void pause() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            }
        }
    }

    @Override
    public void play() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
                    || mediaPlayer.getStatus() == MediaPlayer.Status.READY
                    || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                mediaPlayer.play();
            }
        }
    }

    @Override
    public void previous() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING
                    || mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
                    || mediaPlayer.getStatus() == MediaPlayer.Status.READY
                    || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                mediaPlayer.seek(new Duration(0));
            }
        }
    }

    @Override
    public void remove(int index) {
        queue.remove(index);
    }

    @Override
    public void stop() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.stop();
                mediaPlayer.seek(mediaPlayer.getStartTime());
            }
        }
    }
}
