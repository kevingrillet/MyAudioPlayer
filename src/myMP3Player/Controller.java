package myMP3Player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;

import javax.sound.sampled.*;

public class Controller {
    @FXML
    private ComboBox<String> comboAudioOutput ;

    @FXML
    private Slider sliderMasterVolume;

    public void initialize() {
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
        sliderMasterVolume.setValue(1.00);
        /*______ MASTER LEVEL ______*/
    }
}
