package Jesty;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Evan on 11/3/2016.
 * Loads and saves client settings such as the hostname and portnumber
 * Settings file name: settings.txt
 */
public class Settings {

    private static HashMap<String, String> propertys;

    private static File settingsfile = new File(Settings.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "\\settings.txt");

    public static boolean setFile(File file) {
        if (file.exists()) {
            settingsfile = file;
            return true;
        }
        return false;
    }

    public static void load() {
        propertys = new HashMap<String, String>();
        //Todo: Load the client specific property's into the HashMap
        try {
            //settingsfile.mkdirs();
            if (settingsfile.createNewFile()) {
                //Create defaults
                propertys.put("hostname", "localhost");
                propertys.put("portnumber", "16000");
                propertys.put("theme", "dark");
                save();
            }
            else {
                BufferedReader in = new BufferedReader(new FileReader(settingsfile));
                String line = "";
                line = in.readLine();
                while (line != null) {
                    String[] split = line.split(":");
                    propertys.put(split[0], split[1]);
                    line = in.readLine();
                }
                in.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            PrintWriter out = new PrintWriter((new PrintWriter(settingsfile)));
            for (Map.Entry<String, String> entry: propertys.entrySet()) {
                out.println(entry.getKey() + ":" + entry.getValue());
            }
            out.close();
        }
        catch (Exception e) {

        }
    }

    public static Object getProperty(String name) {
        return propertys.get(name);
    }

    public static boolean hasProperty(String name) {
        return propertys.containsKey(name);
    }

    public static void setProperty(String name, String value) {
        propertys.replace(name, value);
        save();
    }
}
