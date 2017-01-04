package Server;

import Jesty.Settings;
import javafx.scene.paint.Color;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.awt.*;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * Created by Evan on 12/27/2016.
 */
public class TronLobby extends Lobby {

    List<TronPlayer> players;

    TronPlayer creator;

    int mapWidth;

    int mapHeight;

    int sleepTime;

    TronState tronState;

    int countdown = 0;

    final int MAX_SUPPORTED_PLAYERS = 12;

    final int DEFAULT_COUNTDOWN_TIME = 3;

    //Ensure all generated colors are not similar
    Color[] colortable = new Color[] {
            Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.LIGHTBLUE, Color.LIGHTGREEN, Color.LIGHTPINK, Color.PURPLE
            , Color.INDIGO, Color.IVORY, Color.MIDNIGHTBLUE, Color.LIMEGREEN, Color.VIOLET
    };

    Direction[] directiontable = new Direction[] {
        Direction.DOWN, Direction.UP, Direction.RIGHT, Direction.LEFT
    };



    int usersalive = 0;

    public TronLobby(User creator, String lobbyName, int maxPlayers, ChatContext chatContext) {

        Settings.load();

        mapWidth = Settings.getIntProperty("tronmapwidth");
        mapHeight = Settings.getIntProperty("tronmapheight");

        sleepTime = Settings.getIntProperty("tronsleeptime");

        this.name = lobbyName;
        this.chatContext = chatContext;
        players = new ArrayList<>();
        TronPlayer creatorplayer = new TronPlayer(creator, directiontable[0], new Point(0, 0), colortable[0]);
        this.creator = creatorplayer;
        players.add(creatorplayer);
        if (maxPlayers > MAX_SUPPORTED_PLAYERS || maxPlayers <= 1) maxPlayers = MAX_SUPPORTED_PLAYERS;
        this.maxSize = maxPlayers;
        chatContext.sendMessage(null, "Waiting for 1 more player...");
        tronState = TronState.WAITING;
    }


//    public Point getNewStartingPoint() {
////        Point point;
////        //Point Down
////        if (multi == 0) {
////            point = new Point(players.size() * 4, 0);
////        }
////        //Point Up
////        else if (multi == 1) {
////            point = new Point(players.size() * 4, mapHeight);
////        }
////        //Point Right
////        else if (multi == 2) {
////            point = new Point(0, players.size() * 4);
////        }
////        //Point Left
////        else if (multi == 3) {
////            point = new Point(mapWidth, players.size() * 4);
////        }
////        else {
////            point = null;
////        }
//
//        Point point;
//
//        //determine what to divide by
//        int divideby;
//        if (above4) divideby = 4;
//        else divideby = 2;
//        //
//
//        //Point down
//        if (multi == 0) {
//            point = new Point(mapWidth / divideby, 0);
//        }
//        //Point up
//        else if (multi == 1) {
//            point = new Point(mapWidth / divideby, mapHeight);
//        }
//        //Point Right
//        else if (multi == 2) {
//            point = new Point(0, mapHeight / divideby);
//        }
//        //Point left
//        else if (multi == 3) {
//            point = new Point(mapWidth, mapHeight / divideby);
//        }
//        //Point ???
//        else {
//            point = new Point(mapWidth / divideby, mapHeight / divideby);
//        }
//
//        return point;
//    }

    @Override
    public boolean onClose(User user) {
        getChat().removeUser(user);
        TronPlayer player = getPlayerByUser(user);
        if (player == null) return false;
        players.remove(player);
        if (players.size() == 1) {
            chatContext.sendMessage(null, "Waiting for 1 more player...");
            tronState = TronState.WAITING;
        }
        //user.setCurrentLobby(null);
        //Transfer ownership
        if (player == creator && !players.isEmpty()) {
            creator = players.get(0);
        }
        //Determine if the lobby needs to be dissolved
        return players.isEmpty();
    }

