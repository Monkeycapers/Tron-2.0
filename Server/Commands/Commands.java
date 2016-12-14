package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Jesty.TCPBridge.Clients;
import Server.Authenticate;
import Server.User;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Evan on 12/13/2016.
 */
public class Commands {

    ArrayList<Command> commands;

    Clients clients;

    public Commands() {
        //Init the commands ArrayList
        commands = new ArrayList<>();
        commands.add(new SignInCommand());
        commands.add(new SignUpCommand());
    }

    public String orchastrateCommand(ClientWorker clientWorker, JSONObject jsonObject) {
        StringWriter stringWriter = new StringWriter();
        User user =(User)clientWorker.clientData;
        Command command = getCommand(jsonObject.getString("argument"));
        if (command != null) {
            if (!Authenticate.checkRank(user.getRank(), command.minrank)) {
                new JSONWriter(stringWriter).object()
                        .key("argument").value("errornotprivilidged").endObject();
                return stringWriter.toString();
            }
            if (command.doreturn) {
                return command.docommand(clientWorker, clients, jsonObject, user);
            }
            else {
                return "noreturnsuccsess";
            }
        }

        new JSONWriter(stringWriter).object()
                .key("argument").value("errornocommand").endObject();
        return stringWriter.toString();
    }

    public Command getCommand(String argument) {
        for (Command c: commands) {
            if (c.name.equals(argument)) {
                return c;
            }
        }
        return null;
    }


}
