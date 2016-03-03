package server;

/**
 * Created by Baljit Singh Sarai on 17.02.16.
 * @author Baljit Singh Sarai
 */

import logging.CustomLogger;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Tries to fix the socket.
 * If fails, deletes the client that the socket belongs to.
 */
public class SocketTerminate implements Runnable {

    private Client client;
    private IOException ioException;
    private TrafficServer trafficServer;
    private TrafficController trafficController;
    private CustomLogger logger = CustomLogger.getInstance();

    /**
     * Creates a SocketTerminator with the following parameters.
     * @param client
     * @param ioe
     */
    public SocketTerminate(TrafficServer trafficServer,Client client,IOException ioe,TrafficController trafficController){
        this.trafficController = trafficController;
        this.trafficServer = trafficServer;
        this.client = client;
        this.ioException = ioe;
    }

    @Override
    public void run() {
            try{
                client.getSocket().close();
                this.trafficServer.clientArrayList.remove(client);
                this.trafficController.getServerGUI().refreshClientlist();
            }catch (IOException ioe){
                logger.log("IOEXption: When trying to close the socket properly", Level.WARNING);
                //Nothing to do here really. We were about to kill it
                // anyway
            }
            finally {
                //I am not sure about this but removing the whole client object anyway.
                this.trafficServer.clientArrayList.remove(client);
                this.trafficController.getServerGUI().refreshClientlist();
            }
    }
}
