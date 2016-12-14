package Server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Evan on 12/6/2016.
 */
public class Authenticate {

    private static File usersfile;

    private static Rank[] rankOrder = new Rank[] {Rank.Banned, Rank.Guest, Rank.User, Rank.Op, Rank.Admin};

    public static void setUp() {

    }

    public static void setFile(File file) {
        usersfile = file;
    }

    public static HashMap<String, Object> authenticate(String name, String pass) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            String in = "";
            String total = "";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(usersfile));
            while ((in = bufferedReader.readLine()) != null) {total += in;}
            bufferedReader.close();
            JSONObject jsonObject = new JSONObject(total);
            List<Object> users = jsonObject.getJSONArray("users").toList();
            for (Object u: users) {
                System.out.println((String)u.toString());
                JSONObject user = new JSONObject((HashMap)u);
                //JSONObject user = new JSONObject((String)u.toString().replaceAll("=", ":"));
                //
                if (user.getString("name").equals(name)) {
                    if (user.getString("pass").equals(pass)) {
                        if (user.getString("rank").equals("Banned")) {
                            //banned (bad boy!)
                            result.put("result", false);
                            result.put("reason", 2);
                            return result;
                        }
                        System.out.println("true");
                        result.put("result", true);
                        result.put("name", user.getString("name"));
                        result.put("email", user.getString("email"));
                        result.put("rank", user.getString("rank"));
                        return result;
                    }
                }
            }
            result.put("reason", 0);
            System.out.println("printing users");
            for (Object o: users) {
                System.out.println(o);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result.put("reason", 1);
        }
        result.put("result", false);
        return result;
    }

    public static HashMap<String, Object> signUp(String name, String pass, String email, String rank) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            //Check if the user does not exist
            HashMap<String, Object> checkisalreadyauth = authenticate(name, pass);
            if (checkisalreadyauth.containsKey("reason")) {
                //User does not exist
                //Write the user details to file
                String in = "";
                String total = "";
                BufferedReader bufferedReader = new BufferedReader(new FileReader(usersfile));
                while ((in = bufferedReader.readLine()) != null) {total += in;}
                bufferedReader.close();
                JSONObject jsonObject = new JSONObject(total);
                JSONArray users = jsonObject.getJSONArray("users");
                StringWriter stringWriter = new StringWriter();
                
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("users", users);
                System.out.println("JSonObject1 : " + jsonObject1.toString());
                //System.out.println(jsonObject1.toString());
//                PrintWriter out = new PrintWriter(new FileWriter(usersfile));
//                JSONWriter writer = new JSONWriter(out).object()
//                        .key("users")
//                        .array().

                result.put("result", true);
                return result;
            }
            else {
                //User already exists, or an error occurred

                //...//
                result.put("result", false);
                return result;

            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        result.put("result", false);
        return result;
    }

    public static boolean checkRank (Rank rank, Rank minRank) {
        int i1 = 0, i2 = 0;
        for (int i = 0; i < rankOrder.length -1; i++) {
            if (rankOrder[i].equals(rank)) i1 = i;
            if (rankOrder[i].equals(minRank)) i2 = i;
        }
        //System.out.println(rank.toString() + ":" + i1);
        //System.out.println(minRank.toString() + ":" + i2);
        return (i2 <= i1);
    }
}
