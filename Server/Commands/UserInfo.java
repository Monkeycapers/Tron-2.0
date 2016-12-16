package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Server.GameServer;
import Server.Rank;
import Server.User;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.StringWriter;

/**
 * Created by Evan on 12/14/2016.
 */
public class UserInfo extends Command {

    public UserInfo () {
        this.name = "userinfo";
        this.doreturn = true;
        this.minrank = Rank.User;
    }

    @Override
    public String docommand(ClientWorker clientWorker, GameServer gameServer, JSONObject input, User user) {
        StringWriter stringWriter = new StringWriter();
        if (input.getBoolean("type")) {
            //Type 1: self info
            new JSONWriter(stringWriter).object()
                    .key("name").value(user.getName())
                    .key("email").value(user.getEmail())
                    .key("rank").value(user.getRank())
                    .key("currentlobby").value(user.getCurrentLobby())
                    .endObject();
        }
        else {
            //Type 2: get Info of another user
        }
        return stringWriter.toString();
    }



}
