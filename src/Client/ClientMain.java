package Client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Adrian Siim Melsom, Anh Thu Pham Le
 */
public class ClientMain extends Application{



    public static void main(String... args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        new ClientController(primaryStage);
    }
}
