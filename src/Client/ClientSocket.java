package Client;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.omg.CosNaming.NamingContextPackage.NotFoundReason;

import javax.activity.InvalidActivityException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Adrian on 15/02/2016.
 */
public class ClientSocket {

    private Scanner scanner = new Scanner(System.in);
    private String handshake = "abc";
    private String expected = "bca";
    private String secondHandShake = "Connect plz";
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
            System.out.println("Could not connect to : ["+host+"] with port number : ["+portNumber+"].");
        }
        start();
    }

    /**
     * Hand shake protocol
     */
    public void handShake() {
        try {
            String i = scanner.next();
            dos.writeUTF(i);
            i = ios.readUTF();
            if(i.equals(expected)) {
                dos.writeUTF(secondHandShake);
            } else {
                throw new InvalidActivityException(i + " is an invalid command!");
            }
        } catch(IOException ioe) {
            System.out.println("Could not send message: " + handshake);
        }
    }

    /**
     * Sets the new light routine for the controller
     * @param red
     * @param yellow
     * @param green
     */
    public void setLightRoutine(int red, int yellow, int green) {

    }

    /**
     * Initializes running loop
     */
    private void start() {
        String input;
        while(true) {
            input=scanner.next();
            System.out.println(handle(input));
        }
    }

    /**
     * Handles input from the user via switch-case
     * @param s
     * @return an appropriate String to the input.
     */
    private String handle(String s) {
        switch (s) {
            case ("exit") : {
                try{
                    if(socket != null) socket.close();
                } catch (IOException ioe) {
                    System.out.println("No socket to close");
                }
                System.out.println("Closing client");
                System.exit(0);
            }
            default: return "Unsupported command!";
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
