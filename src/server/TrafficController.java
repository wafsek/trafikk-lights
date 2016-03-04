package server;

import serverGUI.ServerGUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import logging.CustomLogger;

import java.util.logging.Level;

/**
 * This is the main controller of the program. Which means all the major threads are started here.
 * The program is initiated from this clas's start method.
 * Created by Baljit Singh Sarai on 25.02.16.
 * @author Baljit Singh Sarai
 * @author Kim Long Vu
 */
public class TrafficController extends Application{
    private ServerGUI serverGUI;
    private Stage stage;
    private TrafficServer trafficServer;
    private Client reciver;
    private CustomLogger logger = CustomLogger.getInstance();
    public TrafficController(){
    }

    /**
     * Start the controller.
     * Create the backbone threads of the program and fire them up.
     * @param stage {@link Stage}
     */
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

    /**
     * Returns serverGUI {@link serverGUI.ServerGUI}
     * @return {@link serverGUI.ServerGUI}
     */
    public ServerGUI getServerGUI() {
        return serverGUI;
    }

    /**
     * Handles the user-input from the gui.
     * @param input Input from the user.
     */
    public void handleInput(String input){
        String result;
        Double[] times = new Double[3];
        times[0] = this.serverGUI.getRedSlider().getValue();
        times[1] = this.serverGUI.getYellowslider().getValue();
        times[2] = this.serverGUI.getGreenslider().getValue();
        this.reciver = serverGUI.getClientlist().getSelectionModel().getSelectedItem();
        result = this.trafficServer.messageRequest(input,this.reciver,times);
        this.serverGUI.refreshCommand(result+"\n");
    }

    /**
     * Starts the main logic of the server.
     */
    public void startServer(){
        this.trafficServer.startServer();
    }

    /**
     * Stops the main logic of the server.
     */
    public void stopServer(){
        this.trafficServer.serverStop();
        this.getServerGUI().refreshClientlist();
        this.logger.log("Server Stopped", Level.INFO);
    }

    /**
     * Resets the server and starts it again.
     */
    public void restartTrafficServer(){
        this.logger.log("Restarting server wait...", Level.INFO);
        this.trafficServer.serverStop();
        this.trafficServer.startServer();
        this.getServerGUI().refreshClientlist();
    }

    /**
     * Returns the {@link server.TrafficServer}
     * @return trafficServer {@link server.TrafficServer}
     */
    public TrafficServer getTrafficServer(){
        return (TrafficServer) this.trafficServer;
    }

    /**
     * Returns observableList {@link ObservableList} that the gui uses to show the clients {@link server.Client}
     * @return {@link ObservableList}
     */
    public ObservableList getClientObervableList(){
        return FXCollections.observableArrayList(TrafficServer.getInstance().clientArrayList);

    }

}
