package myMP3Player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Properties;

/**
 *  Heart link to MyMP3Player.fxml
 */
public class Controller {
    private Duration duration;
    private MediaPlayer mediaPlayer;

    @FXML // fx:id="comboAudioOutput"
    private ComboBox<String> comboAudioOutput; // Value injected by FXMLLoader

    @FXML // fx:id="labelPlayerName"
    private Label labelPlayerName; // Value injected by FXMLLoader

    @FXML // fx:id="labelPlayerTime"
    private Label labelPlayerTime; // Value injected by FXMLLoader

    @FXML // fx:id="sliderMasterVolume"
    private Slider sliderMasterVolume; // Value injected by FXMLLoader

    @FXML // fx:id="sliderPlayerTime"
    private Slider sliderPlayerTime; // Value injected by FXMLLoader

    /**
     * Handle the events of the buttons
     * @param event ActionEvent to get the node ID.
     */
    @FXML
    void handleButtonAction(ActionEvent event) {
        String id = ((Node) event.getSource()).getId();
        switch (id) {
            case "buttonPlayerStop":
                if ( mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.stop();
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                }
                break;
            case "buttonPlayerPause":
                if ( mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                }
                break;
            case "buttonPlayerPlay":
                if ( mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
                        || mediaPlayer.getStatus() == MediaPlayer.Status.READY
                        || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                    mediaPlayer.play();
                }
                break;
            case "buttonPlaylistAdd":
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    setMedia(file);
                }
                break;
            case "buttonPlaylistRemove":
                mediaPlayer.dispose();
                break;
        }
    }

    /**
     * Handle the events of the combos
     * @param event ActionEvent to get the node ID.
     */
    @FXML
    void handleComboAction(ActionEvent event) {
        String id = ((Node) event.getSource()).getId();
        if ("comboAudioOutput".equals(id)) {
            writeProperties();
        }
    }

    /**
     *  This method is called by the FXMLLoader when initialization is complete
     */
    @FXML
    void initialize() {
        assert labelPlayerName != null : "fx:id=\"labelPlayerName\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert labelPlayerTime != null : "fx:id=\"labelPlayerTime\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert comboAudioOutput != null : "fx:id=\"comboAudioOutput\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert sliderMasterVolume != null : "fx:id=\"sliderMasterVolume\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert sliderPlayerTime != null : "fx:id=\"sliderPlayerTime\" was not injected: check your FXML file 'MyMP3Player.fxml'.";

        /*______ AUDIO OUTPUT ______*/
        // How to get list of AudioOutput.
        Mixer.Info[] mixerInfo =  AudioSystem.getMixerInfo();
        ObservableList<String> listAudioOutput = FXCollections.observableArrayList();
        // Output
        Line.Info playbackLine = new Line.Info(SourceDataLine.class);
        // Input
//        Line.Info captureLine = new Line.Info(TargetDataLine.class);

        for (Mixer.Info info : mixerInfo) {
            // Filter on Output
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.isLineSupported(playbackLine)){
                listAudioOutput.add(info.getName());
            }
        }

        comboAudioOutput.setItems(listAudioOutput);

        // Play: https://stackoverflow.com/questions/37609430/play-sound-on-specific-sound-device-java
        /*______ AUDIO OUTPUT ______*/
        setDefaultValues();
        readProperties();
    }

    /**
     *  Set default values
     */
    private void setDefaultValues() {
        /*______ AUDIO OUTPUT ______*/
        // set default output with getMixer(null)
        comboAudioOutput.getSelectionModel().select(AudioSystem.getMixer(null).getMixerInfo().getName());
        /*______ MASTER LEVEL ______*/
        sliderMasterVolume.setValue(100.0);
        /*______ MASTER LEVEL ______*/
        /*______ MEDIA PLAYER ______*/
        sliderPlayerTime.setValue(0);
        sliderPlayerTime.setDisable(true);
        /*______ MEDIA PLAYER ______*/
    }

    /**
     *
     * @param file MediaFile to Read
     */
    private void setMedia (File file) {
        /*______ MEDIA PLAYER ______*/
        // https://docs.oracle.com/javafx/2/media/playercontrol.htm

        Media media = new Media(file.toURI().toString());
        labelPlayerName.setText(media.getSource());

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.currentTimeProperty().addListener(observable -> updateTimeValue());
        mediaPlayer.setOnReady(() -> {
            duration = mediaPlayer.getMedia().getDuration();
            updateTimeValue();
        });
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.stop());
        /*______ MEDIA PLAYER ______*/

        /*______ MASTER LEVEL ______*/
        mediaPlayer.setVolume(sliderMasterVolume.getValue() / 100.0);

        sliderMasterVolume.valueProperty().addListener(observable -> {
            if (sliderMasterVolume.isValueChanging()) {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(sliderMasterVolume.getValue() / 100.0);
                }
                writeProperties();
            }
        });
        /*______ MASTER LEVEL ______*/

        /*______ TIME SLIDER ______*/
        sliderPlayerTime.valueProperty().addListener(observable -> {
            if (sliderPlayerTime.isValueChanging()) {
                if (mediaPlayer != null) {
                    mediaPlayer.seek(duration.multiply(sliderPlayerTime.getValue() / 100.0));
                }
            }
        });
        /*______ TIME SLIDER ______*/
    }

    /**
     *  Load properties on startup
     */
    private void readProperties() {
        try (InputStream inputStream = new FileInputStream("src/myMP3Player/config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            sliderMasterVolume.setValue(Double.parseDouble(properties.getProperty("sliderMasterVolume","100.0")));
            String mixerName = properties.getProperty("comboAudioOutput","");
            if (!mixerName.isEmpty()) {
                Mixer.Info[] mixerInfo =  AudioSystem.getMixerInfo();
                for (Mixer.Info info : mixerInfo) {
                    if (info.getName().equals(mixerName)) {
                        comboAudioOutput.getSelectionModel().select(AudioSystem.getMixer(info).getMixerInfo().getName());
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  Save properties
     */
    private void writeProperties() {
        try (OutputStream outputStream = new FileOutputStream("src/myMP3Player/config.properties")) {
            Properties properties = new Properties();

            properties.setProperty("sliderMasterVolume", String.valueOf(sliderMasterVolume.getValue()));
            properties.setProperty("comboAudioOutput", comboAudioOutput.getSelectionModel().getSelectedItem());

            properties.store(outputStream, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     *  Update Media time Label & Slider
     */
    private void updateTimeValue(){
        Duration currentTime = mediaPlayer.getCurrentTime();
        labelPlayerTime.setText(formatTime(currentTime, duration));
        sliderPlayerTime.setDisable(duration.isUnknown());
        if (!sliderPlayerTime.isDisabled()
                && duration.greaterThan(Duration.ZERO)
                && !sliderPlayerTime.isValueChanging()) {
            sliderPlayerTime.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
        }
    }

    /**
     *
     * @param elapsed Current time of the Media
     * @param duration Duration of the Media
     * @return String 00:00/00:00
     */
    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 -
                    durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds,durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }
}
