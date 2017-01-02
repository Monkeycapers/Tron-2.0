package Server;


import javafx.scene.paint.Color;

/**
 * Created by Evan on 12/10/2016.
 */
public class Body {

    int xCord;
    int yCord;

    Color c;

    public Body() {
        c = Color.WHITE;
    }

    public Body(int x, int y, Color color) {
        this.xCord = x;
        this.yCord = y;
        this.c = color;
    }

}
