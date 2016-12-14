package Server;


import Jesty.Settings;
import Jesty.TCPBridge.ClientWorker;
import Jesty.TCPBridge.Server;
import Server.Commands.Commands;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Evan on 10/21/2016.
 *
 * Extends the TCPBridge Server class, handles messages from clients and sets up lobbys.
 * Also handles new connections and dropped connections
 */
public class GameServer extends Server {

    //HashMap<ClientWorker, User> users;

    ArrayList<User> users;

    Commands commands;

    public GameServer(int raw_port, int web_port) {
        super(raw_port, web_port);
        users = new ArrayList<User>();
        HashMap<String, String> defaults = new HashMap<>();
        //...//
        //set up defaults
        Settings.setFile(new File("settings.txt"), defaults);
        Authenticate.setFile(new File("users.json"));
        Settings.load();
        commands = new Commands();
    }

    @Override
    public void onMessage(ClientWorker clientWorker, String message) {
        try {
            User user = (User)(clientWorker.clientData);
            JSONObject jsonObject = new JSONObject(message);
            String argument = jsonObject.getString("argument");
            StringWriter stringWriter = new StringWriter();
            //get a result from commands
            String result = commands.orchestrateCommand(clientWorker, jsonObject);
            //if the result is not empty, or the command executed successfully and has things to return,
            //send the result back to the client
            if (!result.equals("noreturnsuccsess") && !result.equals("")) clientWorker.sendMessage(result);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Terminating client connection for client: " + clientWorker);
            clientWorker.forcedisconnect();
        }

//        if (argument.equals("...")) {
//            //...//
//            //To send back to client:
//            JSONWriter jsonWriter = new JSONWriter(stringWriter)
//                    .object()
//                    .key("argument")
//                    .value("...")
//                    .endObject();
//            //
//        }
        //        if (!stringWriter.toString().equals("")) {
//            clientWorker.sendMessage(stringWriter.toString());
//        }
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
