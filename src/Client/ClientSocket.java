package Client;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Adrian on 15/02/2016.
 */
public class ClientSocket extends Thread{

    private Scanner scanner = new Scanner(System.in);
    private String handshake = "abc";
    private String expected = "bca";
    private String secondHandShake = "Connect plz";
    private DataInputStream dis ;
    private Socket socket;
    private DataOutputStream dos;
    private String host;
    private int portNumber;
    private ClientController clientController;


    /**
     * Initializes the client socket
     */
    public ClientSocket(ClientController clientController) {
        this.clientController = clientController;
    }

    public void connect(String host, int portNumber) {
        try {
            this.host = host;
            this.portNumber = portNumber;
            socket = new Socket(this.host, this.portNumber);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch(IOException ioe) {
            System.out.println("Could not connect to : ["+host+"] with port number : ["+portNumber+"].");
        }
    }

    /**
     * Hand shake protocol
     */
    /* boolean handShake(String handshake) {
        try {
            String i = scanner.nextLine();
            dos.writeUTF(i);
            i = dis.readUTF();
            if(i.equals(expected)) {
                dos.writeUTF(secondHandShake);
                return true;
            } else {
                throw new InvalidActivityException(i + " is an invalid command!");
            }
        } catch(IOException ioe) {
            System.out.println("Could not send message: " + handshake);
        }

    }/*

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
    private void startSocket() {
        String input;
        while(true) {
            input=scanner.next();
            handle(input);
        }
    }

    public void run() {
        this.startSocket();
    }

    /**
     * Handles input from the user via switch-case
     * @param s
     * @return an appropriate String to the input.
     */
    private void handle(String s) {
        /*switch (s) {
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
        }*/
        /*try {
            dos.writeUTF(s);
        } catch(IOException ioe) {
            System.out.println("Could not send: "+s);
        }*/

        System.out.println(s);
        String[] a = s.split(",");
        System.out.println(Arrays.toString(a));

        int[] ab = {Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2])};
        Platform.runLater(() -> clientController.changeLightSequence(ab[0], ab[1], ab[2]));
    }



    /**
     * returns the status of the light
     * @return
     */
    public String getLightStatus() {
        throw new UnsupportedOperationException();
    }
}
