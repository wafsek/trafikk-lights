package Client;

/**
 * Created by Baljit Singh Sarai on 15.02.16.
 * @author Baljit Sarai
 */
public class ClientMain {



    public static void main(String... args){
        ClientSocket clientSocket = new ClientSocket("localhost",12345);
        //clientSocket.handShake();
    }
}
