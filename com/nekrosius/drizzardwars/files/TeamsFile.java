package com.nekrosius.drizzardwars.files;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import com.nekrosius.drizzardwars.utils.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nekrosius.drizzardwars.Main;

public class TeamsFile {
	
	static File file;
	public static FileConfiguration config;
	
	private Main pl;
	public TeamsFile(Main plugin)
	{
		createConfig();
		pl = plugin;
	}
	
	public static void createConfig()
	{
		(new File("plugins" + File.separator + "DrizzardWars" + File.separator
				+ "")).mkdirs();
		file = new File("plugins" + File.separator + "DrizzardWars",
				"teams.yml");
		config = ConfigLoader.loadConfiguration(file);
		config.options().header("You can find color codes here:\nColor code list: http://jd.bukkit.org/rb/apidocs/org/bukkit/ChatColor.html\ndo not use: bold, magic, italic, strikethrough, underline, dark_aqua");
		config.addDefault("red.name", "Red");
		config.addDefault("red.color", "red");
		config.addDefault("blue.name", "Blue");
		config.addDefault("blue.color", "blue");
		config.addDefault("green.name", "Green");
		config.addDefault("green.color", "green");
		config.addDefault("yellow.name", "Yellow");
		config.addDefault("yellow.color", "yellow");
		config.options().copyDefaults(true);
		saveConfig();
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
	
	public static Set<String> getTeams() {
		return config.getKeys(false);
	}
	
	public static int getTeamsAmount() {
		return config.getKeys(false).size();
	}
	
	public Main getMainClass()
	{
		return pl;
	}

}
