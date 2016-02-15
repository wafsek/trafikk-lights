package server;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.AbstractList;

/**
 * This class primarily accepts incoming connections and deals with them appropriately.
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class ClientHandler extends Thread {

    private ServerSocket serverSocket;
    private DataOutputStream out;
    private DataInputStream in;


    /**
     *
     * @param serverSocket SocketServer Object
     */
    public ClientHandler(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }


    /**
     * Starts the ClientHandler and start accepting incoming connections
     */
    public void run(){

        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from: "+clientSocket.getInetAddress());
                this.handshakeProtocol(clientSocket);


            }catch(SocketTimeoutException s)
            {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
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
            System.out.println("Somthing went wrong");
        }

        //This is where i intent to write code to handle the incoming connections- Baljit Sarai.
    }

    /**
     * This method close and deletes the socket and do the cleaning job.
     */
    public void closeSocket(){

    }

}//End of class ClientHandler
