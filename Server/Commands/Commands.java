package Server.Commands;

import Jesty.TCPBridge.ClientWorker;
import Jesty.TCPBridge.Clients;
import Server.Authenticate;
import Server.GameServer;
import Server.User;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Evan on 12/13/2016.
 *
 * A class that contains all commands, and orchestrates commands, that is given
 * a command name, find a command to execute, and return it's results if necessary
 */
public class Commands {

    ArrayList<Command> commands;

    GameServer gameServer;

    public Commands(GameServer gameServer) {
        //Init the commands ArrayList
        this.gameServer = gameServer;
        commands = new ArrayList<>();
        commands.add(new SignInCommand());
        commands.add(new SignUpCommand());
        commands.add(new StopCommand());
        commands.add(new KickCommand());
        commands.add(new UserInfo());
        commands.add(new BanCommand());
        commands.add(new PromoteCommand());
        commands.add(new UserExistsCommand());
        commands.add(new ChatMessageCommand());
        commands.add(new PmUserCommand());
        commands.add(new JoinLobbyCommand());
        commands.add(new LobbyMessageCommand());
        commands.add(new CreateLobbyCommand());
        commands.add(new LobbyListCommand());
        commands.add(new LeaveLobbyCommand());
        commands.add(new SignOutCommand());
    }

    public String orchestrateCommand(ClientWorker clientWorker, JSONObject jsonObject) {
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
                return command.docommand(clientWorker, gameServer, jsonObject, user);
            }
            else {
                command.docommand(clientWorker, gameServer, jsonObject, user);
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
