package Server;


import Jesty.TCPBridge.ClientWorker;
import Jesty.TCPBridge.Server;

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

    }

    @Override
    public void onClose(ClientWorker clientWorker, int code) {

    }
    @Override
    public void onOpen(ClientWorker clientWorker, int code) {

    }

}
