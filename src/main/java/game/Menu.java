package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Menu extends Application {

    final static private Logger logger = LogManager.getLogger(Menu.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Brick Breaker");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        logger.warn("Stage shown {}", primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}