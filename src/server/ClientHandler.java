package server;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import logging.CustomLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.AbstractList;
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
    private CustomLogger logger = CustomLogger.getInstance();


    /**
     *
     * @param serverSocket SocketServer Object
     */
    public ClientHandler(TrafficServer trafficServer,ServerSocket serverSocket){
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
                this.logger.log("Waiting for client on port" + serverSocket.getLocalPort() + "...", Level.INFO);

                Socket clientSocket = serverSocket.accept();
                this.logger.log("Client connected from: "+clientSocket.getInetAddress() +":"+clientSocket.getPort(),Level.INFO);


                ////////////////////////////////////////
                //The code written between these comment lines
                // should be removed later. very important.
                //It bypasses the security and is only going to be used
                // in the developing time.
                trafficServer.clientArrayList.add(new Client(clientSocket));
                //System.out.println(TrafficServer.getInstance().clientArrayList.size());


                ///////////////////////////////////////


                //validatingService.execute(new ValidateConnections(clientSocket));
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
