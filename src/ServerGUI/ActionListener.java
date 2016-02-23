package ServerGUI;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Created by kim on 23.02.2016.
 */
public class ActionListener implements EventHandler<Event>{
    ServerGUI serverGUI;
    public void handle(Event event){
        if(event instanceof MouseEvent){
            if(event.getSource() == serverGUI.getRedSlider()){
                //DO SOMETHING WITH THE RED SLIDER
            }
            if(event.getSource() == serverGUI.getYellowslider()){
                //DO SOMETHING WITH THE YELLOW SLIDER
            }
            if(event.getSource() == serverGUI.getGreenslider()){
                //DO SOMETHING WITH THE GREEN SLIDER
            }
        }
        if(event instanceof ActionEvent){

        }
    }
}
