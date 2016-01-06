package com.nekrosius.drizzardwars.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nekrosius.drizzardwars.files.MapFile;
import com.nekrosius.drizzardwars.files.MessageFile;
import com.nekrosius.drizzardwars.managers.BarManager;
import com.nekrosius.drizzardwars.utils.ItemStackGenerator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.nekrosius.drizzardwars.managers.MapManager;
import com.nekrosius.drizzardwars.managers.TeamManager;

public class PlayerHandler {

	static Map<String, Integer> playerVote = new HashMap<String, Integer>();
	static Map<String, Boolean> playerPlaying = new HashMap<String, Boolean>();
	static Map<String, String> playerKit = new HashMap<String, String>();
	static Map<String, Integer> compassStatus = new HashMap<String, Integer>();
	static Map<String, Boolean> playerSpectating = new HashMap<String, Boolean>();
	static Map<String, Integer> playerGold = new HashMap<>();
	static List<String> hidden = new ArrayList<String>();
	
	public static void quit(Player player) {
		playerVote.remove(player.getName());
		compassStatus.remove(player.getName());
		boolean isHidden = isPlayerHidden(player);
		for(Player p : Bukkit.getOnlinePlayers()){
			if(isPlayerHidden(p)){
				player.showPlayer(p);
			}
			if(isHidden){
				p.showPlayer(player);
			}
		}

		BarManager.removeBar(player);
		//Game.hasPlayers(player);
	}

	public static void setTeamOfPlayer(Player player, Team team) {
		TeamManager.setPlayerTeam(player, team);
	}
	
	public static void setPlayerVote(Player player, Integer map) {
		if(map == null){
			if(getPlayerVote(player) != null){
				MapManager.getMap(getPlayerVote(player)).removeVote();
			}
		}
		if(map != null){
			if(getPlayerVote(player) != null){
				if(getPlayerVote(player) != 0)
					MapManager.getMap(getPlayerVote(player)).removeVote();
			}
			MapManager.getMap(map).addVote();
		}
		playerVote.put(player.getName(), map);
	}
	
	public static Integer getPlayerVote(Player player){
		if(playerVote.get(player.getName()) != null) return playerVote.get(player.getName());
		return 0;
	}

	public static Boolean isPlayerPlaying(Player player) {
//		if(playerPlaying.get(player.getName()) != null) return playerPlaying.get(player.getName());
//		return false;
		return TeamManager.hasTeam(player);
	}

	public static void clearPlayersPlayingMap(){
		String[] strings = playerPlaying.keySet().toArray(new String[]{});
		for(String s:strings){
			playerPlaying.remove(s);
		}
	}

	public static void setPlayerPlaying(Player player, Boolean playing) {
		playerPlaying.put(player.getName(), playing);
	}
	
	public static void setPlayerKit(Player player, String kit) {
		playerKit.put(player.getName(), kit);
	}
	
	public static String getPlayerKit(Player player) {
		return playerKit.get(player.getName());
	}
	
	public static int getCompassStatus(Player player) {
		if(compassStatus.get(player.getName()) != null){
			return compassStatus.get(player.getName());
		}
		return 0;
	}
	
	public static void setCompassStatus(Player player, int status) {
		compassStatus.put(player.getName(), status);
	}
	
	public static boolean isSpectating(Player player){
		if(playerSpectating.get(player.getName()) != null) return playerSpectating.get(player.getName());
		return false;
	}
	
	public static void setSpectating(Player player, boolean bool){
		if (bool){
			player.getInventory().clear();
			MapFile.createConfig("plugins/DrizzardWars/Maps/" + MapManager.getActiveMap().getName());
			Location loc;
			Team t = TeamManager.getTeam(player);
			if(t != null){
				loc = t.getSpectatorSpawnpoint();
			}else {
				loc = TeamManager.getTeam(0).getSpectatorSpawnpoint();
			}
			player.teleport(loc);
			//player.getInventory().addItem(ItemStackGenerator.createItem(Material.COMPASS, 0, 0, MessageHandler.format(MessageFile.getMessage("compass.default")), null));
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(true);
			player.setFlying(true);
			PlayerHandler.hidePlayer(player);
		}else{
			player.setGameMode(GameMode.SURVIVAL);
			player.setLevel(0);
			player.setFlying(false);
			player.setFallDistance(0f);
			player.setAllowFlight(false);
			PlayerHandler.unhidePlayer(player);
		}
		playerSpectating.put(player.getName(), bool);
	}
	
	public static List<String> getHiddenPlayers() {
		return hidden;
	}
	
	public static boolean isPlayerHidden(Player player) {
		return hidden.contains(player.getName());
	}
	
	public static void hidePlayer(Player player){
		hidden.add(player.getName());
		for(Player p : Bukkit.getOnlinePlayers()){
			p.hidePlayer(player);
		}
	}
	
	public static void unhidePlayer(Player player){
		hidden.remove(player.getName());
		for(Player p : Bukkit.getOnlinePlayers()){
			p.showPlayer(player);
		}
	}
	
	public static void hideHiddenPlayers(Player player) {
		if(player == null) return;
		for(String p : getHiddenPlayers()) {
			if(Bukkit.getPlayer(p) != null) {
				player.hidePlayer(Bukkit.getPlayer(p));
			}
		}
	}
	
	public static int amountOfGold(Player player){
		int amount = 0;
		for(ItemStack item : player.getInventory().getContents()){
			if(item != null)
				if(item.getType().equals(Material.GOLD_INGOT)){
					amount += item.getAmount();
				}
		}
		return amount;
	}

	public static void teamNexusIsDestroyed(Player player){
		if(player.hasPermission("drwars.spectator")){
			PlayerHandler.setSpectating(player, true);
			Lobby.setupLobby(player);
			return;
		}else {
			PlayerHandler.quit(player);
			//PlayerHandler.setTeamOfPlayer(player, null);
		}
	}

	public static void setPlayerGold(Player player, Integer gold){
		if(gold == null){
			playerGold.remove(player);
		}else{
			playerGold.put(player.getName(),gold);
		}
	}

	public static Integer getPlayerGold(Player player) {
		return  playerGold.get(player.getName());
	}

	public static void clearPlayerGold(){
		playerGold.clear();
	}
}