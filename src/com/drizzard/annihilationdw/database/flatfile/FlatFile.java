package com.drizzard.annihilationdw.database.flatfile;

import com.drizzard.annihilationdw.files.PlayerFile;
import com.drizzard.annihilationdw.database.Database;
import com.drizzard.annihilationdw.handlers.Stats;
import org.bukkit.entity.Player;

import java.util.List;
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
	public void setPoints(Player player, int points) {
		setPoints(player.getUniqueId(), points);
	}

	@Override
	public void setPoints(UUID playerId, int points) {
		PlayerFile.setPoints(playerId, points);
	}

	@Override
	public void addKit(Player player, String kit) {
		addKit(player.getUniqueId(), kit);
	}

	@Override
	public void addKit(UUID playerId, String kit) {
		PlayerFile.createConfig(playerId);
		List<String> kits = PlayerFile.config.getStringList("kits");
		kits.add(kit);
		PlayerFile.set("kits", kits);
		PlayerFile.saveConfig();
	}

	@Override
	public void clearKits(Player player) {
		clearKits(player.getUniqueId());
	}

	@Override
	public void clearKits(UUID playerId) {
		PlayerFile.createConfig(playerId);
		List<String> kits = PlayerFile.config.getStringList("kits");
		kits.clear();
		PlayerFile.set("kits", kits);
		PlayerFile.saveConfig();
	}

	@Override
	public List<String> getKits(Player player) {
		return getKits(player.getUniqueId());
	}

	@Override
	public List<String> getKits(UUID playerId) {
		PlayerFile.createConfig(playerId);
		return PlayerFile.config.getStringList("kits");
	}

	@Override
	public void setKills(Player player, int kills) {
		setKills(player.getUniqueId(), kills);
	}

	@Override
	public void setKills(UUID playerId, int kills) {
		PlayerFile.setKills(playerId, kills);
	}

	@Override
	public void setGames(Player player, int games) {
		setGames(player.getUniqueId(), games);
	}

	@Override
	public void setGames(UUID playerId, int games) {
		PlayerFile.setGames(playerId, games);
	}

	@Override
	public void setWins(Player player, int wins) {
		setWins(player.getUniqueId(), wins);
	}

	@Override
	public void setWins(UUID playerId, int wins) {
		PlayerFile.setWins(playerId, wins);
	}

	@Override
	public Stats loadStats(Player player) {
		return loadStats(player.getUniqueId());
	}

	@Override
	public Stats loadStats(UUID playerId) {
		return PlayerFile.getStats(playerId);
	}
}
