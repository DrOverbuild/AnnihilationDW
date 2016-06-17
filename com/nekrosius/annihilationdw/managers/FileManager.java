package com.nekrosius.annihilationdw.managers;

import com.nekrosius.annihilationdw.Main;
import com.nekrosius.annihilationdw.files.ConfigFile;
import com.nekrosius.annihilationdw.files.KitsFile;
import com.nekrosius.annihilationdw.files.MapFile;
import com.nekrosius.annihilationdw.files.MessageFile;
import com.nekrosius.annihilationdw.files.ShopFile;
import com.nekrosius.annihilationdw.files.TeamsFile;

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