package com.drizzard.annihilationdw.files;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.utils.ConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class MapFile {

    public static FileConfiguration config;
    static File file;
    private Main pl;

    public MapFile(Main plugin) {
        createFolder();
        pl = plugin;
    }

    public static void createConfig(String path) {
        file = new File(path + File.separator + "config.yml");

        config = ConfigLoader.loadConfiguration(file);
        config.addDefault("phase-time", 6000);
        config.options().copyDefaults(true);

        saveConfig();
    }

    public static void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set(String key, Object value) {
        config.set(key, value);
    }

    private void createFolder() {
        (new File("plugins" + File.separator + "AnnihilationDW" + File.separator + "Maps" + File.separator + "")).mkdirs();
    }

    public Main getMainClass() {
        return pl;
    }
}