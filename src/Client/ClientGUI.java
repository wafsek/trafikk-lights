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

/**
 * Created by at_p9_000 on 16.02.2016.
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

    final Duration DEFAULTTIME = Duration.millis(2000);


    public ClientGUI(Stage stage, ClientController clientController) {
        hasSequence = false;
        isConnected = false;
        this.clientController = clientController;
        this.stage = stage;

        duration[0] = duration[1] = duration[2] = duration[3] = DEFAULTTIME;

        setIdleTimeLine();
        setRunningTimeLine();
        //Konstruerer trafikklyset
        StackPane trafikklys = new StackPane();
        Rectangle rektangel = new Rectangle(125, 300, 125, 300);
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
            clientController.requestConnection(handshakeField.getText().trim(),
                    hostField.getText().trim(),
                    Integer.parseInt(portField.getText().trim()));
            clearFields();
        });

        VBox rightVBox = new VBox(10, new Label("Host"),hostField,new Label("PortNumber"),
                portField,new Label("Handshake Command"), handshakeField, connect);
        rightVBox.setPadding(new Insets(10));

        VBox vBox = new VBox(5);
        vBox.getChildren().add(stop);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));


        rektangel.setFill(Color.BLACK);
        rektangel.setStroke(Color.BLACK);
        trafikklys.getChildren().add(rektangel);


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

    private void clearFields() {
        hostField.setText("");
        handshakeField.setText("");
        portField.setText("");
    }

    public void start() {

        stage.show();
        idle();
    }

    public void tryStart() {
        if(!hasSequence) {
            idle();
            hasSequence = !hasSequence;
            System.out.println("ran idle "+hasSequence);
        } else {
            animation();
            hasSequence = !hasSequence;
            System.out.println("ran animation "+hasSequence);
        }
    }

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

    private void setRunningTimeLine() {
        runningTimeLine = new Timeline[4];
        runningTimeLine[0] = new Timeline(new KeyFrame(
                duration[0],
                a -> {
                    yellowLight.setFill(Color.GREY);
                    redLight.setFill(Color.GREY);
                    greenLight.setFill(Color.GREEN);

                }));

        runningTimeLine[1] = new Timeline(new KeyFrame(
                duration[1],
                e -> {
                    yellowLight.setFill(Color.YELLOW);
                    redLight.setFill(Color.GREY);
                    greenLight.setFill(Color.GREY);

                }));

        runningTimeLine[2] = new Timeline(new KeyFrame(
                duration[2],
                ai -> {
                    yellowLight.setFill(Color.GREY);
                    redLight.setFill(Color.RED);
                    greenLight.setFill(Color.GREY);

                }));

        runningTimeLine[3] = new Timeline(new KeyFrame(
                duration[3],
                ie -> {
                    yellowLight.setFill(Color.YELLOW);
                    redLight.setFill(Color.RED);
                    greenLight.setFill(Color.GREY);

                }));
    }

    public void animation() {

        sqt.stop();
        sqt.getChildren().clear();
        sqt.getChildren().addAll(runningTimeLine[0], runningTimeLine[1], runningTimeLine[2], runningTimeLine[3]);
        sqt.setCycleCount(sqt.INDEFINITE);
        sqt.play();
    }

    public void setSequence(int a, double b) {
        duration[a] = Duration.millis(b);
    }

    public Duration getDuration(int a){
        return duration[a];
    }

    private void connect() {

    }
}