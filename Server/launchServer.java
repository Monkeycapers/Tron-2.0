package Server;

/**
 * Created by Evan on 1/2/2017.
 */
public class launchServer {

    public static void main (String[] args) {
        GameServer gameServer = new GameServer(16000, false);
        gameServer.start();
    }

}
