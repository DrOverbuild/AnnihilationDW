package com.nekrosius.annihilationdw.database;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Database {

	void connect();

	boolean isConnected();

	void disconnect();

	void prepare();

	int getPoints(Player player);

	int getPoints(UUID playerId);

	void setPoints(Player player, int points);

	void setPoints(UUID playerId, int points);

	void addKit(Player player, String kit);

	void addKit(UUID playerId, String kit);

	void clearKits(Player player);

	void clearKits(UUID playerId);

	List<String> getKits(Player player);

	List<String> getKits(UUID playerId);

}
