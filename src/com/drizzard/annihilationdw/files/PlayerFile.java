package com.drizzard.annihilationdw.files;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.handlers.Stats;
import com.drizzard.annihilationdw.utils.ConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerFile {

    public static FileConfiguration config;
    static File file;
    private static UUID currentConfig;

    private Main pl;

    public static void createConfig(Player player) {
        createConfig(player.getUniqueId());
    }

    public static void createConfig(UUID playerId) {
        if (currentConfig == null || !currentConfig.equals(playerId)) {
            currentConfig = playerId;
            (new File("plugins" + File.separator + "AnnihilationDW" + File.separator + "players" + File.separator + "")).mkdirs();
            file = new File("plugins" + File.separator + "AnnihilationDW" + File.separator + "players" + File.separator + playerId.toString()
                    + ".yml");
            config = ConfigLoader.loadConfiguration(file);
            config.addDefault("points", 0);
            config.options().copyDefaults(true);
            saveConfig();
        }
    }

    public static void setPoints(Player player, int points) {
        setPoints(player.getUniqueId(), points);
    }

    public static void setPoints(UUID playerId, int points) {
        createConfig(playerId);
        config.set("points", points);
        saveConfig();
    }

    public static void setKills(Player player, int kills) {
        setKills(player.getUniqueId(), kills);
    }

    public static void setKills(UUID playerId, int kills) {
        createConfig(playerId);
        config.set("kills", kills);
        saveConfig();
    }

    public static void setGames(Player player, int games) {
        setGames(player.getUniqueId(), games);
    }

    public static void setGames(UUID playerId, int games) {
        createConfig(playerId);
        config.set("games", games);
        saveConfig();
    }

    public static void setWins(Player player, int wins) {
        setWins(player.getUniqueId(), wins);
    }

    public static void setWins(UUID playerId, int wins) {
        createConfig(playerId);
        config.set("wins", wins);
        saveConfig();
    }

    public static Stats getStats(UUID playerId) {
        createConfig(playerId);
        return new Stats(playerId, config.getInt("points", 0), config.getInt("kills", 0), config.getInt("games", 0), config.getInt("wins", 0));
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

    public Main getMainClass() {
        return pl;
    }
}
