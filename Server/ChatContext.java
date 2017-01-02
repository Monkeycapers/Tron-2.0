package Server;

import Jesty.TCPBridge.ClientWorker;

/**
 * Created by Evan on 12/19/2016.
 */
public abstract class ChatContext {

    String name;

    public abstract void sendMessage (GameServer gameServer, String message);

    public abstract boolean removeUser(User user);

    //public abstract void onClose (GameServer gameServer)

}
