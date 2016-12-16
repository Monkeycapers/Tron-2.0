package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Jesty.TCPBridge.Clients;
import Server.GameServer;
import Server.Rank;
import Server.User;
import Server.authenticationstatus;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.StringWriter;

/**
 * Created by Evan on 12/13/2016.
 */
public class SignUpCommand extends Command {
    public SignUpCommand() {
        this.name = "signup";
        this.doreturn = true;
        this.minrank = Rank.Guest;
    }

    public String docommand(ClientWorker clientWorker, GameServer gameServer, JSONObject input, User user) {
        StringWriter stringWriter = new StringWriter();
        authenticationstatus status = user.signup(input.getString("username"), input.getString("password"), input.getString("email"));
        if (status == authenticationstatus.Success) {
            new JSONWriter(stringWriter).object()
                    .key("argument").value("returnsignup")
                    .key("success").value(true).endObject();
        }
        else {
            int reason = 0;
            new JSONWriter(stringWriter).object()
                    .key("argument").value("returnsignup")
                    .key("success").value(false)
                    .key("reason").value(reason)
                    .endObject();
        }
        return stringWriter.toString();
    }
}
