package server;

import logging.CustomLogger;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * This class runs in a loop and gets input from the user in the console to execute commands on the server.
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class Terminal extends Thread {
    Scanner scanner;
    String input;
    private CustomLogger logger = CustomLogger.getInstance();
    private TrafficServer trafficServer = TrafficServer.getInstance();
    CommandHandler commandHandler = new CommandHandler(trafficServer);

    @Override
    public void run(){
        scanner = new Scanner(System.in);
        this.logger.log("The Terminal window Traffic Light System v1.0", Level.INFO);
        while (true) {
            input = scanner.next();
            //This class is not needed as this program has a GUI.
            //This maybe be usefull if ever the terminal is needed.
            System.out.println(input);
        }
    }
}
