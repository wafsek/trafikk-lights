package client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class
 * @author Adrian Siim Melsom
 * @author Anh Thu Pham Le
 */
public class ClientMain extends Application{

    /**
     * Main method
     * @param args String...
     */
    public static void main(String... args) {
        launch(args);
    }

    /**
     * Initiantes the program controller
     * @param primaryStage JavaFX Stage
     */
    public void start(Stage primaryStage) {
        new ClientController(primaryStage);
    }
}
