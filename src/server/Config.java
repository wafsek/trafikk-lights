package server;

/**
 * Created by Baljit Singh Sarai on 17.02.16.
 * @author Baljit Singh Sarai
 */

import logging.CustomLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

/**
 * This class is going to set all configurations of the program.
 */
public class Config {
    private static CustomLogger logger = CustomLogger.getInstance();

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
     * Trys to convert a String to an integer and return it.
     * @param string The input string.
     * @param name Name of the property to set.
     * @return integer. If it fails it returns -1.
     */
    private static int stringToInt(String string,String name){
        int result = -1;
        try{
            result = Integer.parseInt(string);
        }catch (NumberFormatException nfe){
            logger.log("Was unable to set " +name+ " property ",Level.WARNING);
            System.out.println();
        }
        finally {
            return result;
        }
    }



    /**
     * Returns the size that the buffer-size should be.
     * @return The port number to be set to the server.
     */
    public static int getServerPort() {
        int result;
        String name = "port";
        String stringBufferSize = getProperties().getProperty(name);
        result = stringToInt(stringBufferSize,name);
        if(result != -1){
            return result;
        }
        return 12345;
    }

    /**
     * Returns the size that the buffer-size should be.
     *
     * @return The buffer size
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
     * @return The number of service threads
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
     * @return The number of terminator threads
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
     * @return The loopback time
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
     * @return the application's name
     */
    public static String getApplicationName() {
        return getProperties().getProperty("applicationName");
    }

    public static Level getGuiLoggingLevel(){
        String guilevel = getProperties().getProperty("guiLoggingLevel");;

        switch (guilevel){
            case "severe":{
                return  Level.SEVERE;
            }
            case "warning":{
                return Level.WARNING;
            }
            case "info":{
                return Level.INFO;
            }
            case "config":{
                return Level.CONFIG;
            }
            case "fine":{
                return Level.FINE;
            }
            case "finer":{
                return Level.FINER;
            }
            case "finest":{
                return Level.FINEST;
            }
            default:{
                return Level.INFO;
            }
        }
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
