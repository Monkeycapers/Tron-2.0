package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Server.*;
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
        //int reason;
        //Step 1: Check if the User is trying to promote to a user higher or equal to their current rank
        Rank rankToPromoteTo = Rank.valueOf(input.getString("rank"));
        if ((Authenticate.compareRanks(user.getRank(), rankToPromoteTo)) == -1) {
            new JSONWriter(result).object()
                    .key("argument").value("returnPromote")
                    .key("result").value(false)
                    .key("reason").value(5).endObject();
            return result.toString();
        }
        //Step 2:Promote the User

        String username = input.getString("user");
        int reason = 0;
        if (!username.equals("")) {
            User u = gameServer.getUserByName(username);
            if (u != null) {

                    //gameServer.ban(u, input.getStrin;g("reason"))
                    //Todo: Allow for hot rank changing
                    u.updateRank(rankToPromoteTo);
                    gameServer.kick(u, "Your rank has been updated, please sign in again.");
                    new JSONWriter(result).object()
                            .key("argument").value("returnPromote")
                            .key("result").value(true).endObject();
                    return result.toString();

            }
            else {
                //Try to update the offline data
                OfflineUser offlineUser = new OfflineUser();
                offlineUser.unSecureSignIn(username);
                if (input.has("reason")) {
                    offlineUser.banreason = input.getString("reason");
                }
                else {
                    offlineUser.banreason = "Banned";
                }
                System.out.println(offlineUser.name + ", " + offlineUser.email);
                int comparedrank = Authenticate.compareRanks(user.getRank(), offlineUser.rank);
                if (comparedrank > 0) {
                    //offlineUser.rank = rankToPromoteTo;
                    //offlineUser.banreason = input.getString("reason");
                    if (offlineUser.unSecureUpdateRank(rankToPromoteTo) == authenticationstatus.Success) {
                        new JSONWriter(result).object()
                                .key("argument").value("returnPromote")
                                .key("result").value(true).endObject();
                        return result.toString();
                    }
                    reason = 2;
                }
                else {
                    reason = 3;
                }
            }
        }
        else reason = 4;
        new JSONWriter(result).object()
                .key("argument").value("returnPromote")
                .key("result").value(false)
                .key("reason").value(reason)
                .endObject();
        return result.toString();

    }
}
