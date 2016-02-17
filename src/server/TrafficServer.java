package server;
import java.net.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * The class that handles all the basic socket related chores.
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class TrafficServer{
    //The Constants
    private final int BUFFERSIZE = 20;
    private final int LOOPBACKTIME = 500;
    private final int SERVICESWORKERS = 2;
    private final int TERMINATORS = 1;

    //Variables
    private ServerSocket serverSocket ;
    private static TrafficServer trafficServer;
    public List<Client> clientArrayList  = new CopyOnWriteArrayList<Client>();
    private ServiceQueue trafficService;
    private ServiceQueue socketTerminator;
    private Thread clientHandler;
    private boolean stopped = false;


    /**
     * Creats a TrafficServer and set sets the following parameters.
     * @param port port-number
     * @throws IOException Input/output Exception
     */
    private TrafficServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        //serverSocket.setSoTimeout(100);
    }


    /**
     * Starts the main server.
     */
    public void start() {
        clientHandler = new ClientHandler(this.serverSocket);
        clientHandler.start(); //Starts accepting incoming connections.
        this.serverForever();
    }

    /**
     * Loops thro all the clients. If it finds any data on anyone of them it creates a task
     * and gives it to a ServiceQueue type. If it finds a broken socket, its deletes it form the list.
     * IMPORTANT! This This method assumes the a char is one byte. This might not always be
     * true everywhere. The portability of this method is not a guarantee.
     */
    public void serverForever() {
        trafficService = new ServiceQueue(SERVICESWORKERS);
        socketTerminator = new ServiceQueue(TERMINATORS);
        while (!stopped) {
            //System.out.println(this.clientArrayList.size());
            try {
                Thread.sleep(LOOPBACKTIME);
            } catch (InterruptedException ie) {
                //This should be handled properly --Sarai:P
            }
            //System.out.println("TICK");
            for(Client client: this.clientArrayList){
                try{
                    client.getDataOutputStream().writeUTF("T");
                    if( client.getDataInputStream().available()> 0){
                        trafficService.execute(new ServiceTask(client,BUFFERSIZE
                        ));
                    }
                }catch (IOException ioe){
                    System.out.println("Something on this socket is not right :)");
                    socketTerminator.execute(new SocketTerminate(client,ioe));
                    //This task Should be given to the socketTerminator that
                    // i have made above as the server must go on and can not wait for
                    // closing the socket properly as this could risk the server. :) --Sarai
                }
            }//System.out.println("TACK");
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

