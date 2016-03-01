package Client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Baljit Singh Sarai on 15.02.16.
 * @author Baljit Singh Sarai
 */
public class ClientMain extends Application{



    public static void main(String... args) {

        launch(args);
    }

    public void start(Stage primaryStage) {
        new ClientController(primaryStage, "localhost", 12345);


    }
}
