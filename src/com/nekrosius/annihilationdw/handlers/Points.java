package com.nekrosius.annihilationdw.handlers;

import com.nekrosius.annihilationdw.Main;
import com.nekrosius.annihilationdw.files.ConfigFile;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Points {

	static Map<String, Integer> playerPoints = new HashMap<String, Integer>();
	static ExecutorService async = Executors.newSingleThreadExecutor();

	private static int kill;
	private static int win;
	private static int death;

	public static void setupPoints() {
		setKillPoints(ConfigFile.config.getInt("points.kill"));
		setWinPoints(ConfigFile.config.getInt("points.win"));
		setDeathPoints(ConfigFile.config.getInt("points.death"));
	}

	public static void setPoints(Player player, int points) {
		setPoints(player.getName(), points);
		savePoints(player);
	}

	public static void setPoints(String name, int points) {
		playerPoints.put(name, points);
	}

	public static void addPoints(Player player, int points) {
		setPoints(player, getPoints(player) + points * getPointsMultiplier(player));
	}

	public static int getPoints(Player player) {
		return playerPoints.getOrDefault(player.getName(), 0);
	}

	public static void savePoints(Player p) {
		if (playerPoints.containsKey(p.getName())) {
			int points = playerPoints.get(p.getName());
			async.submit(new Runnable() {

				@Override
				public void run() {
					Main.getDatabaseImpl().setPoints(p.getUniqueId(), points);
				}

			});
		}
	}

	public static void removePlayerFromMap(Player p) {
		playerPoints.remove(p.getName());
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

	public static int getPointsMultiplier(Player p) {
		int multiplier = 1;
		for (int i = 1; i <= ConfigFile.config.getInt("max-points-multiplier", 10); i++) {
			if (p.hasPermission("dw.multiplier." + i)) {
				multiplier = i;
			}
		}

		return multiplier;
	}
}