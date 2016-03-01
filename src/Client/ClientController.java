package Client;

import javafx.stage.Stage;
import server.Terminal;

/**
 * Created by Adrian on 25/02/2016.
 */
public class ClientController {

    private Thread clientSocket;
    private ClientGUI clientGUI;

    public ClientController(Stage primaryStage, String host, int portNumber) {
        clientGUI = new ClientGUI(primaryStage, this);
    }

    public void requestConnection(String handshake, String host, int portNumber) {
        clientSocket = new ClientSocket(this);
        if(this.clientSocket instanceof ClientSocket){
            ((ClientSocket)this.clientSocket).connect(host, portNumber);
        }
        clientSocket.start();
    }

    public void changeLightSequence(int red, int yellow, int green) {
        clientGUI.changeLightSequence(red, yellow, green);
    }
}
