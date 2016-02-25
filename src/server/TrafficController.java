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
    private Thread trafficServer;
    private ObservableList clientObervableList;

    public TrafficController(){

    }


    public void start(Stage stage){
        this.stage = stage;

            this.trafficServer = TrafficServer.getInstance();
            this.trafficServer.start();
            ServerGUI serverGUI = new ServerGUI(this,this.stage);
            serverGUI.run();

    }


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