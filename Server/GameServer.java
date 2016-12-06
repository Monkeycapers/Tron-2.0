package Server;


import Jesty.TCPBridge.ClientWorker;
import Jesty.TCPBridge.Server;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.StringWriter;

/**
 * Created by Evan on 10/21/2016.
 *
 * Extends the TCPBridge Server class, handles messages from clients and sets up lobbys.
 * Also handles new connections and dropped connections
 */
public class GameServer extends Server {

    public GameServer(int raw_port, int web_port) {
        super(raw_port, web_port);
    }

    @Override
    public void onMessage(ClientWorker clientWorker, String message) {
        User user = (User)(clientWorker.clientData);
        JSONObject jsonObject = new JSONObject(message);
        String argument = jsonObject.getString("argument");

        if (argument.equals("...")) {
            //...//
            //To send back to client:
            StringWriter stringWriter = new StringWriter();
            JSONWriter jsonWriter = new JSONWriter(stringWriter)
                    .object()
                    .key("argument")
                    .value("...")
                    .endObject();
            //
        }

    }

    @Override
    public void onClose(ClientWorker clientWorker, int code) {
        User user = (User)(clientWorker.clientData);
    }

    @Override
    public void onOpen(ClientWorker clientWorker, int code) {
        clientWorker.clientData = new User();
    }

}
