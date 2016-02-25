package ServerGUI;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import server.TrafficController;
import server.TrafficServer;

import java.io.IOException;


/**
 * Created by kim on 15.02.2016.
 */
public class ServerGUI{

    private ScrollPane terminalwindow;
    private TableView clientlist;
    private Scene scene;
    private Stage stage;
    private Label redLabel,yellowLabel,greenLabel;
    private RadioButton red, yellow, green;
    private ToggleGroup colourGroup;
    private VBox left,nameoption,coloroption, slideroption, valueoption;
    private HBox lightoption;
    private Slider redslider, yellowslider, greenslider;
    private Button startServer,stopServer;
    private TrafficController trafficController;
    private ObservableList clientObservableList;


    public ServerGUI(TrafficController trafficController, Stage stage){
        this.trafficController = trafficController;
        this.refreshClientlist();
        this.stage = stage;
        Label redname = new Label("RED");

        Label yellowname = new Label("YELLOW");
        Label greenname = new Label("GREEN");


        colourGroup = new ToggleGroup();
        red = new RadioButton();
        red.setToggleGroup(colourGroup);

        red.setOnAction(e-> redRadioButtonAction());
        yellow = new RadioButton();
        yellow.setToggleGroup(colourGroup);
        yellow.setOnAction(e->yellowRadioButtonAction());
        green = new RadioButton();
        green.setToggleGroup(colourGroup);

        green.setOnAction(e->greenRadioButtonAction());

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

        terminalwindow = new ScrollPane();
        terminalwindow.setPrefSize(1000,300);
        clientlist = new TableView(clientObservableList);
        clientlist.setPrefSize(300,800);

      /*  GridPane gpane = new GridPane();*/
        valueoption = new VBox();
        nameoption = new VBox();
        coloroption = new VBox();
        left = new VBox();
        slideroption = new VBox();
        lightoption = new HBox();

        //Start knapp
        startServer = new Button();
        startServer.setOnAction(e -> startServer());
        stopServer = new Button();
        stopServer.setOnAction(e-> shutdownServer());

        BorderPane bpane = new BorderPane();


        nameoption.getChildren().addAll(redname,yellowname,greenname);
        coloroption.getChildren().addAll(red,yellow,green);
        slideroption.getChildren().addAll(redslider,yellowslider,greenslider);
        valueoption.getChildren().addAll(redLabel,yellowLabel,greenLabel);
        lightoption.getChildren().addAll(nameoption,coloroption,slideroption,valueoption);
        left.getChildren().addAll(lightoption,terminalwindow,startServer,stopServer);

        bpane.setPrefSize(1000,1000);

        bpane.setLeft(left);
        bpane.setRight(clientlist);

        scene = new Scene(bpane,1300,900);
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

    //RADIOBUTTONACTIONS
    public void redRadioButtonAction(){
        //DO SOMETHING WITH THE RED RADIO BUTTON
    }
    public void yellowRadioButtonAction(){
        //DO SOMETHING WITH THE yellow RADIO BUTTON
    }
    public void greenRadioButtonAction(){
        //DO SOMETHING WITH THE green RADIO BUTTON
    }
    //SLIDERACTIONS
    public void redSliderAction(){
        redLabel.setText(String.format("%.0f",redslider.getValue()));
        System.out.println(redslider.getValue());
    }
    public void yellowSliderAction(){
        yellowLabel.setText(String.format("%.0f",yellowslider.getValue()));
        System.out.println(yellowslider.getValue());
    }
    public void greenSliderAction(){
        greenLabel.setText(String.format("%.0f",greenslider.getValue()));
        System.out.println(greenslider.getValue());
    }

    //START/LAGE SERVER METODE
    public void startServer(){
        this.trafficController.startServer();
    }

    public void shutdownServer(){
        this.trafficController.shutdownServer();
    }

    public void refreshClientlist(){
        this.clientObservableList = this.trafficController.getClientObervableList();
    }
}
