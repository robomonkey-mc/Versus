package me.robomonkey.versus.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {


    /**
     * Obtains a named file in a plugin's data folder. If the file does not exist, it will be created and then returned.
     *
     * @param plugin   The plugin folder the data file should inhabit.
     * @param fileName The name of the file.
     * @return A named file in the plugin's data folder.
     */
    public static File getDataFile(Plugin plugin, String fileName) {
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
     * @param object the object to be written
     * @param file   the file to be written on
     * @throws IOException
     */
    public static <T> void writeObject(Type type, T object, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = new FileWriter(file, false);
        gson.toJson(object, type, writer);
        writer.close();
    }

    public static <T> void writeObjectList(List<T> list, File file) throws IOException {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        Gson gson = new Gson();
        Writer writer = new FileWriter(file, false);
        gson.toJson(list, listType, writer);
        writer.close();
    }

    /**
     * @param type the classtype to be read
     * @param file the file to be read from
     * @throws IOException
     */
    public static <T> T readObject(Type type, File file) throws IOException {
        Gson gson = new Gson();
        Reader reader = new FileReader(file);
        T fetchedObject = gson.fromJson(reader, type);
        return fetchedObject;
    }

    public static <T> ArrayList<T> readObjectList(Class<T> type, File file) throws IOException {
        Type listType = new TypeToken<ArrayList<T>>() {
        }.getType();
        Gson gson = new Gson();
        Reader reader = new FileReader(file);
        ArrayList<T> fetchedList = gson.fromJson(reader, listType);
        return fetchedList;
    }

    public static JsonElement readJsonElement(File file) throws FileNotFoundException {
        //TODO: Worry about Async calls
        Gson gson = new Gson();
        Reader reader = new FileReader(file);
        JsonElement jsonElement = JsonParser.parseReader(reader);
        return jsonElement;
    }


}
