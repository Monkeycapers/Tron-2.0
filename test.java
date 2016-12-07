import Client.GameClient;
import Server.GameServer;

import java.util.Scanner;

/**
 * Created by Evan on 12/6/2016.
 */
public class test {

    public static void main (String[] args) {
       GameClient client =  new GameClient("localhost", 16000);
       client.start();
        new GameServer(16000, 8080).start();

        try {Thread.sleep(100);}catch (Exception e) { }
        while (true) {
            System.out.print(">");
            client.sendMessage(new Scanner(System.in).nextLine());
        }
    }
}
