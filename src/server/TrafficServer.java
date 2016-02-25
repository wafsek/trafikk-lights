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
public class TrafficServer extends Thread{
    //The Constants
    private final int BUFFERSIZE = Config.getBufferSize();
    private final int LOOPBACKTIME = Config.getLoopbackTime();
    private final int SERVICESWORKERS = Config.getServiceWorkers();
    private final int TERMINATORS = Config.getTerminators();

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

    public void run(){
        this.start();
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
     * Loops through all the clients. If it finds any data on anyone of them it creates a task
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
                    client.getDataOutputStream().writeUTF("CC");//Just to check if it is alive :)
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
     * Attempts a clean shutdown. Terminates finally
     */
    public void shutdown(){
        System.out.println("Initiating Shutdown");
        System.out.println("Closing all the connections...");
        //Close all the sockets and then remove the clients they belong to.
        for(Client client: clientArrayList){
            try{
                client.getSocket().close();
            }catch (IOException ioe){

            }
            clientArrayList.add(client);
        }

        this.trafficService = null;
        this.socketTerminator = null;

        System.out.println("Closing socket");
        try{
            this.serverSocket.close();
            System.exit(0);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        finally{
            System.exit(-1);
        }
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

