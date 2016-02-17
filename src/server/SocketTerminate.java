package server;

/**
 * Created by Baljit Singh Sarai on 17.02.16.
 * @author Baljit Sarai
 */

import java.io.IOException;

/**
 * Tries to fix the socket.
 * If fails, deletes the client that the socket belongs to.
 */
public class SocketTerminate implements Runnable {

    private Client client;
    private IOException ioException;


    /**
     * Creates a SocketTerminator with the following parameters.
     * @param client
     * @param ioe
     */
    public SocketTerminate(Client client,IOException ioe){
        this.client = client;
        this.ioException = ioe;
    }

    @Override
    public void run() {
            TrafficServer.getInstance().clientArrayList.remove(client);
    }
}
