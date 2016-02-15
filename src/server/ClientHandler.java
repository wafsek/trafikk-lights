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
    private ServiceQueue validatingService;



    /**
     *
     * @param serverSocket SocketServer Object
     */
    public ClientHandler(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
        this.validatingService = new ServiceQueue(1);
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
                validatingService.execute(new ValidateConnections(clientSocket));


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
     * This method close and deletes the socket and do the cleaning job.
     */
    public void closeSocket(){

    }

}//End of class ClientHandler