    @Override
    public void onConnect(User user) {
        user.setCurrentLobby(this);
        getChat().users.add(user);
        TronPlayer player = new TronPlayer(user, directiontable[0], new Point(0, 0), colortable[players.size()]);
        players.add(player);

        if (tronState == TronState.WAITING) {
            countdown = DEFAULT_COUNTDOWN_TIME;
            tronState = TronState.BEGIN_COUNTDOWN;
        }
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
            if (tronState == TronState.WAITING) {
                try {Thread.sleep(1000);} catch (Exception e) { }
            }
            else if (tronState == TronState.BEGIN_COUNTDOWN) {
                chatContext.sendMessage(null, "Time left until game begins: " + countdown);
                countdown --;
                try {Thread.sleep(1000);} catch (Exception e) {}
                if (countdown <= 0) {
                    tronState = TronState.IN_GAME;
                    chatContext.sendMessage(null, "The game has begun!");
                    reset();
                }
            }
            else if (tronState == TronState.IN_GAME) {
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
                            if (otherTronPlayer.isVisible) {
                                //If the heads hit and it isn't the same tron
                                if (tronPlayer.snake.head.xCord == otherTronPlayer.snake.head.xCord && tronPlayer.snake.head.yCord == otherTronPlayer.snake.head.yCord && otherTronPlayer.isAlive && tronPlayer != otherTronPlayer) {
                                    //Two heads run into each other
                                    chatContext.sendMessage(null, "Wow! " + tronPlayer.user.chatFormatDisplay() + " and " + otherTronPlayer.user.chatFormatDisplay() + " Killed each other!");
                                    tronPlayer.isAlive = false;
                                    otherTronPlayer.isAlive = false;
                                }
                                else {
                                    for (Body body:otherTronPlayer.snake.body) {
                                        if (tronPlayer.snake.head.xCord == body.xCord && tronPlayer.snake.head.yCord == body.yCord) {
                                            chatContext.sendMessage(null, otherTronPlayer.user.getName() + " Killed " + tronPlayer.user.getName() + "!");
                                            tronPlayer.isAlive = false;
                                        }
                                    }
                                }
                            }
                        }
                        tronPlayer.snake.tick2();
                    }
                }

                usersalive = getUsersStillAlive();

                //System.out.println("users alive " + usersalive + ", " + "users: " + players.size() + ";");



                //Render
                //Gen a list of all snake's points

                List<String> renderList = new ArrayList<>();
                for (TronPlayer tronPlayer: players) {
                    if (tronPlayer.isVisible) {

                        for (Body b : tronPlayer.snake.body) {
                            renderList.add((int) (tronPlayer.snake.color.getRed() * 255) + "," + (int) (tronPlayer.snake.color.getGreen() * 255) + "," + (int) (tronPlayer.snake.color.getBlue() * 255) + "," + (b.xCord + 1) + "," + (b.yCord + 1));
                        }

                        renderList.add((255) + "," +  (255) + "," + (255) + "," + (tronPlayer.snake.head.xCord + 1) + "," + (tronPlayer.snake.head.yCord + 1));

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

                //

                if (usersalive < 2) {
                    countdown = DEFAULT_COUNTDOWN_TIME;
                    tronState = TronState.BEGIN_COUNTDOWN;
                    TronPlayer winner = getWinner();
                    if (winner == null) {
                        chatContext.sendMessage(null, "Game has finished. Nobody won this game!" + getScoreBoard());
                    }
                    else {
                        chatContext.sendMessage(null, "Game has finished. Winner: " + winner.user.chatFormatDisplay() + getScoreBoard());
                    }

                    //reset();
                }

                //Sleep (equivalent of a timer)
                try {Thread.sleep(sleepTime);} catch (Exception e) {e.printStackTrace();}
            }
        }
    }

    public String getScoreBoard() {
        String toSend = "";
        for (TronPlayer tronPlayer: players) {
            toSend += "\n" + tronPlayer.user.chatFormatDisplay() + " Score: " + tronPlayer.score;
        }
        return toSend;
    }

    //This must only be called after users alive has been updated, and users alive is < 2
    //This must also only be called once
    public TronPlayer getWinner() {
        TronPlayer returnPlayer = null;
        for (TronPlayer tronPlayer: players) {
            if (tronPlayer.isAlive) {
                tronPlayer.score++;
                returnPlayer = tronPlayer;
            }
        }
        players.sort(new Comparator<TronPlayer>() {
            @Override
            public int compare(TronPlayer o1, TronPlayer o2) {
                if (o1.score > o2.score) {
                    return -1;
                }
                else if (o1.score == o2.score) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
        });
        return returnPlayer;
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

        mapWidth = Settings.getIntProperty("tronmapwidth");
        mapHeight = Settings.getIntProperty("tronmapheight");

        sleepTime = Settings.getIntProperty("tronsleeptime");

        int multi = 0;
        for (int i = 0; i < players.size(); i ++) {
            TronPlayer player = players.get(i);
            player.reset();
            Point point;

            double divideby = 1;
            boolean doAdd = false;
            if (i <= 3) {
                divideby = 2;
            }
            else if (i <= 7) {
                divideby = 4;
            }
            else if (i <= 11) {
                divideby = 2;
                doAdd = true;
            }

            //Point down
            if (multi == 0) {
                if (doAdd) {
                    point = new Point((int)(mapWidth / divideby) + (mapWidth / 4), 0);
                }
                else {
                    point = new Point((int)(mapWidth / divideby), 0);
                }
            }
            //Point up
            else if (multi == 1) {
                if (doAdd) {
                    point = new Point((int)(mapWidth / divideby) + (mapWidth / 4), mapHeight);
                }
                else {
                    point = new Point((int)(mapWidth / divideby), mapHeight);
                }
            }
            //Point Right
            else if (multi == 2) {
                if (doAdd) {
                    point = new Point(0, (int)(mapHeight / divideby) + (mapHeight / 4));
                }
                else {
                    point = new Point(0, (int)(mapHeight / divideby));
                }
            }
            //Point left
            else if (multi == 3) {
                if (doAdd) {
                    point = new Point(mapWidth, (int)(mapHeight / divideby) + (mapHeight / 4));
                }
                else {
                    point = new Point(mapWidth, (int)(mapHeight / divideby));
                }
            }
            //Point ???
            else {
                point = new Point((int)(mapWidth / divideby), (int)(mapHeight / divideby));
            }

            player.snake = new Snake(1, directiontable[multi], SnakeType.TRON, point, colortable[i]);

            multi ++;
            if (multi >= 4) {
                multi = 0;
            }
            players.set(i, player);
        }

//        for (TronPlayer player: players) {
//            System.out.println("User " + player.user.chatFormatDisplay() + ", Is alive: " + player.isAlive + "Is visible " + player.isVisible + " Direction " + player.snake.direction + ", " + player.snake.direction);
//        }
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

    @Override
    public List<User> getUsers() {
        List<User> userlist = new ArrayList<>();
        for (TronPlayer tronPlayer: players) {
            userlist.add(tronPlayer.user);
        }
        return userlist;
    }
}
