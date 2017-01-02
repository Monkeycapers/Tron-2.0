package Client;

/**
 * Created by Evan on 12/15/2016.
 */
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Jesty.*;
import Jesty.test.testServer;
import Server.GameServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONWriter;

public class showSetupGui extends Application {

    public static Pane rootLayout;

    public static Pane outOfMenuLayout;

    public static Pane loginLayout;

    public static Pane connectLayout;

    //public static Pane loadingLayout;

    public static Stage stage;

    public static FXMLLoader loginloader;

    public static FXMLLoader outOfMenuLoader;

    public static FXMLLoader connectLoader;

    public static GameClient client;

    public static testServer testServer;

    public static Canvas canvas;

    public static AnchorPane canvasPane;

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

    public boolean isANumber(String number) {
        try {
            Integer.parseInt(number);
        }
        catch (NumberFormatException e) {
            //Not a integer number
            return false;
        }
        //Is a integer number
        return true;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            //This is where all of the gui's are initially loaded (not shown), and the client is started.

            chatTabHashMap = new HashMap<>();


            HashMap<String, String> defaults = new HashMap<>();
            defaults.put("host", "localhost");
            defaults.put("port", "16000");

            Settings.setFile(new File("clientsettings.txt"), defaults);
            Settings.load();

            String host = Settings.getProperty("host");
            String port = Settings.getProperty("port");

            //Error checking: The host and ports must be not null, and the port must be a int
            if (host.equals("") || port.equals("") || isANumber(port)) {
                host = "localhost";
                port = "16000";
                Settings.setProperty("host", host);
                Settings.setProperty("port", port);
                Settings.save();
            }



//            GameServer gameServer =  new GameServer(16000, 8080);
//            gameServer.start();

//            testServer = new testServer(16000, 8080);
//            testServer.start();

            // Load root layout from fxml file.
            loginloader = new FXMLLoader();
            loginloader.setLocation(showSetupGui.class.getResource("signin.fxml"));
            //rootLayout = (Pane) loginloader.load();
            loginLayout = loginloader.load();
            //signinController signin = loginloader.getController();

            outOfMenuLoader = new FXMLLoader();
            outOfMenuLoader.setLocation(showSetupGui.class.getResource("outofmenu.fxml"));
            outOfMenuLayout = (Pane) outOfMenuLoader.load();
            canvas = ((serverListController)(outOfMenuLoader.getController())).canvas;
            canvasPane = ((serverListController)(outOfMenuLoader.getController())).canvasAnchorPane;

            FXMLLoader loadingloader = new FXMLLoader();
            loadingloader.setLocation(showSetupGui.class.getResource("loading.fxml"));
            //Yo dawg...
            rootLayout = (Pane) loadingloader.load();

            connectLoader = new FXMLLoader();
            connectLoader.setLocation(showSetupGui.class.getResource("connect.fxml"));
            connectLayout = connectLoader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage = primaryStage;
            stage.setScene(scene);
            stage.show();

            //The gui's are loaded, safe to load the client
            // (if this was before the gui loading code, there would be a null pointer exception)
            client =  new GameClient(host, Integer.valueOf(port));
            client.start();
            //

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void showOutOfGameMenu() {
//
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Scene scene = new Scene(outOfMenuLayout);
//                        stage.setScene(scene);
//                        stage.show();
//                    } catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//    }

    //Replace the current gui with another gui
    public static void showLayout(Pane layout) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Scene scene = layout.getScene();
                    if (scene == null) {
                        scene = new Scene(layout);
                    }
                    stage.setScene(scene);
                    stage.show();
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    //This just means that the gui is already visible, don't need to worry
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Show another gui, over top of the main gui
    public static void showAnotherLayout(Pane layout) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Stage stage2 = new Stage();
                    Scene scene2 = new Scene(layout);
                    stage2.setScene(scene2);
                    stage2.show();
                }
                catch (Exception e) {
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
                        textArea.appendText(message + "\n");
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

    public static void removeChatTab(String name) {
        chatTabController chattab = chatTabHashMap.get(name);
        if (chattab != null) {
            serverListController c = outOfMenuLoader.getController();
            Tab t = c.tabNameHashMap.get(name);
            c.removeTab(t);
        }
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

    public static void updateCanvas() {
        canvas.setHeight(canvasPane.getHeight());
        canvas.setWidth(canvasPane.getWidth());
    }

}
