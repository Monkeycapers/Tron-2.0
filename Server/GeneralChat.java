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
    public void sendMessage(GameServer gameServer, String message, User user) {
        //Send the message to all users
        StringWriter stringWriter = new StringWriter();
        new JSONWriter(stringWriter).object()
                .key("argument").value("chatmessage")
                .key("name").value(name)
                .key("message").value(genMessage(message, user))
                .key("displayname").value("General")
                .endObject();
        sendMessage(gameServer, stringWriter.toString());
    }
    //Format the message
    public String genMessage(String message, User user) {
        return "<" + user.getRank().toString() + ">" + " " + user.getName() + " " + message;
    }

    public void sendMessage(GameServer gameServer, String message) {
        gameServer.sendToAll(Rank.User, message);
    }

    //todo: switch all of this to one method
    public void userJoinedMessage(GameServer gameServer, User user) {
        StringWriter stringWriter = new StringWriter();
        new JSONWriter(stringWriter).object()
                .key("argument").value("chatmessage")
                .key("name").value(name)
                .key("color").value("125,125,125")
                .key("message").value("* [" + user.getRank() + "] " +  user.getName() + " has joined the server! Server population: " + (gameServer.getUserList().size()) + " *")
                .key("displayname").value("General")
                .endObject();
        sendMessage(gameServer, stringWriter.toString());
    }

    public void userLeftMessage(GameServer gameServer, User user) {
        StringWriter stringWriter = new StringWriter();
        new JSONWriter(stringWriter).object()
                .key("argument").value("chatmessage")
                .key("name").value(name)
                .key("color").value("125,125,125")
                .key("message").value("* [" + user.getRank() + "] " +  user.getName() + " has left the server! Server population: " + (gameServer.getUserList().size()) + " *")
                .key("displayname").value("General")
                .endObject();
        sendMessage(gameServer, stringWriter.toString());
    }
    //
}
