package myAudioPlayer.AudioPlayer;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import myAudioPlayer.Bean;

import javax.sound.sampled.Mixer;
import java.io.File;

public class MyMediaPlayer extends MyAudioPlayerAbstract {
    private MediaPlayer mediaPlayer;

    public MyMediaPlayer(Bean bean) {
        super(bean);
        super.formats = "*.aif, *.aiff, *.aifc, *.m4a, *.wav, *.WAV";
        // WIP: LAG MACHINE
        bean.timeProperty().addListener(e -> seek(bean.getTime()));
    }

    @Override
    public String getMediaName() {
        if (!(mediaPlayer == null)) {
            String title = mediaPlayer.getMedia().getSource();
            title = title.substring(title.lastIndexOf("/") + 1, title.lastIndexOf(".")).replaceAll("%20", " ");
            mediaPlayer.getMedia().getMetadata().addListener((MapChangeListener.Change<? extends String, ?> c) -> {
                if (c.wasAdded()) {
                    if ("title".equals(c.getKey())) {
                        bean.setTitle(c.getValueAdded().toString());
                    }
                }
            });
            return title;
        } else {
            return "";
        }
    }

    @Override
    public double getTime() {
        if (!(mediaPlayer == null)) {
            return mediaPlayer.getCurrentTime().toMillis();
        } else {
            return -1;
        }
    }

    @Override
    public double getVolume() {
        if (!(mediaPlayer == null)) {
            return mediaPlayer.getVolume();
        } else {
            return -1;
        }
    }

    @Override
    public void setVolume(double volume) {
        assert (volume >= 0 && volume <= 1);
        if (!(mediaPlayer == null)) {
            mediaPlayer.setVolume(volume);
        }
    }

    @Override
    public void setAudioOutput(Mixer.Info audioOutput) {
        // TODO can't?
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
    public void seek(double time) {
        if (!(mediaPlayer == null)) {
            mediaPlayer.seek(Duration.millis(time));
        }
    }

    @Override
    public void setMedia() {
        double volume = 100.0;
        if (!(mediaPlayer == null)) {
            volume = mediaPlayer.getVolume();
            mediaPlayer.dispose();
        }
        if (bean.getQueue().isEmpty()) return;

        Media media = new Media(new File(bean.getQueue().get(0)).toURI().toString());

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.currentTimeProperty().addListener(observable -> bean.setTime(getTime()));
        mediaPlayer.setOnReady(() -> {
            setDuration(mediaPlayer.getTotalDuration().toMillis());
            bean.setTime(getTime());
            bean.setTitle(getMediaName());
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            mediaPlayer.seek(new Duration(0));
            bean.getQueue().remove(0);
            if (bean.getQueue().size() > 0) {
                setMedia();
                mediaPlayer.play();
            } else {
                setDuration(0);
                bean.setTitle("");
                bean.setTime(0);
            }
        });
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
