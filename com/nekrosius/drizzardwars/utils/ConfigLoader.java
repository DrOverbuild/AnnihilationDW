package com.nekrosius.drizzardwars.utils;

import com.nekrosius.drizzardwars.Main;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by jasper on 12/22/15.
 */
public class ConfigLoader {

	public static YamlConfiguration loadConfiguration(File file){
		Validate.notNull(file, "File cannot be null");

		YamlConfiguration config = new YamlConfiguration();

		try {
			config.load(file);
		}catch (FileNotFoundException e){
		}catch (IOException e){
			Bukkit.getPluginManager().getPlugin("DrizzardWars").getLogger().log(Level.SEVERE, "Cannot load " + file, e);
		}catch (InvalidConfigurationException e){
			Main.println(file + " cannot be loaded because it is incorrectly configured.");
			Main.println("Renaming " + file + " to " + file + ".broken and creating a new config.");
			File newFile = new File(file.getAbsolutePath());
			newFile.renameTo(new File(file.getAbsolutePath() + ".broken"));
		}

		return config;
	}

}
