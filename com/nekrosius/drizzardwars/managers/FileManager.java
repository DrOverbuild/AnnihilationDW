package com.nekrosius.drizzardwars.managers;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.ConfigFile;
import com.nekrosius.drizzardwars.files.KitsFile;
import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.files.ShopFile;
import com.nekrosius.drizzardwars.files.TeamsFile;

import java.io.File;

public class FileManager {

	
	private Main pl;
	public FileManager(Main plugin)
	{
		this.pl = plugin;

		// This code will be removed after a few updates. We don't want to rename a data folder other plugins that we
		// didn't write named DrizzardWars.
		File oldDataFolder = new File(pl.getDataFolder().getParentFile().getAbsolutePath() + System.getProperty("file.separator") + "DrizzardWars");
		if(oldDataFolder.exists()){
			Main.println("DrizzardWars has been renamed to AnnihilationDW.");
			Main.println("Renaming " + oldDataFolder.getAbsolutePath() + " to " + pl.getDataFolder().getAbsolutePath() + ".");
			oldDataFolder.renameTo(pl.getDataFolder());
		}
		// End temporary code.

		new MessageFile(pl);
		new MapFile(pl);
		new ConfigFile(pl);
		new KitsFile(pl);
		new ShopFile(pl);
		new TeamsFile(pl);
		//TabHandler.pts = MessageFile.getMessage("general.points");
	}
	
	
	public Main getMainClass()
	{
		return pl;
	}
}