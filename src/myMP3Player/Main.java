package myMP3Player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    }

    /**
     *
     * @param args launch arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
