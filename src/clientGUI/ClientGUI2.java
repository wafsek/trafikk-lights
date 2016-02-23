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

import java.sql.Time;

/**
 * Created by at_p9_000 on 16.02.2016.
 */
public class ClientGUI2 extends Application{

    GridPane layout = new GridPane();
    Circle rodSirkel = new Circle(150, 60, 40);
    Circle gulSirkel = new Circle(150, 60, 40);
    Circle gronnSirkel = new Circle(150, 60, 40);
    Stage vindu;
    Scene scene;



    public void start(Stage primaryStage){
        vindu = primaryStage;
        //Konstruerer trafikklyset
        StackPane trafikklys = new StackPane();
        Rectangle rektangel = new Rectangle(125, 300, 125, 300);
        Button stoppKnapp = new Button("Stopp");
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



    public void animasjon() {

        Timeline timeline;

        timeline = new Timeline(new KeyFrame(
                    Duration.seconds(4),
                    ae -> {
                        gulSirkel.setFill(Color.GREY);
                        rodSirkel.setFill(Color.GREY);
                        gronnSirkel.setFill(Color.GREEN);

                        Timeline timeline1 = new Timeline(new KeyFrame(
                                Duration.seconds(1),
                                e -> {
                                    rodSirkel.setFill(Color.GREY);
                                    gronnSirkel.setFill(Color.GREY);
                                    gulSirkel.setFill(Color.YELLOW);

                                    Timeline timeline2 = new Timeline(new KeyFrame(
                                            Duration.seconds(3),
                                            a -> {
                                                gronnSirkel.setFill(Color.GREY);
                                                gulSirkel.setFill(Color.GREY);
                                                rodSirkel.setFill(Color.RED);



                                                Timeline timeline3 = new Timeline(new KeyFrame(
                                                        Duration.seconds(1),
                                                        ai -> {
                                                            gronnSirkel.setFill(Color.GREY);
                                                            gulSirkel.setFill(Color.YELLOW);
                                                            rodSirkel.setFill(Color.GREY);

                                                        }
                                                ));



                                                timeline3.play();
                                            }
                                    ));

                                    timeline2.play();

                                }
                        ));

                        timeline1.play();

                    }
         ));


        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();

    }




    public static void main(String[] args){
        launch(args);
    }
}
