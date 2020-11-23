package myMusicPlayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main of MyMP3Player
 */
public class Main extends Application {

    /**
     * @param args launch arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MyMusicPlayer.fxml"));
        primaryStage.setTitle("MyMP3Player");
        primaryStage.setScene(new Scene(root/*, 300, 275*/));
        primaryStage.show();
    }
}