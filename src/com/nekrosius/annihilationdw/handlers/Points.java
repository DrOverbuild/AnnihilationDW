package com.nekrosius.annihilationdw.handlers;

import com.nekrosius.annihilationdw.Main;
import com.nekrosius.annihilationdw.files.ConfigFile;
import com.nekrosius.annihilationdw.utils.AsyncUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Points {

	static Map<UUID, Integer> playerPoints = new HashMap<UUID, Integer>();

	private static int kill;
	private static int win;
	private static int death;

	public static void setupPoints() {
		setKillPoints(ConfigFile.config.getInt("points.kill"));
		setWinPoints(ConfigFile.config.getInt("points.win"));
		setDeathPoints(ConfigFile.config.getInt("points.death"));
	}

	public static void setPoints(Player player, int points) {
		setPoints(player.getUniqueId(), points);
		savePoints(player);
		ScoreboardHandler.update(player);
	}

	public static void setPoints(UUID playerId, int points) {
		playerPoints.put(playerId, points);
	}

	public static void addPoints(Player player, int points) {
		setPoints(player, getPoints(player) + points * getPointsMultiplier(player));
	}

	public static int getPoints(Player player) {
		return playerPoints.getOrDefault(player.getUniqueId(), 0);
	}

	public static void savePoints(Player p) {
		if (playerPoints.containsKey(p.getUniqueId())) {
			int points = playerPoints.get(p.getUniqueId());
			AsyncUtil.run(new Runnable() {

				@Override
				public void run() {
					Main.getDatabaseImpl().setPoints(p.getUniqueId(), points);
				}

			});
		}
	}

	public static void removePlayerFromMap(Player p) {
		playerPoints.remove(p.getUniqueId());
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