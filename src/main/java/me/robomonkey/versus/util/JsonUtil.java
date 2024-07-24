package me.robomonkey.versus.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class JsonUtil {


    /**
     * Obtains a named file in a plugin's data folder. If the file does not exist, it will be created and then returned.
     * @param plugin The plugin folder the data file should inhabit.
     * @param fileName The name of the file.
     * @return A named file in the plugin's data folder.
     */
    public static File getDataFile(Plugin plugin, String fileName){
        plugin.getDataFolder().mkdir();
        File file = new File(plugin.getDataFolder(), fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
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

    public static JsonElement readJsonElement(File file) throws FileNotFoundException {
        //TODO: Worry about Async calls
        Gson gson = new Gson();
        Reader reader = new FileReader(file);
        JsonElement jsonElement = JsonParser.parseReader(reader);
        return jsonElement;
    }


}
