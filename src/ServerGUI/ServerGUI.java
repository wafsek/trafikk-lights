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

    private ActionListener actionListener;
    private ScrollPane terminalwindow, clientlist;
    private Scene scene;
    private TextArea redValue,yellowValue,greenValue;
    private RadioButton red, yellow, green;
    private ToggleGroup colourGroup;
    private VBox left,nameoption,coloroption, slideroption, valueoption;
    private HBox lightoption;
    private Slider redslider, yellowslider, greenslider;

    public ServerGUI(/*ActionListener actionListener*/){
       // this.actionListener = actionListener;
        Label redname = new Label("RED");
        redname.setPadding(new Insets(20,0,35,0));
        Label yellowname = new Label("YELLOW");
        Label greenname = new Label("GREEN");
        greenname.setPadding(new Insets(35,0,0,0));

        colourGroup = new ToggleGroup();
        red = new RadioButton();
        red.setToggleGroup(colourGroup);
        red.setPadding(new Insets(20,0,35,0));
        red.setOnAction(e-> redRadioButtonAction());
        yellow = new RadioButton();
        yellow.setToggleGroup(colourGroup);
        yellow.setOnAction(e->yellowRadioButtonAction());
        green = new RadioButton();
        green.setToggleGroup(colourGroup);
        green.setPadding(new Insets(35,0,0,0));
        green.setOnAction(e->greenRadioButtonAction());

        redslider = new Slider(0,100,50);
        redslider.setPadding(new Insets(24,0,40,0));
        redslider.setOnMouseDragged(e->redSliderAction());
        redslider.setPrefSize(900,0);

        redValue = new TextArea();
        redValue.setPrefSize(1,1);
        redValue.setText(String.format("%.0f",redslider.getValue()));

        yellowslider = new Slider(0,100,50);
        yellowslider.setPadding(new Insets(0,0,45,0));
        yellowslider.setOnMouseDragged(e->yellowSliderAction());

        yellowValue = new TextArea();
        yellowValue.setPrefSize(1,1);
        yellowValue.setText(String.format("%.0f",yellowslider.getValue()));

        greenslider = new Slider(0,100,50);
        greenslider.setPadding(new Insets(0,0,0,0));
        greenslider.setOnMouseDragged(e->greenSliderAction());

        greenValue = new TextArea();
        greenValue.setPrefSize(1,1);
        greenValue.setText(String.format("%.0f",greenslider.getValue()));


        terminalwindow = new ScrollPane();
        terminalwindow.setPrefSize(1000,300);
        clientlist = new ScrollPane();
        clientlist.setPrefSize(300,800);

      /*  GridPane gpane = new GridPane();*/
        valueoption = new VBox();
        nameoption = new VBox();
        coloroption = new VBox();
        left = new VBox();
        slideroption = new VBox();
        lightoption = new HBox();

        BorderPane bpane = new BorderPane();


        nameoption.getChildren().addAll(redname,yellowname,greenname);
        coloroption.getChildren().addAll(red,yellow,green);
        slideroption.getChildren().addAll(redslider,yellowslider,greenslider);
        valueoption.getChildren().addAll(redValue,yellowValue,greenValue);
        lightoption.getChildren().addAll(nameoption,coloroption,slideroption,valueoption);
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
        redValue.setText(String.format("%.0f",redslider.getValue()));
        System.out.println(redslider.getValue());
    }
    public void yellowSliderAction(){
        yellowValue.setText(String.format("%.0f",yellowslider.getValue()));
        System.out.println(yellowslider.getValue());
    }
    public void greenSliderAction(){
        greenValue.setText(String.format("%.0f",greenslider.getValue()));
        System.out.println(greenslider.getValue());
    }
}
