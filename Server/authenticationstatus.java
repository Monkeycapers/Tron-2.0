package Server;

/**
 * Created by Evan on 12/6/2016.
 */
public enum authenticationstatus {
    //General
    Success, Failure,
    //Signin
    NoUser, NoPassword, WrongToken,
    //Signup
    UserAlreadyExists, InvalidPassword
}
