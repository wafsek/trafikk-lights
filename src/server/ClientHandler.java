package server;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import logging.CustomLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
    private TrafficController trafficController;
    private CustomLogger logger = CustomLogger.getInstance();


    /**
     *
     * @param serverSocket SocketServer Object
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
                this.logger.log("Waiting for client on port" + serverSocket.getLocalPort() + "...", Level.INFO);

                Socket clientSocket = serverSocket.accept();
                this.logger.log("Socket connected "+clientSocket.getInetAddress() +":"+clientSocket.getPort(),Level.FINE);



                ////////////////////////////////////////
                //The code written between these comment lines
                // should be removed later. very important.
                //It bypasses the security and is only going to be used
                // in the developing time.
                //trafficServer.clientArrayList.add(new Client(clientSocket));
                //System.out.println(TrafficServer.getInstance().clientArrayList.size());


                ///////////////////////////////////////


                validatingService.execute(new ValidateConnections(clientSocket,this.trafficController));
            }catch(SocketTimeoutException s)
            {
                System.out.println("Socket timed out!");
                break;
            }catch (SocketException se){
                System.out.println("kadfasdf");
                this.logger.log("this socket's close status is "+this.serverSocket.isClosed(),Level.FINE);
                //Thread.currentThread().interrupt();
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("sssssssss");
        this.logger.log("ClientHandler interupted",Level.FINE);
    }




    /**
     * This method close and deletes the socket and do the cleaning job.
     */
    public void closeSocket(){

    }

}//End of class ClientHandler
