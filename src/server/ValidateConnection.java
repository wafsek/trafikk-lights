package server;

import logging.CustomLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

/**
 * This class will contain the task of validating the handshake of the newly incoming connection.
 * Created by Baljit Singh Sarai on 15.02.16.
 * @author Baljit Singh Sarai
 */


public class ValidateConnection implements Runnable{

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String handshakeString = "secret";
    private byte[] handshakeMsg = new byte[20];
    private final int OFFSET = 2;
    private final String[] handshakeArray = {"handshake","protocol"};
    private CustomLogger logger;
    private TrafficController trafficController;


    /**
     * Creates a ValidateConnection instance with the given parameters.
     * @param socket The socket to be validated.
     * @param trafficController {@link server.TrafficController}
     */
    public ValidateConnection(Socket socket, TrafficController trafficController){
        this.socket = socket;
        this.logger = CustomLogger.getInstance();
        this.trafficController = trafficController;
    }

    @Override
    public void run() {
        this.logger.log("Validating Socket", Level.FINE);
        this.handshakeProtocol(this.socket);
    }

    /**
     * Initiate the Handshake protocol.
     * If the hanshake is passed it creates a new {@link server.Client} object and adds it to the list.
     * @param socket The {@link Socket} to be tested.
     */
    public void handshakeProtocol(Socket socket){
        int msgIndex = 2;
        boolean clientAdded;
        byte[] msgRecieved= new byte[20];
        this.handshakeMsg[0] = 3;
        this.handshakeMsg[1] = (byte)this.handshakeString.length();
        for(int i = 0;i < this.handshakeString.length();i++){
            this.handshakeMsg[i+OFFSET] =(byte)this.handshakeString.charAt(i);
        }
        String o;
        boolean validate;
        int count = 0;
        try{
            this.logger.log("waiting for handshake message",Level.FINER);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            in.read(msgRecieved,0,20);
            if(this.checkHandshakeMsg(msgRecieved,0)){
                count++;
            }else{
                this.socket.close();
                return;
            }

            this.clearBuffer(msgRecieved);
            this.logger.log("Trying to write back the handshake msg to the client ",Level.FINEST);
            out.write(this.handshakeMsg);
            this.logger.log("written  the handshake feedback to the client",Level.FINEST);
            in.read(msgRecieved,0,20);
            if(this.checkHandshakeMsg(msgRecieved,1)){
                count++;
            }else{
                this.socket.close();
                return;
            }
            this.logger.log("Creating a new Client object and adding it to the clientArratlist ",Level.FINER);
            clientAdded =  TrafficServer.getInstance().clientArrayList.add(new Client(this.socket));
            this.trafficController.getServerGUI().refreshClientlist();
            if(clientAdded){
                this.logger.log("New Client Connected",Level.INFO);
            }
            this.logger.log("Status for added_new_client: "+clientAdded,Level.FINEST);


        }catch (IOException io ){
            System.out.println("Something went wrong");
        }
        //This is where i intent to write code to handle the incoming connections- Baljit Sarai.
    }

    /**
     * Checks whenter the individual handshake message was a valid one or not.
     * @param msg The message to be checked.
     * @param no The message number it is supposed to be checked against.
     * @return <code>true</code> if passed. <code>false</code> if failed.
     */
    public boolean checkHandshakeMsg(byte[] msg,int no){
        String handshake = this.handshakeArray[no];
        if(msg[1] != handshake.length()){
            this.logger.log("Handshake message validating failed at length",Level.FINER);
            return false;
        }else{
            for(int i = 0;i<msg[1];i++){
                if(handshake.charAt(i)!=msg[i+OFFSET]){
                    this.logger.log("Handshake message validating failed",Level.FINER);
                    return false;
                }
            }
        }
        this.logger.log("Handshake message validating passed",Level.FINER);
        return true;
    }

    /**
     * Clears a buffer array.
     * @param buffer - The byte array to be cleared.
     */
    private void clearBuffer(byte[] buffer){
        for(int i = 0;i<buffer.length;i++){
            buffer[i] = 0;
        }
    }

}


