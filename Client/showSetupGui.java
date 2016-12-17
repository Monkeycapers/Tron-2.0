package Client;

/**
 * Created by Evan on 12/15/2016.
 */
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Server.GameServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class showSetupGui extends Application {

    Pane rootLayout;

    public static FXMLLoader loader;

    public static GameClient client;

    public static void main(String[] args) {
        Application.launch(showSetupGui.class, (java.lang.String[])null);
    }

    public static void showSetupGui() {
        Application.launch(showSetupGui.class, (java.lang.String[])null);
    }

    public void showGui() {

        Application.launch(showSetupGui.class, (java.lang.String[])null);

    }

    @Override
    public void start(Stage primaryStage) {
        try {
            client =  new GameClient("localhost", 16000);
            client.start();

            //showSetupGui.showSetupGui();
            GameServer gameServer =  new GameServer(16000, 8080);
            gameServer.start();

            //new Thread(new Rayc()).start();
            // Load root layout from fxml file.
            loader = new FXMLLoader();
            loader.setLocation(showSetupGui.class.getResource("signin.fxml"));
            rootLayout = (Pane) loader.load();
            signinController signin = loader.getController();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

//            try {Thread.sleep(100);}catch (Exception e) { }
//            while (true) {
//                System.out.print(">");
//                client.sendMessage(new Scanner(System.in).nextLine());
//            }
            System.out.println(this.getClass().toString());
            new Thread(new ConsoleThread()).start();

        } catch (Exception ex) {
            Logger.getLogger(showSetupGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
