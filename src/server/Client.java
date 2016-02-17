package server;

import java.io.*;
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
    private BufferedInputStream bufferedInputStream;
    private BufferedReader bufferedReader;


    public Client(Socket socket){
        this.socket = socket;
        try{

            /*PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);
            this.bufferedReader =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn =
                    new BufferedReader(
                            new InputStreamReader(System.in));

            */
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
            this.bufferedInputStream = new BufferedInputStream(in);
        }catch (IOException ioe){
            System.out.println("Could not creat the out/input streams");
        }
    }

    public Socket getSocket(){
        return this.socket;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BufferedInputStream bufferedInputStream(){
        return this.bufferedInputStream;
    }

    public BufferedReader getBufferedReader(){
        return this.bufferedReader;
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
