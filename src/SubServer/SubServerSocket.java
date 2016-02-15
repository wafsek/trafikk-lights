package SubServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Adrian on 15/02/2016.
 */
public class SubServerSocket {

    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket socket;

    /**
     * Initializes the SubServerSocket
     * @param host
     * @param portNumber
     */
    public SubServerSocket(String host, int portNumber) {
        try {
            socket = new Socket(host, portNumber);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Could not connect to : ["+host+"] with port number : ["+portNumber+"].");
        }
    }

    /**
     * Handles a request to connect
     * @return String
     */
    public String clientConnect(String host, int portNumber) {
        throw new UnsupportedOperationException();
    }

    /**
     * Validates "hand shake" input
     * @return String
     */
    private String validate() {
        throw new UnsupportedOperationException();
    }
}
