package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Server.Authenticate;
import Server.GameServer;
import Server.Rank;
import Server.User;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.StringWriter;

/**
 * Created by Evan on 12/15/2016.
 */
public class PromoteCommand extends Command {

    public PromoteCommand () {
        this.name = "promote";
        this.doreturn = true;
        this.minrank = Rank.Op;
    }

    @Override
    public String docommand(ClientWorker clientWorker, GameServer gameServer, JSONObject input, User user) {
        StringWriter result = new StringWriter();
        int reason;
        //Step 1: Check if the User is trying to promote to a user higher or equal to their current rank
        Rank rankToPromoteTo = Rank.valueOf(input.getString("rank"));
        if ((Authenticate.compareRanks(user.getRank(), rankToPromoteTo)) != -1) {
            new JSONWriter(result).object()
                    .key("result").value(false)
                    .key("reason").value(0).endObject();
            return result.toString();
        }
        //Step 2:

        return null;
    }
}
