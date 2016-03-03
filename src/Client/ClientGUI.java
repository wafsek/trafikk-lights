package Client;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.util.Duration;

import java.util.Optional;

/**
 * This purpose of this class is
 * @author Adrian Siim Melsom, Anh Thu Pham Le
 */
public class ClientGUI {

    private GridPane layout = new GridPane();
    private Circle redLight = new Circle(150, 60, 40);
    private Circle yellowLight = new Circle(150, 60, 40);
    private Circle greenLight = new Circle(150, 60, 40);
    private FillTransition redTrans, yelTrans, greTrans, yelIdleTrans;
    private PauseTransition redDur, yelDur, greDur, yelIdleDur, grayIdleDur;
    private SequentialTransition redSeq, yelSeq, greSeq, mainSequence;
    private Stage stage;
    private Scene scene;
    private Button stop, connect;
    private ClientController clientController;
    private TextField handshakeField, hostField, portField;
    private boolean hasSequence, isIdle;
    //private Timeline[] idleTimeLine, runningTimeLine;
    Duration[] duration = new Duration[4];

    final int DEFAULTTIME = 2000;


    /**
     * Constructor. Constructs the ClientGUI with all relevant fields.
     * @param stage
     * @param clientController
     */
    public ClientGUI(Stage stage, ClientController clientController) {
        hasSequence = false;
        this.clientController = clientController;
        this.stage = stage;

        //setIdleTimeLine();
        //setRunningTimeLine(DEFAULTTIME, DEFAULTTIME, DEFAULTTIME);
        //Konstruerer trafikklyset
        StackPane trafikklys = new StackPane();
        Rectangle rectangle = new Rectangle(125, 300, 125, 300);
        connect = new Button("Connect");

        hostField = new TextField();
        hostField.setPromptText("Host");

        portField = new TextField();
        portField.setPromptText("Port Number");

        handshakeField = new TextField();
        handshakeField.setPromptText("Connection Command");
        connect.setOnAction(e -> {

            if(isIdle) {
                if(handshakeField.getText().length() == 0) {
                    handshakeField.setPromptText("Can't be empty");
                    handshakeField.setStyle("-fx-effect: dropshadow"
                            + "(three-pass-box, rgba(250, 0, 0, 250), 5, 0, 0, 0);");
                }else {
                    handshakeField.setPromptText("Connection Command");
                    handshakeField.setStyle("");
                    clientController.requestConnection(handshakeField.getText().trim(),
                            hostField.getText().trim(),
                            Integer.parseInt(portField.getText().trim()));
                    clearFields();
                }
            } else {
                idle();
            }


        });

        VBox rightVBox = new VBox(10, new Label("Host"),hostField,new Label("Port number"),
                portField,new Label("Handshake Command"), handshakeField, connect);
        rightVBox.setPadding(new Insets(10));

        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));


        rectangle.setFill(Color.BLACK);
        rectangle.setStroke(Color.BLACK);
        trafikklys.getChildren().add(rectangle);


        //Desinger sirklene på trafikklyset

        redLight.setStroke(Color.BLACK);
        redLight.setFill(Color.GREY);
        layout.add(redLight, 1, 1);

        yellowLight.setStroke(Color.BLACK);
        yellowLight.setFill(Color.GRAY);
        layout.add(yellowLight, 1, 2);

        layout.setVgap(5);
        layout.setAlignment(Pos.CENTER);

        greenLight.setStroke(Color.BLACK);
        greenLight.setFill(Color.GREY);
        layout.add(greenLight, 1, 3);
        trafikklys.getChildren().add(layout);

        //Konstruerer hoved layouten
        BorderPane hovedLayout = new BorderPane();
        hovedLayout.setPadding(new Insets(10));
        hovedLayout.setCenter(trafikklys);
        hovedLayout.setBottom(vBox);
        hovedLayout.setRight(rightVBox);

        // Konstruerer vinduet
        scene = new Scene(hovedLayout);
        stage.setTitle("Trafikklys");
        stage.setScene(scene);
        initiateLightSequence();
        initiateIdleSequence();
        mainSequence = new SequentialTransition();
        mainSequence.setAutoReverse(true);
        mainSequence.setCycleCount(SequentialTransition.INDEFINITE);
        start();
    }

    /**
     * Clears the contents of all the text fields.
     */
    private void clearFields() {
        hostField.setText("");
        handshakeField.setText("");
        portField.setText("");
    }

    /**
     * Show the stage and puts it on idle mode.
     */
    public void start() {
        stage.show();
        idle();
    }

    /**
     * Tries to start the animation based on a boolean
     */
    public void tryStart() {
        if(!hasSequence) {
            idle();
            hasSequence = !hasSequence;
        } else {
            //animation();
            hasSequence = !hasSequence;
        }
    }

    /**
     * Initiates the idle animation
     */
    public void idle() {
        isIdle = true;
        mainSequence.stop();
        mainSequence.getChildren().clear();
        mainSequence.getChildren().addAll(yelIdleDur, yelIdleTrans, grayIdleDur);
        mainSequence.play();
    }

    /**
     * Starts the main animation (red, yellow and green light).
     */
    public void animation(int red, int yellow, int green) {

        mainSequence.stop();
        redDur.setDuration(Duration.millis(red/4));
        yelDur.setDuration(Duration.millis(yellow/2));
        greDur.setDuration(Duration.millis(green/4));
        System.out.println(red + " " + yellow + " " + green);
        mainSequence.getChildren().clear();
        mainSequence.getChildren().addAll(redSeq, yelSeq, greSeq);
        mainSequence.play();
    }

    private void initiateLightSequence() {

        redTrans = new FillTransition(Duration.millis(1), redLight, Color.GRAY, Color.RED);
        yelTrans = new FillTransition(Duration.millis(1), yellowLight, Color.GRAY, Color.YELLOW);
        greTrans = new FillTransition(Duration.millis(1), greenLight, Color.GRAY, Color.GREEN);

        redDur= new PauseTransition(Duration.millis(1));
        yelDur = new PauseTransition(Duration.millis(1));
        greDur = new PauseTransition(Duration.millis(1));

        redSeq = new SequentialTransition(redTrans, redDur);
        yelSeq = new SequentialTransition(yelTrans, yelDur);
        greSeq = new SequentialTransition(greTrans, greDur);
        redSeq.setAutoReverse(true);
        redSeq.setCycleCount(2);
        yelSeq.setAutoReverse(true);
        yelSeq.setCycleCount(2);
        greSeq.setAutoReverse(true);
        greSeq.setCycleCount(2);
    }

    public void initiateIdleSequence() {

        yelIdleTrans = new FillTransition(Duration.millis(100), yellowLight, Color.GRAY, Color.YELLOW);
        yelIdleDur = new PauseTransition(Duration.millis(1000));
        grayIdleDur = new PauseTransition(Duration.millis(1000));
    }

    /**
     *
     * @param red
     * @param yellow
     * @param green
     */
    public void changeLightSequence(int red, int yellow, int green) {
        animation(red, yellow, green);
    }
}