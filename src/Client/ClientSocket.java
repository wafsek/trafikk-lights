package Client;

import org.omg.CosNaming.NamingContextPackage.NotFoundReason;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Adrian on 15/02/2016.
 */
public class ClientSocket {

    String handshake = "abc";
    String expected = "bca";
    String secondHandShake = "Connect plz";

    private DataInputStream ios;
    private Socket socket;
    private DataOutputStream dos;


    /**
     * Initializes the client socket
     * @param host
     * @param portNumber
     */
    public ClientSocket(String host, int portNumber) {
        try {
            socket = new Socket(host, portNumber);
            dos = new DataOutputStream(socket.getOutputStream());
            ios = new DataInputStream(socket.getInputStream());
        } catch(IOException ioe) {
            System.out.println("Could not connect to : ["+host+"] with port number : ["+portNumber +"].");
        }
    }

    /**
     * Hand shake protocol
     */
    public void handShake() {
        try {
            dos.writeBytes(handshake);
            if(ios.readUTF().equals(expected)) {
                dos.writeBytes(secondHandShake);
            }
        } catch(IOException ioe) {
            System.out.println("Could not send message: " + handshake);
        }
    }

    /**
     * returns the status of the light
     * @return
     */
    public String getLightStatus() {
        throw new UnsupportedOperationException();
    }
}
