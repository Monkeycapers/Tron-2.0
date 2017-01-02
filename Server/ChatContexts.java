package Server;

import java.util.ArrayList;

/**
 * Created by S199753733 on 12/21/2016.
 */
public class ChatContexts {

    private ArrayList<ChatContext> chatContexts;

    public ChatContexts() {
        chatContexts = new ArrayList<>();
    }

    public void addNewContext(ChatContext chatContext) {
        chatContexts.add(chatContext);
    }

    public void removeContext(ChatContext chatContext) {
        chatContexts.remove(chatContext);
    }

    public ChatContext getContext (String name) {
        for (ChatContext chatContext: chatContexts) {
//            System.out.println("The name: " + name);
//            System.out.println("Is the chatContext null?" + (chatContext == null));
//            System.out.println("The chatContext name: " + chatContext.name);
            if (chatContext.name.equals(name)) {
                return chatContext;
            }
        }
    return null;
    }

    public void doChatMessage(GameServer gameServer, User user, String name, String message) {
        ChatContext chatContext = getContext(name);
        if (chatContext == null) return;
        chatContext.sendMessage(gameServer, user.chatFormatDisplay() + " " + message);
    }

    public void removeUser(User user) {
        for (ChatContext chatContext: chatContexts) {
            if (chatContext.removeUser(user)) {
                //Dissolve the chat
                removeContext(chatContext);
                break;
            }
        }
    }
}
