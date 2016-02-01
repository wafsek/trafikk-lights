package server;
import java.net.*;
import java.io.*;


/**
 * The class that handles all the basic socket related chores; accepting incoming connections etc.
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Sarai
 */
public class TrafficServer {
    private ServerSocket serverSocket ;
    private static TrafficServer trafficServer;


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
     * Starts the main server and start accepting incoming connetions
     */
    public void start() {
        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket clientSocket = serverSocket.accept();
                //This is where i intent to write code to handle the incoming connections- Baljit Sarai

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

