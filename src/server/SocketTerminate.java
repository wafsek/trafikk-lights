package server;

/**
 * Created by Baljit Singh Sarai on 17.02.16.
 * @author Baljit Singh Sarai
 */

import java.io.IOException;

/**
 * Tries to fix the socket.
 * If fails, deletes the client that the socket belongs to.
 */
public class SocketTerminate implements Runnable {

    private Client client;
    private IOException ioException;
    private TrafficServer trafficServer;

    /**
     * Creates a SocketTerminator with the following parameters.
     * @param client
     * @param ioe
     */
    public SocketTerminate(TrafficServer trafficServer,Client client,IOException ioe){
        this.trafficServer = trafficServer;
        this.client = client;
        this.ioException = ioe;
    }

    @Override
    public void run() {
            try{
                client.getSocket().close();
            }catch (IOException ioe){
                //Nothing to do here really. We were about to kill it
                // anyway
            }
            finally {
                //I am not sure about this but removing the whole client object anyway.
                this.trafficServer.clientArrayList.remove(client);
            }
    }
}
