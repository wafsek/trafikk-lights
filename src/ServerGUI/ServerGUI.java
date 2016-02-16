package ServerGUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Created by kim on 15.02.2016.
 */
public class ServerGUI extends Application {

    private ScrollPane terminalwindow, clientlist;
    private Scene scene;
    private RadioButton red, yellow, green;
    private ToggleGroup colourGroup;
    private VBox left,nameoption,coloroption, slideroption;
    private HBox lightoption;
    private Slider redslider, yellowslider, greenslider;
    public ServerGUI(){
        Label redname = new Label("RED");
        Label yellowname = new Label("YELLOW");
        Label greenname = new Label("GREEN");

        colourGroup = new ToggleGroup();
        red = new RadioButton();
        red.setToggleGroup(colourGroup);
        yellow = new RadioButton();
        yellow.setToggleGroup(colourGroup);
        green = new RadioButton();
        green.setToggleGroup(colourGroup);

        redslider = new Slider();
        redslider.setPadding(new Insets(5,0,5,0));

        yellowslider = new Slider();
        yellowslider.setPadding(new Insets(0,0,5,0));

        greenslider = new Slider();
        greenslider.setPadding(new Insets(0,0,0,0));


        terminalwindow = new ScrollPane();
        terminalwindow.setPrefSize(1000,300);
        clientlist = new ScrollPane();
        clientlist.setPrefSize(300,800);

      /*  GridPane gpane = new GridPane();*/
        nameoption = new VBox();
        coloroption = new VBox();
        left = new VBox();
        slideroption = new VBox();
        lightoption = new HBox();

        BorderPane bpane = new BorderPane();




        nameoption.getChildren().addAll(redname,yellowname,greenname);
        coloroption.getChildren().addAll(red,yellow,green);
        slideroption.getChildren().addAll(redslider,yellowslider,greenslider);
        lightoption.getChildren().addAll(nameoption,coloroption,slideroption);
        left.getChildren().addAll(lightoption,terminalwindow);

        bpane.setPrefSize(1000,1000);

        bpane.setLeft(left);
        bpane.setRight(clientlist);

        scene = new Scene(bpane,1300,900);

     /*   gpane.setPrefSize(1000,1000);
     //   gpane.setAlignment(Pos.CENTER);


        gpane.add(left, 0, 0);
        gpane.add(clientlist, 1,0);

        scene = new Scene(gpane, 1300, 900);*/

    }
    public void start(Stage primaryStage){
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
