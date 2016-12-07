package Server;

import Jesty.TCPBridge.ClientWorker;

/**
 * Created by S199753733 on 12/6/2016.
 */
public class User {

    private String name;
    private String email;
    private boolean isAuthenticated;
    private Rank rank;
    private Lobby currentLobby;
    public ClientWorker clientWorker;

    public User(ClientWorker clientWorker) {
        //Default to an un-authenticated user
        this.name = "guest";
        this.email = "";
        this.isAuthenticated = false;
        this.rank = Rank.Guest;
        this.clientWorker = clientWorker;
    }

    public User(ClientWorker clientWorker, String name, String securepass) {
        //...//
        this.clientWorker = clientWorker;
        authenticate(name, securepass);
    }

    public User(ClientWorker clientWorker, String securetoken) {
        //...//
        this.clientWorker = clientWorker;
        authenticate(securetoken);
    }

    public authenticationstatus authenticate(String name, String securepass) {
        //...//
        Authenticate.authenticate(name, securepass);
        return authenticationstatus.Success;
    }

    public authenticationstatus authenticate(String securetoken) {
        //...//
        return authenticationstatus.Success;
    }

    public authenticationstatus signup(String name, String securepass, String email) {
        return authenticationstatus.Failure;
    }

    public Rank getRank() {
        return rank;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Lobby getCurrentLobby () {
        return currentLobby;
    }

    public boolean setCurrentLobby(Lobby lobby) {
        //...//
        return true;
    }


}
