package ServerGUI;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.Client;
import server.TrafficController;
import server.TrafficServer;

import javax.net.ssl.SSLContext;
import java.io.IOException;


/**
 * Created by kim on 15.02.2016.
 * GUI for Server.
 */
public class ServerGUI{

    private ScrollPane commandWindow,loggWindow;
    private TextArea logg,command;
    private TableView<Client> clientlist;
    private Scene scene;
    private Stage stage;
    private Button restart,stop;
    private Label redLabel,yellowLabel,greenLabel;
    private TextField serverInput;
    private VBox left,nameoption,coloroption, slideroption, valueoption;
    private HBox lightoption,buttonoption;
    private Slider redslider, yellowslider, greenslider;
    private TrafficController trafficController;
    private static final int SCENE_WIDTH = 1400;
    private static final int SCENE_HEIGTH = 900;


    public ServerGUI(TrafficController trafficController, Stage stage){
        this.trafficController = trafficController;

        this.stage = stage;
        Label redname = new Label("RED");

        Label yellowname = new Label("YELLOW");
        Label greenname = new Label("GREEN");




        redslider = new Slider(0,100,50);

        redslider.setOnMouseDragged(e->redSliderAction());
        redslider.setPrefSize(900,0);

        redLabel = new Label(String.format("%.0f",redslider.getValue()));
        redLabel.setPrefSize(50,0);

        yellowslider = new Slider(0,100,50);

        yellowslider.setOnMouseDragged(e->yellowSliderAction());

        yellowLabel = new Label(String.format("%.0f",redslider.getValue()));


        greenslider = new Slider(0,100,50);

        greenslider.setOnMouseDragged(e->greenSliderAction());

        greenLabel = new Label(String.format("%.0f",redslider.getValue()));

        command = new TextArea("Command Transcations:\n");
        command.setEditable(false);
        command.setPrefSize(1100,220);
        commandWindow = new ScrollPane(command);



        clientlist = new TableView();

        TableColumn clientName = new TableColumn("Client Name");
        clientName.setCellValueFactory(new PropertyValueFactory<Client,String>("name"));
        clientName.setPrefWidth(300);

        clientlist.getColumns().addAll(clientName);
        clientlist.getSelectionModel().cellSelectionEnabledProperty();
        clientlist.setPrefSize(300,100);
        clientlist.getSelectionModel().cellSelectionEnabledProperty();
        clientlist.setFocusTraversable(false);
        clientlist.focusedProperty();
        this.refreshClientlist();

        //Loggwindow
        logg = new TextArea();
        logg.setPrefSize(1100,220);
        logg.setEditable(false);
        loggWindow = new ScrollPane(logg);


        valueoption = new VBox();
        nameoption = new VBox();
        coloroption = new VBox();
        left = new VBox();
        slideroption = new VBox();
        lightoption = new HBox();

        BorderPane bpane = new BorderPane();

        //Serverinput
        serverInput = new TextField();
        serverInput.setPromptText("SEND COMMANDS TO CLIENT");
        serverInput.setPrefSize(300,200);
        serverInput.setOnKeyPressed(e-> {

            if (e.getCode() == KeyCode.ENTER) {
                this.trafficController.handleInput(serverInput.getText());
                serverInput.clear();
                this.refreshClientlist();
                /*Client clienttest = clientlist.getSelectionModel().getSelectedItem();
                String text = serverInput.getText();
                if(clienttest != null){ //HVIS NOE ER TRUKKET PÅ
                    trafficController.send(clienttest.getName(),text);
                }
                else if(!text.matches("(?m)^(/time)$")){ // HVIS IKKE NOE ER TRYKKET PÅ
                    System.out.println("TIL ALLE");
                    System.out.println(text);
                    trafficController.broadcast(text); // broadcast message
                }else{
                                                    serverInput.setText("YOU CAN'T USE /time WITHOUT CHOOSING A CLIENT");
                    System.out.println("DU HAR IKKE VALGT CLEINT");
                }
                // clear text
                serverInput.clear();*/

            }

        });
        stop = new Button("STOP");
        stop.setOnAction(e-> shutdownServer());
        restart = new Button("RESTART");
        restart.setOnAction(e->);

        buttonoption = new HBox();
        buttonoption.getChildren().addAll(stop,restart);


        nameoption.getChildren().addAll(redname,yellowname,greenname);

        slideroption.getChildren().addAll(redslider,yellowslider,greenslider);
        valueoption.getChildren().addAll(redLabel,yellowLabel,greenLabel);
        lightoption.getChildren().addAll(nameoption,slideroption,valueoption);
        left.getChildren().addAll(lightoption,commandWindow,serverInput,loggWindow,buttonoption);


        bpane.setLeft(left);
        bpane.setRight(clientlist);

        scene = new Scene(bpane,SCENE_WIDTH,SCENE_HEIGTH);
        stage.setTitle("Server");
        stage.setScene(scene);


    }
    public void run(){
        stage.show();
    }

 /*   public static void main(String[] args){
        launch(args);
    }*/
// RETURN THE DIFFERENT ELEMENTS--------------------------------------------------------------
    public Slider getRedSlider(){
        return redslider;
    }
    public Slider getYellowslider(){
        return yellowslider;
    }
    public Slider getGreenslider() {
        return greenslider;
    }
    // RETURN THE DIFFERENT ELEMENTS--------------------------------------------------------------
    //DIFFERENT METHODS ON DIFFERNT ACTIONS-------------------------------------------------------

    public int getRedValue(){
        return (int)redslider.getValue();
    }

    public int getYellowValue(){
        return (int)yellowslider.getValue();
    }

    public int getGreenValue(){
        return (int)greenslider.getValue();
    }


    //SLIDERACTIONS
    public void redSliderAction(){
        redLabel.setText(String.format("%.0f",redslider.getValue()));
        //System.out.println(redslider.getValue());
    }
    public void yellowSliderAction(){
        yellowLabel.setText(String.format("%.0f",yellowslider.getValue()));
      // System.out.println(yellowslider.getValue());
    }
    public void greenSliderAction(){
        greenLabel.setText(String.format("%.0f",greenslider.getValue()));
        //System.out.println(greenslider.getValue());
    }

    //START/CREATE SERVER
    public void startServer(){
        this.trafficController.startServer();
    }

    public void refreshServer(){
        this.trafficController.restartTrafficServer();
    }

    public void shutdownServer(){
        this.trafficController.shutdownServer();
    }

    //REFRESH CLIENT LIST
    public void refreshClientlist(){

        clientlist.getSelectionModel().clearSelection();

        clientlist.setItems(this.trafficController.getClientObervableList());

    }

    public TableView<Client> getClientlist() {
        return clientlist;
    }

    public TextArea getLogg(){
        return logg;
    }

    public void refreshCommand(String commandtext){
        command.appendText(commandtext);
    }
}
