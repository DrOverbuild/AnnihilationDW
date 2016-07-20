package com.drizzard.annihilationdw.database;

import com.drizzard.annihilationdw.handlers.Stats;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Database {

    void connect();

    boolean isConnected();

    void disconnect();

    void prepare();

    void setPoints(Player player, int points);

    void setPoints(UUID playerId, int points);

    void addKit(Player player, String kit);

    void addKit(UUID playerId, String kit);

    void clearKits(Player player);

    void clearKits(UUID playerId);

    List<String> getKits(Player player);

    List<String> getKits(UUID playerId);

    void setKills(Player player, int kills);

    void setKills(UUID playerId, int kills);

    void setGames(Player player, int games);

    void setGames(UUID playerId, int games);

    void setWins(Player player, int wins);

    void setWins(UUID playerId, int wins);

    Stats loadStats(Player player);

    Stats loadStats(UUID playerId);

}
