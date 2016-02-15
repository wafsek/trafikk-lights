package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class will contain the task of validating the handshake of the newly incomming connection
 * Created by Baljit Sarai on 15.02.16.
 * @author Baljit Sarai
 */


public class ValidateConnections implements Runnable{

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ValidateConnections(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        this.handshakeProtocol(this.socket);
        System.out.println("Validating the socket");
        //This method is going to be the methos that validates the connection
        // If this mathod does more then just the hand shake----we will change the name of
        // the class :)
    }

    /**
     *Handshake protocol
     *
     */
    public void handshakeProtocol(Socket socket){
        String o;
        try{
            System.out.println("waiting for msg");
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            System.out.println("msssssssssssssssss");
            o = in.readUTF();
            System.out.println("message recived");
            System.out.println(o);
            out.writeUTF("bca");
            System.out.println(in.readUTF());
        }catch (IOException io ){
            System.out.println("Something went wrong");
        }

        //This is where i intent to write code to handle the incoming connections- Baljit Sarai.
    }
}


