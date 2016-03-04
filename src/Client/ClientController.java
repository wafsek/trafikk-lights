package Client;

import javafx.stage.Stage;

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
     * @param primaryStage
     */
    public ClientController(Stage primaryStage) {
        clientGUI = new ClientGUI(primaryStage, this);
        connected = false;
        clientSocket = new ClientSocket(this);
    }

    /**
     * Requests a hand shake with the server via the socket created,
     * based on the two last parameters host and portNumber.
     * @param handshake
     * @param host
     * @param portNumber
     */
    public void requestConnection(String handshake, String host, int portNumber) {
        if(!connected && this.clientSocket instanceof ClientSocket){
            connected = ((ClientSocket)this.clientSocket).connect(host, portNumber, handshake);
            if(connected) clientSocket.start();
        }
    }

    /**
     * Changes a state variable which keeps track of if there is a connection or not.
     */
    public void disconnect() {
        connected = false;
    }

    /**
     * Linking method between socket and GUI for changing the light sequence.
     * @param red
     * @param yellow
     * @param green
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
