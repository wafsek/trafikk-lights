package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This is Runnable type class that does the task of reading available data from a stream.
 * Created by Baljit Sarai on 15.02.16.
 * @author Baljit Singh Sarai
 */
public class ServiceTask implements Runnable{

    private Client client;
    private DataInputStream dataInputStream;

    /**
     * Creates a ServiceTask with the given parameters.
     * @param client The client {@link server.Client}.
     */
    public ServiceTask(Client client){
        this.client = client;
        this.dataInputStream = client.getDataInputStream();
    }

    /**
     * Reads the available data from the input-stream and prints is if its not a ping message.
     */
    @Override
    public void run() {
        byte[] contents = new byte[TrafficServer.getInstance().BUFFERSIZE];
        int availableData;
        try {
            availableData = this.dataInputStream.available();
            this.dataInputStream.read(contents, 0, availableData);
            if(!isPing(contents)){
                printMsg(contents);
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Prints the data out to System.out.
     * @param data- The data to be printed.
     */
    public void printMsg(byte[] data){
        System.out.print(client.getSocket().getInetAddress() + " Said: ");
        for (int i = 2; i < data.length; i++){
            if(data[i] == 0){
                continue;
            }
            System.out.print((char) data[i]);}
    }

    /**
     * Checks if the data is a "PING" data.
     * The sequence for a "ping" data is : 2,2,67,67  followed by 16 0's
     * @param data The data to be checked.
     * @return <code>true</code> if it a ping data, <code>false</code> otherwise.
     */
    public boolean isPing(byte[] data){
        if(data[2] == 'C' && data[3] == 'C'){
            return true;
        }
        return false;
    }
}
