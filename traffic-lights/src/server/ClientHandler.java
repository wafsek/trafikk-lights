package server;

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
                //This is where i intent to write code to handle the incoming connections- Baljit Sarai

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

}//End of class ClientHandler
