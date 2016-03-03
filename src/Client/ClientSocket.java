package Client;

import javafx.scene.control.TextInputDialog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;

/**
 * This class is a socket for the light client to the server.
 * It's job is to establish connection, maintain it and listen
 * for inputs from the server itself. If something should happen
 * to the connection, it should also handle the situation
 * appropriately.
 * @author Adrian Siim Melsom, Anh Thu Pham Le
 */
public class ClientSocket extends Thread{

    private DataInputStream dis;
    private Socket socket;
    private DataOutputStream dos;
    private String host;
    private int portNumber;
    private TextInputDialog tid;
    private ClientController clientController;
    private static final String[] COMMANDS = {"CC", "TM", "ST"};
    private static final byte[] PING = {0,2,67,67,0,0,0,0,0,0};
    private final int OFFSET = 2;
    private final String expected = "secret";
    private boolean connected;
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

    /**
     * Tries to connect the socket based on the parameters host and portNumber.
     * If the connection is established, there will be sent a handshake
     * to the server. If the server responds with the expected handshake,
     * there will be a pop-up for a second handshake which is then sent to the server.
     * If all the info is correct, connection will not be cut.
     * @param host
     * @param portNumber
     * @param handshake
     * @return boolean
     */
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
        }
        return false;
    }

    /**
     * Extention of connect(). The actual code for the handshake is
     * being executed here.
     * @param handshake
     * @return
     * @throws IOException
     */
    private boolean handshake(String handshake) throws IOException{
        byte[] content = toByteArray(handshake, 3);
        dos.write(content);
        clearBuffer(content);
        int bytesRead = dis.read(content, 0, BUFFERSIZE);
        if(bytesRead == -1) {
            clientController.disconnect();
            disconnectSocket();
            throw new IOException("Connection was cut");
        }

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

    /**
     * Converts a String to a byte[]. Also, the first byte
     * (command defining byte) is
     * @param handshake
     * @param commandType
     * @return byte[]
     */
    private byte[] toByteArray(String handshake, int commandType) {
        byte[] content = new byte[BUFFERSIZE];
        content[0] = (byte)commandType;
        content[1] = (byte)handshake.length();
        for(int i = 0; i < handshake.length(); i++) {
            content[i+OFFSET] = (byte)handshake.charAt(i);
        }
        return content;
    }

    /**
     *
     * @param content
     * @return boolean
     */
    private boolean compare(byte[] content) {
        for(int i = 0; i < content[1]; i++) {
            System.out.println(expected.charAt(i) + " " + (char)content[i+OFFSET]);
            if(expected.charAt(i) != (char)content[i+OFFSET]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Fills the array with 0 on every index.
     * @param content
     */
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
     * Initializes running loop which listens to input from the server and
     * sends it to be processed.
     */
    private void startSocket() {
        byte[] content = new byte[20];
        connected = true;
        while(connected) {
            try{
                this.dis.read(content, 0, 20);
                handle(content);
            } catch(IOException ioe) {
                clientController.setIdle();
                disconnectSocket();
            }

        }
    }

    /**
     * Initiates the code which listens for input from the server.
     */
    public void run() {
        this.startSocket();
    }

    /**
     * Handles input from the user via switch-case
     * @param content
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

    /**
     * reads the input as a command
     * @param content
     */
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
        } else if(command.equals(COMMANDS[2])) {
            clientController.setIdle();
        }
    }

    /**
     * reads the input as a numeric input
     * @param content
     * @param length
     */
    private void readNumeric(byte[] content, int length) {

    }

    /**
     * reads the input as a character sequence.
     * @param content
     * @param length
     */
    private void readChar(byte[] content, int length) {

    }

    /**
     * Disconnects the socket
     */
    private void disconnectSocket() {
        System.out.println("Could not establish connection, closing socket.");
        connected = false;
    }
}
