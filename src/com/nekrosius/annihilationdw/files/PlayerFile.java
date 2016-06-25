package com.nekrosius.annihilationdw.files;

import com.nekrosius.annihilationdw.Main;
import com.nekrosius.annihilationdw.utils.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerFile {

	static File file;
	public static FileConfiguration config;
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

	public static int getPoints(UUID playerId) {
		createConfig(playerId);
		return config.getInt("points", 0);
	}

	public static int getPoints(Player player) {
		return getPoints(player.getUniqueId());
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
