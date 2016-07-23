package com.drizzard.annihilationdw.handlers;

import com.drizzard.annihilationdw.Main;
import com.drizzard.annihilationdw.files.ConfigFile;
import com.drizzard.annihilationdw.files.StatSignFile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Stats {

    public static int deathPoints = 1;
    public static int killPoints = 1;
    public static int winPoints = 1;
    private static Map<UUID, Stats> playerStats = new HashMap<>();
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

    public static void setupPoints() {
        killPoints = ConfigFile.config.getInt("points.kill");
        winPoints = ConfigFile.config.getInt("points.win");
        deathPoints = ConfigFile.config.getInt("points.death");
    }

    public static Stats getStats(Player player) {
        return getStats(player.getUniqueId());
    }

    public static Stats getStats(UUID playerId) {
        if (!playerStats.containsKey(playerId)) {
            Stats stats = new Stats(playerId, 0, 0, 0, 0);
            stats.refresh();
            playerStats.put(playerId, stats);
            return stats;
        }
        return playerStats.get(playerId);
    }

    public static void unloadStats(Player player) {
        playerStats.remove(player.getUniqueId());
    }

    private void refresh() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

            @Override
            public void run() {
                Stats stats = Main.getDatabaseImpl().loadStats(playerId);
                points = stats.points;
                kills = stats.kills;
                games = stats.games;
                wins = stats.wins;
            }

        });
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getPoints() {
        return points;
    }

    public Stats setPoints(int points) {
        this.points = points;
        StatSignFile.update(StatSignFile.StatType.POINTS, playerId, this.points);
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> Main.getDatabaseImpl().setPoints(playerId, points));
        return this;
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

    public Stats addKill() {
        this.kills++;
        StatSignFile.update(StatSignFile.StatType.KILLS, playerId, this.kills);
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> Main.getDatabaseImpl().setKills(playerId, kills));
        return this;
    }

    public Stats addGame() {
        this.games++;
        StatSignFile.update(StatSignFile.StatType.GAMES, playerId, this.games);
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> Main.getDatabaseImpl().setKills(playerId, games));
        return this;
    }

    public Stats addWin() {
        this.wins++;
        StatSignFile.update(StatSignFile.StatType.WINS, playerId, this.wins);
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> Main.getDatabaseImpl().setKills(playerId, wins));
        return this;
    }
}
