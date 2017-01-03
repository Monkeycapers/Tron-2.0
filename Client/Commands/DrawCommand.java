package Client.Commands;

import Client.GameClient;
import Client.showSetupGui;
import Jesty.Settings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Evan on 12/28/2016.
 */
public class DrawCommand extends Command {

    public DrawCommand () {
        this.name = "lobbydraw";
        this.doReturn = false;
    }

    @Override
    public String docommand(GameClient gameClient, JSONObject input) {
        List<Object> render = input.getJSONArray("render").toList();
        int mapWidth = input.getInt("mapwidth") + 2;
        int mapHeight = input.getInt("mapheight") + 2;
        showSetupGui.render(render, mapWidth, mapHeight);
        return null;
    }

}
