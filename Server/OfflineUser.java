package Server;

import Jesty.TCPBridge.ClientWorker;

import java.util.HashMap;

/**
 * Created by Evan on 12/15/2016.
 */
public class OfflineUser {

    public String name;

    public String email;

    public Rank rank;

    public String banreason;

    public void unSecureSignIn(String name) {
        HashMap<String, Object> result = Authenticate.authenticate(name, "", true);
        if ((boolean)result.get("result")) {
            this.name = (String)result.get("name");
            this.email = (String)result.get("email");
            this.rank = Rank.valueOf((String)result.get("rank"));
            //return authenticationstatus.Success;
        }
        else {
            int reason = (int)result.get("reason");
            if (reason == 0) {
                //return authenticationstatus.NoUserOrPassword;
            }
            else if (reason == 2) {
                //banreason = (String)result.get("banreason");
                //return authenticationstatus.Banned;
            }
        }
        //return authenticationstatus.Failure;
    }

    public authenticationstatus unSecureUpdateRank (Rank rank, String name) {
        return Authenticate.update(this);
    }

}
