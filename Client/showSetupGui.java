package Client;

/**
 * Created by Evan on 12/15/2016.
 */
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Jesty.test.testServer;
import Server.GameServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONWriter;

public class showSetupGui extends Application {

    public static Pane rootLayout;

    public static Pane outOfMenuLayout;

    public static Stage stage;

    public static FXMLLoader loginloader;

    public static FXMLLoader outOfMenuLoader;

    public static GameClient client;

    public static testServer testServer;

    public static HashMap<String , chatTabController> chatTabHashMap;

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
            chatTabHashMap = new HashMap<>();
            client =  new GameClient("localhost", 16000);
            client.start();

//            GameServer gameServer =  new GameServer(16000, 8080);
//            gameServer.start();

//            testServer = new testServer(16000, 8080);
//            testServer.start();

            // Load root layout from fxml file.
            loginloader = new FXMLLoader();
            loginloader.setLocation(showSetupGui.class.getResource("signin.fxml"));
            rootLayout = (Pane) loginloader.load();
            signinController signin = loginloader.getController();

            outOfMenuLoader = new FXMLLoader();
            outOfMenuLoader.setLocation(showSetupGui.class.getResource("outofmenu.fxml"));
            outOfMenuLayout = (Pane) outOfMenuLoader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage = primaryStage;
            stage.setScene(scene);
            stage.show();
//            Scene scene = new Scene(outOfMenuLayout);
//            stage = primaryStage;
//            stage.setScene(scene);
//            stage.show();

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

    public static void showOutOfGameMenu() {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Scene scene = new Scene(outOfMenuLayout);
                        stage.setScene(scene);
                        stage.show();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        public static void addChatTab(String name, chatTabController t) {
            chatTabHashMap.putIfAbsent(name, t);
        }

    public static void handleChatMessage(String name, String text) {
        if (!chatTabHashMap.containsKey(name)) {
            return;
        }
        StringWriter stringWriter = new StringWriter();
        new JSONWriter(stringWriter).object()
                .key("argument").value("chatmessage")
                .key("name").value(name)
                .key("message").value(text)
                .endObject();
        client.sendMessage(stringWriter.toString());
    }

    public static void pushMessage(String name, String displayName, String message) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                        serverListController serverList = (serverListController) outOfMenuLoader.getController();

                        if (!chatTabHashMap.containsKey(name)) {
                            serverList.addNewTab(name, displayName);
                        }
                        chatTabController chattab = chatTabHashMap.get(name);
                        TextArea textArea = chattab.chatTextArea;
                        if (textArea.getText().equals("")) {
                            textArea.setText(message);
                        }
                        else {
                            textArea.setText(textArea.getText() + "\n" + message);
                        }
                    chattab.lastDisplayName = displayName;
                    for (Tab tab: serverList.chatTabPane.getTabs()) {
                        System.out.println("Tab text: " + tab.getText());
                        System.out.println(tab.getText().startsWith(displayName));
                        if (tab.getText().startsWith(displayName)) {
                            if (!tab.isSelected()) tab.setText(displayName + " " + (chattab.addPendingMessage()));
                        }
                    }
                    }
            });
    }


        //chatTabHashMap.get(name).chatTextArea.setText(chatTabHashMap.get(name).chatTextArea.getText() + "\n" + message);


    public static void addUser(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                serverListController serverList = (serverListController) outOfMenuLoader.getController();
                serverList.addUser(text);
            }
        });
    }

    public static void addUsers(List<Object> users) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                serverListController serverList = (serverListController) outOfMenuLoader.getController();
                for (Object s: users) {
                    serverList.addUser((String)s);
                }
                //serverList.addUser(text);
            }
        });
    }

    public static void removeUser(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                serverListController serverList = (serverListController) outOfMenuLoader.getController();
                serverList.removeUser(text);
            }
        });
    }

}
