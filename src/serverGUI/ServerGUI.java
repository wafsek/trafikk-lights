package serverGUI;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import server.Client;
import server.TrafficController;


/**
 * The GUI class for server side\n
 * Created by Baljit Singh Sarai on 15.02.16.
 * @author Kim Long Vu
 */

public class ServerGUI extends Thread{

    private ScrollPane commandWindow,loggWindow;
    private TextArea log,command;
    private TableView<Client> clientlist;
    private Scene scene;
    private Stage stage;
    private Button restart,stop,start;
    private Label redLabel,yellowLabel,greenLabel;
    private TextField serverInput;
    private VBox left,nameoption,coloroption, slideroption, valueoption;
    private HBox lightoption,buttonoption;
    private Slider redslider, yellowslider, greenslider;
    private TrafficController trafficController;
    private static final int SCENE_WIDTH = 1400;
    private static final int SCENE_HEIGTH = 900;

    /**
     * Constructor which creates the whole GUI
     * @param trafficController The trafficController object {@link server.TrafficController}
     * @param stage Stage object
     */
    public ServerGUI(TrafficController trafficController, Stage stage){
        this.trafficController = trafficController;

        this.stage = stage;
        Label redname = new Label("RED");

        Label yellowname = new Label("YELLOW");
        Label greenname = new Label("GREEN");




        redslider = new Slider(2,100,2);

        redslider.setOnMouseDragged(e->redSliderAction());
        redslider.setOnMouseReleased(e->redSliderAction());
        redslider.setPrefSize(900,0);

        redLabel = new Label(String.format("%.0f",redslider.getValue()));
        redLabel.setPrefSize(50,0);

        yellowslider = new Slider(2,100,2);

        yellowslider.setOnMouseDragged(e->yellowSliderAction());
        yellowslider.setOnMouseReleased(e->yellowSliderAction());

        yellowLabel = new Label(String.format("%.0f",redslider.getValue()));

        greenslider = new Slider(2,100,2);

        greenslider.setOnMouseDragged(e->greenSliderAction());
        greenslider.setOnMouseReleased(e->greenSliderAction());

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
        log = new TextArea();
        log.setPrefSize(1100,220);
        log.setEditable(false);
        loggWindow = new ScrollPane(log);

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
            }

        });

        stop = new Button("STOP");
        stop.setOnAction(e-> shutdownServer());
        start = new Button("START");
        start.setOnAction(e-> startServer());
        restart = new Button("RESTART");
        restart.setOnAction(e->refreshServer());

        buttonoption = new HBox();
        buttonoption.getChildren().addAll(start,stop,restart);


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

    /**
     * Method shows the stage
     */
    public void run(){
        stage.show();
    }

    /**
     * Method to close the stage which will close the GUI.
     */
    public void close(){
        stage.close();
    }

    /**
     * Method for returning the red slider
     * @return slider for the red color
     */
    public Slider getRedSlider(){
        return redslider;
    }
    /**
     * Method for returning the yellow slider
     * @return slider for the yellow color
     */
    public Slider getYellowslider(){
        return yellowslider;
    }
    /**
     * Method for returning the green slider
     * @return slider for the green color
     */
    public Slider getGreenslider() {
        return greenslider;
    }


    //SLIDERACTIONS

    /**
     * Method that updates the red label with the red sliders value
     */
    public void redSliderAction(){
        redLabel.setText(""+((int)(new Double(redslider.getValue()).byteValue())));
        //System.out.println(redslider.getValue());
    }
    /**
     * Method that updates the yellow label with the yellow sliders value
     */
    public void yellowSliderAction(){
        yellowLabel.setText(""+((int)(new Double(yellowslider.getValue()).byteValue())));
      // System.out.println(yellowslider.getValue());
    }
    /**
     * Method that updates the green label with the green sliders value
     */
    public void greenSliderAction(){
        greenLabel.setText(""+((int)(new Double(greenslider.getValue()).byteValue())));
        //System.out.println(greenslider.getValue());
    }

    /**
     * This method starts the server
     */
    public void startServer(){
        this.trafficController.startServer();
    }
    /**
     * This method refreshes the server
     */
    public void refreshServer(){
        this.trafficController.restartTrafficServer();
    }
    /**
     * This method shutdowns the server
     */
    public void shutdownServer(){
        this.trafficController.stopServer();
    }

    /**
     * Method refreshes the clientlist
     */
    public void refreshClientlist(){

        clientlist.getSelectionModel().clearSelection();

        clientlist.setItems(this.trafficController.getClientObervableList());

    }

    /**
     * This method returns the tablview object for clientlist to check if anything is pressed on
     * @return the tableview for clientlist
     */
    public TableView<Client> getClientlist() {
        return clientlist;
    }

    /**
     * Method returns the textarea
     * @return the log textarea object
     */
    public TextArea getLogg(){
        return log;
    }

    /**
     * Method refreshes the command window
     * @param commandtext The sent command as String
     */
    public void refreshCommand(String commandtext){
        command.appendText(commandtext);
    }
}
