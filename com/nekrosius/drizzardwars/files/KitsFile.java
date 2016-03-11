package com.nekrosius.drizzardwars.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nekrosius.drizzardwars.utils.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nekrosius.drizzardwars.Main;

public class KitsFile {
	
	static File file;
	public static FileConfiguration config;
	
	private Main pl;
	public KitsFile(Main plugin)
	{
		createConfig();
		pl = plugin;
	}
	
	public static void createConfig()
	{
		(new File("plugins" + File.separator + "DrizzardWars" + File.separator
				+ "")).mkdirs();
		file = new File("plugins" + File.separator + "DrizzardWars",
				"kits.yml");
		config = ConfigLoader.loadConfiguration(file);
		config.addDefault("ranger.name", "&bRanger");
		List<String> description = new ArrayList<String>();
		description.add("&7Slay your enemies with might arrows!");
		description.add("&7You're fast and ready to kill!");
		config.addDefault("ranger.description", description);
		config.addDefault("ranger.icon", 261);
		config.addDefault("ranger.price", 5);
		config.addDefault("ranger.vip-only", false);
		config.addDefault("ranger.items.1.id", 306);
		config.addDefault("ranger.items.2.id", 261);
		List<String> enchantments = new ArrayList<String>();
		enchantments.add("49, 2");
		enchantments.add("50, 1");
		config.addDefault("ranger.items.2.enchantment", enchantments);
		config.addDefault("ranger.items.3.id", 262);
		config.addDefault("ranger.items.3.amount", 32);
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
	
	public Main getMainClass()
	{
		return pl;
	}
}