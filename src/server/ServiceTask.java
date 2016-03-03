package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Baljit Sarai on 15.02.16.
 * @author Baljit Singh Sarai
 */
public class ServiceTask implements Runnable{

    private Client client;
    private DataInputStream dataInputStream;
    private int buffersize;
    public ServiceTask(Client client,int buffersize){
        this.buffersize = buffersize;
        this.client = client;
        this.dataInputStream = client.getDataInputStream();
    }

    /**
     * This method contains the actual task that needs to be done.
     */
    @Override
    public void run() {
        byte[] contents = new byte[this.buffersize];
        int availableData;
        try {
            availableData = this.dataInputStream.available();
            this.dataInputStream.read(contents, 0, availableData);
            if(!isPing(contents)){
                printMsg(contents);
            }
            System.out.println();
            //This is the method that is going to handle the incoming signal
            // from a given socket/Client .
            // I have not yet decided yet whether to send in the client to htis object or socket is enough.
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void printMsg(byte[] data){
        System.out.print(client.getSocket().getInetAddress() + " Said: ");
        for (int i = 2; i < data.length; i++){
            if(data[i] == 0){
                continue;
            }
            System.out.print((char) data[i]);}
    }

    public boolean isPing(byte[] data){
        if(data[2] == 'C' && data[3] == 'C'){
            return true;
        }
        return false;
    }
}
