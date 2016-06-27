package com.nekrosius.annihilationdw.handlers;

import com.nekrosius.annihilationdw.Main;
import com.nekrosius.annihilationdw.files.ConfigFile;
import com.nekrosius.annihilationdw.files.StatSignFile;
import com.nekrosius.annihilationdw.utils.AsyncUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Stats {

	public static int killPoints = 1;
	public static int winPoints = 1;
	public static int deathPoints = 1;

	public static void setupPoints() {
		killPoints = ConfigFile.config.getInt("points.kill");
		winPoints = ConfigFile.config.getInt("points.win");
		deathPoints = ConfigFile.config.getInt("points.death");
	}

	private static Map<UUID, Stats> playerStats = new HashMap<>();

	public static Stats getStats(Player player) {
		return getStats(player.getUniqueId());
	}

	public static Stats getStats(UUID playerId) {
		return playerStats.get(playerId);
	}

	public static void unloadStats(Player player) {
		playerStats.remove(player.getUniqueId());
	}

	private final UUID playerId;
	private int points, kills, games, wins;

	public Stats(UUID playerId, int points, int kills, int games, int wins) {
		this.playerId = playerId;
		this.points = points;
		this.kills = kills;
		this.games = games;
		this.wins = wins;

		StatSignFile.update(StatSignFile.StatType.POINTS, playerId, points);
		StatSignFile.update(StatSignFile.StatType.KILLS, playerId, kills);
		StatSignFile.update(StatSignFile.StatType.GAMES, playerId, games);
		StatSignFile.update(StatSignFile.StatType.WINS, playerId, wins);
		playerStats.put(playerId, this);
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public int getPoints() {
		return points;
	}

	public int getKills() {
		return kills;
	}

	public int getGames() {
		return games;
	}

	public int getWins() {
		return wins;
	}

	public Stats addPoints(int points) {
		return setPoints(this.points + points);
	}

	public Stats setPoints(int points) {
		this.points = points;
		StatSignFile.update(StatSignFile.StatType.POINTS, playerId, this.points);
		AsyncUtil.run(new Runnable() {

			@Override
			public void run() {
				Main.getDatabaseImpl().setPoints(playerId, points);
			}

		});
		return this;
	}

	public Stats addKill() {
		this.kills++;
		StatSignFile.update(StatSignFile.StatType.KILLS, playerId, this.kills);
		AsyncUtil.run(new Runnable() {

			@Override
			public void run() {
				Main.getDatabaseImpl().setKills(playerId, kills);
			}

		});
		return this;
	}

	public Stats addGame() {
		this.games++;
		StatSignFile.update(StatSignFile.StatType.GAMES, playerId, this.games);
		AsyncUtil.run(new Runnable() {

			@Override
			public void run() {
				Main.getDatabaseImpl().setKills(playerId, games);
			}

		});
		return this;
	}

	public Stats addWin() {
		this.wins++;
		StatSignFile.update(StatSignFile.StatType.WINS, playerId, this.wins);
		AsyncUtil.run(new Runnable() {

			@Override
			public void run() {
				Main.getDatabaseImpl().setKills(playerId, wins);
			}

		});
		return this;
	}
}
