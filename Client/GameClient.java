package Client;

/**
 * Created by Evan on 11/3/2016.
 * Extends TCPBridge client class, makes a connection with the Server and handles/sends messages. Functionally similar
 * to the webclient.
 */
public class GameClient extends Jesty.TCPBridge.Client {

    public GameClient(String hostName, int portNumber) {
        super(hostName, portNumber);
    }

    public GameClient() {
        super();
    }
//
    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onOpen() {

    }
    @Override
    public void onClose() {

    }

    public void close() {
        System.exit(0);
    }

}
