package Client;

import javafx.application.Platform;

import javax.activity.InvalidActivityException;
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
    private static final String[] COMMANDS = {"CC", "TM"};
    private static final byte[] PING = {0,2,67,67,0,0,0,0,0,0};
    private boolean conntected;


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
    /*public void handShake(String handshake) {
        try {
            String i = scanner.nextLine();
            dos.writeUTF(i);
            i = dis.readUTF();
            if(i.equals(expected)) {
                dos.writeUTF(secondHandShake);
            } else {
                throw new InvalidActivityException(i + " is an invalid command!");
            }
        } catch(IOException ioe) {
            System.out.println("Could not send message: " + handshake);
        }
    }*/

    /**
     * Sets the new light routine for the controller
     * @param red
     * @param yellow
     * @param green
     */
    public void setLightRoutine(int red, int yellow, int green) {
        clientController.changeLightSequence(red,yellow,green);
    }

    /**
     * Initializes running loop
     */
    private void startSocket() {
        byte[] content = new byte[10];
        conntected = true;
        while(conntected) {
            try{
                this.dis.read(content, 0, 10);
                for(int i = 0; i < 10; i++) {
                    System.out.println(content[i]);
                }
                handle(content);
            } catch(IOException ioe) {
                disconnectSocket();
            }

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
    private void handle(byte[] content) {

        if(content[0] == 0) {
            readChar(content, content[1]);
        } else if(content[0] == 1) {
           readNumeric(content, content[1]);
        } else if(content[0] == 2) {
            readCommand(Arrays.copyOfRange(content, 1, content[1]));
        }

        /*System.out.println(s);
        String[] a = s.split(",");
        System.out.println(Arrays.toString(a));

        int[] ab = {Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2])};
        Platform.runLater(() -> clientController.changeLightSequence(ab[0], ab[1], ab[2]));*/
    }

    private void readCommand(byte[] content) {
        String command = new String(new char[] {(char)content[0], (char)content[1]});
        if(command.equals(COMMANDS[0])) {
            setLightRoutine((int)content[2]*100, (int)content[3]*100, (int)content[4]*100);
        }
        else if(command.equals(COMMANDS[1])) {
            try {
                dos.write(PING);
            } catch (IOException ioe) {
                disconnectSocket();
            }
        }
    }

    private void readNumeric(byte[] content, int length) {

    }

    private void readChar(byte[] content, int length) {

    }

    private void disconnectSocket() {
        System.out.println("Could not establish connection, closing socket.");
        conntected = false;
    }



    /**
     * returns the status of the light
     * @return
     */
    public String getLightStatus() {
        throw new UnsupportedOperationException();
    }
}
