package Server;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 12/28/2016.
 */
public class Lobbys {

    private List<Lobby> lobbys;

    public Lobbys() {
        lobbys = new ArrayList<>();
    }

    public void addNewLobby(Lobby lobby) {
        lobbys.add(lobby);
    }

    public void removeLobby (Lobby lobby) {
        lobbys.remove(lobby);
    }

    public void doLobbyMessage(User user, String name, JSONObject input) {
        Lobby lobby = getLobbyByName(name);
        if (lobby == null) return;
        lobby.onMessage(user, input);
    }

    public Lobby getLobbyByName (String name) {
        for (Lobby lobby: lobbys) {
            if (lobby.name.equals(name)) return lobby;
        }
        return null;
    }

    public void removeUser (User user) {
        for (Lobby lobby: lobbys) {
            if (lobby.onClose(user)) {
                //Dissolve the lobby
                removeLobby(lobby);
                break;
            }
        }
    }

    public List<Lobby> getList() {
        return lobbys;
    }

}
