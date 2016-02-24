import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.event.*;

/**
 * Created by at_p9_000 on 16.02.2016.
 */
public class ClientGUI2 extends Application{

    GridPane layout = new GridPane();
    Circle rodSirkel = new Circle(150, 60, 40);
    Circle gulSirkel = new Circle(150, 60, 40);
    Circle gronnSirkel = new Circle(150, 60, 40);
    SequentialTransition sqt;
    Stage vindu;
    Scene scene;



    public void start(Stage primaryStage){
        vindu = primaryStage;
        //Konstruerer trafikklyset
        StackPane trafikklys = new StackPane();
        Rectangle rektangel = new Rectangle(125, 300, 125, 300);
        Button stoppKnapp = new Button("Stopp");
        stoppKnapp.setOnAction((ActionEvent event) -> stopp());


        VBox stopp = new VBox(5);
        stopp.getChildren().add(stoppKnapp);
        stopp.setAlignment(Pos.CENTER);
        stopp.setPadding(new Insets(10));


        rektangel.setFill(Color.BLACK);
        rektangel.setStroke(Color.BLACK);
        trafikklys.getChildren().add(rektangel);


        //Desinger sirklene på trafikklyset

        rodSirkel.setStroke(Color.BLACK);
        rodSirkel.setFill(Color.GREY);
        layout.add(rodSirkel, 1, 1);

        gulSirkel.setStroke(Color.BLACK);
        gulSirkel.setFill(Color.YELLOW);
        layout.add(gulSirkel, 1, 2);

        layout.setVgap(5);
        layout.setAlignment(Pos.CENTER);

        gronnSirkel.setStroke(Color.BLACK);
        gronnSirkel.setFill(Color.GREY);
        layout.add(gronnSirkel, 1, 3);
        trafikklys.getChildren().add(layout);

        //Konstruerer hoved layouten
        BorderPane hovedLayout = new BorderPane();
        hovedLayout.setPadding(new Insets(10));
        hovedLayout.setCenter(trafikklys);
        hovedLayout.setBottom(stopp);

        // Konstruerer vinduet
        scene = new Scene(hovedLayout);
        vindu.setTitle("Trafikklys");
        vindu.setScene(scene);
        vindu.show();


        animasjon();
    }




    public void stopp(){

        sqt.stop();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            rodSirkel.setFill(Color.GREY);
            gronnSirkel.setFill(Color.GREY);
            gulSirkel.setFill(Color.YELLOW);
        }));

        Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            rodSirkel.setFill(Color.GREY);
            gronnSirkel.setFill(Color.GREY);
            gulSirkel.setFill(Color.GREY);
        }));

        SequentialTransition sqt = new SequentialTransition(timeline, timeline1);

        sqt.setCycleCount(sqt.INDEFINITE);
        sqt.play();

    }

    public void animasjon() {

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2000),
                a -> {
                    gulSirkel.setFill(Color.GREY);
                    rodSirkel.setFill(Color.GREY);
                    gronnSirkel.setFill(Color.GREEN);

                }));

        Timeline timeline1 = new Timeline(new KeyFrame(
                Duration.millis(2000),
                e -> {
                    gulSirkel.setFill(Color.YELLOW);
                    rodSirkel.setFill(Color.GREY);
                    gronnSirkel.setFill(Color.GREY);

                }));

        Timeline timeline2 = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ai -> {
                    gulSirkel.setFill(Color.GREY);
                    rodSirkel.setFill(Color.RED);
                    gronnSirkel.setFill(Color.GREY);

                }));

        Timeline timeline3 = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ie -> {
                    gulSirkel.setFill(Color.YELLOW);
                    rodSirkel.setFill(Color.RED);
                    gronnSirkel.setFill(Color.GREY);

                }));




        sqt = new SequentialTransition(timeline, timeline1, timeline2, timeline3);
        sqt.setCycleCount(sqt.INDEFINITE);
        sqt.play();
}


    public static void main(String[] args){
        launch(args);
    }
}
