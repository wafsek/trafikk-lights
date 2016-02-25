package Client;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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


    Timeline[] timelines = new Timeline[4];
    Duration[] duration = new Duration[4];

    final Duration DEFAULTTIME = Duration.millis(2000);


    public ClientGUI(Stage stage, ClientController clientController) {
        this.clientController = clientController;
        this.stage = stage;
        //Konstruerer trafikklyset
        StackPane trafikklys = new StackPane();
        Rectangle rektangel = new Rectangle(125, 300, 125, 300);
        stop = new Button("Stop");
        stop.setOnAction(e -> idle());
        connect = new Button("Connect");

        hostField = new TextField();
        hostField.setPromptText("Host");

        portField = new TextField();
        portField.setPromptText("Port Number");

        handshakeField = new TextField();
        handshakeField.setPromptText("Connection Command");
        connect.setOnAction(e -> {
            clientController.requestConnection(handshakeField.getText().trim());
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
        duration[0] = duration[1] = duration[2] = duration[3] = DEFAULTTIME;
        animation();
    }


    public void idle() {

        sqt.stop();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            redLight.setFill(Color.GREY);
            greenLight.setFill(Color.GREY);
            yellowLight.setFill(Color.YELLOW);
        }));

        Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            redLight.setFill(Color.GREY);
            greenLight.setFill(Color.GREY);
            yellowLight.setFill(Color.GREY);
        }));

        SequentialTransition sqt = new SequentialTransition(timeline, timeline1);

        sqt.setCycleCount(sqt.INDEFINITE);
        sqt.play();

    }

    public void animation() {


        timelines[0] = new Timeline(new KeyFrame(
                duration[0],
                a -> {
                    yellowLight.setFill(Color.GREY);
                    redLight.setFill(Color.GREY);
                    greenLight.setFill(Color.GREEN);

                }));

        timelines[1] = new Timeline(new KeyFrame(
                duration[1],
                e -> {
                    yellowLight.setFill(Color.YELLOW);
                    redLight.setFill(Color.GREY);
                    greenLight.setFill(Color.GREY);

                }));

        timelines[2] = new Timeline(new KeyFrame(
                duration[2],
                ai -> {
                    yellowLight.setFill(Color.GREY);
                    redLight.setFill(Color.RED);
                    greenLight.setFill(Color.GREY);

                }));

        timelines[3] = new Timeline(new KeyFrame(
                duration[3],
                ie -> {
                    yellowLight.setFill(Color.YELLOW);
                    redLight.setFill(Color.RED);
                    greenLight.setFill(Color.GREY);

                }));


        sqt = new SequentialTransition(timelines[0], timelines[1], timelines[2], timelines[3]);
        sqt.setCycleCount(sqt.INDEFINITE);
        sqt.play();
    }

    public void setDuration(int a, double b) {
        duration[a] = Duration.millis(b);
    }

    public Duration getDuration(int a){
        return duration[a];
    }

    private void connect() {

    }
}