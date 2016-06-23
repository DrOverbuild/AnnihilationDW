package com.nekrosius.annihilationdw.database.flatfile;

import com.nekrosius.annihilationdw.database.Database;
import com.nekrosius.annihilationdw.files.PlayerFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FlatFile implements Database {

	@Override
	public void connect() {
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public void disconnect() {
	}

	@Override
	public void prepare() {
	}

	@Override
	public int getPoints(Player player) {
		return PlayerFile.getPoints(player);
	}

	@Override
	public int getPoints(UUID playerId) {
		Player player = Bukkit.getPlayer(playerId);
		return player != null ? PlayerFile.getPoints(player) : 0;
	}

	@Override
	public void setPoints(Player player, int points) {
		PlayerFile.setPoints(player, points);
	}

	@Override
	public void setPoints(UUID playerId, int points) {
		Player player = Bukkit.getPlayer(playerId);
		if (player != null) {
			setPoints(player, points);
		}
	}
}
