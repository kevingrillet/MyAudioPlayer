package myMP3Player;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;

public class Controller {
    private MediaPlayer mediaPlayer;

    @FXML // fx:id="sliderMasterVolume"
    private Slider sliderMasterVolume; // Value injected by FXMLLoader

    @FXML // fx:id="comboAudioOutput"
    private ComboBox<String> comboAudioOutput; // Value injected by FXMLLoader

    @FXML
    void handleButtonAction(ActionEvent event) {
        String id = ((Node) event.getSource()).getId();
        switch (id) {
            case "buttonPlayerStop":
                break;
            case "buttonPlayerPause":
                break;
            case "buttonPlayerPlay":
                break;
            case "buttonPlaylistAdd":
                break;
            case "buttonPlaylistRemove":
                break;
        }
    }

    @FXML
    void handleComboAction(ActionEvent event) {
        String id = ((Node) event.getSource()).getId();
        if ("comboAudioOutput".equals(id)) {
            // how to put the Output...
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert sliderMasterVolume != null : "fx:id=\"sliderMasterVolume\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert comboAudioOutput != null : "fx:id=\"comboAudioOutput\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        // Basics
        // https://docs.oracle.com/javafx/2/media/playercontrol.htm

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
        // set default output with getMixer(null)
        comboAudioOutput.getSelectionModel().select(AudioSystem.getMixer(null).getMixerInfo().getName());

        // Play: https://stackoverflow.com/questions/37609430/play-sound-on-specific-sound-device-java
        /*______ AUDIO OUTPUT ______*/

        /*______ MASTER LEVEL ______*/
        sliderMasterVolume.valueProperty().addListener(observable -> {
            if (sliderMasterVolume.isValueChanging()) {
                mediaPlayer.setVolume(sliderMasterVolume.getValue() / 100.0);
            }
        });
        sliderMasterVolume.setValue(1.00);
        /*______ MASTER LEVEL ______*/
    }
}
