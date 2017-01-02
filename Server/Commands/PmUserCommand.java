package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Server.GameServer;
import Server.PmChat;
import Server.Rank;
import Server.User;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.StringWriter;

/**
 * Created by Evan on 12/22/2016.
 *
 * Creates a private chat context with a user
 */
public class PmUserCommand extends Command {

    public PmUserCommand () {
        this.name = "pmuser";
        this.minrank = Rank.User;
        this.doreturn = false;
    }

    @Override
    public String docommand(ClientWorker clientWorker, GameServer gameServer, JSONObject input, User user) {
        User targetUser = gameServer.getUserByName(input.getString("name"));
        if (targetUser != null) {
            PmChat context = new PmChat(user, targetUser);
            context.sendMessage(gameServer, targetUser.chatFormatDisplay() + " is online.",  true);
            gameServer.chatContexts.addNewContext(context);
        }
        return "";
    }
}
