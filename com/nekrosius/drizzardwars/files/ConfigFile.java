package com.nekrosius.drizzardwars.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nekrosius.drizzardwars.handlers.MessageHandler;
import com.nekrosius.drizzardwars.utils.ItemStackGenerator;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.utils.ConfigLoader;
import org.bukkit.inventory.ItemStack;

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
		config.addDefault("max-points-multiplier",10);
		config.addDefault("boss-bar",true);
		config.addDefault("kit-selector.name","&4&lKits");
		config.addDefault("kit-selector.type", Material.STICK.name());
		config.addDefault("kit-selector.data",0);
		config.addDefault("kit-selector.lore", Arrays.asList("&7Select a kit!"));
		config.options().copyDefaults(true);

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

	public static ItemStack getKitSelectorItem(){
		String name = MessageHandler.format(config.getString("kit-selector.name"));
		Material type = null;
		int data = config.getInt("kit-selector.data",0);

		if(config.isString("kit-selector.type")){
			type = Material.matchMaterial(config.getString("kit-selector.type","stick"));
		}else if(config.isInt("kit-selector.type")){
			type = Material.getMaterial(config.getInt("kit-selector.type",Material.STICK.ordinal()));
		}else{
			type = Material.STICK;
		}

		List<String> lore = new ArrayList<>();
		for(String loreStr : config.getStringList("kit-selector.lore")){
			lore.add(MessageHandler.format(loreStr));
		}

		return ItemStackGenerator.createItem(type,1,data,name,lore);
	}

	public Main getMainClass()
	{
		return pl;
	}
}