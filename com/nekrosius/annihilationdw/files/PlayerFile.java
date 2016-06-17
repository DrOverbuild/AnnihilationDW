package com.nekrosius.annihilationdw.files;

import java.io.File;
import java.io.IOException;

import com.nekrosius.annihilationdw.handlers.ScoreboardHandler;
import com.nekrosius.annihilationdw.utils.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.nekrosius.annihilationdw.Main;

public class PlayerFile {
	
	static File file;
	public static FileConfiguration config;
	
	private Main pl;
	
	public static void createConfig(Player player)
	{
		(new File("plugins" + File.separator + "AnnihilationDW" + File.separator + "players" + File.separator + "")).mkdirs();
		file = new File("plugins" + File.separator + "AnnihilationDW" + File.separator + "players" + File.separator + player.getUniqueId().toString() + ".yml");
		config = ConfigLoader.loadConfiguration(file);
		config.addDefault("points", 0);
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	public static void setPoints(Player player, int points) {
		createConfig(player);
		config.set("points", points);
		saveConfig();
		ScoreboardHandler.update(player);
	}
	
	public static int getPoints(Player player) {
		createConfig(player);
		return config.getInt("points");
	}
	
	public static void saveConfig()
	{
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void set(String key, Object value)
	{
		config.set(key, value);
	}
	
	public Main getMainClass()
	{
		return pl;
	}
}
