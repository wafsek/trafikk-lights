package server;

import logging.CustomLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

/**
 * This class will contain the task of validating the handshake of the newly incomming connection
 * Created by Baljit Singh Sarai on 15.02.16.
 * @author Baljit Singh Sarai
 */


public class ValidateConnections implements Runnable{

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String handshakeString = "secret";
    private byte[] handshakeMsg = new byte[20];
    private final int OFFSET = 2;
    private final String[] handshakeArray = {"handshake","protocol"};
    private CustomLogger logger;
    private TrafficController trafficController;


    public ValidateConnections(Socket socket,TrafficController trafficController){
        this.socket = socket;
        this.logger = CustomLogger.getInstance();
        this.trafficController = trafficController;
    }

    @Override
    public void run() {
        this.logger.log("Validating Socket", Level.FINE);
        this.handshakeProtocol(this.socket);
        //This method is going to be the methos that validates the connection
        // If this mathod does more then just the hand shake----we will change the name of
        // the class :)
    }

    /**
     *Handshake protocol
     *
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
    

    private void clearBuffer(byte[] buffer){
        for(int i = 0;i<buffer.length;i++){
            buffer[i] = 0;
        }
    }

}


