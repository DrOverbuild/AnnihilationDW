package com.nekrosius.drizzardwars.managers;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.ConfigFile;
import com.nekrosius.drizzardwars.files.KitsFile;
import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.files.ShopFile;
import com.nekrosius.drizzardwars.files.TeamsFile;

public class FileManager {

	
	private Main pl;
	public FileManager(Main plugin)
	{
		this.pl = plugin;
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