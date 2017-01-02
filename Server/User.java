package Server;

import Jesty.TCPBridge.ClientWorker;

import java.util.HashMap;

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

    private String banreason;

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
        if (isAuthenticated) return authenticationstatus.Failure;
        HashMap<String, Object> result = Authenticate.authenticate(name, securepass, false);
        if ((boolean)result.get("result")) {
            this.name = (String)result.get("name");
            this.email = (String)result.get("email");
            this.rank = Rank.valueOf((String)result.get("rank"));
            isAuthenticated = true;
            return authenticationstatus.Success;
        }
        else {
            int reason = (int)result.get("reason");
            if (reason == 0) {
                return authenticationstatus.NoUserOrPassword;
            }
            else if (reason == 2) {
                banreason = (String)result.get("banreason");
                return authenticationstatus.Banned;
            }
        }
        return authenticationstatus.Failure;
    }

    public authenticationstatus authenticate(String securetoken) {
        //...//
        return authenticationstatus.Failure;
    }

    public authenticationstatus signup(String name, String securepass, String email) {
        //...//
        if (isAuthenticated) return authenticationstatus.Failure;
        HashMap<String, Object> result = Authenticate.signUp(name, securepass, email, "User");
        if ((boolean)result.get("result")) {
            this.name = (String)result.get("name");
            this.email = (String)result.get("email");
            this.rank = Rank.User;
            isAuthenticated = true;
            return authenticationstatus.Success;
        }
        else {
            System.out.println(result.toString());
//            int reason = (int)result.get("reason");
//            if (reason == 0) {
//                return authenticationstatus.InvalidPassword;
//            }
//            else if (reason == 2) {
//                return authenticationstatus.Banned;
//            }
        }
        return authenticationstatus.Failure;
    }

    public authenticationstatus updateRank(Rank rank) {
        this.rank = rank;
        return Authenticate.update(this);
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

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public boolean setCurrentLobby(Lobby lobby) {
        this.currentLobby = lobby;
        return true;
    }

    public String getBanReason() {
        return banreason;
    }

    public void setBanReason(String reason) {
            banreason = reason;
    }

    public String chatFormatDisplay() {
        return "[" + rank + "] " + name;
    }
}
