package logging;

import ServerGUI.ServerGUI;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * Created by Baljit Singh Sarai on 01.03.16.
 * @author Baljit Singh Sarai
 */
public class CustomHandler extends Handler {
    private ServerGUI serverGUI;

    CustomHandler(ServerGUI serverGUI,Level level) {
        super.setLevel(level);
        this.serverGUI = serverGUI;
    }

    @Override
    public void publish(LogRecord record) {
        if(this.getLevel().intValue() <= record.getLevel().intValue()){
            this.serverGUI.getLogg().appendText(record.getMessage()+"\n");
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
