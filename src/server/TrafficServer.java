package server;
import logging.CustomLogger;

import java.awt.geom.Arc2D;
import java.net.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;


/**
 * The class that handles all the basic socket related chores.\n
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class TrafficServer extends Thread{
    //The Constants
    private final int BUFFERSIZE = Config.getBufferSize();
    private final int LOOPBACKTIME = Config.getLoopbackTime();
    private final int SERVICESWORKERS = Config.getServiceWorkers();
    private final int TERMINATORS = Config.getTerminators();
    public final int MESSAGE_SIZE =20;
    private final byte[] PING = {2,2,67,67,0,0,0,0,0,0};



    //Variables
    private ServerSocket serverSocket ;
    private static TrafficServer trafficServer;
    public List<Client> clientArrayList  = new CopyOnWriteArrayList<Client>();
    private ServiceQueue trafficService;
    private ServiceQueue socketTerminator;
    private Thread clientHandler;
    private boolean stopped = false;

    private CustomLogger logger = CustomLogger.getInstance();
    private TrafficController trafficController;
    private CommandHandler commandHandler;


    /**
     * Creats a TrafficServer and set sets the following parameters.
     * @param port port-number
     * @throws IOException Input/output Exception
     */
    private TrafficServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        commandHandler = new CommandHandler(this);
        //serverSocket.setSoTimeout(100);
    }

    public void setTrafficController(TrafficController trafficController) {
        this.trafficController = trafficController;
    }

    /**
     * Starts the main server.
     */
    public void run() {

        //this.clientArrayList.add(new Client(new Socket()));
        clientHandler = new ClientHandler(this,this.serverSocket,this.trafficController);
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
        this.logger.log("Serving forver", Level.FINE);
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
                    client.getDataOutputStream().write(PING);//Just to check if it is alive :)
                    if( client.getDataInputStream().available()> 0){
                        trafficService.execute(new ServiceTask(client,BUFFERSIZE
                        ));
                    }
                }catch (IOException ioe){
                    this.logger.log("Was unable to write to a socket.Attemting to delete it",Level.FINE);
                    socketTerminator.execute(new SocketTerminate(this,client,ioe,this.trafficController));
                    //This task Should be given to the socketTerminator that
                    // i have made above as the server must go on and can not wait for
                    // closing the socket properly as this could risk the server. :) --Sarai
                }
            }//System.out.println("TACK");
        }
    }


    public String messageRequest(String msg,Client client,Double[] times){
        String command;
        String result = "Something unexpected happened";
        DataControl dataControl;
        byte[] data;
        if(msg.charAt(0) == '/'){
            dataControl = commandHandler.validateCommand(msg.substring(1),client);
            result = dataControl.getDescription();
            if(dataControl.equals(DataControl.SUCCESS)){
                result = commandHandler.command(msg.substring(1),client,times);
            }
        }
        return result;
    }




    public StringBuilder createMsg(byte[] data,int offset,int numofbytes){
        StringBuilder result = new StringBuilder(numofbytes);
        for(int i = offset;i<numofbytes;i++){
            result.append(","+(char)data[i]);
        }
        return result;
    }


    /**
     *
     */
    public void broardcast(byte[] data){
        for(Client client: this.clientArrayList){
            try{
                client.getDataOutputStream().write(data);
            }catch (IOException ioe){
                this.logger.log("Was unable to write to a socket.Attemting to delete it",Level.FINE);
                socketTerminator.execute(new SocketTerminate(this,client,ioe,this.trafficController));
                //This task Should be given to the socketTerminator that
                // i have made above as the server must go on and can not wait for
                // closing the socket properly as this could risk the server. :) --Sarai
            }
        }//System.out.println("TACK");
    }



    /**
     *
     * @param client
     * @param data
     */
    public void send(Client client,byte[] data ){
        try{
            client.getDataOutputStream().write(data);
        }catch (IOException ioe){
            this.logger.log("Was unable to write to a socket.Attemting to delete it",Level.FINE);
            socketTerminator.execute(new SocketTerminate(this,client,ioe,this.trafficController));
            //This task Should be given to the socketTerminator that
            // i have made above as the server must go on and can not wait for
            // closing the socket properly as this could risk the server. :) --Sarai
        }
    }//System.out.println("TACK");




    /**
     * Attempts a clean shutdown. Terminates finally
     */
    public void shutdown(){
        this.logger.log("Initiating Shutdown",Level.INFO);
        this.logger.log("Closing all the connections...",Level.INFO);
        //Close all the sockets and then remove the clients they belong to.
        for(Client client: clientArrayList){
            try{
                client.getSocket().close();
            }catch (IOException ioe){

            }
            clientArrayList.add(client);
        }

        this.socketTerminator = null;

        this.logger.log("Closing Socket",Level.INFO);
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
                CustomLogger.getInstance().log("Could not create the server instance",Level.SEVERE);

            }
        }
        return trafficServer;
    }

}//End of class TrafficServer

