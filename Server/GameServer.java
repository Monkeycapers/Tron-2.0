package Server;


import Jesty.Settings;
import Jesty.TCPBridge.ClientWorker;
import Jesty.TCPBridge.Server;
import Server.Commands.Commands;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Evan on 10/21/2016.
 *
 * Extends the TCPBridge Server class, handles messages from clients and sets up lobbys.
 * Also handles new connections and dropped connections
 */
public class GameServer extends Server {

    //HashMap<ClientWorker, User> users;

    ArrayList<User> users;

    public ChatContexts chatContexts;

    public Commands commands;

    public Lobbys lobbys;

    public GameServer(int raw_port, int web_port) {
        super(raw_port, web_port);
        users = new ArrayList<User>();
        HashMap<String, String> defaults = new HashMap<>();
        //...//
        //set up defaults
        Settings.setFile(new File("settings.txt"), defaults);
        Authenticate.setFile(new File("users.json"));
        Settings.load();
        commands = new Commands(this);
        chatContexts = new ChatContexts();
        chatContexts.addNewContext(new GeneralChat());
        lobbys = new Lobbys();

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
        catch (JSONException e) {
            e.printStackTrace();
           // System.out.println("invalid format: " + e.getMessage());
            //Todo: send this back to the client
        }
        catch (Exception e) {
            //Protect the server, and kill the connection
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
        chatContexts.removeUser(user);
        Lobby lobby = user.getCurrentLobby();
        if (lobby != null) {
            if (lobby.onClose(user)) {
                try {
                    lobby.isRunning = false;
                    lobby.thread.join();
                    lobbys.removeLobby(lobby);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (Authenticate.checkRank(user.getRank(), Rank.User)) {
            //System.out.println("sending leave message");
            ((GeneralChat)(chatContexts.getContext("general"))).userLeftMessage(this, user);
        }
        //Tell the clients to add the user to their user list
        StringWriter writer2 = new StringWriter();
        new JSONWriter(writer2).object()
                .key("argument").value("updateusers")
                .key("name").value("general")
                .key("type").value("remove")
                .key("user").value(user.chatFormatDisplay())
                .endObject();
        //Don't need to use send to peers since the user has already disconnected
        sendToAll(Rank.User, writer2.toString());
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
            if (u.getName().equals(name) || u.chatFormatDisplay().equals(name)) {
                return u;
            }
        }
        return null;
    }

    public void kick (User user, String reason) {
        ClientWorker targetClientWorker = user.clientWorker;
        users.remove(user);
        user = new User(targetClientWorker);
        targetClientWorker.clientData = user;

        StringWriter stringWriter = new StringWriter();
        new JSONWriter(stringWriter).object()
                .key("argument").value("kicked")
                .key("reason").value(reason)
                .endObject();
        targetClientWorker.sendMessage(stringWriter.toString());
    }

    public void ban(User user, String reason) {
        kick(user, reason);
        //Todo: handle IO errors
        user.setBanReason(reason);
        user.updateRank(Rank.Banned);
    }
    //Todo: I have users for a reason....
    public void sendToAll (String message){
        for (ClientWorker w: clients.getList()) {
            w.sendMessage( message);
        }
    }

    public void sendToAll (Rank minRank, String message){
        for (User u: users) {
            if (Authenticate.checkRank(u.getRank(), minRank)) {
                u.clientWorker.sendMessage(message);
            }
        }
    }
    //Send to all except the user
    public void sendToPeers (Rank minRank, User user, String message) {
        for (User u: users) {
            if ((u != user) && Authenticate.checkRank(u.getRank(), minRank)) {
                u.clientWorker.sendMessage(message);
            }
        }
    }
    //
    public List<String> getUserList () {
        List<String> userlist = new ArrayList<>();
        for (User user: users) {
            if (Authenticate.checkRank(user.getRank(), Rank.User))
            userlist.add("[" + user.getRank() + "] " + user.getName());
        }
        return userlist;
    }
}
