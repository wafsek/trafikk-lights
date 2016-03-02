package Client;

import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextInputDialog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by Adrian on 15/02/2016.
 */
public class ClientSocket extends Thread{

    private DataInputStream dis;
    private Socket socket;
    private DataOutputStream dos;
    private String host;
    private int portNumber;
    private TextInputDialog tid;
    private ClientController clientController;
    private static final String[] COMMANDS = {"CC", "TM"};
    private static final byte[] PING = {0,2,67,67,0,0,0,0,0,0};
    private final int OFFSET = 2;
    private final String expected = "secret";
    private boolean conntected;
    private final int BUFFERSIZE = 20;


    /**
     * Initializes the client socket
     */
    public ClientSocket(ClientController clientController) {
        this.clientController = clientController;
        tid = new TextInputDialog();
        tid.setTitle("Second Handshake");
        tid.setContentText("Please enter the second handshake");
    }

    public boolean connect(String host, int portNumber, String handshake) {
        try {
            this.host = host;
            this.portNumber = portNumber;
            socket = new Socket(this.host, this.portNumber);
            if(socket != null) {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                handshake(handshake);
                return true;
            }
        } catch(IOException ioe) {
            System.out.println("Could not connect to : ["+host+"] with port number : ["+portNumber+"].\n");
            ioe.printStackTrace();
        }
        return false;
    }

    private boolean handshake(String handshake) throws IOException{
        byte[] content = toByteArray(handshake, 3);
        dos.write(content);
        clearBuffer(content);
        dis.read(content, 0, BUFFERSIZE);
        if(!compare(content)) {
            throw new IOException("Invalid handshake");
        }

        Optional<String> hs_two = tid.showAndWait();
        if(hs_two.isPresent()) {
            content = toByteArray(hs_two.get(), 3);
            dos.write(content);
        } else {
            throw new IOException("Not valid input");
        }

        return true;
    }

    private byte[] toByteArray(String handshake, int commandType) {
        byte[] content = new byte[BUFFERSIZE];
        content[0] = (byte)commandType;
        content[1] = (byte)handshake.length();
        for(int i = 0; i < handshake.length(); i++) {
            content[i+OFFSET] = (byte)handshake.charAt(i);
        }
        return content;
    }

    private boolean compare(byte[] content) {
        for(int i = 0; i < content[1]; i++) {
            System.out.println(expected.charAt(i) + " " + (char)content[i+OFFSET]);
            if(expected.charAt(i) != (char)content[i+OFFSET]) {
                return false;
            }
        }
        return true;
    }

    private void clearBuffer(byte[] content) {
        for(int i = 0; i < 20; i++) {
            content[i] = 0;
        }
    }

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
        byte[] content = new byte[20];
        conntected = true;
        while(conntected) {
            try{
                this.dis.read(content, 0, 20);
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
     * @param content
     * @return an appropriate String to the input.
     */
    private void handle(byte[] content) {

        if(content[0] == 0) {
            readChar(content, content[1]);
        } else if(content[0] == 1) {
           readNumeric(content, content[1]);
        } else if(content[0] == 2) {
            readCommand(Arrays.copyOfRange(content, 2, (content[1]+2)));
        }
    }

    private void readCommand(byte[] content) {
        String command = new String(new char[] {(char)content[0], (char)content[1]});
        if(command.equals(COMMANDS[0])) {
            try {
                dos.write(PING);
            } catch (IOException ioe) {
                disconnectSocket();
            }
        }
        else if(command.equals(COMMANDS[1])) {
            if((int)content[3] < 2) {
                setLightRoutine((int)content[2]*1000, 2000, (int)content[4]*1000);
            } else {
                setLightRoutine((int)content[2]*1000, (int)content[3]*1000, (int)content[4]*1000);
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
