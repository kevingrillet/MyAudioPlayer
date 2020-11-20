package myMP3Player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public class Controller {
    @FXML
    private ComboBox<String> comboAudioOutput ;

    @FXML
    private Slider sliderMasterVolume;

    public void initialize() {
        // Basics
        // https://docs.oracle.com/javafx/2/media/playercontrol.htm





        // tests WIP


        /*______ AUDIO OUTPUT ______*/
//      How to get list of AudioOutput.
        Mixer.Info[] mixerInfo =  AudioSystem.getMixerInfo();
        ObservableList<String> listAudioOutput = FXCollections.observableArrayList();

        for (Mixer.Info info : mixerInfo) {
            listAudioOutput.add(info.getName());
        }

//        ComboBox<String> comboAudioOutput = new ComboBox<String>();
//        comboAudioOutput.setItems(listAudioOutput);
//        comboAudioOutput.getSelectionModel().select(1);

        /*Play: https://stackoverflow.com/questions/37609430/play-sound-on-specific-sound-device-java*/
        /*______ AUDIO OUTPUT ______*/

        /*______ MASTER LEVEL ______*/
//        sliderMasterVolume.getValue();
        /*______ MASTER LEVEL ______*/
    }
}
