package server;
import java.net.*;
import java.io.*;
import java.util.ArrayList;


/**
 * The class that handles all the basic socket related chores.
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Sarai
 */
public class TrafficServer {
    private ServerSocket serverSocket ;
    private static TrafficServer trafficServer;
    private ClientHandler clientHandler;
    protected static  ArrayList<Client> clientArrayList  = new ArrayList<>();


    /**
     *
     * @param port port-number
     * @throws IOException Input/output Exception
     */
    private TrafficServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        //serverSocket.setSoTimeout(10000);
    }


    /**
     * Starts the main server.
     */
    public void start() {
        clientHandler = new ClientHandler(this.serverSocket);
        clientHandler.run(); //Starts accepting incoming connections.

    }

    /**
     * Shutdowns the server.
     */
    public void shutdown(){

    }

    public void startAcceptingConnections(){

    }


    /**
     * The method is used to restrain this class to a singleton.
     * @return TrafficServer
     */
    public static TrafficServer getInstance(){
        if(trafficServer == null){
            try{
                trafficServer = new TrafficServer(12345);
            }catch (IOException e){
                System.out.println("Could not create the server instance");
            }

        }
        return trafficServer;
    }
}//End of class TrafficServer

