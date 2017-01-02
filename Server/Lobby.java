package Server;

import org.json.JSONObject;

/**
 * Created by Evan on 12/6/2016.
 */
public abstract class Lobby implements Runnable {

    boolean isPrivate;

    String name;

    ChatContext chatContext;

    boolean isRunning;

    Thread thread;

    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public abstract void onConnect(User user);

    public abstract boolean onClose(User user);

    public abstract void onMessage(User user, JSONObject input);

    //Pause whatever is going on
    public abstract void pause();

    //Check if the User is eligible to connect
    public abstract boolean canConnect(User user);

}
