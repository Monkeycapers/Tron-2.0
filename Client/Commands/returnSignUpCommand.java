package Client.Commands;

import Client.GameClient;
import Client.showSetupGui;
import Client.signinController;
import org.json.JSONObject;

/**
 * Created by Evan on 12/16/2016.
 */
public class returnSignUpCommand extends Command {

    public returnSignUpCommand () {
        this.name = "returnsignup";
        this.doReturn = false;
    }

    @Override
    public String docommand(GameClient gameClient, JSONObject input) {
        if (input.getBoolean("success")) {
            System.out.println("authed...");
            //Todo: setup the next gui
        }
        else {
            int reason = input.getInt("reason");
            String message = "";
            message = "Error! " + reason;
            if (reason == 2) {
                message = "Banned for: " + input.getString("banreason");
            }
            ((signinController)(showSetupGui.loader.getController())).updateSignInOrSignUpError(message);
        }
        return "";
    }
}
