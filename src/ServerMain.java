import ServerGUI.ServerGUI;
import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.application.Application;
import javafx.stage.Stage;
import server.Terminal;
import server.TrafficController;
import server.TrafficServer;

/**
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class ServerMain extends Application{
    private static Thread terminal;


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Welcome to the traffic light program");
        TrafficController trafficController = new TrafficController();
        trafficController.start(primaryStage);
        /*terminal = new Terminal();
        terminal.start();
        Thread serverGUI = new ServerGUI(primaryStage);
        serverGUI.run();*/
    }
}
