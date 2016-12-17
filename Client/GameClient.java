package Client;

import Client.Commands.Commands;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Evan on 11/3/2016.
 * Extends TCPBridge client class, makes a connection with the Server and handles/sends messages. Functionally similar
 * to the webclient.
 */
public class GameClient extends Jesty.TCPBridge.Client {

    Commands commands;

    public GameClient(String hostName, int portNumber) {
        super(hostName, portNumber);
        commands = new Commands(this);
    }

    public GameClient() {
        super();
    }
//
    @Override
    public void onMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            System.out.println(jsonObject.toString());

            String result = commands.orchestrateCommand(jsonObject);
            System.out.println(result);
        }
        catch (JSONException e) {
            System.out.println("invalid format: " + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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
