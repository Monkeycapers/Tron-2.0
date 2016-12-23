package Server;

import org.json.JSONWriter;

import java.io.StringWriter;

/**
 * Created by Evan on 12/22/2016.
 */
public class PmChat extends ChatContext {

    User u1, u2;

    public PmChat(User u1, User u2) {
        this.name = "pm " + u1.getName() + ", " + u2.getName();
        this.u1 = u1;
        this.u2 = u2;
    }

    @Override
    public void sendMessage(GameServer gameServer, String message, User user) {
        sendMessage(gameServer, message, user, false);
    }

    public void sendMessage(GameServer gameServer, String message, User user, boolean starter) {
        StringWriter stringWriter = new StringWriter();
        String displayname = "Error";
        if (user == u1) {
            displayname = "Private message with " + u1.getName();
        }
        else if (user == u2) {
            displayname = "Private message with " + u2.getName();
        }
        new JSONWriter(stringWriter).object()
                .key("argument").value("chatmessage")
                .key("name").value(name)
                .key("displayname").value(displayname)
                .key("message").value(user.chatFormatDisplay() + " " + message).endObject();

        u1.clientWorker.sendMessage(stringWriter.toString());
        if (!starter) u2.clientWorker.sendMessage(stringWriter.toString());
    }

}
