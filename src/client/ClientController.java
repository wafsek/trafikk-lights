package client;

import javafx.stage.Stage;

import java.net.Socket;

/**
 * Controller class
 * @author Adrian Siim Melsom
 * @author Anh Thu Pham Le
 */
public class ClientController {

    private Thread clientSocket;
    private ClientGUI clientGUI;
    private boolean connected;

    /**
     * Constructor which initiated the GUI.
     * @param primaryStage JavaFX Stage
     */
    public ClientController(Stage primaryStage) {
        clientGUI = new ClientGUI(primaryStage, this);
        connected = false;
        clientSocket = null;
    }

    /**
     * Requests a hand shake with the server via the socket created,
     * based on the two last parameters host and portNumber.
     * @param handshake String
     * @param host String
     * @param portNumber int
     */
    public void requestConnection(String handshake, String host, int portNumber) {
        if(!connected && clientSocket == null){
            clientSocket = new ClientSocket(this);
            connected = ((ClientSocket)this.clientSocket).connect(host, portNumber, handshake);
            if(connected) {
                clientSocket.start();
            } else {
                clientSocket = null;
            }
        }
    }

    /**
     * Changes a state variable which keeps track of if there is a connection or not.
     */
    public void disconnect() {
        clientSocket = null;
        connected = false;
    }

    /**
     * Linking method between socket and GUI for changing the light sequence.
     * @param red int
     * @param yellow int
     * @param green int
     */
    public void changeLightSequence(int red, int yellow, int green) {
        clientGUI.changeLightSequence(red, yellow, green);
    }

    /**
     * Sets the display mode on GUI to idle.
     */
    public void setIdle() {
        clientGUI.idle();
    }
}
