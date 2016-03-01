package server;
import logging.CustomLogger;

import java.awt.geom.Arc2D;
import java.net.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;


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
    private final int MESSAGE_SIZE =20;
    private final byte[] PING = {2,2,67,67,0,0,0,0,0,0};



    //Variables
    private ServerSocket serverSocket ;
    private static TrafficServer trafficServer;
    public List<Client> clientArrayList  = new CopyOnWriteArrayList<Client>();
    private ServiceQueue trafficService;
    private ServiceQueue socketTerminator;
    private Thread clientHandler;
    private boolean stopped = false;
    private String[] commands = new String[10];
    private CustomLogger logger = CustomLogger.getInstance();


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
    public void run() {
        this.commands[0] = "time";
        this.commands[1] = "timeall";
        //this.clientArrayList.add(new Client(new Socket()));
        clientHandler = new ClientHandler(this,this.serverSocket);
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
                    //client.getDataOutputStream().write(PING);//Just to check if it is alive :)
                    if( client.getDataInputStream().available()> 0){
                        trafficService.execute(new ServiceTask(client,BUFFERSIZE
                        ));
                    }
                }catch (IOException ioe){
                    this.logger.log("Something on this socket is not right :)",Level.FINE);
                    socketTerminator.execute(new SocketTerminate(this,client,ioe));
                    //This task Should be given to the socketTerminator that
                    // i have made above as the server must go on and can not wait for
                    // closing the socket properly as this could risk the server. :) --Sarai
                }
            }//System.out.println("TACK");
        }
    }


    public String messageRequest(String msg,Client client,Double[] times){
        String command;
        String result = "Something unexpected happend";
        DataControl dataControl;
        byte[] data;
        if(msg.charAt(0) == '/'){
            dataControl = this.validateCommand(msg.substring(1),client);
            if(dataControl.equals(DataControl.SUCCESS)){
                result = this.command(msg.substring(1),client,times);
            }
        }
        return result;
    }


    public  DataControl validateCommand(String command,Client client){
        boolean found = false;
        for(String co: this.commands){
            if(co != null && co.equals(command)){
                found = true;
            }
        }

        if (!found){
            return DataControl.COMMAND_NOT_FOUND;
        }

        switch (command){
            case "time":{
                if(client == null){
                    return DataControl.NO_CLIENT_SELECTED;
                }
            }
        }
        return DataControl.SUCCESS;
    }


    public String command(String command,Client client,Double[] times){
        byte[] msg = new byte[this.MESSAGE_SIZE];
        msg[0] = 2;
        String result = "";
        switch (command){
            case "time":{
                msg[1] = 5;
                msg[2] = 'T';
                msg[3] = 'M';
                msg[4] =  times[0].byteValue();
                msg[5] = times[1].byteValue();
                msg[6] = times[2].byteValue();
                this.send(client,msg);
                result = "Server -> "+client.getName()+": "+command+" "+msg[4]+msg[5]+msg[6]+"\n";
                break;
            }
            case "timeall":{
                msg[1] = 5;
                msg[2] = 'T';
                msg[3] = 'M';
                msg[4] =  times[0].byteValue();
                msg[5] = times[1].byteValue();
                msg[6] = times[2].byteValue();
                this.broardcast(msg);
                result = "Server -> all clients :"+msg;
                break;
            }
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
                this.logger.log("Something on this socket is not right :)",Level.FINE);
                socketTerminator.execute(new SocketTerminate(this,client,ioe));
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
            this.logger.log("Something on this socket is not right :)",Level.FINE);
            socketTerminator.execute(new SocketTerminate(this,client,ioe));
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

