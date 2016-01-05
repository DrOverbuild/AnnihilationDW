package com.nekrosius.drizzardwars.handlers;

import com.nekrosius.drizzardwars.files.PlayerFile;
import org.bukkit.entity.Player;

import com.nekrosius.drizzardwars.files.ConfigFile;

public class Points {

//	static Map<String, Integer> playerPoints = new HashMap<String, Integer>();
	
	private static int kill;
	private static int win;
	private static int death;
	
	public static void setupPoints() {
		setKillPoints(ConfigFile.config.getInt("points.kill"));
		setWinPoints(ConfigFile.config.getInt("points.win"));
		setDeathPoints(ConfigFile.config.getInt("points.death"));
	}
	
	public static void setPoints(Player player, int points) {
//		playerPoints.put(player.getName(), points);
		PlayerFile.setPoints(player,points);
	}

	public static void addPoints(Player player, int points){
//		if(playerPoints.get(player.getName()) == null) setPoints(player, 0);
//		setPoints(player, getPoints(player) + points);
		PlayerFile.setPoints(player,getPoints(player) + points);
	}
	
	public static int getPoints(Player player) {
//		if(playerPoints.get(player.getName()) == null) setPoints(player, 0);
//		return playerPoints.get(player.getName());
		return PlayerFile.getPoints(player);
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
}