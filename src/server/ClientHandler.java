package server;

import logging.CustomLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;

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
    private TrafficServer trafficServer;
    private TrafficController trafficController;
    private CustomLogger logger = CustomLogger.getInstance();


    /**
     * Creates a ClientHandler with the given values.
     * @param trafficServer The server {@link server.TrafficServer}
     * @param serverSocket The server socket {@link ServerSocket}
     * @param trafficController The main controller {@link server.TrafficController}
     */
    public ClientHandler(TrafficServer trafficServer,ServerSocket serverSocket,TrafficController trafficController){
        this.trafficController = trafficController;
        this.trafficServer = trafficServer;
        this.serverSocket = serverSocket;
        this.validatingService = new ServiceQueue(1);
    }


    /**
     * Starts the ClientHandler and start accepting incoming connections
     */
    public void run(){
        while(!this.isInterrupted())
        {
            try
            {
                this.logger.log("Waiting for client on Port: " + serverSocket.getLocalPort() + "", Level.FINE);
                Socket clientSocket = serverSocket.accept();
                this.logger.log("Socket connected "+clientSocket.getInetAddress() +":"+clientSocket.getPort(),Level.FINE);
                validatingService.execute(new ValidateConnection(clientSocket,this.trafficController));
            }catch(SocketTimeoutException s)
            {
                System.out.println("Socket timed out!");
                break;
            }catch (SocketException se){
                this.logger.log("this socket's close status is "+this.serverSocket.isClosed(),Level.FINE);
                se.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
        this.logger.log("ClientHandler interupted",Level.FINE);
    }

}//End of class ClientHandler
