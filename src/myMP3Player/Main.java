package myMP3Player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *  Main of MyMP3Player
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MyMP3Player.fxml"));
        primaryStage.setTitle("MyMP3Player");
        primaryStage.setScene(new Scene(root/*, 300, 275*/));
        primaryStage.show();



        /*MusicPlayer musicPlayer = new MusicPlayer();
        FileChooser fileChooser = new FileChooser();
        List<File> fileList = fileChooser.showOpenMultipleDialog(null);
        musicPlayer.addMusic(fileList.stream().map(e -> e.getPath()).collect(Collectors.toList()));

        musicPlayer.play();
        TimeUnit.SECONDS.sleep(2);
        musicPlayer.setVolume(1);
        TimeUnit.SECONDS.sleep(2);
        musicPlayer.setVolume(0.1f);
        TimeUnit.SECONDS.sleep(2);
        musicPlayer.setVolume(0.5f);*/


    }

    /**
     *
     * @param args launch arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
