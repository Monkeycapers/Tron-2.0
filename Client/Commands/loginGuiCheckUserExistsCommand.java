package Client.Commands;

import Client.showSetupGui;
import Client.signinController;
import Client.GameClient;
import org.json.JSONObject;

/**
 * Created by Evan on 12/16/2016.
 */
public class loginGuiCheckUserExistsCommand extends Client.Commands.Command {

    public loginGuiCheckUserExistsCommand () {
        this.name = "returnuserexists";
        this.doReturn = false;
    }

    @Override
    public String docommand(GameClient gameClient, JSONObject input) {
        String message;
        boolean isGood = !input.getBoolean("result");
        if (isGood) {
            message = "✓ is available!";
        }
        else {
            message = "✘ is not available :(";
        }
        ((signinController)(showSetupGui.loader.getController())).updateCheckIfUserExistsField(message, isGood);
        return "";
    }
}
