package server;

import ServerGUI.ServerGUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.AbstractList;

/**
 * Created by Baljit Singh Sarai on 25.02.16.
 */
public class TrafficController extends Application{

    private ServerGUI serverGUI;
    private Stage stage;
    private TrafficServer trafficServer;
    private ObservableList clientObervableList;
    private Client reciver;

    public TrafficController(){

    }


    public void start(Stage stage){
        this.stage = stage;
        this.trafficServer = TrafficServer.getInstance();
        this.trafficServer.start();
        serverGUI = new ServerGUI(this,this.stage);
        serverGUI.run();

    }


    public void handleInput(String input){
        String result;
        Double[] times = new Double[3];
        times[0] = this.serverGUI.getRedSlider().getValue();
        times[1] = this.serverGUI.getYellowslider().getValue();
        times[2] = this.serverGUI.getGreenslider().getValue();
        this.reciver = serverGUI.getClientlist().getSelectionModel().getSelectedItem();
        this.trafficServer.messageRequest(input,this.reciver,times);



        /*if(reciver != null){
            send(reciver.getName(),input);
        }else{
            broadcast(input);
        }*/
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

    public void startServer(){

        System.out.println("hey");
    }

    public void shutdownServer(){
        TrafficServer.getInstance().shutdown();
    }

    public TrafficServer getTrafficServer(){
        return (TrafficServer) this.trafficServer;
    }

    public ObservableList getClientObervableList(){
        return FXCollections.observableArrayList(TrafficServer.getInstance().clientArrayList);

    }

}
