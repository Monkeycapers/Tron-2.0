package Client.Commands;

import Client.GameClient;
import Client.showSetupGui;
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
        showSetupGui.updateCanvas();
        GraphicsContext gc = showSetupGui.canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, showSetupGui.canvas.getWidth(), showSetupGui.canvas.getHeight());
        //Todo: get wall widths and heights from Settings
        int height = 10;
        int width = 10;
        for (Object object: render) {
            String renderstring = (String)(object);
            String[] ints = renderstring.split(",");
            //double d = 100.0;
            //System.out.println((int)d);
            Color color = Color.rgb(Integer.valueOf(ints[0]), Integer.valueOf(ints[1]),  Integer.valueOf(ints[2]));
            gc.setFill(color);
            int x = Integer.valueOf(ints[3]);
            int y = Integer.valueOf(ints[4]);
            gc.fillRect(width * x, height * y, width, height);

            //System.out.println("RenderString: " + renderstring);
        }
        gc.setFill(Color.WHITE);
        //Todo: this info must be passed in input
        int mapWidth = input.getInt("mapwidth") + 2;
        int mapHeight = input.getInt("mapheight") + 2;
        //Top wall
        gc.fillRect(0, 0, (mapWidth * width), height);
        //Left wall
        gc.fillRect(0, 0, width, (mapHeight * height));
        //Right wall
        gc.fillRect((mapWidth * width), 0, width, (mapHeight * height));
        //Bottom wall
        gc.fillRect(0, (mapHeight * height), (mapWidth * width), height);

        gc.save();

        return null;
    }

}
