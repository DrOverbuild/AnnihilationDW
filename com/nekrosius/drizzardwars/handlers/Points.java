package com.nekrosius.drizzardwars.handlers;

import com.nekrosius.drizzardwars.files.PlayerFile;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.files.ConfigFile;

import java.util.HashMap;
import java.util.Map;

public class Points {

	static Map<String, Integer> playerPoints = new HashMap<String, Integer>();

	private static int kill;
	private static int win;
	private static int death;
	
	public static void setupPoints() {
		setKillPoints(ConfigFile.config.getInt("points.kill"));
		setWinPoints(ConfigFile.config.getInt("points.win"));
		setDeathPoints(ConfigFile.config.getInt("points.death"));
	}
	
	public static void setPoints(Player player, int points) {
		playerPoints.put(player.getName(), points);
//		PlayerFile.setPoints(player,points);
	}

	public static void addPoints(Player player, int points){
		setPoints(player, getPoints(player) + points * getPointsMultiplier(player));
//		PlayerFile.setPoints(player,getPoints(player) + points);
	}
	
	public static int getPoints(Player player) {
		if(playerPoints.get(player.getName()) == null) setPoints(player, PlayerFile.getPoints(player));
		return playerPoints.get(player.getName());
//		return PlayerFile.getPoints(player);
	}

	public static void savePoints(Player p){
		if(playerPoints.containsKey(p)) {
			PlayerFile.setPoints(p, getPoints(p));
		}
	}

	public static void removePlayerFromMap(Player p){
		playerPoints.remove(p);
	}

	public static int getKillPoints() {
		return kill;
	}

	public static void setKillPoints(int kill) {
		Points.kill = kill;
	}

	public static int getWinPoints() {
		return win;
	}

	public static void setWinPoints(int win) {
		Points.win = win;
	}

	public static int getDeathPoints() {
		return death;
	}

	public static void setDeathPoints(int death) {
		Points.death = death;
	}

	public static int getPointsMultiplier(Player p){
		int multiplier = 1;
		for(int i = 1; i <= ConfigFile.config.getInt("max-points-multiplier",10); i++){
			if(p.hasPermission("dw.multiplier."+i)){
				multiplier = i;
			}
		}

		return multiplier;
	}
}