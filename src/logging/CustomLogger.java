package logging;
import java.io.IOException;
import java.util.logging.*;

import ServerGUI.ServerGUI;
import server.Config;
/**
 * Created by Baljit Singh Sarai on 01.03.16.
 * @author Baljit Singh Sarai
 */
public class CustomLogger {

    private Logger logger = null;
    private FileHandler txtFileHandler;
    private ConsoleHandler consoleHandler;
    String className = CustomLogger.class.getName();

    private static CustomLogger customLogger;

    private CustomLogger() {
        try {
            CustomFormatter customFormatter = new CustomFormatter();
            txtFileHandler = new FileHandler("log.txt",true);
            txtFileHandler.setFormatter(customFormatter);
            consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new ConsoleFormatter());
        } catch (IOException ioe) {
            System.out.print("Something went wrong opening the log files");
        }
        this.logger = Logger.getLogger(this.className);
        this.setConsoleOutput();
        this.setLoggerLevel(Level.FINEST);
        this.logger.addHandler(txtFileHandler);
    }

    public static CustomLogger getInstance() {
        if(customLogger==null) {
            customLogger= new CustomLogger();
        }
        return customLogger;
    }

    private void customLog(String msg, Level level) {
        this.logger.logp(level,
                Thread.currentThread().getStackTrace()[3].getClassName(),
                Thread.currentThread().getStackTrace()[3].getMethodName(), msg);
    }

    public void log(String msg, Level level) {
        customLog(msg, level);
    }

    public void setConsoleOutput() {
        if (Config.getConsoleOutputOption().toLowerCase().equals("trimmed")) {
            this.logger.addHandler(consoleHandler);
            logger.setUseParentHandlers(false);
        } else if (Config.getConsoleOutputOption().toLowerCase().equals("detail")) {
            logger.setUseParentHandlers(true);
        } else if (Config.getConsoleOutputOption().toLowerCase().equals("none")) {
            logger.removeHandler(consoleHandler);
            logger.setUseParentHandlers(false);
        }
    }

    public void setTextOutput(){

    }

    public void addTextAreaLog(ServerGUI serverGUI){
        this.logger.addHandler(new CustomHandler(serverGUI,Level.INFO));
    }

    private void setLoggerLevel(Level loggerLevel) {
        this.logger.setLevel(loggerLevel);
    }


}
