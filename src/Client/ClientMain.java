package Client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Baljit Singh Sarai on 15.02.16.
 * @author Baljit Singh Sarai
 */
public class ClientMain extends Application{



    public static void main(String... args) {
        
        String a = "/time";
        String b = "asd/time";
        String c = "/timeasd";

        String reg = "(?m)^(/time)$";

        System.out.println("a = " + a.matches(reg) + "\nb = " + b.matches(reg) + "\nc = " + c.matches(reg));

        launch(args);
    }

    public void start(Stage primaryStage) {
        new ClientController(primaryStage, "localhost", 12345);


    }
}
