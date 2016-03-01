package server;

import logging.CustomLogger;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class Terminal extends Thread {
    Scanner scanner;
    String input;
    private CustomLogger logger = CustomLogger.getInstance();


    public void run(){
        scanner = new Scanner(System.in);
        this.logger.log("The Terminal window Traffic Light System v1.0", Level.INFO);
        while (true) {
            input = scanner.next();
            switch (input) {
                case ("shutdown"): {
                    //TrafficServer.getInstance().shutdown();
                    System.exit(0);
                }
                default:
                    System.out.println("Unsupported command!");
            }
            System.out.println(input);
        }
    }
}
