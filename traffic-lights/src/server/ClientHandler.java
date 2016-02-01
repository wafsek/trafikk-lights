package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Sarai
 */
public class ClientHandler implements Runnable {

    private ServerSocket serverSocket;
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

}
