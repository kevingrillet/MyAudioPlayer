package myMP3Player;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import myMP3Player.Utils.UtilsDateTime;
import myMP3Player.Utils.UtilsProperties;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Heart link to MyMP3Player.fxml
 */
public class Controller {
    private Duration duration;
    private List<String> listPath;
    private Map<String,String> mapProperties;
    private MediaPlayer mediaPlayer;
    private Path path;

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
     * @param event ActionEvent to get the node ID.
     */
    @FXML
    void handleButtonAction(ActionEvent event) {
        String id = ((Node) event.getSource()).getId();
        switch (id) {
            case "buttonPlayerStop":
                if (!(mediaPlayer == null)) {
                    if ( mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.stop();
                        mediaPlayer.seek(mediaPlayer.getStartTime());
                    }
                }
                break;
            case "buttonPlayerPause":
                if (!(mediaPlayer == null)) {
                    if ( mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.pause();
                    }}
                break;
            case "buttonPlayerPlay":
                if (!(mediaPlayer == null)) {
                    if ( mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
                            || mediaPlayer.getStatus() == MediaPlayer.Status.READY
                            || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                        mediaPlayer.play();
                    }}
                break;
            case "buttonPlaylistAdd":
                FileChooser fileChooser = new FileChooser();
//                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.mp3"));
                if (!path.toString().isEmpty()) {
                    fileChooser.setInitialDirectory(new File(path.toString()));
                }
                List<File> fileList = fileChooser.showOpenMultipleDialog(null);
                for (File file : fileList){
                    if (file != null) {
                        path = Paths.get(file.toURI());
                        path = path.getParent();

                        listPath.add(file.toString());
                        listViewPlaylist.getItems().add(file.getName());
//                        setMedia(file);

                        mapProperties.replace("pathToMusic", path.toString());
                        UtilsProperties.writeProperties(mapProperties);
                    }
                }
                setMedia(new File(listPath.get(0)));
                break;
            case "buttonPlaylistRemove":
                if (listViewPlaylist.getSelectionModel().getSelectedIndex()>=0){
                    if (listViewPlaylist.getSelectionModel().getSelectedIndex() == 0) {
                        if (!(mediaPlayer == null)) {
                            mediaPlayer.stop();
                            if (listViewPlaylist.getItems().size() > 1) {
                                setMedia(new File(listPath.get(1)));
                            }
                        }
                    }
                    listPath.remove(listViewPlaylist.getSelectionModel().getSelectedIndex());
                    listViewPlaylist.getItems().remove(listViewPlaylist.getSelectionModel().getSelectedIndex());
                }
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
            // WIP: 20/11/2020 AudioOutput + MediaPlayer
//            Mixer.Info[] mixerInfo =  AudioSystem.getMixerInfo();
//            for (Mixer.Info info : mixerInfo) {
//                if (info.getName().equals(comboAudioOutput.getSelectionModel().getSelectedItem())) {
//                    try {
//                        Clip clip = AudioSystem.getClip(AudioSystem.getMixer(info).getMixerInfo());
//                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(listViewPlaylist.getItems().get(0)));
//                        clip.open(inputStream);
//                        clip.start();
//                    } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
//            }
            mapProperties.replace("audioOutput",comboAudioOutput.getSelectionModel().getSelectedItem());
            UtilsProperties.writeProperties(mapProperties);
        }
    }

    /**
     *  This method is called by the FXMLLoader when initialization is complete
     */
    @FXML
    void initialize() {
        assert comboAudioOutput != null : "fx:id=\"comboAudioOutput\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert labelPlayerName != null : "fx:id=\"labelPlayerName\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert labelPlayerTime != null : "fx:id=\"labelPlayerTime\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert listViewPlaylist != null : "fx:id=\"listViewPlaylist\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert sliderMasterVolume != null : "fx:id=\"sliderMasterVolume\" was not injected: check your FXML file 'MyMP3Player.fxml'.";
        assert sliderPlayerTime != null : "fx:id=\"sliderPlayerTime\" was not injected: check your FXML file 'MyMP3Player.fxml'.";

        listPath = new ArrayList<>();

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

        /*______ LOAD PROPERTIES _____*/
        mapProperties = UtilsProperties.readProperties();

        sliderMasterVolume.setValue(Double.parseDouble(mapProperties.get("masterVolume")));
        String mixerName = mapProperties.get("audioOutput");
        if (mixerName.isEmpty()) {
            // set default output with getMixer(null)
            comboAudioOutput.getSelectionModel().select(AudioSystem.getMixer(null).getMixerInfo().getName());
        }else{
            for (Mixer.Info info : mixerInfo) {
                if (info.getName().equals(mixerName)) {
                    comboAudioOutput.getSelectionModel().select(AudioSystem.getMixer(info).getMixerInfo().getName());
                    break;
                }
            }
        }
        path = Paths.get(mapProperties.get("pathToMusic"));
        /*______ LOAD PROPERTIES _____*/
    }

    /**
     *
     * @param file MediaFile to Read
     */
    private void setMedia (File file) {
        /*______ MEDIA PLAYER ______*/
        // https://docs.oracle.com/javafx/2/media/playercontrol.htm

        Media media = new Media(file.toURI().toString());
        labelPlayerName.setText("");
        media.getMetadata().addListener((MapChangeListener.Change<? extends String,?> c)-> {
            if (c.wasAdded()){
                if ("title".equals(c.getKey())){
                    labelPlayerName.setText(c.getValueAdded().toString());
                }
//                else if ("artist".equals(c.getKey())){
//                    System.out.println(c.getValueAdded().toString());
//                }else if ("album".equals(c.getKey())){
//                    System.out.println(c.getValueAdded().toString());
//                }
            }
        });
        if (labelPlayerName.getText().isEmpty()) {
            String title = media.getSource();
            title = title.substring(0, title.length() - ".mp3".length());
            title = title.substring(title.lastIndexOf("/") + 1).replaceAll("%20", " ");
            labelPlayerName.setText(title);
        }

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.currentTimeProperty().addListener(observable -> updateTimeValue());
        mediaPlayer.setOnReady(() -> {
            duration = mediaPlayer.getMedia().getDuration();
            updateTimeValue();
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            mediaPlayer.seek(new Duration(0));
            listPath.remove(0);
            listViewPlaylist.getItems().remove(0);
            if (listViewPlaylist.getItems().size() > 0) {
                setMedia(new File(listPath.get(0)));
                mediaPlayer.play();
            }
        });
        /*______ MEDIA PLAYER ______*/

        /*______ MASTER LEVEL ______*/
        mediaPlayer.setVolume(sliderMasterVolume.getValue() / 100.0);

        sliderMasterVolume.valueProperty().addListener(observable -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(sliderMasterVolume.getValue() / 100.0);
                mapProperties.replace("masterVolume", String.valueOf(sliderMasterVolume.getValue()));
                UtilsProperties.writeProperties(mapProperties);
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
     *  Update Media time Label & Slider
     */
    private void updateTimeValue(){
        Duration currentTime = mediaPlayer.getCurrentTime();
        labelPlayerTime.setText(UtilsDateTime.formatTime(currentTime, duration));
        sliderPlayerTime.setDisable(duration.isUnknown());
        if (!sliderPlayerTime.isDisabled()
                && duration.greaterThan(Duration.ZERO)
                && !sliderPlayerTime.isValueChanging()) {
            sliderPlayerTime.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
        }
    }
}
