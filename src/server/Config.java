package server;

/**
 * Created by Baljit Singh Sarai on 17.02.16.
 * @author Baljit Singh Sarai
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is going to set all configurations of the program.
 */
public class Config {

    /**
     * Returns the properties for this config.
     *
     * @return the properties for this config
     */
    private static Properties getProperties() {
        Properties prop = new Properties();
        try (InputStream inputStream =
                     Config.class.getResourceAsStream("config.properties")) {
            prop.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }


    /**
     * Try to convert A String
     */
    private static int stringToInt(String string,String name){
        int result = -1;
        try{
            result = Integer.parseInt(string);
        }catch (NumberFormatException nfe){
            System.out.println("Was unable to set " +name+ " property ");
        }
        finally {
            return result;
        }
    }


    /**
     * Returns the size that the buffer-size should be.
     *
     * @returnt The buffer size
     */
    public static int getBufferSize() {
        int result;
        String name = "buffersize";
        String stringBufferSize = getProperties().getProperty(name);
        result = stringToInt(stringBufferSize,name);
        if(result != -1){
            return result;
        }
        return 20;
    }

    /**
     * Returns the number of service worker threads should be.
     *
     * @returnt The number of service threads
     */
    public static int getServiceWorkers() {
        int result;
        String name = "serviceworkers";
        String stringBufferSize = getProperties().getProperty(name);
        result = stringToInt(stringBufferSize,name);
        if(result != -1){
            return result;
        }
        return 2;
    }

    /**
     * Returns the number of terminator worker threads should be.
     *
     * @returnt The number of terminator threads
     */
    public static int getTerminators() {
        int result;
        String name = "terminators";
        String stringBufferSize = getProperties().getProperty(name);
        result = stringToInt(stringBufferSize,name);
        if(result != -1){
            return result;
        }
        return 1;
    }

    /**
     * Returns the size that the loopback time should be.
     *
     * @returnt The loopback time
     */
    public static int getLoopbackTime() {
        int result;
        String name = "loopbacktime";
        String stringBufferSize = getProperties().getProperty(name);
        result = stringToInt(stringBufferSize,name);
        if(result != -1){
            return result;
        }
        return 500;
    }


    /**
     * Returns the application's name. Will among other things be shown in the
     * window title.
     *
     * @returnt the application's name
     */
    public static String getApplicationName() {
        return getProperties().getProperty("applicationName");
    }

    /**
     * Returns the current console output option.
     *
     * @return the current console output option
     */
    public static String getConsoleOutputOption() {
        return getProperties().getProperty("consoleOutput");
    }

    /**
     * Returns a string with the current logging level.
     *
     * @return the current logging level
     */
    public static String getLoggingLevelString() {
        return getProperties().getProperty("loggingLevel");
    }

}
