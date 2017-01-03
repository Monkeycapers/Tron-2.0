package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Server.*;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.StringWriter;

/**
 * Created by S199753733 on 12/15/2016.
 */
public class BanCommand extends Command {

    public BanCommand() {
        this.name = "ban";
        this.doreturn = true;
        this.minrank = Rank.Op;
    }

    @Override
    public String docommand(ClientWorker clientWorker, GameServer gameServer, JSONObject input, User user) {
        input.put("rank", "Banned");
        User u = gameServer.getUserByName(input.getString("user"));
        if (u != null) {
            u.setBanReason(input.getString("reason"));
        }
        JSONObject jout = new JSONObject(gameServer.commands.getCommand("promote").docommand(clientWorker, gameServer, input, user)) ;
//        if(jout.getBoolean("result")) {
//            //Kick the user if they are still on the server
//            String username = input.getString("user");
//            User u = gameServer.getUserByName(username);
//            if (u != null)  gameServer.kick(u, input.getString("reason"));
//            //
//        }
        jout.remove("argument");
        jout.put("argument", "returnban");
        return jout.toString();
       // StringWriter result = new StringWriter();

//        String username = input.getString("user");
//        int reason = 0;
//        if (!username.equals("")) {
//            User u = gameServer.getUserByName(username);
//            if (u != null) {
//                //Check if the user's rank is greater than the target
//                int comparedrank = Authenticate.compareRanks(user.getRank(), u.getRank());
//                if (comparedrank > 0) {
//                    //System.out.println("bup");
//                    gameServer.ban(u, input.getString("reason"));
//                    new JSONWriter(result).object()
//                            .key("argument").value("returnKick")
//                            .key("result").value(true).endObject();
//                    return result.toString();
//                }
//                else {
//                    reason = 1;
//                }
//            }
//            else {
//                //Try to update the offline data
//                OfflineUser offlineUser = new OfflineUser();
//                offlineUser.unSecureSignIn(username);
//                int comparedrank = Authenticate.compareRanks(user.getRank(), offlineUser.rank);
//                if (comparedrank > 0) {
//                    offlineUser.rank = Rank.Banned;
//                    offlineUser.banreason = input.getString("reason");
//                    if (authenticationstatus.Success == offlineUser.unSecureUpdateRank(Rank.Banned)) {
//                        new JSONWriter(result).object()
//                                .key("argument").value("returnKick")
//                                .key("result").value(true).endObject();
//                        return result.toString();
//                    }
//                    reason = 2;
//                }
//                reason = 3;
//            }
//        }
//        else reason = 4;
//        new JSONWriter(result).object()
//                .key("argument").value("returnKick")
//                .key("result").value(false).endObject();
//        return result.toString();
    }
}
