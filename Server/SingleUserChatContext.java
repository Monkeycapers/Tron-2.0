package Server;

import org.json.JSONWriter;

import java.io.StringWriter;

/**
 * Created by Evan on 1/3/2017.
 */
public class SingleUserChatContext extends ChatContext {

    User user;

    public SingleUserChatContext (User user, String name, String displayName) {
        this.user = user;
        this.name = name;
        this.displayName = displayName;
    }

    @Override
    public void sendMessage(GameServer gameServer, String message) {
        StringWriter stringWriter = new StringWriter();
        new JSONWriter(stringWriter).object()
                .key("argument").value("chatmessage")
                .key("name").value(name)
                .key("displayname").value(displayName)
                .key("message").value(message)
                .endObject();
        user.clientWorker.sendMessage(stringWriter.toString());
    }

    @Override
    public boolean removeUser(User user) {
        return this.user == user;
    }

}
