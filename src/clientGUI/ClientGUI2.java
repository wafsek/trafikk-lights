import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.*;

/**
 * Created by at_p9_000 on 16.02.2016.
 */
public class ClientGUI2 extends Application{



    public void start(Stage primaryStage){

        //Konstruerer trafikklyset
        StackPane trafikklys = new StackPane();
        Rectangle rektangel = new Rectangle();

        trafikklys.getChildren().add(rektangel);
        rektangel.setFill(Color.BLACK);
        rektangel.setStroke(Color.BLACK);

        //Designer trafikklyset
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.setHgap(5);
        layout.setVgap(3);
        trafikklys.getChildren().add(layout);

        //Desinger sirklene på trafikklyset
        Circle rodSirkel = new Circle(150, 60, 40);
        rodSirkel.setStroke(Color.BLACK);
        rodSirkel.setFill(Color.GREY);
        layout.add(rodSirkel, 1, 1);

        Circle gronnSirkel = new Circle(150, 60, 40);
        rodSirkel.setStroke(Color.BLACK);
        rodSirkel.setFill(Color.GREY);
        layout.add(gronnSirkel, 1, 3);

        Circle gulSirkel = new Circle(150, 60, 40);
        rodSirkel.setStroke(Color.BLACK);
        rodSirkel.setFill(Color.GREY);
        layout.add(gulSirkel, 1, 2);

        //Konstruerer hoved layouten
        BorderPane hovedLayout = new BorderPane();
        hovedLayout.setCenter(trafikklys);

        // Konstruerer vinduet
        Scene scene = new Scene(hovedLayout);
        primaryStage.setTitle("Trafikklys");
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    public static void main(String[] args){
        launch(args);
    }
}
