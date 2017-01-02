package Server;

import Jesty.TCPBridge.ClientWorker;

/**
 * Created by Evan on 12/19/2016.
 */
public abstract class ChatContext {

    public String name;

    public String displayName;

    public abstract void sendMessage (GameServer gameServer, String message);

    public abstract boolean removeUser(User user);

    //public abstract void onClose (GameServer gameServer)

}
