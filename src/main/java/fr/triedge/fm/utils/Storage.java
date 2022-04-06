package fr.triedge.fm.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.reflect.TypeToken;
import fr.triedge.fm.controller.ProgramController;
import fr.triedge.fm.model.FileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class Storage {
    private static final Logger log = LogManager.getLogger(Storage.class);

    public static void storeJson(Object obj, String path) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter w = new FileWriter(path);
        gson.toJson(obj, w);
        w.flush();
        w.close();
    }

    public static HashMap<String, FileInfo> loadIndexes(String path) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader r = new FileReader(path);
        Type mapType = new TypeToken<HashMap<String, FileInfo>>() {}.getType();
        return gson.fromJson(r, mapType);
    }
}
