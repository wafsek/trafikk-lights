package logging;

import serverGUI.ServerGUI;
import javafx.application.Platform;

import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Baljit Singh Sarai on 01.03.16.
 * @author Baljit Singh Sarai
 */
public class CustomHandler extends Handler {
    private ServerGUI serverGUI;
    private static final String lineSep = System.getProperty("line.separator");
    CustomHandler(ServerGUI serverGUI,Level level) {
        super.setLevel(level);
        this.serverGUI = serverGUI;
    }

    @Override
    public void publish(LogRecord record) {
        String loggerName = record.getLoggerName();
        if(loggerName == null) {
            loggerName = "root";
        }
        StringBuilder output = new StringBuilder()
                .append("[")
                .append(record.getLevel()).append('|')
                .append(new Date(record.getMillis()))
                .append("]: ")
                .append(record.getMessage())
                .append(lineSep);
        output.toString();


        if(this.getLevel().intValue() <= record.getLevel().intValue()){
            Platform.runLater(() -> this.serverGUI.getLogg().appendText(output.toString()));
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
