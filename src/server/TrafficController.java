package server;

import ServerGUI.ServerGUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import logging.CustomLogger;

import java.util.logging.Level;

/**
 * Created by Baljit Singh Sarai on 25.02.16.
 * @author Baljit Singh Sarai
 * @author Kim Long Vu
 */
public class TrafficController extends Application{

    private ServerGUI serverGUI;
    private Stage stage;
    private TrafficServer trafficServer;
    private ObservableList clientObervableList;
    private Client reciver;
    private CustomLogger logger = CustomLogger.getInstance();

    public TrafficController(){

    }


    public void start(Stage stage){
        this.stage = stage;
        this.trafficServer = TrafficServer.getInstance();
        this.trafficServer.setTrafficController(this);
        serverGUI = new ServerGUI(this,this.stage);
        this.logger.addTextAreaLog(this.serverGUI);
        serverGUI.run();
        this.logger.log("Welcome to the traffic light program.", Level.INFO);
        this.trafficServer.start();
    }

    public ServerGUI getServerGUI() {
        return serverGUI;
    }

    public void handleInput(String input){
        String result;
        Double[] times = new Double[3];
        times[0] = this.serverGUI.getRedSlider().getValue();
        times[1] = this.serverGUI.getYellowslider().getValue();
        times[2] = this.serverGUI.getGreenslider().getValue();
        this.reciver = serverGUI.getClientlist().getSelectionModel().getSelectedItem();
        result = this.trafficServer.messageRequest(input,this.reciver,times);
        this.serverGUI.refreshCommand(result+"\n");


        /*if(reciver != null){
            send(reciver.getName(),input);
        }else{
            broadcast(input);
        }*/
    }


    public void startServer(){
        this.trafficServer.run();
    }

    public void stopServer(){
        this.trafficServer.serverStop();
        this.getServerGUI().refreshClientlist();
        this.logger.log("Server Stopped", Level.INFO);
    }

    public void restartTrafficServer(){
        this.logger.log("Restarting server wait...", Level.INFO);
        this.trafficServer.serverStop();
        this.trafficServer.startServer();
        this.getServerGUI().refreshClientlist();
    }


    public Client getReciver(){
        return this.reciver;
    }

    /*public void send(String id,String msg){
        this.trafficServer.messageRequest(msg,false);
    }

    public void broadcast(String msg){
        this.trafficServer.messageRequest(msg,true);
    }*/

    public void setTrafficServer(){

    }

    public TrafficServer getTrafficServer(){
        return (TrafficServer) this.trafficServer;
    }

    public ObservableList getClientObervableList(){
        return FXCollections.observableArrayList(TrafficServer.getInstance().clientArrayList);

    }

}
