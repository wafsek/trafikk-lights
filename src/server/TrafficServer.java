package server;
import com.sun.istack.internal.Nullable;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * The class that handles all the basic socket related chores.
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class TrafficServer{
    private ServerSocket serverSocket ;
    private static TrafficServer trafficServer;
    public List<Client> clientArrayList  = new CopyOnWriteArrayList<Client>();
    private int roundRobinTimeout;
    private ArrayList<Socket> socketArrayList = new ArrayList<Socket>();
    private ServiceQueue trafficService;
    private ServiceQueue socketTerminator;
    private Thread clientHandler;

    private boolean stoped  = false;


    /**
     *
     * @param port port-number
     * @throws IOException Input/output Exception
     */
    private TrafficServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        this.roundRobinTimeout = 2000;
        //serverSocket.setSoTimeout(10000);
    }


    /**
     * Starts the main server.
     */
    public void start() {
        clientHandler = new ClientHandler(this.serverSocket);
        clientHandler.start(); //Starts accepting incoming connections.
        trafficService = new ServiceQueue(1);
        this.serverForever();
    }

    /**
     * Waits, Collect all the incoming messages and give them to a ServiceQueue type.
     */
    public void serverForever() {
        String message;
        trafficService = new ServiceQueue(2);
        //List<Client> =
        while (!stoped) {
            try {
                Thread.sleep(this.roundRobinTimeout);
            } catch (InterruptedException ie) {
                //This should be handled properly --Sarai:P
            }

            for(Client client: this.clientArrayList){
                try{
                    message = client.getDataInputStream().readUTF();
                    System.out.println(message);
                    //if(message != null || message == ""){
                        System.out.println("stj");
                        //trafficService.execute(new ServiceTask(client.getSocket(),(String)message));
                    //}
                }catch (IOException ioe){
                    //This task Should be given to the socketTerminator that
                    // i have made above as the server must go on and can not wait for
                    // closing the socket properly as this could risk the server. :) --Sarai
                }
            }
        }
    }


    /**
     * Shutdowns the server.
     */
    public void shutdown(){

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

