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
 *
 * authenticates with the server
 *
 * MUST be currently unauthenticated
 *
 * Arguments: String User, String Pass OR byte[] SecureToken
 * Returns Boolean success
 *
 * If TRUE, byte[] securetoken
 * If FALSE, int reason
 * Reason 0 --> Wrong pass or username (or invalid token)
 * Reason 1 --> Cannot signin (server error)
 * Reason 2 --> Banned --> String banreason
 * Reason 3 --> User is already signed in
 * Minimum rank: Guest
 *
 */
public class SignInCommand extends Command {

    public SignInCommand() {
        this.name = "signin";
        this.doreturn = true;
        this.minrank = Rank.Guest;
    }

    public String docommand(ClientWorker clientWorker, GameServer gameServer, JSONObject input, User user) {
        StringWriter stringWriter = new StringWriter();
        if (gameServer.getUserByName(input.getString("username")) != null) {
            new JSONWriter(stringWriter).object()
            .key("argument").value("returnsignin")
                    .key("success").value(false)
                    .key("reason").value(3).endObject();
        }
        else {
            authenticationstatus status = user.authenticate(input.getString("username"), input.getString("password"));
            if (status == authenticationstatus.Success) {
                new JSONWriter(stringWriter).object()
                        .key("argument").value("returnsignin")
                        .key("success").value(true).endObject();
            }
            else if (status == authenticationstatus.Banned) {
                new JSONWriter(stringWriter).object()
                        .key("argument").value("returnsignin")
                        .key("success").value(false)
                        .key("reason").value(2)
                        .key("banreason").value(user.getBanReason())
                        .endObject();
            }
            else {
                int reason = 0;
                new JSONWriter(stringWriter).object()
                        .key("argument").value("returnsignin")
                        .key("success").value(false)
                        .key("reason").value(reason)
                        .endObject();
            }
        }
        return stringWriter.toString();
    }
}
