package Client;

import org.json.JSONObject;

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
        JSONObject jsonObject = new JSONObject(message);
        System.out.println(jsonObject.toString());
        //String argument = jsonObject.getString("argument");

        //if (argument.equals("")) {
            //...//
        //}

    }

    @Override
    public void onOpen() {

    }
    @Override
    public void onClose() {

    }

    @Override
    public void onHighPing(long latency) {
        System.out.println("Warning: High Ping (" + latency + ")");
    }

    public void close() {
        System.exit(0);
    }

}
