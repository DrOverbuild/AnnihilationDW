package com.nekrosius.annihilationdw.database.flatfile;

import com.nekrosius.annihilationdw.database.Database;
import com.nekrosius.annihilationdw.files.PlayerFile;
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
	public int getPoints(Player player) {
		return getPoints(player.getUniqueId());
	}

	@Override
	public int getPoints(UUID playerId) {
		return PlayerFile.getPoints(playerId);
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
}
