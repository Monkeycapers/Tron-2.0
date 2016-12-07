package Server;

import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Evan on 12/6/2016.
 */
public class Authenticate {

    private static File usersfile;

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
            JSONObject jsonObject = new JSONObject(total);
            List<Object> users = jsonObject.getJSONArray("users").toList();
            for (Object u: users) {
                System.out.println((String)u.toString());
                //Feast your eyes
                JSONObject user = new JSONObject((HashMap)u);
                //JSONObject user = new JSONObject((String)u.toString().replaceAll("=", ":"));
                //
                if (user.getString("name").equals(name)) {
                    if (user.getString("pass").equals(pass)) {
                        System.out.println("true");
                        result.put("result", true);
                        result.put("name", user.getString("name"));
                        result.put("email", user.getString("email"));
                        result.put("rank", user.getString("rank"));
                    }
                }
            }
            System.out.println("printing users");
            for (Object o: users) {
                System.out.println(o);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        result.put("result", false);
        return result;
    }
}
