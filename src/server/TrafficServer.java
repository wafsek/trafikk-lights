package server;
import logging.CustomLogger;


import java.net.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;


/**
 * The TrafficServer is the main server.Tt contains and handle the main server logic.
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class TrafficServer extends Thread{
    //The Constants
    private static final int PORT = Config.getServerPort();
    public final int BUFFERSIZE = Config.getBufferSize();
    private final int LOOPBACKTIME = Config.getLoopbackTime();
    private final int SERVICESWORKERS = Config.getServiceWorkers();
    private final int TERMINATORS = Config.getTerminators();
    private final int REATTEMPTWAIT = 1000;
    public final int MESSAGE_SIZE =20;
    private final byte[] PING = {2,2,67,67,0,0,0,0,0,0};



    //Variables
    private ServerSocket serverSocket ;
    private static TrafficServer trafficServer;
    public List<Client> clientArrayList  = new CopyOnWriteArrayList<Client>();
    private ServiceQueue trafficService;
    private ServiceQueue socketTerminator;
    private Thread clientHandler;
    private volatile boolean stopped = false;
    private volatile boolean hasStopped = false;
    private CustomLogger logger = CustomLogger.getInstance();
    private TrafficController trafficController;
    private CommandHandler commandHandler;
    private String displayAddresse = "";


    /**
     * Creats a TrafficServer and set sets the following parameters.
     * @param port port-number
     * @throws IOException Input/output Exception
     */
    private TrafficServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        commandHandler = new CommandHandler(this);
        this.displayAddresse = getIP();
        //serverSocket.setSoTimeout(100);
    }

    /**
     * Sets the trafficController.
     * @param trafficController- A {@link server.TrafficServer} Type.
     */
    public void setTrafficController(TrafficController trafficController) {
        this.trafficController = trafficController;
    }

    /**
     * A {@link InetAddress} with "localhost" as host in a human readable format.
     * @return A {@link InetAddress} in a human readable format.
     */
    public String getIP(){
        try{
            return InetAddress.getByName("localhost").toString();
        }catch (UnknownHostException uhe){
            uhe.printStackTrace();
        }
        return this.serverSocket.getInetAddress().getCanonicalHostName();
    }


    /**
     * Starts the main server.Will start the main logic if the stopped variable is set <code>false</code>
     */
    public void run() {
        while(true) {
            try {
                Thread.sleep(REATTEMPTWAIT);
            } catch (InterruptedException ie) {
                //This should be handled properly --Sarai:P
            }
            if(!this.stopped){
                logger.log("Server started. Host: "+this.displayAddresse
                        +" Port: "+this.serverSocket.getLocalPort(),Level.INFO);
                clientHandler = new ClientHandler(this.serverSocket,this.trafficController);
                clientHandler.start(); //Starts accepting incoming connections.
                this.serverForever();
            }
        }
    }

    /**
     * Starts the main logic loop.
     */
    public void startServer(){
        this.logger.log("Starting server...",Level.FINE);
        this.stopped = false;
    }

    /**
     * Stop the main server logic.(Can be restarted with startServer() method.)
     */
    public void serverStop(){
        disconnectAall();
        clientHandler = null;
        this.logger.log("Stopping server...",Level.FINE);
        this.stopped = true;
        while(!this.hasStopped){

        }
        this.logger.log("Server stopped",Level.FINE);
        this.clientArrayList.clear();
    }



    /**
     * Loops through all the clients. If it finds any data on anyone of them it creates a task
     * and gives it to a ServiceQueue type. If it finds a broken socket, its deletes it form the list.
     * IMPORTANT! This This method assumes the a char is one byte. This might not always be
     * true everywhere. The portability of this method is not a guarantee.
     */
    public void serverForever() {
        this.logger.log("Service Started.", Level.INFO);
        trafficService = new ServiceQueue(SERVICESWORKERS);
        socketTerminator = new ServiceQueue(TERMINATORS);
        this.hasStopped = false;
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
                        trafficService.execute(new ServiceTask(client));
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
        this.hasStopped = true;
        this.logger.log("Server main loop stopped", Level.FINE);
    }


    /**
     * Handles any incoming data from the Client.
     * @param msg The message that is requested.
     * @param client The Client to whome this message is intended.
     * @param times The current times on the server wigets.
     * @return A user-friendly message as to what happened.
     */
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

    /**
     * Creates a human readable message from byte array.
     * @param data The byte array that it converts to a String.
     * @param offset The offset.
     * @param numofbytes The number of bytes that it converts to a String.
     * @return A human friendly message.
     */
    private StringBuilder createMsg(byte[] data,int offset,int numofbytes){
        StringBuilder result = new StringBuilder(numofbytes);
        for(int i = offset;i<numofbytes;i++){
            result.append(","+(char)data[i]);
        }
        return result;
    }


    /**
     * This method takes some data and sends it to all the clients connected to the server.
     * @param data The data to be sent.
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
     *This method takes some data and sends it to the given {@link server.Client} Client.
     * @param client The Client the message is intended for.
     * @param data The data to be sent.
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


    public void disconnectAall(){
        for(Client client:clientArrayList){
            disconnectClient(client);
        }
    }

    /**
     * Try to cleanly close the socket and remove the Client.
     * @param client The Client to be removed.
     */
    public void disconnectClient(Client client){
        byte[] msg = new byte[MESSAGE_SIZE];
        msg[0] = 2;
        msg[1] = 5;
        msg[2] = 'D';
        msg[3] = 'C';
        send(client,msg);
        disconnectClientSocket(client.getSocket(),client.getDataInputStream(),client.getDataOutputStream());
        clientArrayList.remove(client);
        trafficController.getServerGUI().refreshClientlist();
    }

    /**
     * Closes the {@link Socket} , {@link DataInputStream} and {@link DataOutputStream}
     * @param socket The {@link Socket} to be closed.
     * @param in The {@link DataInputStream}
     * @param out {@link DataOutputStream}
     */
    public void disconnectClientSocket(Socket socket,DataInputStream in ,DataOutputStream out){
        try{
            out.flush();
            out.close();
            in.close();
            socket.close();
        }catch (SocketException se){
            logger.log("Socket Exeception while trying to close the socket",Level.WARNING);
        }catch (IOException ioe){
            logger.log("IOExeception while trying to close the streams and socket",Level.WARNING);
        }
    }

    /**
     * Attempts a clean shutdown. Terminates finally
     */
    public void shutdown(){
        this.logger.log("Initiating Shutdown",Level.INFO);
        this.stopped = true;
        this.logger.log("Closing all the connections...",Level.INFO);
        //Close all the sockets and then remove the clients they belong to.
        for(Client client: clientArrayList){
            try{
                client.getSocket().close();
            }catch (IOException ioe){

            }
            clientArrayList.remove(client);
        }

        this.socketTerminator = null;

        this.logger.log("Closing Socket",Level.INFO);
        try{
            this.serverSocket.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        finally{
        }
    }

    /**
     * The method is used to restrain this class to a singleton.
     * @return TrafficServer
     */
    public static TrafficServer getInstance(){
        if(trafficServer == null){
            try{
                trafficServer = new TrafficServer(PORT);
            }catch (IOException e){
                CustomLogger.getInstance().log("Could not create the server instance",Level.SEVERE);

            }
        }
        return trafficServer;
    }

}//End of class TrafficServer

