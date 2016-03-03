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
    private SequentialTransition sqt;
    private Stage stage;
    private Scene scene;
    private Button stop, connect;
    private ClientController clientController;
    private TextField handshakeField, hostField, portField;
    private boolean hasSequence, isConnected;
    private Timeline[] idleTimeLine, runningTimeLine;
    Duration[] duration = new Duration[4];

    final int DEFAULTTIME = 2000;


    /**
     * Constructor. Constructs the ClientGUI with all relevant fields.
     * @param stage
     * @param clientController
     */
    public ClientGUI(Stage stage, ClientController clientController) {
        hasSequence = false;
        isConnected = false;
        this.clientController = clientController;
        this.stage = stage;

        setIdleTimeLine();
        setRunningTimeLine(DEFAULTTIME, DEFAULTTIME, DEFAULTTIME);
        //Konstruerer trafikklyset
        StackPane trafikklys = new StackPane();
        Rectangle rectangle = new Rectangle(125, 300, 125, 300);
        stop = new Button("Stop");
        stop.setOnAction(e -> tryStart());
        connect = new Button("Connect");

        hostField = new TextField();
        hostField.setPromptText("Host");

        portField = new TextField();
        portField.setPromptText("Port Number");

        handshakeField = new TextField();
        handshakeField.setPromptText("Connection Command");
        connect.setOnAction(e -> {
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
        });

        VBox rightVBox = new VBox(10, new Label("Host"),hostField,new Label("PortNumber"),
                portField,new Label("Handshake Command"), handshakeField, connect);
        rightVBox.setPadding(new Insets(10));

        VBox vBox = new VBox(5);
        vBox.getChildren().add(stop);
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
        yellowLight.setFill(Color.YELLOW);
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
            animation();
            hasSequence = !hasSequence;
        }
    }

    /**
     * Sets the animation rules for idle.
     */
    private void setIdleTimeLine() {
        idleTimeLine = new Timeline[2];
        idleTimeLine[0] = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            redLight.setFill(Color.GREY);
            greenLight.setFill(Color.GREY);
            yellowLight.setFill(Color.YELLOW);
        }));

        idleTimeLine[1] = new Timeline(new KeyFrame(Duration.millis(1500), e -> {
            redLight.setFill(Color.GREY);
            greenLight.setFill(Color.GREY);
            yellowLight.setFill(Color.GREY);
        }));
    }

    /**
     * Initiates the idle animation
     */
    public void idle() {

        if(sqt != null) {
            sqt.stop();
        }
        try {
            sqt.getChildren().clear();
            sqt.getChildren().addAll(idleTimeLine[0], idleTimeLine[1]);
        } catch (NullPointerException npe) {
            sqt = new SequentialTransition(idleTimeLine[0], idleTimeLine[1]);
        }
        sqt.setCycleCount(sqt.INDEFINITE);
        sqt.play();
    }

    /**
     * Sets the run time for the different lights based on three parameters.
     * @param red
     * @param yellow
     * @param green
     */
    private void setRunningTimeLine(int red, int yellow, int green) {
        duration[0] = new Duration(green);
        duration[1] = new Duration(yellow);
        duration[2] = new Duration(red);
        runningTimeLine = new Timeline[4];
        runningTimeLine[0] = new Timeline(new KeyFrame(
                duration[1],
                a -> {
                    yellowLight.setFill(Color.GREY);
                    redLight.setFill(Color.GREY);
                    greenLight.setFill(Color.GREEN);

                }));

        runningTimeLine[1] = new Timeline(new KeyFrame(
                duration[0],
                e -> {
                    yellowLight.setFill(Color.YELLOW);
                    redLight.setFill(Color.GREY);
                    greenLight.setFill(Color.GREY);

                }));

        runningTimeLine[2] = new Timeline(new KeyFrame(
                duration[1],
                ai -> {
                    yellowLight.setFill(Color.GREY);
                    redLight.setFill(Color.RED);
                    greenLight.setFill(Color.GREY);

                }));

        runningTimeLine[3] = new Timeline(new KeyFrame(
                duration[2],
                ie -> {
                    yellowLight.setFill(Color.YELLOW);
                    redLight.setFill(Color.RED);
                    greenLight.setFill(Color.GREY);

                }));
    }

    /**
     * Starts the main animation (red, yellow and green light).
     */
    public void animation() {

        sqt.stop();
        sqt.getChildren().clear();
        sqt.getChildren().addAll(runningTimeLine[0], runningTimeLine[1], runningTimeLine[2], runningTimeLine[3]);
        sqt.setCycleCount(sqt.INDEFINITE);
        sqt.play();
    }

    /**
     *
     * @param red
     * @param yellow
     * @param green
     */
    public void changeLightSequence(int red, int yellow, int green) {
        setRunningTimeLine(red, yellow, green);
        animation();
    }

    /**
     *
     * @param a
     * @param b
     */
    public void setSequence(int a, double b) {
        duration[a] = Duration.millis(b);
    }

    /**
     *
     * @param a
     * @return
     */
    public Duration getDuration(int a){
        return duration[a];
    }
}