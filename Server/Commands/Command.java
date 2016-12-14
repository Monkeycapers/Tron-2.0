package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Jesty.TCPBridge.Clients;
import Server.Rank;
import Server.User;
import org.json.JSONObject;

/**
 * Created by Evan on 12/13/2016.
 */
public abstract class Command {

    public String name;

    boolean doreturn;

    Rank minrank;

    public abstract String docommand(ClientWorker clientWorker, Clients clients, JSONObject input, User user);

}
