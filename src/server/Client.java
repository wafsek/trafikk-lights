package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Baljit Sarai on 15.02.16.
 * @author Baljit Sarai
 */
public class Client {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;


    public Client(Socket socket){
        this.socket = socket;
        try{
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
        }catch (IOException ioe){
            System.out.println("Could not creat the out/input streams");
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataInputStream getDataInputStream(){
        return in;
    }

    public String getMessage(){
        String message = null;
        try{
            message = in.readUTF();
        }catch (IOException ioe){
            //Must handle this at some time
        }
        return message;
    }

}
