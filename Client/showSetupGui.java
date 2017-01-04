package Client;

/**
 * Created by Evan on 12/15/2016.
 */
import java.io.File;
import java.io.StringWriter;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import Jesty.*;
import Jesty.test.testServer;
import Server.GameServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

public class showSetupGui extends Application {

    public static Pane rootLayout;

    public static Pane outOfMenuLayout;

    public static Pane loginLayout;

    public static Pane connectLayout;

    public static Pane lobbyListLayout;

    //public static Pane loadingLayout;

    public static Stage stage;

    public static FXMLLoader loginloader;

    public static FXMLLoader outOfMenuLoader;

    public static FXMLLoader connectLoader;

    public static FXMLLoader lobbyListLoader;

    public static GameClient client;

    public static testServer testServer;

    public static Canvas canvas;

    public static AnchorPane canvasPane;

    public static HashMap<String , chatTabController> chatTabHashMap;

    public static HashMap<String, String> lobbyListHashMap;

    public static List<Object> render;

    public static boolean isHighRank = false;

    public static String userDisplayName = "[Guest] guest";

    public static int mapWidth = 0;

    public static int mapHeight = 0;

    public static void main(String[] args) {
        Application.launch(showSetupGui.class, (java.lang.String[])null);
    }

    public static void showSetupGui() {
        Application.launch(showSetupGui.class, (java.lang.String[])null);
    }

    public void showGui() {

        Application.launch(showSetupGui.class, (java.lang.String[])null);

    }
    //Needed to check if the port is a number
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
            render = new ArrayList<>();
            chatTabHashMap = new HashMap<>();

            lobbyListHashMap = new HashMap<>();


            HashMap<String, String> defaults = new HashMap<>();
            defaults.put("host", "localhost");
            defaults.put("port", "16000");
            defaults.put("wallwidth", "10");
            defaults.put("wallheight", "10");

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

            FXMLLoader loadingloader = new FXMLLoader();
            loadingloader.setLocation(showSetupGui.class.getResource("loading.fxml"));
            //Yo dawg...
            rootLayout = (Pane) loadingloader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);

            stage = primaryStage;

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(1);
                }
            });

            stage.setScene(scene);
            stage.show();


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

            connectLoader = new FXMLLoader();
            connectLoader.setLocation(showSetupGui.class.getResource("connect.fxml"));
            connectLayout = connectLoader.load();

            lobbyListLoader = new FXMLLoader();
            lobbyListLoader.setLocation(showSetupGui.class.getResource("lobbylist.fxml"));
            lobbyListLayout = lobbyListLoader.load();

            //The gui's are loaded, safe to load the client
            // (if this was done before the gui loading code, there would be a null pointer exception)
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
                    //e.printStackTrace();
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
                    Scene scene = layout.getScene();
                    if (scene == null) {
                        scene = new Scene(layout);
                    }
                    Stage stage2 = new Stage();
                    stage2.setScene(scene);
                    stage2.show();
                }
                catch (IllegalArgumentException e) {
                    //e.printStackTrace();
                    //This just means that the gui is already visible, don't need to worry
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
        //Check for internal Client commands
        String toSend = "";

        if (text.startsWith(".help")) {
            toSend = "Commands:\n.changeproperty [propertyname] [newvalue] --> Updates a Settings property" +
                    "\n.listpropertys --> Lists the propertys and their values" +
                    "\n.loadpropertys --> Loads the settings from the txt file";
        }
        else if (text.startsWith(".changeproperty")) {
            String[] split = text.split(" ");
            Settings.setProperty(split[1], split[2]);
            toSend = "Changed property: " + split[1] + " to " + split[2] + ".\n" + Settings.listPropertys();
        }
        else if (text.startsWith(".listpropertys")) {
            toSend = Settings.listPropertys();
        }
        else if (text.startsWith(".loadpropertys")) {
            Settings.load();
            toSend = "Updated propertys.\n" + Settings.listPropertys();
        }
        //Does not match internal commands, send message to server
        else {
            StringWriter stringWriter = new StringWriter();
            new JSONWriter(stringWriter).object()
                    .key("argument").value("chatmessage")
                    .key("name").value(name)
                    .key("message").value(text)
                    .endObject();
            client.sendMessage(stringWriter.toString());
        }
        if (!toSend.isEmpty()) {
            //Loop back
            pushMessage("Client-->local", "Client", toSend);
        }
    }

//    public static String listPropertys() {
//        String toSend = "Propertys: " + "\n";
//        for (Map.Entry<String, String> entry:Settings.getPropertys().entrySet()) {
//            toSend += entry.getKey() + ":" + entry.getValue() + "\n";
//        }
//        return toSend;
//    }

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

    public static void setLobbyList(JSONArray lobbys) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lobbyListHashMap = new HashMap<String, String>();
                lobbyListController controller = lobbyListLoader.getController();
                //Remove all of the lobbys items
                controller.listView.getItems().removeIf(new Predicate() {
                    @Override
                    public boolean test(Object o) {
                        return true;
                    }
                });
                for (Object object: lobbys) {
                    System.out.println(object.toString());
                    JSONObject jsonObject = new JSONObject((String.valueOf(object)));
                    String name = jsonObject.getString("name");
                    String displayname = jsonObject.getString("displayname");
                    boolean isPrivate = jsonObject.getBoolean("isprivate");
                    String players = jsonObject.getString("players");
                    String gamemode = jsonObject.getString("gamemode");

                    lobbyListHashMap.put(displayname + " " + players + " " + gamemode, name);
                    controller.listView.getItems().add(displayname + " " + players + " " + gamemode);
                }
                controller.listView.getItems().sort(new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return String.valueOf(o1).compareTo(String.valueOf(o2));
                    }
                });

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
                serverList.sort();
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
                serverList.sort();
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
                serverList.sort();
            }
        });
    }

    public static void updateCanvas() {
        canvas.setHeight(canvasPane.getHeight());
        canvas.setWidth(canvasPane.getWidth());
    }

    public static void requestLobbyList() {
        StringWriter stringWriter = new StringWriter();
        new JSONWriter(stringWriter).object()
                .key("argument").value("lobbylist")
                .endObject();
        showSetupGui.client.sendMessage(stringWriter.toString());
    }
    //Wipe gui elements (This is used for ex if you are kicked and rejoin)
    public static void clearAllGuiElements() {
        chatTabHashMap = new HashMap<>();
        lobbyListHashMap = new HashMap<>();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                serverListController serverList = (serverListController) outOfMenuLoader.getController();
                serverList.userListView.getItems().clear();
                serverList.tabControllerHashMap = new HashMap<Tab, chatTabController>();
                serverList.chatTabPane.getTabs().clear();
            }
        });
    }
    //Synchronized means that other threads must wait for this to be done
    //(similar to how code waits for a JOptionPane to be done)
    //Ensures that Canvas is only being changed once
    public static synchronized void render(List<Object> render, int mapWidth, int mapHeight) {

        showSetupGui.updateCanvas();

        showSetupGui.render = render;
        showSetupGui.mapWidth = mapWidth;
        showSetupGui.mapHeight = mapHeight;


    }
}
