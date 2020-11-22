package myMusicPlayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import myMusicPlayer.AudioPlayer.MyAudioPlayerInterface;
import myMusicPlayer.AudioPlayer.MyMediaPlayer;
import myMusicPlayer.Utils.UtilsDateTime;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * Heart link to MyMusicPlayer.fxml
 */
public class Controller {
    private final Bean bean = new Bean();
    private MyProperties myProperties;
    private MyAudioPlayerInterface myAudioPlayer;

    @FXML // fx:id="comboAudioOutput"
    private ComboBox<String> comboAudioOutput; // Value injected by FXMLLoader

    @FXML // fx:id="labelPlayerName"
    private Label labelPlayerName; // Value injected by FXMLLoader

    @FXML // fx:id="labelPlayerTime"
    private Label labelPlayerTime; // Value injected by FXMLLoader

    @FXML // fx:id="listViewPlaylist"
    private ListView<String> listViewPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="sliderMasterVolume"
    private Slider sliderMasterVolume; // Value injected by FXMLLoader

    @FXML // fx:id="sliderPlayerTime"
    private Slider sliderPlayerTime; // Value injected by FXMLLoader

    /**
     * Handle the events of the buttons
     *
     * @param event ActionEvent to get the node ID.
     */
    @FXML
    void handleButtonAction(ActionEvent event) {
        String id = ((Node) event.getSource()).getId();
        switch (id) {
            case "buttonPlayerStop":
                myAudioPlayer.stop();
                break;
            case "buttonPlayerPause":
                myAudioPlayer.pause();
                break;
            case "buttonPlayerPlay":
                myAudioPlayer.play();
                break;
            case "buttonPlayerNext":
                myAudioPlayer.next();
                break;
            case "buttonPlayerPrevious":
                myAudioPlayer.previous();
                break;
            case "buttonPlaylistAdd":
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", myProperties.getFormats()));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("AudioPlayer", myAudioPlayer.getFormats()));
                if (!myProperties.getPathToMusic().toString().isEmpty()) {
                    fileChooser.setInitialDirectory(new File(myProperties.getPathToMusic().toString()));
                }
                List<File> fileList = fileChooser.showOpenMultipleDialog(null);
                if (fileList != null) {
                    for (File file : fileList) {
                        if (file != null) {
//                            bean.getQueue().add(file.toString());
                            myAudioPlayer.add(file.toString());
//                            listViewPlaylist.getItems().add(file.getName());
                            myProperties.setPathToMusic(Paths.get(file.toURI()).getParent());
                        }
                    }
                    myAudioPlayer.setMedia();
                }
                break;
            case "buttonPlaylistRemove":
                if (listViewPlaylist.getSelectionModel().getSelectedIndex() >= 0) {
                    myAudioPlayer.remove(listViewPlaylist.getSelectionModel().getSelectedIndex());
//                    listViewPlaylist.getItems().remove(listViewPlaylist.getSelectionModel().getSelectedIndex());
                    if (listViewPlaylist.getSelectionModel().getSelectedIndex() == 0) {
                        myAudioPlayer.stop();
                        if (listViewPlaylist.getItems().size() > 1) {
                            myAudioPlayer.setMedia();
                        }
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    /**
     * Handle the events of the combos
     *
     * @param event ActionEvent to get the node ID.
     */
    @FXML
    void handleComboAction(ActionEvent event) {
        String id = ((Node) event.getSource()).getId();
        if ("comboAudioOutput".equals(id)) {
            // WIP: 20/11/2020 AudioOutput + MediaPlayer
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            for (Mixer.Info info : mixerInfo) {
                if (info.getName().equals(comboAudioOutput.getSelectionModel().getSelectedItem())) {
//                    try {
//                        Clip clip = AudioSystem.getClip(AudioSystem.getMixer(info).getMixerInfo());
//                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(listViewPlaylist.getItems().get(0)));
//                        clip.open(inputStream);
//                        clip.start();
//                    } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
//                        e.printStackTrace();
//                    }
                    myProperties.setAudioOutput(info);
                    break;
                }
            }
        }
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete
     */
    @FXML
    void initialize() {
        assert comboAudioOutput != null : "fx:id=\"comboAudioOutput\" was not injected: check your FXML file 'MyMusicPlayer.fxml'.";
        assert labelPlayerName != null : "fx:id=\"labelPlayerName\" was not injected: check your FXML file 'MyMusicPlayer.fxml'.";
        assert labelPlayerTime != null : "fx:id=\"labelPlayerTime\" was not injected: check your FXML file 'MyMusicPlayer.fxml'.";
        assert listViewPlaylist != null : "fx:id=\"listViewPlaylist\" was not injected: check your FXML file 'MyMusicPlayer.fxml'.";
        assert sliderMasterVolume != null : "fx:id=\"sliderMasterVolume\" was not injected: check your FXML file 'MyMusicPlayer.fxml'.";
        assert sliderPlayerTime != null : "fx:id=\"sliderPlayerTime\" was not injected: check your FXML file 'MyMusicPlayer.fxml'.";

        myAudioPlayer = new MyMediaPlayer(bean);

        /*______ AUDIO OUTPUT ______*/
        // How to get list of AudioOutput.
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        ObservableList<String> listAudioOutput = FXCollections.observableArrayList();
        // Output
        Line.Info playbackLine = new Line.Info(SourceDataLine.class);
        // Input
//        Line.Info captureLine = new Line.Info(TargetDataLine.class);

        for (Mixer.Info info : mixerInfo) {
            // Filter on Output
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.isLineSupported(playbackLine)) {
                listAudioOutput.add(info.getName());
            }
        }
        comboAudioOutput.setItems(listAudioOutput);
        /*______ AUDIO OUTPUT ______*/

        /*______ MASTER LEVEL ______*/
        myAudioPlayer.setVolume(sliderMasterVolume.getValue() / 100.0);

        sliderMasterVolume.valueProperty().addListener(observable -> {
            myAudioPlayer.setVolume(sliderMasterVolume.getValue() / 100.0);
            myProperties.setMasterVolume(sliderMasterVolume.getValue());
        });
        /*______ MASTER LEVEL ______*/

        /*______ TIME SLIDER ______*/
        sliderPlayerTime.valueProperty().addListener(observable -> bean.setTime(myAudioPlayer.getDuration() * sliderPlayerTime.getValue() / 100.0));
        bean.timeProperty().addListener(o -> updateTimeValue());
        /*______ TIME SLIDER ______*/

        /*______ PLAYLIST ______*/
        bean.queueProperty().addListener(observable -> listViewPlaylist.setItems((ObservableList<String>) bean.getQueue()));
        /*______ PLAYLIST ______*/

        /*______ LOAD PROPERTIES _____*/
        myProperties = new MyProperties();
        sliderMasterVolume.setValue((myProperties.getMasterVolume()));
        comboAudioOutput.getSelectionModel().select(AudioSystem.getMixer(myProperties.getAudioOutput()).getMixerInfo().getName());
        myProperties.setAutoSave(true);
        /*______ LOAD PROPERTIES _____*/
    }

    /**
     * Update Media time Label & Slider
     */
    public void updateTimeValue() {
        Duration duration = new Duration(myAudioPlayer.getDuration());
        Duration currentTime = new Duration(myAudioPlayer.getTime());
        labelPlayerTime.setText(UtilsDateTime.formatTime(currentTime, duration));
        sliderPlayerTime.setDisable(duration.isUnknown());
        if (!sliderPlayerTime.isDisabled()
                && duration.greaterThan(Duration.ZERO)
                && !sliderPlayerTime.isValueChanging()) {
            sliderPlayerTime.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
        }
    }
}
