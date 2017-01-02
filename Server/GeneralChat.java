package Server;

import org.json.JSONWriter;

import java.io.StringWriter;

/**
 * Created by S199753733 on 12/21/2016.
 */
public class GeneralChat extends ChatContext {

    public GeneralChat () {
        this.name = "general";
    }

    @Override
    public void sendMessage(GameServer gameServer, String message) {
        //Send the message to all users
        StringWriter stringWriter = new StringWriter();
        new JSONWriter(stringWriter).object()
                .key("argument").value("chatmessage")
                .key("name").value(name)
                .key("message").value(message)
                .key("displayname").value("General")
                .endObject();
        gameServer.sendToAll(Rank.User, stringWriter.toString());
    }

    //todo: switch all of this to one method
    public void userJoinedMessage(GameServer gameServer, User user) {
        sendMessage(gameServer, "* [" + user.getRank() + "] " +  user.getName() + " has joined the server! Server population: " + (gameServer.getUserList().size()) + " *");
    }

    public void userLeftMessage(GameServer gameServer, User user) {

        sendMessage(gameServer, "* [" + user.getRank() + "] " +  user.getName() + " has left the server! Server population: " + (gameServer.getUserList().size()) + " *");
    }

    @Override
    public boolean removeUser(User user) {
        return false;
    }

    //
}
