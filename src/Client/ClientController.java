package Client;

import javafx.stage.Stage;

/**
 * Created by Adrian on 25/02/2016.
 */
public class ClientController {

    private Thread clientSocket;
    private ClientGUI clientGUI;

    public ClientController(Stage primaryStage, String host, int portNumber) {
        clientSocket = new ClientSocket();
        clientSocket.start();
        System.out.println("dsafasdf");
        clientGUI = new ClientGUI(primaryStage, this);
    }

    public void requestConnection(String handshake) {
        if(this.clientSocket instanceof ClientSocket){

            ((ClientSocket)this.clientSocket).handShake(handshake);
        }
    }

}
