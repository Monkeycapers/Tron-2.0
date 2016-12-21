package Client;

import java.util.Scanner;

/**
 * Created by Evan on 12/16/2016.
 */
public class ConsoleThread implements Runnable {

    @Override
    public void run() {
        try {Thread.sleep(100);}catch (Exception e) { }
        while (true) {
            System.out.print(">");
            showSetupGui.testServer.sendMessage(new Scanner(System.in).nextLine());
        }
    }
}
