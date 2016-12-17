import Client.GameClient;
import Server.GameServer;

import Client.showSetupGui;

import java.util.Scanner;

/**
 * Created by Evan on 12/6/2016.
 */
public class test {
    public static GameClient client;
    public static void main (String[] args) {
       client =  new GameClient("localhost", 16000);
       client.start();
        showSetupGui.showSetupGui();
       GameServer gameServer =  new GameServer(16000, 8080);
       gameServer.start();

        try {Thread.sleep(100);}catch (Exception e) { }
        while (true) {
            System.out.print(">");
            client.sendMessage(new Scanner(System.in).nextLine());
        }
        //new Thread(new Rayc()).start();
    }
}
