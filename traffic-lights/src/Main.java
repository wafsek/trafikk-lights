import com.sun.org.apache.xpath.internal.SourceTree;
import server.TrafficServer;

/**
 * Created by Baljit Singh Sarai on 01.02.16.
 * @author Baljit Singh Sarai
 */
public class Main {

    public static void main(String[] args){
        System.out.println("Welcome to the traffic light program");
        TrafficServer server = TrafficServer.getInstance();
        server.start();
    }
}
