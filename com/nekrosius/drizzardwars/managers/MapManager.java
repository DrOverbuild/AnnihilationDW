package com.nekrosius.drizzardwars.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.Main;
import com.nekrosius.drizzardwars.files.ConfigFile;
import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.handlers.Maps;
import com.nekrosius.drizzardwars.utils.Convert;
import com.nekrosius.drizzardwars.utils.CopyWorld;

public class MapManager {
	
	static List<Maps> maps = new ArrayList<Maps>();
	
	private static Maps activeMap = null;
	
	private static Main pl;
	public MapManager(Main plugin)
	{
		pl = plugin;
		setMaps();
	}
	
	public static String mapName = null;
	public static String path = null;
	public static String mapsPath = null;
	
	public Main getMainClass() {
		return pl;
	}

	private static void setMaps()
	{
		setMapName();
		File file = new File(mapsPath);
		String[] files = file.list();
		Bukkit.unloadWorld(mapName, false);
		deleteWorld(mapName);
		int id = 1;
		for(String str : files){
			if((new File(mapsPath + str + File.separator).isDirectory())){
				Main.unloadWorld(mapsPath + str, true);
				MapFile.createConfig(mapsPath + str);
				maps.add(new Maps(id, str));
				id++;
			}
		}
	}
	
	public static List<Maps> getMaps() {
		return maps;
	}
	
	public static Maps getMap(int id) {
		for(Maps map : getMaps()) {
			if(map.getId() == id) {
				return map;
			}
		}
		return null;
	}
	
	public static Maps getMap(String worldName){
		for(Maps map : getMaps()){
			if(worldName.contains(map.getName())){
				return map;
			}
		}
		return null;
	}
	
	public static Maps chooseMap() {
		List<Maps> mostVotes = new ArrayList<Maps>();
		int max = maxVotes();
		for(Maps map : getMaps()){
			if(map.getVotes() == max){
				mostVotes.add(map);
			}
		}
		if(mostVotes.size() == 1){
			setActiveMap( mostVotes.get(0));
			return mostVotes.get(0);
		}else{
			int id = Main.getRandom(0, mostVotes.size()-1);
			setActiveMap(mostVotes.get(id));
			return mostVotes.get(id);
		}
	}
	
	public static void createWorld(Maps map) {
		if(Bukkit.getWorld(mapName) != null)
			deleteWorld(mapName);
		Location loc;
		if(ConfigFile.config.getString("spawn-location") == null) {
			loc = Bukkit.getWorlds().get(0).getSpawnLocation();
		}else {
			loc = Convert.StringToLocation(ConfigFile.config.getString("spawn-location"), true, false);
		}
		if(Bukkit.getWorld("plugins/DrizzardWars/Maps/" + map.getName()) != null) {
			if(Bukkit.getWorld("plugins/DrizzardWars/Maps/" + map.getName()).getPlayers().size() > 0) {
				for(Player p : Bukkit.getWorld("plugins/DrizzardWars/Maps/" + map.getName()).getPlayers()){
					p.teleport(loc);
				}
			}
		}
		if(Bukkit.getWorld("plugins/DrizzardWars/Maps/" + map.getName()) != null) {
			Bukkit.getServer().unloadWorld("plugins/DrizzardWars/Maps/" + map.getName(), true);
		}
		CopyWorld.copyWorld(new File("plugins/DrizzardWars/Maps/" + map.getName()),
				new File(mapName));
		Bukkit.createWorld(new WorldCreator(mapName));
	}
	
	public static void deleteWorld(String map) {
		File file = new File(map);
		if(file.exists()){
			if(Bukkit.getWorld(map) == null){
				Bukkit.unloadWorld(map, false);
				Main.deleteDirectory(file);
			} else if(Bukkit.getWorld(map).getPlayers() == null){
				Bukkit.unloadWorld(map, false);
				Main.deleteDirectory(file);
			} else if(Bukkit.getWorld(map).getPlayers().size() == 0){
				Bukkit.unloadWorld(map, false);
				Main.deleteDirectory(file);
			} else{
				Location loc;
				if(ConfigFile.config.getString("spawn-location") == null){
					loc = Bukkit.getWorlds().get(0).getSpawnLocation();
				}else{
					loc = Convert.StringToLocation(ConfigFile.config.getString("spawn-location"), true, false);
				}
				for(Player p : Bukkit.getWorld(map).getPlayers()){
					p.teleport(loc);
				}
				Bukkit.unloadWorld(map, false);
				Main.deleteDirectory(file);
			}
		}
	}
	
	public static int maxVotes() {
		int max = -1;
		for(Maps map : getMaps()){
			if(map.getVotes() > max){
				max = map.getVotes();
			}
		}
		return max;
	}
	
	public static int amountOfMaps() {
		int amount = 0;
		File file = new File("plugins" + File.separator + "DrizzardWars" + File.separator
				+ "Maps" + File.separator);
		String[] files = file.list();
		for(String str : files){
			if((new File(path + "Maps" + File.separator + str + File.separator).isDirectory())){
				amount++;
			}
		}
		
		return amount;
	}


	public static Maps getActiveMap() {
		return activeMap;
	}


	public static void setActiveMap(Maps activeMap) {
		MapManager.activeMap = activeMap;
	}
	
	public static void setMapName() {
		path = pl.getDataFolder() + File.separator;
		mapsPath = path + "Maps" + File.separator;
		mapName = path + "CurrentMap";
	}
}