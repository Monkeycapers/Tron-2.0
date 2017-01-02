package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Server.*;
import org.json.JSONObject;

/**
 * Created by Evan on 12/28/2016.
 */
public class CreateLobbyCommand extends Command {

    public CreateLobbyCommand () {
        this.name = "createlobby";
        this.minrank = Rank.User;
        this.doreturn = false;
    }

    @Override
    public String docommand(ClientWorker clientWorker, GameServer gameServer, JSONObject input, User user) {
        String name = user.getName() + "," + input.getString("name");
        String type = input.getString("type");
        ListChatContext chatContext = new ListChatContext(user, name, input.getString("name"));
        gameServer.chatContexts.addNewContext(chatContext);
        if (type.equals("tron")) {
            TronLobby tronLobby = new TronLobby(user, name, input.getInt("maxplayers"), chatContext);
            gameServer.lobbys.addNewLobby(tronLobby);
            user.setCurrentLobby(tronLobby);
            tronLobby.start();
        }
        return "";
    }
}
