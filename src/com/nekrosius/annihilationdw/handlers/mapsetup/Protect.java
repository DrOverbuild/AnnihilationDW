package com.nekrosius.annihilationdw.handlers.mapsetup;

import java.util.HashMap;

import com.nekrosius.annihilationdw.handlers.GameMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.nekrosius.annihilationdw.files.MapFile;
import com.nekrosius.annihilationdw.utils.Convert;

public class Protect {
	
	private static java.util.Map<Player, Integer> protectionStatus = new HashMap<>();
	private static java.util.Map<Player, String> name = new HashMap<>();
	private static java.util.Map<Player, Boolean> pvp = new HashMap<>();
	private static java.util.Map<Player, Location> pointOne = new HashMap<>();
	private static java.util.Map<Player, Location> pointTwo = new HashMap<>();
	
	private static java.util.Map<Integer, Location> first = new HashMap<>();
	private static java.util.Map<Integer, Location> second = new HashMap<>();
	
	//-------------------------------------------------------------//
	
	public static void setupAreas(GameMap map) {
		MapFile.createConfig("plugins/AnnihilationDW/Maps/" + map.getName());
		int i = 0;
		if(MapFile.config.getConfigurationSection("areas") == null) return;
		for(String str : MapFile.config.getConfigurationSection("areas").getKeys(false)){
			String loc1 = MapFile.config.getString("areas." + str + ".first.x") + ","
					+ MapFile.config.getString("areas." + str + ".first.y") + ","
					+ MapFile.config.getString("areas." + str + ".first.z");
			first.put(i, Convert.StringToLocation(loc1, false, true));
			String loc2 = MapFile.config.getString("areas." + str + ".second.x") + ","
					+ MapFile.config.getString("areas." + str + ".second.y") + ","
					+ MapFile.config.getString("areas." + str + ".second.z");
			second.put(i, Convert.StringToLocation(loc2, false, true));
			i++;
		}
		
	}
	
	public static void createProtectedArea(Player player) {
		MapFile.createConfig(player.getWorld().getName());
		int 	x1 = getPointOne(player).getBlockX(),
				y1 = getPointOne(player).getBlockY(),
				z1 = getPointOne(player).getBlockZ(),
				x2 = getPointTwo(player).getBlockX(),
				y2 = getPointTwo(player).getBlockY(),
				z2 = getPointTwo(player).getBlockZ();
		MapFile.config.set("areas." + getName(player) + ".PVP", getPVP(player));
		MapFile.config.set("areas." + getName(player) + ".first.x", x1);
		MapFile.config.set("areas." + getName(player) + ".first.y", y1);
		MapFile.config.set("areas." + getName(player) + ".first.z", z1);
		MapFile.config.set("areas." + getName(player) + ".second.x", x2);
		MapFile.config.set("areas." + getName(player) + ".second.y", y2);
		MapFile.config.set("areas." + getName(player) + ".second.z", z2);
		MapFile.saveConfig();
		setProtectionStatus(player, 0);
		setPointOne(player, null);
		setPointTwo(player, null);
		setName(player, null);
		setPVP(player, false);
	}
	
	//------------------------AREAS SETUP--------------------------//
	
	public static int amountOfAreas() {
		for(int i = 1; true; i++){
			if(first.get(i) == null) return i - 1;
		}
	}
	
	//-------------------------------------------------------------//
	
	public static void setProtectionStatus(Player player, int status) {
		if(status == 0) protectionStatus.remove(player);
		else protectionStatus.put(player, status);
	}
	
	public static Integer getProtectionStatus(Player player) {
		if(protectionStatus.get(player) == null) return 0;
		return protectionStatus.get(player);
	}
	
	public static void setName(Player player, String str) {
		if(str == null) name.remove(player);
		else name.put(player, str);
	}
	
	public static String getName(Player player) {
		return name.get(player);
	}
	
	public static void setPVP(Player player, boolean bool) {
		pvp.put(player, bool);
	}
	
	public static Boolean getPVP(Player player) {
		return pvp.get(player);
	}
	
	public static void setPointOne(Player player, Location loc) {
		if(loc == null) pointOne.remove(player);
		else pointOne.put(player, loc);
	}
	
	public static Location getPointOne(Player player) {
		return pointOne.get(player);
	}
	
	public static void setPointTwo(Player player, Location loc) {
		if(loc == null) pointTwo.remove(player);
		else pointTwo.put(player, loc);
	}
	
	public static Location getPointTwo(Player player) {
		return pointTwo.get(player);
	}
	
	public static Location getFirstPoint(int id) {
		return first.get(id);
	}
	
	public static Location getSecondPoint(int id) {
		return second.get(id);
	}
}