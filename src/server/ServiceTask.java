package server;

import java.net.Socket;

/**
 * Created by Baljit Sarai on 15.02.16.
 * @author Baljit Sarai
 */
public class ServiceTask implements Runnable{


    private Socket socket;
    private String message;
    public ServiceTask(Socket socket,String message){
        this.socket = socket;
        this.message = message;
    }

    /**
     * This method contains the actual task that needs to be done.
     */
    @Override
    public void run() {
        System.out.println(socket.getInetAddress()+"Said: "+message);
        //This is the method that is going to handle the incoming signal
        // from a given socket/Client .
        // I have not yet decided yet whether to send in the client to htis object or socket is enough.
    }
}
