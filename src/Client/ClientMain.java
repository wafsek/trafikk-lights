package Client;

/**
 * Created by wafsek on 15.02.16.
 */
public class ClientMain {



    public static void main(String... args){
        ClientSocket clientSocket = new ClientSocket("localhost",12345);
        clientSocket.handShake();


    }
}
