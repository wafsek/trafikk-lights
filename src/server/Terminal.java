package server;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class Terminal extends Thread {
    Scanner scanner;
    String input;


    public void run(){
        scanner = new Scanner(System.in);
        System.out.println("The Terminal window Traffic Light System v1.0");
        while (true) {
            input = scanner.next();
            switch (input) {
                case ("shutdown"): {
                    TrafficServer.getInstance().shutdown();
                    System.exit(0);
                }
                default:
                    System.out.println("Unsupported command!");
            }
        }
    }
}
