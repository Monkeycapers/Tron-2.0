package Server;


import Jesty.Settings;
import Jesty.TCPBridge.ClientWorker;
import Jesty.TCPBridge.Server;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Evan on 10/21/2016.
 *
 * Extends the TCPBridge Server class, handles messages from clients and sets up lobbys.
 * Also handles new connections and dropped connections
 */
public class GameServer extends Server {

    //HashMap<ClientWorker, User> users;

    ArrayList<User> users;

    public GameServer(int raw_port, int web_port) {
        super(raw_port, web_port);
        users = new ArrayList<User>();
        HashMap<String, String> defaults = new HashMap<>();
        //...//
        //set up defaults
        Settings.setFile(new File("settings.txt"), defaults);
        Authenticate.setFile(new File("users.json"));
        Settings.load();
    }

    @Override
    public void onMessage(ClientWorker clientWorker, String message) {
        User user = (User)(clientWorker.clientData);
        JSONObject jsonObject = new JSONObject(message);
        String argument = jsonObject.getString("argument");

        if (argument.equals("...")) {
            //...//
            //To send back to client:
            StringWriter stringWriter = new StringWriter();
            JSONWriter jsonWriter = new JSONWriter(stringWriter)
                    .object()
                    .key("argument")
                    .value("...")
                    .endObject();
            clientWorker.sendMessage(stringWriter.toString());
            //
        }

        if (argument.equals("signin")) {
            System.out.println("bup");
            user.authenticate(jsonObject.getString("username"), jsonObject.getString("password"));
        }

    }

    @Override
    public void onClose(ClientWorker clientWorker, int code) {
        User user = (User)(clientWorker.clientData);
        users.remove(user);
        //user.getCurrentLobby().removeUser(user);
    }

    @Override
    public void onOpen(ClientWorker clientWorker, int code) {
        User user = new User(clientWorker);
        users.add(user);
        clientWorker.clientData = user;
    }

    public User getUserByName(String name) {
        for (User u: users) {
            if (u.getName().equals(name)) {
                return u;
            }
        }
        return null;
    }


}
