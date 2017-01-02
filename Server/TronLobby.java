package Server;

import javafx.scene.paint.Color;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.awt.*;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 12/27/2016.
 */
public class TronLobby extends Lobby {

    List<TronPlayer> players;

    TronPlayer creator;

    int mapWidth;

    int mapHeight;

    //Ensure all generated colors are not similar
    Color[] colortable = new Color[] {
            Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.LIGHTBLUE, Color.LIGHTGREEN, Color.LIGHTPINK, Color.PURPLE
    };

    Direction[] directiontable = new Direction[] {
        Direction.DOWN, Direction.UP, Direction.RIGHT, Direction.LEFT
    };


    int multi = 0;

    int usersalive = 0;

    public TronLobby(User creator, String lobbyName, int maxPlayers, ChatContext chatContext) {

        mapWidth = 50;
        mapHeight = 50;

        this.name = lobbyName;
        this.chatContext = chatContext;
        players = new ArrayList<>();
        TronPlayer creatorplayer = new TronPlayer(creator, directiontable[0], getNewStartingPoint(), colortable[0]);
        this.creator = creatorplayer;
        players.add(creatorplayer);
        this.maxSize = maxPlayers;

    }


    public Point getNewStartingPoint() {
        Point point;
        //Point Down
        if (multi == 0) {
            point = new Point(players.size() * 4, 0);
        }
        //Point Up
        else if (multi == 1) {
            point = new Point(players.size() * 4, mapHeight);
        }
        //Point Right
        else if (multi == 2) {
            point = new Point(0, players.size() * 4);
        }
        //Point Left
        else if (multi == 3) {
            point = new Point(mapWidth, players.size() * 4);
        }
        else {
            point = null;
        }
        return point;
    }

    @Override
    public boolean onClose(User user) {
        getChat().removeUser(user);
        TronPlayer player = getPlayerByUser(user);
        if (player == null) return false;
        players.remove(player);
        user.setCurrentLobby(null);
        //Determine if the lobby needs to be dissolved
        return players.isEmpty() || player == creator;
    }

    @Override
    public void onConnect(User user) {
        user.setCurrentLobby(this);
        getChat().users.add(user);
        multi ++;
        if (multi >= 4) multi = 0;
        TronPlayer player = new TronPlayer(user, directiontable[multi], getNewStartingPoint(), colortable[players.size()]);
        players.add(player);
    }

    @Override
    public void onMessage(User user, JSONObject input) {
        if (input.getString("type").equals("key")) {
            //Key input
            String key = input.getString("key");
            TronPlayer tronPlayer = getPlayerByUser(user);
            if (tronPlayer == null) return;
            //Up
            if (key.equals("W") && !(tronPlayer.snake.direction == Direction.DOWN)) {
                tronPlayer.snake.direction = Direction.UP;
            }
            //Down
            else if (key.equals("S") && !(tronPlayer.snake.direction == Direction.UP)) {
                tronPlayer.snake.direction = Direction.DOWN;
            }
            //Left
            else if (key.equals("A") && !(tronPlayer.snake.direction == Direction.RIGHT)) {
                tronPlayer.snake.direction = Direction.LEFT;
            }
            //Right
            else if (key.equals("D") && !(tronPlayer.snake.direction == Direction.LEFT)) {
                tronPlayer.snake.direction = Direction.RIGHT;
            }
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void run() {
        while (isRunning) {
            //State changes
            for (TronPlayer tronPlayer: players) {
                if (tronPlayer.isAlive) {
                    tronPlayer.snake.tick1();
                }
            }

            for (TronPlayer tronPlayer: players) {
                if (tronPlayer.isAlive) {
                    if (tronPlayer.snake.head.xCord > mapWidth || tronPlayer.snake.head.xCord < 0 || tronPlayer.snake.head.yCord > mapHeight || tronPlayer.snake.head.yCord < 0) {
                        chatContext.sendMessage(null, "Ouch! " + tronPlayer.user.getName() + " ran into a wall!");
                        tronPlayer.isAlive = false;
                    }
//                    for (Body body: tronPlayer.snake.body) {
//                        if (tronPlayer.snake.head.xCord == body.xCord && tronPlayer.snake.head.yCord == body.yCord) {
//                            tronPlayer.isAlive = false;
//                        }
//                    }
                    //Other and self snakes
                    for (TronPlayer otherTronPlayer: players) {
                        for (Body body:otherTronPlayer.snake.body) {
                            if (tronPlayer.snake.head.xCord == body.xCord && tronPlayer.snake.head.yCord == body.yCord) {
                                chatContext.sendMessage(null, otherTronPlayer.user.getName() + " Killed " + tronPlayer.user.getName() + "!");
                                tronPlayer.isAlive = false;
                            }
                        }
                    }
                    tronPlayer.snake.tick2();
                }
            }

            usersalive = getUsersStillAlive();

            //System.out.println("users alive " + usersalive + ", " + "users: " + players.size() + ";");

            if (usersalive < 2) {
                reset();
            }

            //Render
            //Gen a list of all snake's points

            List<String> renderList = new ArrayList<>();
            for (TronPlayer tronPlayer: players) {
                if (tronPlayer.isAlive) {
                    renderList.add((int)(tronPlayer.snake.color.getRed() * 255) + "," + (int)(tronPlayer.snake.color.getGreen() * 255) + "," + (int)(tronPlayer.snake.color.getBlue() * 255) +  "," + (tronPlayer.snake.head.xCord + 1) + "," + (tronPlayer.snake.head.yCord + 1));
                }
                for (Body b: tronPlayer.snake.body) {
                    renderList.add((int)(tronPlayer.snake.color.getRed() * 255) + "," + (int)(tronPlayer.snake.color.getGreen() * 255) + "," + (int)(tronPlayer.snake.color.getBlue() * 255) + "," + (b.xCord + 1) + "," + (b.yCord + 1));
                }
            }
            StringWriter stringWriter = new StringWriter();
            new JSONWriter(stringWriter).object()
                    .key("argument").value("lobbydraw")
                    .key("name").value(name)
                    .key("mapwidth").value(mapWidth)
                    .key("mapheight").value(mapHeight)
                    .key("render").value(renderList)
                    .endObject();
            for (TronPlayer tronPlayer: players) {
                tronPlayer.user.clientWorker.sendMessage(stringWriter.toString());
            }
            //Sleep (equivalent of a timer)
            try {Thread.sleep(100);} catch (Exception e) {e.printStackTrace();}
        }
    }

    public TronPlayer getPlayerByUser(User u) {
        for (TronPlayer tronPlayer: players) {
            if (tronPlayer.user == u) {
                return tronPlayer;
            }
        }
        return null;
    }

    public int getUsersStillAlive() {
        int ualive = 0;
        for (TronPlayer tronPlayer: players) {
            if (tronPlayer.isAlive) ualive ++;
        }
        return ualive;
    }

    public void reset() {
        for (TronPlayer tronPlayer: players) {
            tronPlayer.reset();
        }
    }

    @Override
    public boolean canConnect(User user) {
        return (players.size() <= maxSize && !isPrivate);
    }

    @Override
    public int getPlayerCount() {
        return players.size();
    }

    public ListChatContext getChat() {
        return (ListChatContext)(chatContext);
    }
}
