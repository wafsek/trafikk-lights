import javafx.application.Application;
import javafx.stage.Stage;
import logging.CustomLogger;
import server.TrafficController;

/**
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class ServerMain extends Application{
    private static Thread terminal;
    private CustomLogger logger = CustomLogger.getInstance();

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(e->System.exit(0));
        TrafficController trafficController = new TrafficController();
        trafficController.start(primaryStage);
        //terminal = new Terminal();
        //terminal.start();
    }
}
