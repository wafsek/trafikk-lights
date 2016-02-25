package server;

import java.io.*;
import java.net.Socket;

/**
 * Created by Baljit Sarai on 15.02.16.
 * @author Baljit Singh Sarai
 */
public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;

    /**
     *  Creats a new Client given the following parameters.
     * @param socket -Socket object.
     */
    public Client(Socket socket){
        this.name = "anonymous";
        this.socket = socket;
        try{
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
        }catch (IOException ioe){
            System.out.println("Could not create the out/input streams");
        }
    }


    /**
     * Returns the socket {@link Socket}
     * @return socket -Socket object
     */
    public Socket getSocket(){
        return this.socket;
    }

    /**
     * Returns the name of this client {@link String}
     * @return name -String object.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this client.
     * @param name -String object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the DataOutStream field.
     * @return out -DataOutputStream object.
     */
    public DataOutputStream getDataOutputStream(){
        return out;
    }

    /**
     * Returns the DataOutStream field.
     * @return out -DataOutputStream object.
     */
    public DataInputStream getDataInputStream(){
        return in;
    }
}
