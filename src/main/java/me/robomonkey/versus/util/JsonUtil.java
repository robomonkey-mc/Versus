package me.robomonkey.versus.util;

import com.google.gson.Gson;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class JsonUtil {
    public static File getDataFile(Plugin plugin, String fileName){
        plugin.getDataFolder().mkdir();
        File file = new File(plugin.getDataFolder(), fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }
    }

    /**
     *
     * @param object the object to be written
     * @param file the file to be written on
     * @param append if true, the object will be appended to the end of the file instead of the beginning.
     * @throws IOException
     */
    public static <T> void writeObject(T object, File file, Boolean append) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        Writer writer = new FileWriter(file, append);
        writer.write(json);
        writer.flush();
        writer.close();
    }

    /**
     *
     * @param type the classtype to be read
     * @param file the file to be read from
     * @throws IOException
     */
    public static <T> T readObject(Class<T> type, File file) throws FileNotFoundException {
        Gson gson = new Gson();
        Reader reader = new FileReader(file);
        T fetchedObject = gson.fromJson(reader, type);
        return fetchedObject;
    }
}
