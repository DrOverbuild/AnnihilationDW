package com.nekrosius.drizzardwars.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.utils.ConfigLoader;

public class ConfigFile {
	
	static File file;
	public static FileConfiguration config;
	
	private Main pl;
	public ConfigFile(Main plugin)
	{
		createConfig();
		pl = plugin;
	}
	
	public static void createConfig()
	{
		(new File("plugins" + File.separator + "DrizzardWars" + File.separator
				+ "")).mkdirs();
		file = new File("plugins" + File.separator + "DrizzardWars",
				"config.yml");
		config = ConfigLoader.loadConfiguration(file);
		if(!file.exists()){
			config.addDefault("team-size", 30);
			config.addDefault("minimum-player-amount", 12);
			config.addDefault("time-until-start", 90);
			config.addDefault("nexus-health", 75);
			config.addDefault("gametime", 30);
			config.addDefault("points.kill", 1);
			config.addDefault("points.win", 1);
			config.addDefault("points.death", 0);
			config.addDefault("respawn.time", 5);
			config.addDefault("party.player-limit",9);
			config.addDefault("boss-bar",true);
			config.options().copyDefaults(true);
		}

		if(!config.contains("party.player-limit")){
			config.set("party.player-limit",9);
		}
		if(!config.contains("boss-bar")){
			config.set("boss-bar",true);
		}
		saveConfig();
	}
	
	public static void saveConfig()
	{
		if(getPartyPlayerLimit() > 9){
			set("party.player-limit",9);
		}

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

	public static int getPartyPlayerLimit(){
		return config.getInt("party.player-limit");
	}

	public Main getMainClass()
	{
		return pl;
	}
}