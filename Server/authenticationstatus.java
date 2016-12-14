package Server;

/**
 * Created by Evan on 12/6/2016.
 */
public enum authenticationstatus {
    //General
    Success, Failure,
    //Signin
    NoUserOrPassword, WrongToken, Banned,
    //Signup
    UserAlreadyExists, InvalidPassword
}
