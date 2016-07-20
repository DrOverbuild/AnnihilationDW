package com.drizzard.annihilationdw.files;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.handlers.MessageHandler;
import com.drizzard.annihilationdw.utils.ConfigLoader;
import com.drizzard.annihilationdw.utils.ItemStackGenerator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigFile {

    public static FileConfiguration config;
    static File file;
    private Main pl;

    public ConfigFile(Main plugin) {
        createConfig();
        pl = plugin;
    }

    public static void createConfig() {
        (new File("plugins" + File.separator + "AnnihilationDW" + File.separator
                + "")).mkdirs();
        file = new File("plugins" + File.separator + "AnnihilationDW",
                "config.yml");
        config = ConfigLoader.loadConfiguration(file);
        config.addDefault("database.type", "MySQL");
        config.addDefault("database.mysql.host", "localhost");
        config.addDefault("database.mysql.port", 3306);
        config.addDefault("database.mysql.username", "user");
        config.addDefault("database.mysql.password", "password");
        config.addDefault("database.mysql.database", "database");
        config.addDefault("team-size", 30);
        config.addDefault("minimum-player-amount", 12);
        config.addDefault("time-until-start", 90);
        config.addDefault("nexus-health", 75);
        config.addDefault("gametime", 30);
        config.addDefault("gold-ingots-per-kill", 3);
        config.addDefault("points.kill", 1);
        config.addDefault("points.win", 1);
        config.addDefault("points.death", 0);
        config.addDefault("respawn.time", 5);
        config.addDefault("party.player-limit", 9);
        config.addDefault("max-points-multiplier", 10);
        config.addDefault("boss-bar", false);
        config.addDefault("kit-selector.name", "&4&lKits");
        config.addDefault("kit-selector.type", Material.STICK.name());
        config.addDefault("kit-selector.data", 0);
        config.addDefault("kit-selector.lore", Arrays.asList("&7Select a kit!"));
        config.addDefault("bungeecord-mode.enabled", false);
        config.addDefault("bungeecord-mode.fallback-server", "lobby");
        config.addDefault("bungeecord-mode.timeout", 30);
        Location location = Bukkit.getWorlds().get(0).getHighestBlockAt(0, 0).getLocation();
        String world = location.getWorld().getName();
        double x = location.getX(),
                y = location.getY(),
                z = location.getZ();
        float yaw = location.getYaw(),
                pitch = location.getPitch();
        config.addDefault("spawn-location", world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch);
        if (!config.contains("prefixes")) {
            config.addDefault("prefixes.vip.prefix", "&f[&e&lVIP&f]");
            config.addDefault("prefixes.vip.permission", "dw.vip");
            config.addDefault("prefixes.vip.include-admins", false);
            config.addDefault("prefixes.op.prefix", "&f[&eADMIN&f]");
            config.addDefault("prefixes.op.permission", "dw.op");
            config.addDefault("prefixes.op.include-admins", true);
        }
        config.options().copyDefaults(true);

        saveConfig();
    }

    public static void saveConfig() {
        if (getPartyPlayerLimit() > 9) {
            set("party.player-limit", 9);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set(String key, Object value) {
        config.set(key, value);
    }

    public static int getPartyPlayerLimit() {
        return config.getInt("party.player-limit");
    }

    public static ItemStack getKitSelectorItem() {
        String name = MessageHandler.format(config.getString("kit-selector.name"));
        Material type = null;
        int data = config.getInt("kit-selector.data", 0);

        if (config.isString("kit-selector.type")) {
            type = Material.matchMaterial(config.getString("kit-selector.type", "stick"));
        } else if (config.isInt("kit-selector.type")) {
            type = Material.getMaterial(config.getInt("kit-selector.type", Material.STICK.ordinal()));
        } else {
            type = Material.STICK;
        }

        List<String> lore = new ArrayList<>();
        for (String loreStr : config.getStringList("kit-selector.lore")) {
            lore.add(MessageHandler.format(loreStr));
        }

        return ItemStackGenerator.createItem(type, 1, data, name, lore);
    }

    public static String getFallbackServer() {
        return config.getString("bungeecord-mode.fallback-server", "lobby");
    }

    public static int getTimeout() {
        return config.getInt("bungeecord-mode.timeout", 30);
    }

    public static boolean getBungeeCordModeEnabled() {
        return config.getBoolean("bungeecord-mode.enabled", false);
    }

    public Main getMainClass() {
        return pl;
    }
}