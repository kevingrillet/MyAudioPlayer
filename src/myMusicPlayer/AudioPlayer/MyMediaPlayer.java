package myMusicPlayer.AudioPlayer;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import myMusicPlayer.Bean;
import myMusicPlayer.Utils.UtilsProperties;

import java.io.File;
import java.util.List;

public class MyMediaPlayer extends MyAudioPlayerAbstract {
    // TODO Fix time for slider
    private final static String formats = "*.aif, *.aiff, *.aifc, *.m4a, *.mp3, *.wav, *.WAV";
    private MediaPlayer mediaPlayer;

    public MyMediaPlayer(Bean bean) {
        super(bean);
        bean.timeProperty().addListener(e -> seek(bean.getTime()));
    }

    @Override
    public List<String> getFormats() {
        return UtilsProperties.readFormats(formats);
    }

    @Override
    public String getMediaName() {
        if (!(mediaPlayer == null)) {
            String title = mediaPlayer.getMedia().getSource();
            title = title.substring(0, title.length() - ".mp3".length());
            title = title.substring(title.lastIndexOf("/") + 1).replaceAll("%20", " ");
            // TODO Find how to get title out of the listener.
            mediaPlayer.getMedia().getMetadata().addListener((MapChangeListener.Change<? extends String, ?> c) -> {
                if (c.wasAdded()) {
                    if ("title".equals(c.getKey())) {
                        System.out.println(c.getValueAdded().toString());
//                        title = c.getValueAdded().toString();
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
        if (!(mediaPlayer == null)) mediaPlayer.stop();
        if (bean.getQueue().isEmpty()) return;

        Media media = new Media(new File(bean.getQueue().get(0)).toURI().toString());

        mediaPlayer = new MediaPlayer(media);
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
